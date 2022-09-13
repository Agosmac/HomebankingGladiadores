package com.mnidhub.homebanking.controllers;

import com.mnidhub.homebanking.dtos.AccountDTO;
import com.mnidhub.homebanking.dtos.ClientDTO;
import com.mnidhub.homebanking.models.Account;
import com.mnidhub.homebanking.models.Client;
import com.mnidhub.homebanking.repositories.AccountRepository;
import com.mnidhub.homebanking.repositories.ClientRepository;
import org.aspectj.apache.bcel.generic.InstructionConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class AccountRestControllers {
    @Autowired
    private AccountRepository repo;
    @Autowired
    private ClientRepository repoClient;

    @RequestMapping("/accounts")
    public List<AccountDTO> getAll(){
        return repo.findAll().stream().map(AccountDTO::new).collect(toList());
    }

    @RequestMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        return repo.findById(id).map(AccountDTO::new).orElse(null);
    }

    @RequestMapping("/clients/current/accounts")
    public List<AccountDTO> getAccountsClients(Authentication authentication){
        Client client = repoClient.findByEmail(authentication.getName());
        return client.getAccounts().stream().map(AccountDTO::new).collect(toList());
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> registerAccount(Authentication authentication) {

        Client clientConnect = repoClient.findByEmail(authentication.getName());

        if(clientConnect != null){
            if(clientConnect.getAccounts().size() > 2){
                return new ResponseEntity<>("You have alredy 3 accounts",HttpStatus.FORBIDDEN);
            }else{
                Account account = new Account("VIN-"+getRandomNumber(10000001,100000000), LocalDateTime.now(),0.00);

                clientConnect.addAccount(account);

                repo.save(account);
                return new ResponseEntity<>(HttpStatus.CREATED);
            }
        }else{
            return new ResponseEntity<>("Usuario no autentiado", HttpStatus.FORBIDDEN);
        }
    }
    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
