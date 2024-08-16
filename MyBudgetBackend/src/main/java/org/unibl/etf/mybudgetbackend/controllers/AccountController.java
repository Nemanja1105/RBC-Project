package org.unibl.etf.mybudgetbackend.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.mybudgetbackend.models.dto.AccountDTO;
import org.unibl.etf.mybudgetbackend.models.dto.AccountRequestDTO;
import org.unibl.etf.mybudgetbackend.models.dto.TransactionDTO;
import org.unibl.etf.mybudgetbackend.models.dto.TransactionRequestDTO;
import org.unibl.etf.mybudgetbackend.services.AccountService;
import org.unibl.etf.mybudgetbackend.services.TransactionService;

import java.util.List;

/**
 * REST controller for managing account-related operations.
 * Provides endpoints for creating, reading, updating, and deleting account records.
 */
@RestController
@RequestMapping("/api/v1/accounts")
@CrossOrigin("http://localhost:4200")
public class AccountController {
    private final AccountService service;
    private final TransactionService transactionService;

    public AccountController(AccountService service, TransactionService transactionService) {
        this.service = service;
        this.transactionService = transactionService;
    }


    /**
     * Retrieves the accounts in the system
     *
     * @return a  List of  AccountDTO objects
     */
    @GetMapping
    public List<AccountDTO> findAll() {
        return this.service.findAll();
    }

    /**
     * Retrieves the details of a specific account by its ID.
     *
     * @param id the ID of the account to retrieve
     * @return the  AccountDTO representing the account with the specified ID
     */
    @GetMapping("/{id}")
    public AccountDTO findById(@PathVariable Long id) {
        return this.service.findById(id);
    }

    /**
     * Creates a new account based on the provided request data.
     * If the input data is not valid, the validator throws an exception that is handled by the global
     * exception handler.
     *
     * @param request the details of the account to create
     * @return the AccountDTO object representing the created account
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountDTO insert(@RequestBody @Valid AccountRequestDTO request) {
        return this.service.insert(request);
    }

    /**
     * Updates the details of an existing account.
     * If the requested account does not exist, an exception is thrown that is handled by the global exception handler
     * If the input data is not valid, the validator throws an exception that is handled by the global
     * exception handler.
     *
     * @param id      the ID of the account to update
     * @param request the new details of the account
     * @return the  AccountDTO representing the updated account
     */
    @PutMapping("/{id}")
    public AccountDTO update(@PathVariable Long id, @RequestBody @Valid AccountRequestDTO request) {
        return this.service.update(id, request);
    }

    /**
     * Deletes a specific account by its ID.
     * If the requested account does not exist, an exception is thrown that is handled by the global exception handler.
     *
     * @param id the ID of the account to delete
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        this.service.delete(id);
    }

    /**
     * Retrieves a list of all transactions associated with a specified account.
     * If the requested account does not exist, an exception is thrown that is handled by the global exception handler
     *
     * @param id the ID of the account for which transactions are being retrieved
     * @return a  List of TransactionDTO objects representing all transactions associated with the specified account
     */
    @GetMapping("/{id}/transactions")
    public List<TransactionDTO> getAllTransactionsByAccountId(@PathVariable Long id) {
        return this.transactionService.findAllByAccountId(id);
    }

    /**
     * Creates a new transaction for the specified account.
     *
     * @param id      the ID of the account for which the transaction is being created
     * @param request a  TransactionRequestDTO object containing the details of the transaction to be created
     * @return a  TransactionDTO representing the newly created transaction
     */
    @PostMapping("/{id}/transactions")
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionDTO insertTransaction(@PathVariable Long id, @RequestBody @Valid TransactionRequestDTO request) {
        return this.transactionService.insert(id, request);
    }

    /**
     * Delete all accounts and transactions in system
     */
    @DeleteMapping()
    public void deleteAll() {
        this.service.deleteAll();
        ;
    }
}
