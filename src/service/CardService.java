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
            BigDecimal creditLimit = new BigDecimal(args[2].replace("$", ""));

            Card card = new Card(cardNumber, user, creditLimit);
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
        if (args.length != 3) {
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
        if (user.getCard() != null) {
            validation.addError(Error.USER_ALREADY_HAS_CARD);
            return validation;
        }
        if (card != null) {
            validation.addError(Error.CARD_BELONGS_TO_ANOTHER_USER);
            return validation;
        }
        if (!Luhn.validate(cardNumber)) {
            validation.addError(Error.CARD_NUMBER_INVALID);
            return validation;
        }

        return validation;
    }

    /**
     * Create charge on user's card and update card's balance
     *
     * @param args
     * @return
     */
    public String charge(String[] args) {
        String result = "";
        Validation validation = validateCharge(args);
        if (validation.isValid()) {
            // add charge on user's card and increase balance
            String username = args[0];
            BigDecimal charge = new BigDecimal(args[1].replace("$", ""));
            User user = Database.getUser(username);
            Card card = user.getCard();
            BigDecimal newBalance = card.getBalance().add(charge);
            card.setBalance(newBalance);

            // persist new card balance
            Database.setCard(card);
            user.setCard(card);
            Database.setUser(user);
        } else {
            result = validation.getErrorString();
        }

        return result;
    }

    /**
     * Run validations before charging a user's card
     *
     * @param args
     * @return
     */
    private Validation validateCharge(String[] args) {
        Validation validation = new Validation();
        if (args.length != 2) {
            validation.addError(Error.INVALID_ARGS);
            return validation;
        }

        String username = args[0];
        String charge = args[1];
        if (!Money.validate(charge)) {
            validation.addError(Error.CHARGE_AMOUNT_INVALID);
            return validation;
        }

        User user = Database.getUser(username);
        if (user == null) {
            validation.addError(Error.USER_NOT_FOUND);
            return validation;
        }

        Card card = user.getCard();
        if (card == null) {
            validation.addError(Error.CARD_NOT_FOUND);
            return validation;
        }
        if (!Luhn.validate(card.getNumber())) {
            validation.addError(Error.CARD_NUMBER_INVALID);
            return validation;
        }

        BigDecimal chargeAmount = new BigDecimal(charge.replace("$", ""));
        BigDecimal newBalance = card.getBalance().add(chargeAmount);
        if (newBalance.compareTo(card.getCreditLimit()) == 1) {
            validation.addError(Error.CHARGE_DECLINED);
        }

        return validation;
    }

    /**
     * Credit money towards a card's balance
     *
     * @param args
     * @return
     */
    public String credit(String[] args) {
        String result = "";
        Validation validation = validateCredit(args);
        if (validation.isValid()) {
            // credit money on user's card and decrease balance
            String username = args[0];
            BigDecimal credit = new BigDecimal(args[1].replace("$", ""));
            User user = Database.getUser(username);
            Card card = user.getCard();
            BigDecimal newBalance = card.getBalance().subtract(credit);
            card.setBalance(newBalance);

            // persist new card balance
            Database.setCard(card);
            user.setCard(card);
            Database.setUser(user);
        } else {
            result = validation.getErrorString();
        }

        return result;
    }

    /**
     * Run validations before crediting money towards a card's balance
     *
     * @param args
     * @return
     */
    private Validation validateCredit(String[] args) {
        Validation validation = new Validation();
        if (args.length != 2) {
            validation.addError(Error.INVALID_ARGS);
            return validation;
        }

        String username = args[0];
        String credit = args[1];
        if (!Money.validate(credit)) {
            validation.addError(Error.CREDIT_AMOUNT_INVALID);
            return validation;
        }

        User user = Database.getUser(username);
        if (user == null) {
            validation.addError(Error.USER_NOT_FOUND);
            return validation;
        }

        Card card = user.getCard();
        if (card == null) {
            validation.addError(Error.CARD_NOT_FOUND);
            return validation;
        }
        if (!Luhn.validate(card.getNumber())) {
            validation.addError(Error.CARD_NUMBER_INVALID);
            return validation;
        }

        return validation;
    }
}
