package org.unibl.etf.mybudgetbackend.runners;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.unibl.etf.mybudgetbackend.models.entities.AccountEntity;
import org.unibl.etf.mybudgetbackend.models.entities.TransactionEntity;
import org.unibl.etf.mybudgetbackend.models.enums.TransactionType;
import org.unibl.etf.mybudgetbackend.models.xml_wrappers.Accounts;
import org.unibl.etf.mybudgetbackend.repositories.AccountRepository;
import org.unibl.etf.mybudgetbackend.repositories.TransactionRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Component responsible for seeding the database with initial data.
 * Implements the  ApplicationRunner interface to execute logic
 * after the application context has been loaded.
 */
@Component
@Slf4j
public class DatabaseSeeder implements ApplicationRunner {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final ResourceLoader resourceLoader;

    @Value("${database.seed.url}")
    private String resourceUrl;

    public DatabaseSeeder(AccountRepository repository, TransactionRepository transactionRepository, ResourceLoader resourceLoader) {
        this.accountRepository = repository;
        this.transactionRepository = transactionRepository;
        this.resourceLoader = resourceLoader;
    }

    /**
     * Runs the seeding process.
     *
     * @param args the application arguments
     * @throws Exception if an error occurs during seeding
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (this.accountRepository.count() > 0)
            return;

        try {
            var accounts = this.readXml();
            this.saveToDb(accounts);
            log.info("Database successfully seeded.");

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Reads accounts data from an XML file.
     *
     * @return the accounts data
     * @throws IOException if an error occurs while reading the XML file
     */
    private Accounts readXml() throws IOException {
        XmlMapper mapper = new XmlMapper();
        Resource resource = resourceLoader.getResource("classpath:" + resourceUrl);
        return mapper.readValue(resource.getInputStream(), Accounts.class);
    }


    /**
     * Saves accounts and their transactions to the database.
     *
     * @param accounts the accounts data
     */
    @Transactional
    private void saveToDb(Accounts accounts) {
        List<AccountEntity> accountEntities = new ArrayList<>();
        List<TransactionEntity> transactionEntities = new ArrayList<>();

        for (var el : accounts.getAccounts()) {
            AccountEntity account = AccountEntity.builder()
                    .name(el.getName())
                    .currency(el.getCurrency().toLowerCase())
                    .balance(BigDecimal.valueOf(el.getBalance()))
                    .build();
            accountEntities.add(account);

            if (el.getTransactions() != null) {
                for (var transaction : el.getTransactions()) {
                    TransactionEntity transactionEntity = TransactionEntity.builder()
                            .description(transaction.getDescription())
                            .amount(BigDecimal.valueOf(Math.abs(transaction.getAmount().getValue())))
                            .type(transaction.getAmount().getValue() >= 0 ? TransactionType.INCOME : TransactionType.EXPENSE)
                            .account(account)
                            .build();
                    transactionEntities.add(transactionEntity);
                }
            }
        }

        this.accountRepository.saveAll(accountEntities);
        this.transactionRepository.saveAll(transactionEntities);
    }


}
