package com.mnidhub.homebanking.controllers;

import com.mnidhub.homebanking.dtos.ClientDTO;
import com.mnidhub.homebanking.dtos.LoanApplicationDTO;
import com.mnidhub.homebanking.dtos.LoanDTO;
import com.mnidhub.homebanking.models.*;
import com.mnidhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RestController
public class LoanRestController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    @PostMapping("/api/loans")
    public ResponseEntity<Object> createLoan(Authentication authentication, @RequestBody LoanApplicationDTO loanApplicationDTO){

        Client clientConnect = clientRepository.findByEmail(authentication.getName());
        if(clientConnect == null) return new ResponseEntity<>("Client isnt authorization", HttpStatus.FORBIDDEN);
        System.out.println(loanApplicationDTO.toString());
        if(loanApplicationDTO.getLoanId() == 0 || loanApplicationDTO.getPayments() == 0 || loanApplicationDTO.getToAccountNumber().isEmpty() || loanApplicationDTO.getAmount() <= 0) return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);

        /*Loan loan = null;
        Optional respuesta = loanRepository.findById(loanId);
        if (respuesta.isPresent()){
            loan = (Loan) respuesta.get();
        } else {
            return new ResponseEntity<>("No existe ese tipo de prestamo", HttpStatus.FORBIDDEN);
        }*/

        Loan loan = loanRepository.findById(loanApplicationDTO.getLoanId()).get();
        if(loan == null) return new ResponseEntity<>("No existe ese tipo de prestamo", HttpStatus.FORBIDDEN);

        if(loanApplicationDTO.getAmount() > loan.getMaxAmount()) return new ResponseEntity<>("Prestamo excedido", HttpStatus.FORBIDDEN);

        if(!loan.getPayments().contains(loanApplicationDTO.getPayments())) return new ResponseEntity<>("Cantidad de cuotas no permitidas", HttpStatus.FORBIDDEN);

        Account accountDestino = accountRepository.findByNumber(loanApplicationDTO.getToAccountNumber());
        if(accountDestino == null) return new ResponseEntity<>("Account dosent exist", HttpStatus.FORBIDDEN);

        if(!clientConnect.getAccounts().contains(accountDestino)) return new ResponseEntity<>("Account dosent exist", HttpStatus.FORBIDDEN);;

        ClientLoan clientLoan = new ClientLoan(loanApplicationDTO.getAmount()*1.2,loanApplicationDTO.getPayments());

        clientConnect.addClientLoan(clientLoan);
        loan.addClientLoan(clientLoan);

        clientLoanRepository.save(clientLoan);

        Transaction transactionDestino = new Transaction(TransactionType.CREDIT, loanApplicationDTO.getAmount(), loan.getName()+" - loan approved", LocalDateTime.now());

        accountDestino.addTransaction(transactionDestino);
        transactionRepository.save(transactionDestino);

        accountDestino.setBalance(accountDestino.getBalance()+loanApplicationDTO.getAmount());
        accountRepository.save(accountDestino);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping("/api/loans")
    public List<LoanDTO> getAll(){
        return loanRepository.findAll().stream().map(LoanDTO::new).collect(toList());
    }
}
