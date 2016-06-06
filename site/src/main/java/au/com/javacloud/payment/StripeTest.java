package au.com.javacloud.payment;

/**
 * Example usage of Stripe java bindings.
 * See https://stripe.com/docs/libraries#java-library
 *
 * Usage:
 * mkdir stripe-test
 * cd stripe-test
 * wget https://code.stripe.com/stripe-java-latest.jar
 * wget http://google-gson.googlecode.com/files/google-gson-2.2.2-release.zip
 * unzip google-gson-2.2.2-release.zip
 * javac -classpath stripe-java-latest.jar StripeTest.java
 * java -classpath stripe-java-latest.jar:google-gson-2.2.2/gson-2.2.2.jar:. StripeTest
 */

import java.util.Map;
import java.util.HashMap;

import com.stripe.model.Charge;
import com.stripe.Stripe;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.CardException;
import com.stripe.exception.APIException;

public class StripeTest {
    public static void main(String[] args) throws AuthenticationException, InvalidRequestException,
            APIConnectionException, CardException, APIException  {
        Stripe.apiKey = "tGN0bIwXnHdwOa85VABjPdSn8nWY7G7I"; // stripe public
        // test key

        final Map<String, Object> cardParams = new HashMap<String, Object>();
        cardParams.put("number", "4242424242424242");
        cardParams.put("exp_month", 12);
        cardParams.put("exp_year", 2015);
        cardParams.put("cvc", "123");
        cardParams.put("name", "J Bindings Cardholder");
        cardParams.put("address_line1", "140 2nd Street");
        cardParams.put("address_line2", "4th Floor");
        cardParams.put("address_city", "San Francisco");
        cardParams.put("address_zip", "94105");
        cardParams.put("address_state", "CA");
        cardParams.put("address_country", "USA");

        final Map<String, Object> chargeParams = new HashMap<String, Object>();
        chargeParams.put("amount", 100);
        chargeParams.put("currency", "usd");
        chargeParams.put("card", cardParams);

        final Charge charge = Charge.create(chargeParams);
        System.out.println(charge);
    }
}