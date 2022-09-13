package com.mnidhub.homebanking.repositories;

import com.mnidhub.homebanking.models.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface AccountRepository extends JpaRepository<Account,Long> {
    Account findByNumber(String number);
}
