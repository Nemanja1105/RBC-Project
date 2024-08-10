package org.unibl.etf.mybudgetbackend.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.mybudgetbackend.models.dto.AccountDTO;
import org.unibl.etf.mybudgetbackend.models.dto.AccountRequestDTO;
import org.unibl.etf.mybudgetbackend.services.AccountService;


@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    @GetMapping
    public Page<AccountDTO> findAll(Pageable page) {
        return this.service.findAll(page);
    }

    @GetMapping("/{id}")
    public AccountDTO findById(@PathVariable Long id) {
        return this.service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountDTO insert(@RequestBody @Valid AccountRequestDTO request) {
        return this.service.insert(request);
    }

    @PutMapping("/{id}")
    public AccountDTO update(@PathVariable Long id, @RequestBody @Valid AccountRequestDTO request) {
        return this.service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        this.service.delete(id);
    }
}
