package org.unibl.etf.mybudgetbackend.services;


import org.unibl.etf.mybudgetbackend.models.dto.TransactionDTO;
import org.unibl.etf.mybudgetbackend.models.dto.TransactionRequestDTO;

import java.util.List;

/**
 * Service interface for managing transaction-related operations.
 * Provides abstract methods for CRUD operations on transaction entities.
 */
public interface TransactionService {
    List<TransactionDTO> findAll();
    TransactionDTO findById(Long id);
    List<TransactionDTO> findAllByAccountId(Long id);
    TransactionDTO insert(Long accountId,TransactionRequestDTO request);
}
