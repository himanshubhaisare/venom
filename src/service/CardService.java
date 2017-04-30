package service;

import constants.Error;
import database.Database;
import resource.Card;
import resource.User;
import validator.Luhn;
import validator.Money;
import validator.Validation;
import java.math.BigDecimal;

public class CardService {

    public CardService() {
    }

    /**
     * Create card
     *
     * @param args
     * @return
     */
    public String create(String[] args) {
        String result = "";
        Validation validation = validateCreate(args);
        if (validation.isValid()) {
            User user = Database.getUser(args[0]);
            String cardNumber = args[1];
            String creditLimit = args[2];

            Card card = new Card(cardNumber, user, new BigDecimal(creditLimit));
            Database.setCard(card);
            user.setCard(card);
            Database.setUser(user);
        } else {
            result = validation.getErrorString();
        }

        return result;
    }

    /**
     * Run validations before creating card
     *
     * @param args
     * @return
     */
    private Validation validateCreate(String[] args) {
        Validation validation = new Validation();
        Card card;
        if (args.length < 3) {
            validation.addError(Error.INVALID_ARGS);
            return validation;
        }

        String username = args[0];
        String cardNumber = args[1];
        String creditLimit = args[2];

        if (!Money.validate(creditLimit)) {
            validation.addError(Error.CREDIT_LIMIT_AMOUNT_INVALID);
            return validation;
        }

        User user = Database.getUser(username);
        card = Database.getCard(cardNumber);
        if (user == null) {
            validation.addError(Error.USER_NOT_FOUND);
            return validation;
        }
        if (card != null) {
            validation.addError(Error.CARD_BELONGS_TO_ANOTHER_USER);
            return validation;
        }
        if (user.getCard() != null) {
            validation.addError(Error.USER_ALREADY_HAS_CARD);
            return validation;
        }
        if (!Luhn.validate(cardNumber)) {
            validation.addError(Error.CARD_NUMBER_INVALID);
            return validation;
        }

        return validation;
    }

    public String charge(String[] args) {
        return null;
    }

    public String credit(String[] args) {
        return null;
    }
}
