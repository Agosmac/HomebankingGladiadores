package com.mnidhub.homebanking.controllers;

import com.mnidhub.homebanking.dtos.ClientDTO;
import com.mnidhub.homebanking.models.Account;
import com.mnidhub.homebanking.models.Client;
import com.mnidhub.homebanking.repositories.AccountRepository;
import com.mnidhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;
import java.time.LocalDateTime;
import java.util.List;

import static com.mnidhub.homebanking.utils.CardUtils.getRandomNumber;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class ClientRestControllers {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    public ClientRepository repo;
    @Autowired
    public AccountRepository repoAccount;

    @RequestMapping("/clients")
    public List<ClientDTO> getAll(){
        return repo.findAll().stream().map(ClientDTO::new).collect(toList());
    }

    @RequestMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){
        //return new ClientDTO((repo.findById(id).get()));
        return repo.findById(id).map(ClientDTO::new).orElse(null);
    }

    @GetMapping("/clients/current")
    public ClientDTO getClient(Authentication authentication){
        Client client = this.repo.findByEmail(authentication.getName());
        return new ClientDTO(client);
    }

    @PostMapping("/clients")
    public ResponseEntity<Object> register(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String email, @RequestParam String password) {

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (repo.findByEmail(email) !=  null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }

        Client client = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        repo.save(client);

        Account account = new Account("VIN-"+getRandomNumber(10000001,100000000), LocalDateTime.now(),0.00);

        client.addAccount(account);

        repoAccount.save(account);


        return new ResponseEntity<>(HttpStatus.CREATED);
        }
    /*public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }*/

    boolean estado= true;
    @PostMapping("/clients/{id}")
    public void bloquearUsuarios(Long id){
        String confirmar = JOptionPane.showInputDialog("¿Desea confirmar el cambio?");
         estado= false;

         JOptionPane.showMessageDialog(null, "Cambio de Estado realizado");
    }

}
