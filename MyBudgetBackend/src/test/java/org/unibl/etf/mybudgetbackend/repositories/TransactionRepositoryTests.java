package org.unibl.etf.mybudgetbackend.repositories;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.unibl.etf.mybudgetbackend.models.entities.AccountEntity;
import org.unibl.etf.mybudgetbackend.models.entities.TransactionEntity;
import org.unibl.etf.mybudgetbackend.models.enums.TransactionType;

import java.math.BigDecimal;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class TransactionRepositoryTests {
    @Autowired
    private TransactionRepository repository;
    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void FindByAccountId_AccountWithIdExists_ListOfTransactions(){
        var account=AccountEntity.builder().name("TEST").balance(BigDecimal.valueOf(100.0f)).currency("EUR").build();
        var transaction=TransactionEntity.builder().description("TEST DESC").type(TransactionType.EXPENSE).amount(BigDecimal.valueOf(50.0f)).account(account).build();
        this.accountRepository.save(account);
        this.repository.save(transaction);
        var result=this.repository.findAllByAccountId(account.getId());
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result).isNotEmpty();
        Assertions.assertThat(result.get(0).getId()).isEqualTo(account.getId());
    }

    @Test
    public void FindByAccountId_AccountWithIdNotExists_EmptyList(){
        var account=AccountEntity.builder().name("TEST").balance(BigDecimal.valueOf(100.0f)).currency("EUR").build();
        var transaction=TransactionEntity.builder().description("TEST DESC").type(TransactionType.EXPENSE).amount(BigDecimal.valueOf(50.0f)).account(account).build();
        this.accountRepository.save(account);
        this.repository.save(transaction);
        var result=this.repository.findAllByAccountId(10l);
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result).isEmpty();
    }

}
