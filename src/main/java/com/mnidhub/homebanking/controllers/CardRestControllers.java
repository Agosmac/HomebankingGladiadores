package com.mnidhub.homebanking.controllers;

import com.mnidhub.homebanking.models.*;
import com.mnidhub.homebanking.repositories.CardRepository;
import com.mnidhub.homebanking.repositories.ClientRepository;
import com.mnidhub.homebanking.utils.CardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.plaf.synth.ColorType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

//import static com.mnidhub.homebanking.utils.CardUtils.getRandomNumber;

@RestController
@RequestMapping("web/api")
public class CardRestControllers {

    @Autowired
    public ClientRepository repo;
    @Autowired
    public CardRepository repoCard;

    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> registerCard(Authentication authentication, @RequestParam CardType cardType, @RequestParam CardColor cardColor) {


        Client clientConnect = repo.findByEmail(authentication.getName());

        AtomicInteger count= new AtomicInteger();

        if (clientConnect != null) {

            Stream<Card> stream = clientConnect.getCards().stream();
            stream.forEach((card) -> {if (card.getType() == cardType) count.getAndIncrement();});

            if (count.get() > 2) {
                return new ResponseEntity<>("You have alredy 3 cards of that type", HttpStatus.FORBIDDEN);
            } else {

                /*String randomStr = "";
                for(int i=0 ; i<4; i++){
                    if(i==3){
                        randomStr += getRandomNumber(1001,10000);
                    }
                    else{
                        randomStr += getRandomNumber(1001,10000)+"-";
                    }
                }*/

                Card card = new Card( clientConnect.getFirstName()+" "+clientConnect.getLastName(),cardType, cardColor, CardUtils.getRandomNumber(1001,10000),CardUtils.getRandomNumberCvv(101,1000), LocalDate.now(), LocalDate.now().plusYears(5));

                clientConnect.addCard(card);

                repoCard.save(card);
                return new ResponseEntity<>(HttpStatus.CREATED);
            }
        } else {
            return new ResponseEntity<>("Usuario no autentiado", HttpStatus.FORBIDDEN);
        }
    }

    /*public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }*/

    /*public int getRandomNumberCvv(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }*/
}
