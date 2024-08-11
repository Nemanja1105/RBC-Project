package org.unibl.etf.mybudgetbackend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.unibl.etf.mybudgetbackend.exceptions.NotFoundException;
import org.unibl.etf.mybudgetbackend.models.dto.AccountDTO;
import org.unibl.etf.mybudgetbackend.models.dto.AccountRequestDTO;
import org.unibl.etf.mybudgetbackend.services.AccountService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {AccountController.class})
@ExtendWith(MockitoExtension.class)
public class AccountControllerTests {

    @Autowired
    private MockMvc mockMvc;
    private AccountDTO account;
    private AccountRequestDTO request;

    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private AccountService service;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.account = AccountDTO.builder().id(1l).name("Test").balance(BigDecimal.valueOf(100)).currency("EUR").build();
        this.request = AccountRequestDTO.builder().name("Test").balance(BigDecimal.valueOf(100)).currency("EUR").build();
    }

    @Test
    public void FindAll_ValidRequest_20OKWithContent() throws Exception {
        when(service.findAll()).thenReturn(Arrays.asList(account));
        mockMvc.perform(get("/api/v1/accounts").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
        verify(service).findAll();
    }

    @Test
    public void FindById_AccountExists_200OKWithContent() throws Exception {
        when(service.findById(any(Long.class))).thenReturn(account);
        mockMvc.perform(get("/api/v1/accounts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(account.getId()))
                .andExpect(jsonPath("$.name").value(account.getName()))
                .andExpect(jsonPath("$.balance").value(account.getBalance()))
                .andExpect(jsonPath("$.currency").value(account.getCurrency()));
        verify(service).findById(any(Long.class));
    }

    @Test
    public void FindById_AccountDoesntExist_404NotFound() throws Exception {
        when(service.findById(any(Long.class))).thenThrow(new NotFoundException());
        mockMvc.perform(get("/api/v1/accounts/1"))
                .andExpect(status().isNotFound());
        verify(service).findById(any(Long.class));
    }

    @Test
    public void Insert_ValidRequest_201CreatedWithContent() throws Exception {
        when(service.insert(any(AccountRequestDTO.class))).thenReturn(account);
        mockMvc.perform(post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value(request.getName()))
                .andExpect(jsonPath("$.balance").value(request.getBalance()))
                .andExpect(jsonPath("$.currency").value(request.getCurrency()));
        verify(service).insert(request);
    }

    @ParameterizedTest
    @MethodSource("accountRequestFailTestData")
    public void Insert_InvalidRequest_400BadRequest(String name, BigDecimal balance, String currency) throws Exception {
        var req = AccountRequestDTO.builder().name(name).balance(balance).currency(currency).build();
        mockMvc.perform(post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
        verify(service, times(0)).insert(req);
    }

    @Test
    public void Update_ValidRequestAccountExists_20OKWithContent() throws Exception {
        when(service.update(any(Long.class), any(AccountRequestDTO.class))).thenReturn(account);
        mockMvc.perform(put("/api/v1/accounts/" + account.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value(request.getName()))
                .andExpect(jsonPath("$.balance").value(request.getBalance()))
                .andExpect(jsonPath("$.currency").value(request.getCurrency()));
        verify(service).update(any(Long.class), any(AccountRequestDTO.class));
    }

    @Test
    public void Update_ValidRequestAccountNotExists_404BadRequest() throws Exception {
        when(service.update(any(Long.class), any(AccountRequestDTO.class))).thenThrow(new NotFoundException());
        mockMvc.perform(put("/api/v1/accounts/" + account.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
        verify(service).update(any(Long.class), any(AccountRequestDTO.class));
    }

    @ParameterizedTest
    @MethodSource("accountRequestFailTestData")
    public void Update_InvalidRequest_400BadRequest(String name, BigDecimal balance, String currency) throws Exception {
        var req = AccountRequestDTO.builder().name(name).balance(balance).currency(currency).build();
        mockMvc.perform(put("/api/v1/accounts/" + account.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
        verify(service, times(0)).insert(req);
    }

    @Test
    public void Delete_AccountExists_200OK()throws Exception{
        mockMvc.perform(delete("/api/v1/accounts/"+account.getId()))
                .andExpect(status().isOk());
        verify(service).delete(any(Long.class));
    }

    @Test
    public void Delete_AccountNotExists_404NotFound()throws Exception{
        doThrow(new NotFoundException()).when(service).delete(any(Long.class));
        mockMvc.perform(delete("/api/v1/accounts/"+account.getId()))
                .andExpect(status().isNotFound());
    }


    private static Stream<Arguments> accountRequestFailTestData() {
        return Stream.of(
                Arguments.of("jpGswEatfTiVGXzqRXzjtGiDq" +
                        "yyqilpgkPCFmctsbQwGBphGTG" +
                        "evjJFaLBIZUALwkBEyPnACpnF" +
                        "zgkwvUrMZfxWTMdxadaNAyUsc" +
                        "VvdxjKKRfPjSuIiHpSzYjUhQf" +
                        "oWhRvNwozuQBmvNlkGmqsAWNK" +
                        "XbCGWdNdYbJUJksKfCpLUHkbK" +
                        "LvJNtnjYdByyBOjbRaOWmpJuF" +
                        "IcUpExUgGXWKUuNMeyVDlLONb" +
                        "gDYiEOmfubcCViMtootCmbkoA" +
                        "kxjSHkoNiqeuoMyigFutrViFm" +
                        "rHoXLmCKYbjRdxzfrsAbWZbEV", new BigDecimal("125.35"), "EUR"),
                Arguments.of("", new BigDecimal("125.35"), "EUR"),
                Arguments.of("Nikola Jakovljevic", null, "BAM"),
                Arguments.of("Nikola Jakovljevic", BigDecimal.valueOf(-2.0f), "BAM"),
                Arguments.of("Sarah Linkoln", new BigDecimal("78.00"), ""),
                Arguments.of("Sarah Linkoln", new BigDecimal("78.00"), "123456789123456789")
        );
    }
}
