package org.unibl.etf.mybudgetbackend.models.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SoftDelete;
import org.unibl.etf.mybudgetbackend.models.enums.TransactionType;

import java.math.BigDecimal;

/**
 * Represents a transaction table in the database.
 * Has support for soft deletion.
 * All transactions for the same account are of the same currency.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "transaction")
@SoftDelete
public class TransactionEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(name = "description", nullable = false)
    private String description;

    @Basic
    @Column(name = "amount", nullable = false, precision = 13, scale = 2)
    private BigDecimal amount;

    @Basic
    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private TransactionType type;

    @ManyToOne()
    @JoinColumn(name = "account_id", referencedColumnName = "id", nullable = false)
    private AccountEntity account;


}
