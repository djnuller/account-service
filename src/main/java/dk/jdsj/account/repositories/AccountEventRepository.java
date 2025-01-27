package dk.jdsj.account.repositories;

import dk.jdsj.account.entities.AccountEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountEventRepository extends JpaRepository<AccountEventEntity, Long> {

        @Query("SELECT a FROM AccountEventEntity a WHERE a.account.accountNumber = ?1")
        List<AccountEventEntity> findAllByAccountNumber(final String accountNumber);
}
