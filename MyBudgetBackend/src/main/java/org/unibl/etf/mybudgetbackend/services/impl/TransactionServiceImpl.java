package org.unibl.etf.mybudgetbackend.services.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.unibl.etf.mybudgetbackend.exceptions.InsufficientAccountBalanceException;
import org.unibl.etf.mybudgetbackend.exceptions.NotFoundException;
import org.unibl.etf.mybudgetbackend.models.dto.AccountDTO;
import org.unibl.etf.mybudgetbackend.models.dto.AccountRequestDTO;
import org.unibl.etf.mybudgetbackend.models.dto.TransactionDTO;
import org.unibl.etf.mybudgetbackend.models.dto.TransactionRequestDTO;
import org.unibl.etf.mybudgetbackend.models.entities.AccountEntity;
import org.unibl.etf.mybudgetbackend.models.entities.TransactionEntity;
import org.unibl.etf.mybudgetbackend.models.enums.TransactionType;
import org.unibl.etf.mybudgetbackend.repositories.TransactionRepository;
import org.unibl.etf.mybudgetbackend.services.AccountService;
import org.unibl.etf.mybudgetbackend.services.TransactionService;

import java.math.BigDecimal;
import java.util.List;

/**
 * Implementation of the TransactionService interface.
 * This class provides the business logic for handling transaction-related operations.
 * It interacts with the repository to perform CRUD operations and uses ModelMapper
 * to convert between DTOs and entity objects.
 */
@Service
@Setter
@Slf4j
@Transactional
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountService accountService;
    private final ModelMapper mapper;

    @PersistenceContext
    private EntityManager entityManager;

    public TransactionServiceImpl(TransactionRepository repository, AccountService accountService, ModelMapper mapper) {
        this.transactionRepository = repository;
        this.accountService = accountService;
        this.mapper = mapper;
    }


    /**
     * Finds all accounts in system.
     *
     * @return a  List of  TransactionDTO objects
     */
    @Override
    public List<TransactionDTO> findAll() {
        return this.transactionRepository.findAll().stream().map(el -> mapper.map(el, TransactionDTO.class)).toList();
    }

    /**
     * Finds a transaction by its ID.
     *
     * @param id the ID of the transaction
     * @return the TransactionDTO object corresponding to the transaction
     * @throws NotFoundException if the transaction with the specified ID does not exist
     */
    @Override
    public TransactionDTO findById(Long id) {
        return this.mapper.map(this.transactionRepository.findById(id).orElseThrow(NotFoundException::new), TransactionDTO.class);
    }

    /**
     * Finds all a transaction by related account ID.
     *
     * @param id the ID of the transaction
     * @return a  List of  TransactionDTO objects
     * @throws NotFoundException if the account with the specified ID does not exist
     */
    @Override
    public List<TransactionDTO> findAllByAccountId(Long id) {
        if (!this.accountService.existsById(id))
            throw new NotFoundException();
        return this.transactionRepository.findAllByAccountId(id).stream().map(el -> mapper.map(el, TransactionDTO.class)).toList();
    }

    /**
     * Inserts a new transaction into the system and updates the balance of the specified account.
     * <p>
     * This method performs the following steps:
     * -Retrieves the account by its ID.
     * -Checks if the account has sufficient balance if the transaction type is an expense.
     * -Updates the account balance based on the transaction type.
     * -Creates and saves the transaction entity.
     *
     * @param accountId the ID of the account for which the transaction is being created
     * @param request   the transaction request details including type, amount and description
     * @return a TransactionDTO representing the created transaction
     * @throws InsufficientAccountBalanceException if the transaction type is EXPENSE and the account balance is insufficient
     */
    @Override
    public TransactionDTO insert(Long accountId, TransactionRequestDTO request) {
        var account = this.accountService.findById(accountId);
        if (request.getType() == TransactionType.EXPENSE && account.getBalance().compareTo(request.getAmount()) < 0)
            throw new InsufficientAccountBalanceException();
        this.updateBalance(account, request);
        var transaction = this.createTransactionEntity(accountId, request);
        log.info("Transaction[" + transaction.getId() + "] has been successfully created.");
        return this.mapper.map(transaction, TransactionDTO.class);
    }


    /**
     * Creates a new TransactionEntity based on the provided request details and associates it with the specified account.
     *
     * @param accountId the ID of the account to associate with the transaction
     * @param request   the transaction request details including type, amount and description
     * @return the created  TransactionEntity after being saved and flushed to the database
     */
    private TransactionEntity createTransactionEntity(Long accountId, TransactionRequestDTO request) {
        var transaction = this.mapper.map(request, TransactionEntity.class);
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setId(accountId);
        transaction.setAccount(accountEntity);
        transaction.setId(null);
        transaction = this.transactionRepository.saveAndFlush(transaction);
        entityManager.refresh(transaction);
        return transaction;
    }

    /**
     * Updates the balance of the specified account based on the transaction request details.
     * This method calculates the new balance depending on whether the transaction is an income or an expense.
     * It then updates the account's balance by mapping the updated details and saving them using AccountService.
     *
     * @param account the account whose balance is to be updated
     * @param request the transaction request details including amount and type
     */
    private void updateBalance(AccountDTO account, TransactionRequestDTO request) {
        BigDecimal newBalance = null;
        if (request.getType() == TransactionType.INCOME)
            newBalance = account.getBalance().add(request.getAmount());
        else
            newBalance = account.getBalance().subtract(request.getAmount());
        AccountRequestDTO requestDTO = this.mapper.map(account, AccountRequestDTO.class);
        requestDTO.setBalance(newBalance);
        this.accountService.update(account.getId(), requestDTO);
    }
}
