package org.unibl.etf.mybudgetbackend.models.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.unibl.etf.mybudgetbackend.models.enums.TransactionType;

import java.math.BigDecimal;

/**
 * This class is used to validate and encapsulate the data required for creating or updating a transaction.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequestDTO {
    @NotBlank
    @Length(max = 255)
    private String description;
    @NotNull
    @Positive(message = "Amount must be greater than zero")
    private BigDecimal amount;
    @NotNull
    private TransactionType type;
}
