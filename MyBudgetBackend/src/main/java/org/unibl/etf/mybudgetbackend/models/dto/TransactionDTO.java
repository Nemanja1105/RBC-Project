package org.unibl.etf.mybudgetbackend.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.unibl.etf.mybudgetbackend.models.enums.TransactionType;

import java.math.BigDecimal;

/**
 * Data Transfer Object representing an transaction.
 * This class is used to transfer account data between service and controller layer
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {
    private Long id;
    private String description;
    private BigDecimal amount;
    private TransactionType type;
    private AccountDTO account;
}
