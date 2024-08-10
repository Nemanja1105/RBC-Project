package org.unibl.etf.mybudgetbackend.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.unibl.etf.mybudgetbackend.models.dto.AccountDTO;
import org.unibl.etf.mybudgetbackend.models.dto.AccountRequestDTO;

/**
 * Service interface for managing account-related operations.
 * Provides abstract methods for CRUD operations on account entities.
 */
public interface AccountService {
    Page<AccountDTO> findAll(Pageable page);
    AccountDTO findById(Long id);
    AccountDTO insert(AccountRequestDTO request);
    AccountDTO update(Long id, AccountRequestDTO request);
    void delete(Long id);
}
