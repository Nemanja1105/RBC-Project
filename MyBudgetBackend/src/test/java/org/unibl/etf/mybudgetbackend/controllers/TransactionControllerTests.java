package org.unibl.etf.mybudgetbackend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.unibl.etf.mybudgetbackend.exceptions.NotFoundException;
import org.unibl.etf.mybudgetbackend.models.dto.AccountDTO;
import org.unibl.etf.mybudgetbackend.models.dto.TransactionDTO;
import org.unibl.etf.mybudgetbackend.models.enums.TransactionType;
import org.unibl.etf.mybudgetbackend.services.TransactionService;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {TransactionController.class})
@ExtendWith(MockitoExtension.class)
public class TransactionControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private TransactionService service;

    private AccountDTO account;
    private TransactionDTO transaction;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.account = AccountDTO.builder().id(1l).name("Test").balance(BigDecimal.valueOf(100)).currency("EUR").build();
        this.transaction= TransactionDTO.builder().id(1l).description("DESC").amount(BigDecimal.valueOf(50)).type(TransactionType.INCOME).account(account).build();
    }

    @Test
    public void FindAll_ValidRequest_20OKWithContent() throws Exception {
        when(service.findAll()).thenReturn(Arrays.asList(transaction));
        mockMvc.perform(get("/api/v1/transactions").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
        verify(service).findAll();
    }

    @Test
    public void FindById_TransactionExists_200OKWithContent() throws Exception {
        when(service.findById(any(Long.class))).thenReturn(transaction);
        mockMvc.perform(get("/api/v1/transactions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(transaction.getId()))
                .andExpect(jsonPath("$.description").value(transaction.getDescription()))
                .andExpect(jsonPath("$.amount").value(transaction.getAmount()))
                .andExpect(jsonPath("$.type").value(transaction.getType().toString()));
        verify(service).findById(any(Long.class));
    }

    @Test
    public void FindById_TransactionDoesntExist_404NotFound() throws Exception {
        when(service.findById(any(Long.class))).thenThrow(new NotFoundException());
        mockMvc.perform(get("/api/v1/transactions/1"))
                .andExpect(status().isNotFound());
        verify(service).findById(any(Long.class));
    }
}
