package org.unibl.etf.mybudgetbackend.services.impl;

import jakarta.transaction.Transactional;
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

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AccountRepository repository;
    private final ModelMapper mapper;

    public AccountServiceImpl(AccountRepository repository, ModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Page<AccountDTO> findAll(Pageable page) {
        return this.repository.findAll(page).map(el -> this.mapper.map(el, AccountDTO.class));
    }

    @Override
    public AccountDTO findById(Long id) {
        return mapper.map(this.repository.findById(id).orElseThrow(NotFoundException::new), AccountDTO.class);
    }

    @Override
    public AccountDTO insert(AccountRequestDTO request) {
        var account = this.mapper.map(request, AccountEntity.class);
        account.setId(null);
        account = this.repository.saveAndFlush(account);
        return this.mapper.map(account, AccountDTO.class);
    }

    @Override
    public AccountDTO update(Long id, AccountRequestDTO request) {
        if (!this.repository.existsById(id))
            throw new NotFoundException();
        var account = this.mapper.map(request, AccountEntity.class);
        account.setId(id);
        account = this.repository.saveAndFlush(account);
        return this.mapper.map(account, AccountDTO.class);
    }

    @Override
    public void delete(Long id) {
        if (!this.repository.existsById(id))
            throw new NotFoundException();
        this.repository.deleteById(id);
    }
}
