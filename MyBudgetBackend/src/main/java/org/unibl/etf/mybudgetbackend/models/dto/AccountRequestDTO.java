package org.unibl.etf.mybudgetbackend.models.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

/**
 * This class is used to validate and encapsulate the data required for creating or updating an account.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountRequestDTO {
    @NotBlank
    @Length(max = 255)
    private String name;
    @NotNull
    @Min(0)
    private BigDecimal balance;
    @NotBlank
    @Length(max = 15)
    private String currency;
}
