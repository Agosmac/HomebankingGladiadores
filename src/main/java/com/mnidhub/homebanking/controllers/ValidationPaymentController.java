package com.mnidhub.homebanking.controllers;

import com.mnidhub.homebanking.dtos.CardValidationDTO;
import com.mnidhub.homebanking.models.Account;
import com.mnidhub.homebanking.models.Card;
import com.mnidhub.homebanking.models.Client;
import com.mnidhub.homebanking.repositories.AccountRepository;
import com.mnidhub.homebanking.repositories.CardRepository;
import com.mnidhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Set;

@RestController
public class ValidationPaymentController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Transactional
    @PostMapping("/cardValidation")
    public String cardValidation(@RequestBody CardValidationDTO cardValidationDTO){
         Card card= cardRepository.findByNumber(cardValidationDTO.getCardNumber());


       if(card == null){
            return "Tarjeta inexistente";
       }

        Client client =  card.getClient();
        Set<Account> accounts = client.getAccounts();

       if(card.getCvv() != cardValidationDTO.getCvv()){
           return "Datos no coinciden";
       }

        Account account1= null;
        for (Account account:accounts) {
            if(account.getBalance()>=cardValidationDTO.getAmount()){
                account1= account;
            }
        }
        if(account1==null){
            return "Fondos Insuficientes";
        }
        if(card.getThruDate().isBefore(LocalDate.now())){
            return "Tarjeta vencida";
        }
        if (!card.getCardholder().equals(cardValidationDTO.getCardHolder())) {
            return"Datos no coinciden";
        }

        Account toAccount = accountRepository.findByNumber(cardValidationDTO.getToAccountNumber());
        if(toAccount == null){
            return "cuenta inexistente";
        }

        toAccount.setBalance(toAccount.getBalance() + cardValidationDTO.getAmount());
        account1.setBalance((account1.getBalance() - cardValidationDTO.getAmount()));

        accountRepository.save(toAccount);
        accountRepository.save(account1);

        return "validado";
    }



    }

