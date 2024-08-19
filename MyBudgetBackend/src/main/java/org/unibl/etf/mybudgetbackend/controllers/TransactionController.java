package org.unibl.etf.mybudgetbackend.controllers;

import org.springframework.web.bind.annotation.*;
import org.unibl.etf.mybudgetbackend.models.dto.TransactionDTO;
import org.unibl.etf.mybudgetbackend.services.TransactionService;

import java.util.List;

/**
 * REST controller for managing transaction-related operations.
 */
@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {
    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    /**
     * Retrieves the transactions in the system
     *
     * @return a  List of TransactionDTO objects
     */
    @GetMapping
    public List<TransactionDTO> findAll() {
        return this.service.findAll();
    }

    /**
     * Retrieves the details of a specific transaction by its ID.
     *
     * @param id the ID of the transaction to retrieve
     * @return the  TransactionDTO representing the account with the specified ID
     */
    @GetMapping("/{id}")
    public TransactionDTO findById(@PathVariable Long id) {
        return this.service.findById(id);
    }
}
