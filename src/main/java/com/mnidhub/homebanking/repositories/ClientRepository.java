package com.mnidhub.homebanking.repositories;

import com.mnidhub.homebanking.models.Card;
import com.mnidhub.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ClientRepository extends JpaRepository<Client,Long> {
    Client findByEmail(String email);


}
