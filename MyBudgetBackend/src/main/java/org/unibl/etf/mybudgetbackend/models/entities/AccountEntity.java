package org.unibl.etf.mybudgetbackend.models.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SoftDelete;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "account")
@SoftDelete
public class AccountEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic
    @Column(name = "name", nullable = false)
    private String name;
    @Basic
    @Column(name = "balance", nullable = false, precision = 13, scale = 2) //Generally Accepted Accounting Principles
    private BigDecimal balance;
    @Basic
    @Column(name = "currency", nullable = false,length = 15)
    private String currency;
}
