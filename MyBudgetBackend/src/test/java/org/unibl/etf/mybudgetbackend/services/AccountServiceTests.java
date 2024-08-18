package org.unibl.etf.mybudgetbackend.services;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.unibl.etf.mybudgetbackend.exceptions.NotFoundException;
import org.unibl.etf.mybudgetbackend.models.dto.AccountDTO;
import org.unibl.etf.mybudgetbackend.models.dto.AccountRequestDTO;
import org.unibl.etf.mybudgetbackend.models.entities.AccountEntity;
import org.unibl.etf.mybudgetbackend.repositories.AccountRepository;
import org.unibl.etf.mybudgetbackend.services.impl.AccountServiceImpl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class AccountServiceTests {

    @Mock
    private AccountRepository repository;
    @InjectMocks
    private AccountServiceImpl service;
    private AccountEntity account;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        service = new AccountServiceImpl(repository, modelMapper());
        this.account = AccountEntity.builder()
                .name("Marko Markovic")
                .id(1l)
                .balance(BigDecimal.valueOf(256.5))
                .currency("EUR").build();
    }

    //METHOD-NAME_STATE_EXPECTATION

    @Test
    public void FindAll_ValidRequest_ListOfAccoutDTO() {
        when(repository.findAll()).thenReturn(Arrays.asList(account));
        List<AccountDTO> result = service.findAll();
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.size()).isEqualTo(1);
        verify(repository).findAll();
    }

    @Test
    public void FindAll_ValidPage_PageWithContent() {
        Pageable pageable = PageRequest.of(0, 1);
        Page<AccountEntity> page = new PageImpl<>(Arrays.asList(account), pageable, 1);
        when(repository.findAll(pageable)).thenReturn(page);
        Page<AccountDTO> result = service.findAllWithPage(pageable);
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getContent()).isNotNull();
        Assertions.assertThat(result.getTotalElements()).isEqualTo(1);
        verify(repository).findAll(pageable);
    }

    @Test
    public void FindById_AccountWithIdExists_AccountDTO() {
        when(repository.findById(account.getId())).thenReturn(Optional.of(account));
        var accountDto = service.findById(account.getId());
        Assertions.assertThat(accountDto).isNotNull();
        Assertions.assertThat(accountDto.getId()).isEqualTo(account.getId());
        verify(repository).findById(account.getId());
    }

    @Test
    public void FindById_AccountWithIdNotExists_ThrowsException() {
        when(repository.findById(any(Long.class))).thenReturn(Optional.empty());
        org.junit.jupiter.api.Assertions.assertThrows(NotFoundException.class, () -> service.findById(1l));
        verify(repository).findById(any(Long.class));
    }

    @Test
    public void Insert_ValidRequest_AccountDTO() {
        when(repository.saveAndFlush(any(AccountEntity.class))).thenReturn(account);
        AccountRequestDTO request = new AccountRequestDTO(account.getName(), account.getBalance(), account.getCurrency());
        var dto = service.insert(request);
        Assertions.assertThat(dto).isNotNull();
        Assertions.assertThat(dto.getId()).isGreaterThan(0);
        verify(repository).saveAndFlush(any(AccountEntity.class));
    }

    @Test
    public void Update_AccountWithIdExists_AccountDTO() {
        var updatedAccount = new AccountEntity(account.getId(), "TEST", account.getBalance(), account.getCurrency(), null);
        when(repository.existsById(any(Long.class))).thenReturn(true);
        when(repository.saveAndFlush(any(AccountEntity.class))).thenReturn(updatedAccount);
        var request = new AccountRequestDTO("TEST", account.getBalance(), account.getCurrency());
        var dto = service.update(account.getId(), request);
        Assertions.assertThat(dto).isNotNull();
        Assertions.assertThat(dto.getName()).isEqualTo("TEST");
        verify(repository).existsById(account.getId());
    }

    @Test
    public void Update_AccountWithIdNotExists_ThrowsException() {
        when(repository.existsById(any(Long.class))).thenReturn(false);
        var request = new AccountRequestDTO("TEST", account.getBalance(), account.getCurrency());
        org.junit.jupiter.api.Assertions.assertThrows(NotFoundException.class, () -> service.update(4l, request));
        verify(repository).existsById(4l);
        verify(repository, times(0)).saveAndFlush(any(AccountEntity.class));
    }

    @Test
    public void Delete_AccountWithIdExists_AccountDeleted() {
        Long id = 1l;
        when(repository.existsById(any(Long.class))).thenReturn(true);
        service.delete(id);
        verify(repository, times(1)).deleteById(id);
    }

    @Test
    public void Delete_AccountWithIdNotExists_AccountDeleted() {
        Long id = 1l;
        when(repository.existsById(any(Long.class))).thenReturn(false);
        org.junit.jupiter.api.Assertions.assertThrows(NotFoundException.class, () -> service.delete(id));
        verify(repository, times(0)).deleteById(id);
    }

    @Test
    public void ExistsById_AccountWithIdExists_ReturnTrue() {
        Long id = 1l;
        when(repository.existsById(any(Long.class))).thenReturn(true);
        var result = service.existsById(id);
        Assertions.assertThat(result).isTrue();
        verify(repository).existsById(any(Long.class));
    }

    @Test
    public void ExistsById_AccountWithIdNotExists_ReturnFalse() {
        Long id = 1l;
        when(repository.existsById(any(Long.class))).thenReturn(false);
        var result = service.existsById(id);
        Assertions.assertThat(result).isFalse();
        verify(repository).existsById(any(Long.class));
    }

    private ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setSkipNullEnabled(true);
        return mapper;
    }
}
