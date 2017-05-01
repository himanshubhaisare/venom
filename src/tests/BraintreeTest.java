package tests;

import constants.Error;
import database.Database;
import resource.Card;
import resource.User;
import service.Braintree;

import java.math.BigDecimal;
import java.util.HashMap;

public class BraintreeTest {

    private static final String BAD_COMMAND = "test bad command is not recognized";
    private static final String ADD_CARD_WITHOUT_USER = "test cannot add card without providing user name";
    private static final String ADD_CARD_ON_NON_EXISTING_USER = "test cannot add card on a user that does not exist";
    private static final String ADD_CARD_WITH_USER = "test user can add a card";
    private static final String ADD_NON_LUHN_CARD = "test cannot add a card that's non Luhn compatible";
    private static final String ADD_SECOND_CARD = "test cannot add second card on a user";
    private static final String ADD_ANOTHER_USERS_CARD = "test cannot add a card that belongs to another user";
    private static final String CHARGE_ON_CARD_AFTER_PAYMENT = "test that actor's card is charged after payment";

    private Braintree Braintree;

    private void clear() {
        Database.setUsers(new HashMap<String, User>());
        Database.setCards(new HashMap<String, Card>());
    }

    /**
     * All test cases
     *
     */
    public void run() {
        // 0. bad command
        testBadCommand();

        // 2. test cases for Add command
        testAddCardWithoutUser();
        testAddCardOnNonExistingUser();
        testAddCardWithUser();
        testAddNonLuhnCard();
        testAddSecondCardOnUser();
        testAddAnotherUsersCard();
        testChargeOnCardAfterMakingPayment();

        // test cases for Charge command
        // test cases for Credit command
    }

    public void testChargeOnCardAfterMakingPayment() {
        Braintree.handle("user Himanshu");
        Braintree.handle("add Himanshu 5454545454545454");
        Braintree.handle("user Milana");
        Braintree.handle("add Milana 4111111111111111");
        Braintree.handle("pay Himanshu Milana $10.50 for not doing dishes");
        User himanshu = Database.getUser("Himanshu");
        Card card = himanshu.getCard();
        if (card.getBalance().equals(new BigDecimal("10.50"))) {
            System.out.println(CHARGE_ON_CARD_AFTER_PAYMENT+" : PASS");
        } else {
            System.out.println(CHARGE_ON_CARD_AFTER_PAYMENT+" : FAIL");
        }

        clear();
    }

    private void testAddAnotherUsersCard() {
        Braintree.handle("user Himanshu");
        Braintree.handle("add Himanshu 5454545454545454");
        Braintree.handle("user Milana");
        String result = Braintree.handle("add Milana 5454545454545454");
        if (result.contains(Error.CARD_BELONGS_TO_ANOTHER_USER)) {
            System.out.println(ADD_ANOTHER_USERS_CARD+" Milana 5454545454545454 : PASS");
        } else {
            System.out.println(ADD_ANOTHER_USERS_CARD+" Milana 5454545454545454 : FAIL");
        }

        clear();
    }

    private void testAddSecondCardOnUser() {
        Braintree.handle("user Himanshu");
        Braintree.handle("add Himanshu 5454545454545454");
        String result = Braintree.handle("add Himanshu 4111111111111111");
        if (result.contains(Error.USER_ALREADY_HAS_CARD)) {
            System.out.println(ADD_SECOND_CARD+" Himanshu 4111111111111111 : PASS");
        } else {
            System.out.println(ADD_SECOND_CARD+" Himanshu 4111111111111111 : FAIL");
        }

        clear();
    }

    private void testAddNonLuhnCard() {
        Braintree.handle("user Himanshu");
        String result = Braintree.handle("add Himanshu 1234567890123456");
        if (result.contains(Error.CARD_NUMBER_INVALID)) {
            System.out.println(ADD_NON_LUHN_CARD+" Himanshu 1234567890123456 : PASS");
        } else {
            System.out.println(ADD_NON_LUHN_CARD+" Himanshu 1234567890123456 : FAIL");
        }

        clear();
    }

    private void testAddCardWithUser() {
        Braintree.handle("user Himanshu");
        Braintree.handle("add Himanshu 5454545454545454");
        Card card = Database.getCard("5454545454545454");
        if (card != null) {
            System.out.println(ADD_CARD_WITH_USER+" Himanshu 5454545454545454 : PASS");
        } else {
            System.out.println(ADD_CARD_WITH_USER+" Himanshu 5454545454545454 : FAIL");
        }

        clear();
    }

    private void testAddCardOnNonExistingUser() {
        String result = Braintree.handle("add Himanshu 5454545454545454");
        if (result.contains(Error.USER_NOT_FOUND)) {
            System.out.println(ADD_CARD_ON_NON_EXISTING_USER+" Himanshu : PASS");
        } else {
            System.out.println(ADD_CARD_ON_NON_EXISTING_USER+" Himanshu : FAIL");
        }
    }

    private void testAddCardWithoutUser() {
        String result = Braintree.handle("add");
        if (result.contains(Error.INVALID_ARGS)) {
            System.out.println(ADD_CARD_WITHOUT_USER+" : PASS");
        } else {
            System.out.println(ADD_CARD_WITHOUT_USER+" : FAIL");
        }
    }

    private void testBadCommand() {
        String result = Braintree.handle("foobar");
        if (result.contains(Error.COMMAND_NOT_RECOGNIZED)) {
            System.out.println(BAD_COMMAND+" foobar : PASS");
        } else {
            System.out.println(BAD_COMMAND+" foobar : FAIL");
        }
    }
}