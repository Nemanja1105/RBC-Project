package org.unibl.etf.mybudgetbackend.controllers;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.mybudgetbackend.models.dto.AccountDTO;
import org.unibl.etf.mybudgetbackend.models.dto.AccountRequestDTO;
import org.unibl.etf.mybudgetbackend.services.AccountService;

/**
 * REST controller for managing account-related operations.
 * Provides endpoints for creating, reading, updating, and deleting account records.
 */
@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    /**
     * Retrieves the accounts page
     *
     * @param page the Pageable object that contains pagination information
     * @return a  Page of  AccountDTO objects
     */
    @GetMapping
    public Page<AccountDTO> findAll(Pageable page) {
        return this.service.findAll(page);
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
}
