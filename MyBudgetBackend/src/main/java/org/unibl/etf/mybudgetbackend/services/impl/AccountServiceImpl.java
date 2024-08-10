package org.unibl.etf.mybudgetbackend.services.impl;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.unibl.etf.mybudgetbackend.exceptions.NotFoundException;
import org.unibl.etf.mybudgetbackend.models.dto.AccountDTO;
import org.unibl.etf.mybudgetbackend.models.dto.AccountRequestDTO;
import org.unibl.etf.mybudgetbackend.models.entities.AccountEntity;
import org.unibl.etf.mybudgetbackend.repositories.AccountRepository;
import org.unibl.etf.mybudgetbackend.services.AccountService;

/**
 * Implementation of the AccountService interface.
 * This class provides the business logic for handling account-related operations.
 * It interacts with the repository to perform CRUD operations and uses ModelMapper
 * to convert between DTOs and entity objects.
 */
@Service
@Transactional
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository repository;
    private final ModelMapper mapper;

    public AccountServiceImpl(AccountRepository repository, ModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * Finds all accounts with pagination.
     *
     * @param page the Pageable object that contains pagination information
     * @return a  Page of  AccountDTO objects
     */
    @Override
    public Page<AccountDTO> findAll(Pageable page) {
        return this.repository.findAll(page).map(el -> this.mapper.map(el, AccountDTO.class));
    }

    /**
     * Finds an account by its ID.
     *
     * @param id the ID of the account
     * @return the AccountDTO object corresponding to the account
     * @throws NotFoundException if the account with the specified ID does not exist
     */
    @Override
    public AccountDTO findById(Long id) {
        return mapper.map(this.repository.findById(id).orElseThrow(NotFoundException::new), AccountDTO.class);
    }

    /**
     * Inserts a new account based on the provided request data.
     *
     * @param request the  AccountRequestDTO object containing the validated account data
     * @return the  AccountDTO object representing the inserted account
     */
    @Override
    public AccountDTO insert(AccountRequestDTO request) {
        var account = this.mapper.map(request, AccountEntity.class);
        account.setId(null);
        account = this.repository.saveAndFlush(account);
        log.info("Account[" + account.getId() + "] has been successfully created.");
        return this.mapper.map(account, AccountDTO.class);
    }

    /**
     * Updates an existing account with the given ID using the provided request data.
     *
     * @param id      the ID of the account to update
     * @param request the  AccountRequestDTO object containing the updated account data
     * @return the updated  AccountDTO object
     * @throws NotFoundException if the account with the specified ID does not exist
     */
    @Override
    public AccountDTO update(Long id, AccountRequestDTO request) {
        if (!this.repository.existsById(id))
            throw new NotFoundException();
        var account = this.mapper.map(request, AccountEntity.class);
        account.setId(id);
        account = this.repository.saveAndFlush(account);
        log.info("Account[" + id + "] has been successfully updated.");
        return this.mapper.map(account, AccountDTO.class);
    }

    /**
     * Deletes an account by its ID.
     *
     * @param id the ID of the account to delete
     * @throws NotFoundException if the account with the specified ID does not exist
     */
    @Override
    public void delete(Long id) {
        if (!this.repository.existsById(id))
            throw new NotFoundException();
        this.repository.deleteById(id);
        log.info("Account[" + id + "] has been successfully deleted.");
    }
}
