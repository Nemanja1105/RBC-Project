package org.unibl.etf.mybudgetbackend.models.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Data Transfer Object representing an account.
 * This class is used to transfer account data between service and controller layer
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {
    private Long id;
    private String name;
    private BigDecimal balance;
    private String currency;
}
