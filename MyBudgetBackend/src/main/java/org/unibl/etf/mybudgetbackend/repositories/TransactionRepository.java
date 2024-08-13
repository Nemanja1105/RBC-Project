package org.unibl.etf.mybudgetbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.unibl.etf.mybudgetbackend.models.entities.TransactionEntity;

import java.util.List;

/**
 * Repository interface for performing CRUD operations in database on AccountEntity entities.
 */
@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity,Long> {
    /**
     * Retrieves all transaction entities associated with the specified account ID.
     *
     * @param id the ID of the account whose transactions are to be retrieved.
     * @return a list of TransactionEntity objects associated with the given account ID,
     *         an empty list if no transactions are found
     */
    List<TransactionEntity> findAllByAccountId(Long id);
}
