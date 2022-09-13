package com.mnidhub.homebanking;

import com.mnidhub.homebanking.utils.CardUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureTestDatabase()
public class CardUtilsTest {

    @Test
    public void cardNumberIsCreated(){
        String cardNumber = CardUtils.getRandomNumber(1001,10000);
        assertThat(cardNumber,is(not(emptyOrNullString())));
    }

    @Test
    public void cardNumberCvvIsCreated(){
        int cardNumber = CardUtils.getRandomNumberCvv(101,1000);
        assertThat(cardNumber,is(not(0)));
        assertThat(cardNumber,lessThan(1000));
        assertThat(cardNumber,greaterThan(99));
    }
}
