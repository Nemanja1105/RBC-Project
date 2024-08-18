package org.unibl.etf.mybudgetbackend.services;

import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.unibl.etf.mybudgetbackend.exceptions.InsufficientAccountBalanceException;
import org.unibl.etf.mybudgetbackend.exceptions.NotFoundException;
import org.unibl.etf.mybudgetbackend.models.dto.AccountDTO;
import org.unibl.etf.mybudgetbackend.models.dto.TransactionRequestDTO;
import org.unibl.etf.mybudgetbackend.models.entities.AccountEntity;
import org.unibl.etf.mybudgetbackend.models.entities.TransactionEntity;
import org.unibl.etf.mybudgetbackend.models.enums.TransactionType;
import org.unibl.etf.mybudgetbackend.repositories.TransactionRepository;
import org.unibl.etf.mybudgetbackend.services.impl.TransactionServiceImpl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTests {

    @Mock
    private TransactionRepository repository;
    @Mock
    private AccountService accountService;
    @Mock
    private EntityManager entityManager;
    @InjectMocks
    private TransactionServiceImpl service;

    private AccountEntity account;
    private TransactionEntity transaction;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        service = new TransactionServiceImpl(repository, accountService, modelMapper());
        service.setEntityManager(entityManager);
        this.account = AccountEntity.builder().name("TEST").balance(BigDecimal.valueOf(100.0f)).currency("EUR").id(1l).build();
        this.transaction = TransactionEntity.builder().description("TEST DESC").type(TransactionType.EXPENSE).amount(BigDecimal.valueOf(50.0f)).id(1l).account(account).build();
    }

    @Test
    public void FindAll__ListOfTransactionDTO() {
        when(repository.findAll()).thenReturn(Arrays.asList(transaction));
        var result = this.service.findAll();
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.size()).isEqualTo(1);
        verify(repository).findAll();
    }

    @Test
    public void FindById_TransactionWithIdExists_TransactionDTO() {
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(transaction));
        var transactionDTO = service.findById(transaction.getId());
        Assertions.assertThat(transactionDTO).isNotNull();
        Assertions.assertThat(transactionDTO.getId()).isEqualTo(transaction.getId());
        verify(repository).findById(transaction.getId());
    }

    @Test
    public void FindById_TransactionWithIdNotExists_ThrowsException() {
        when(repository.findById(any(Long.class))).thenReturn(Optional.empty());
        org.junit.jupiter.api.Assertions.assertThrows(NotFoundException.class, () -> service.findById(1l));
        verify(repository).findById(any(Long.class));
    }

    @Test
    public void FindAllByAccountId_AccountWithIdExists_ListOfTransactionDTO() {
        when(repository.findAllByAccountId(any(Long.class))).thenReturn(Arrays.asList(transaction));
        when(accountService.existsById(any(Long.class))).thenReturn(true);
        var result = service.findAllByAccountId(account.getId());
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result).isNotEmpty();
        Assertions.assertThat(result.get(0).getId()).isEqualTo(transaction.getId());
        verify(repository).findAllByAccountId(account.getId());
        verify(accountService).existsById(account.getId());
    }

    @Test
    public void FindAllByAccountId_AccountWithIdNotExists_ThrowsException() {
        when(accountService.existsById(any(Long.class))).thenReturn(false);
        org.junit.jupiter.api.Assertions.assertThrows(NotFoundException.class, () -> service.findAllByAccountId(account.getId()));
        verify(accountService, times(1)).existsById(account.getId());
        verify(repository, times(0)).findAllByAccountId(account.getId());
    }

    @Test
    public void Insert_AccountExistsTransactionTypeINCOME_TransactionCreated() {
        var request = TransactionRequestDTO.builder().description("DESC")
                .amount(BigDecimal.valueOf(100)).type(TransactionType.INCOME).build();
        var accountDto = AccountDTO.builder().balance(account.getBalance())
                .name(account.getName()).currency(account.getCurrency()).id(account.getId()).build();
        when(accountService.findById(any(Long.class))).thenReturn(accountDto);
        when(repository.saveAndFlush(any(TransactionEntity.class))).thenReturn(transaction);
        var result = service.insert(account.getId(), request);
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isGreaterThan(0);
        verify(accountService).findById(any(Long.class));
        verify(repository).saveAndFlush(any(TransactionEntity.class));
        verify(entityManager).refresh(any(TransactionEntity.class));
    }

    @Test
    public void Insert_AccountExistsTransactionTypeEXPENSEInsufficientAccountBalance_ThrowException() {
        var request = TransactionRequestDTO.builder().description("DESC")
                .amount(BigDecimal.valueOf(100)).type(TransactionType.EXPENSE).build();
        var accountDto = AccountDTO.builder().balance(BigDecimal.valueOf(50))
                .name(account.getName()).currency(account.getCurrency()).id(account.getId()).build();
        when(accountService.findById(any(Long.class))).thenReturn(accountDto);
        org.junit.jupiter.api.Assertions.assertThrows(InsufficientAccountBalanceException.class, () -> service.insert(1l, request));
        verify(accountService, times(1)).findById(any(Long.class));
        verify(repository, times(0)).saveAndFlush(any(TransactionEntity.class));
        verify(entityManager, times(0)).refresh(any(TransactionEntity.class));
    }

    @Test
    public void Insert_AccountNotExists_ThrowsException() {
        when(accountService.findById(any(Long.class))).thenThrow(new NotFoundException());
        org.junit.jupiter.api.Assertions.assertThrows(NotFoundException.class, () -> service.insert(1l, null));
        verify(accountService, times(1)).findById(any(Long.class));
        verify(repository, times(0)).saveAndFlush(any(TransactionEntity.class));
        verify(entityManager, times(0)).refresh(any(TransactionEntity.class));
    }

    private ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setSkipNullEnabled(true);
        return mapper;
    }
}
