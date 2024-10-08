package org.unibl.etf.mybudgetbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.unibl.etf.mybudgetbackend.models.entities.AccountEntity;

/**
 * Repository interface for performing CRUD operations in database on AccountEntity entities.
 */
@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
}
