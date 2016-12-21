package tests;

import constants.Error;
import database.Database;
import resource.Card;
import resource.Payment;
import resource.User;
import service.Venmo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hbhaisare on 19/11/2016.
 */
public class VenmoTest {

    private static final String CREATE_USER_WITH_LONGER_NAME = "test cannot create user with a name longer than 15 chars";
    private static final String CREATE_USER_WITHOUT_NAME = "test cannot create user without user name";
    private static final String CREATE_USER_WITH_NAME = "test create user with user name";
    private static final String CREATE_USER_WITH_SHORTER_NAME = "test user cannot have name shorter than 4 chars";
    private static final String CREATE_USER_WITH_UNDERSCORE_NAME = "test user can have underscores name";
    private static final String CREATE_USER_WITH_DASH_NAME = "test user can have dashes in name";
    private static final String CREATE_USER_WITH_ALPHANUM_NAME = "test user can have alphanumeric name";
    private static final String BAD_COMMAND = "test bad command is not recognized";
    private static final String ADD_CARD_WITHOUT_USER = "test cannot add card without providing user name";
    private static final String ADD_CARD_ON_NON_EXISTING_USER = "test cannot add card on a user that does not exist";
    private static final String ADD_CARD_WITH_USER = "test user can add a card";
    private static final String ADD_NON_LUHN_CARD = "test cannot add a card that's non Luhn compatible";
    private static final String ADD_SECOND_CARD = "test cannot add second card on a user";
    private static final String ADD_ANOTHER_USERS_CARD = "test cannot add a card that belongs to another user";
    private static final String CHARGE_ON_CARD_AFTER_PAYMENT = "test that actor's card is charged after payment";
    private static final String CHECK_BALANCE_WITHOUT_USER = "test cannot check check balance without providing user name";
    private static final String STARTING_BALANCE_ON_USER = "test starting balance on a user is always $0";
    private static final String CHECK_BALANCE_USER_NOT_FOUND = "test cannot check balance on a user that does not exist";
    private static final String CHECK_BALANCE_AFTER_MAKING_PAYMENT = "test that balance does not decrease after making payment";
    private static final String CHECK_BALANCE_AFTER_RECEIVING_PAYMENT = "test that balance increases after receiving payment";
    private static final String MAKE_PAYMENT_WITHOUT_USER = "test cannot make payment without providing any arguments";
    private static final String MAKE_PAYMENT_WITHOUT_NOTES = "test cannot make payment without providing notes";
    private static final String MAKE_PAYMENT_TO_SELF = "test cannot make payment to yourself";
    private static final String MAKE_PAYMENT_WITHOUT_CARD = "test actor cannot make payment without card";
    private static final String MAKE_PAYMENT = "test actor can make payment to target";
    private static final String SHOW_FEED_WITHOUT_ARGS = "test cannot show feed without any arguments";
    private static final String SHOW_FEED_FOR_NON_EXISTING_USER = "test cannot show feed for a user that does not exist";
    private static final String SHOW_FEED = "test show feed works";

    private Venmo venmo;

    public VenmoTest(Venmo venmo) {
        this.venmo = venmo;
    }

    private void clear() {
        Database.setUsers(new HashMap<String, User>());
        Database.setCards(new HashMap<String, Card>());
        Database.setPayments(new ArrayList<Payment>());
    }

    /**
     * All test cases
     *
     */
    public void run() {
        // 0. bad command
        testBadCommand();

        // 1. user will create a new user
        testCreateUserWithoutName();
        testCreateUserWithName();
        testCreateUserWithLongerName();
        testCreateUserWithShorterName();
        testCreateUserWithUnderscoreName();
        testCreateUserWithDashName();
        testCreateUserWithAlphanumericName();

        // 2. add adds a card on user
        testAddCardWithoutUser();
        testAddCardOnNonExistingUser();
        testAddCardWithUser();
        testAddNonLuhnCard();
        testAddSecondCardOnUser();
        testAddAnotherUsersCard();
        testChargeOnCardAfterMakingPayment();

        // 3. balance shows user's balance
        testBalanceWithoutUser();
        testBalanceOnNonExistingUser();
        testStartingBalanceOnUser();
        testBalanceAfterMakingPayment();
        testBalanceAfterReceivingPayment();

        // 4. pay makes a payment from actor to target
        testPaymentWithoutArgs();
        testPaymentWithoutNotes();
        testPaymentToYourself();
        testPaymentWithoutCard();
        testPaymentWithActorTargetAmountNotes();

        // 5. feed shows activity feed of the user
        testFeedWithoutArgs();
        testFeedWithoutUser();
        testFeedForUser();
    }

    private void testFeedForUser() {
        venmo.handle("user Himanshu");
        venmo.handle("add Himanshu 5454545454545454");
        venmo.handle("user Milana");
        venmo.handle("user John");
        venmo.handle("user Newton");
        venmo.handle("pay Himanshu Milana $3.14 pie");
        venmo.handle("pay Himanshu John $3.14 pie");
        venmo.handle("pay Himanshu Newton $9.81 gravity");
        String result = venmo.handle("feed Himanshu");
        if (result.contains("You paid Milana $3.14 for pie") &&
            result.contains("You paid John $3.14 for pie") &&
            result.contains("You paid Newton $9.81 for gravity")) {
            System.out.println(SHOW_FEED+" : PASS");
        } else {
            System.out.println(SHOW_FEED+" : FAIL");
        }

        clear();
    }

    private void testFeedWithoutUser() {
        String result = venmo.handle("feed Himanshu");
        if (result.equals(Error.USER_NOT_FOUND)) {
            System.out.println(SHOW_FEED_FOR_NON_EXISTING_USER+" : PASS");
        } else {
            System.out.println(SHOW_FEED_FOR_NON_EXISTING_USER+" : FAIL");
        }
    }

    private void testFeedWithoutArgs() {
        String result = venmo.handle("feed");
        if (result.equals(Error.INVALID_ARGS)) {
            System.out.println(SHOW_FEED_WITHOUT_ARGS+" : PASS");
        } else {
            System.out.println(SHOW_FEED_WITHOUT_ARGS+" : FAIL");
        }
    }

    private void testPaymentWithActorTargetAmountNotes() {
        venmo.handle("user Himanshu");
        venmo.handle("add Himanshu 5454545454545454");
        venmo.handle("user Milana");
        venmo.handle("add Milana 4111111111111111");
        venmo.handle("pay Milana Himanshu $0.10 for being a good husband");
        User himanshu = Database.getUser("Himanshu");

        if (himanshu.getBalance().equals(new BigDecimal("0.10"))) {
            System.out.println(MAKE_PAYMENT+" : PASS");
        } else {
            System.out.println(MAKE_PAYMENT+" : FAIL");
        }

        clear();
    }

    private void testPaymentWithoutCard() {
        venmo.handle("user Himanshu");
        venmo.handle("user Milana");
        String result = venmo.handle("pay Himanshu Milana $10.50 for breaking glass jar");
        if (result.equals(Error.CARD_NOT_FOUND)) {
            System.out.println(MAKE_PAYMENT_WITHOUT_CARD+" : PASS");
        } else {
            System.out.println(MAKE_PAYMENT_WITHOUT_CARD+" : FAIL");
        }

        clear();
    }

    private void testPaymentToYourself() {
        venmo.handle("user Himanshu");
        venmo.handle("add Himanshu 5454545454545454");
        String result = venmo.handle("pay Himanshu Himanshu $10.50 for looper");
        if (result.equals(Error.CANNOT_PAY_SELF)) {
            System.out.println(MAKE_PAYMENT_TO_SELF+" : PASS");
        } else {
            System.out.println(MAKE_PAYMENT_TO_SELF+" : FAIL");
        }

        clear();
    }

    private void testPaymentWithoutNotes() {
        venmo.handle("user Himanshu");
        venmo.handle("add Himanshu 5454545454545454");
        venmo.handle("user Milana");
        venmo.handle("add Milana 4111111111111111");
        String result = venmo.handle("pay Himanshu Milana $10.50");
        if (result.equals(Error.INVALID_ARGS)) {
            System.out.println(MAKE_PAYMENT_WITHOUT_NOTES+" : PASS");
        } else {
            System.out.println(MAKE_PAYMENT_WITHOUT_NOTES+" : FAIL");
        }

        clear();
    }

    private void testPaymentWithoutArgs() {
        String result = venmo.handle("pay");
        if (result.contains(Error.INVALID_ARGS)) {
            System.out.println(MAKE_PAYMENT_WITHOUT_USER+" : PASS");
        } else {
            System.out.println(MAKE_PAYMENT_WITHOUT_USER+" : FAIL");
        }
    }

    private void testBalanceAfterReceivingPayment() {
        venmo.handle("user Himanshu");
        venmo.handle("add Himanshu 5454545454545454");
        venmo.handle("user Milana");
        venmo.handle("add Milana 4111111111111111");
        venmo.handle("pay Himanshu Milana $10.50 for not doing dishes");
        String result = venmo.handle("balance Milana");
        if (result.equals("$10.50")) {
            System.out.println(CHECK_BALANCE_AFTER_RECEIVING_PAYMENT+" Milana "+result+" : PASS");
        } else {
            System.out.println(CHECK_BALANCE_AFTER_RECEIVING_PAYMENT+" Milana "+result+" : FAIL");
        }

        clear();
    }

    private void testBalanceAfterMakingPayment() {
        venmo.handle("user Himanshu");
        venmo.handle("add Himanshu 5454545454545454");
        venmo.handle("user Milana");
        venmo.handle("add Milana 4111111111111111");
        venmo.handle("pay Himanshu Milana $10.50 for not doing dishes");
        String result = venmo.handle("balance Himanshu");
        if (result.equals("$0")) {
            System.out.println(CHECK_BALANCE_AFTER_MAKING_PAYMENT+" Himanshu "+result+" : PASS");
        } else {
            System.out.println(CHECK_BALANCE_AFTER_MAKING_PAYMENT+" Himanshu "+result+" : FAIL");
        }

        clear();
    }

    private void testStartingBalanceOnUser() {
        venmo.handle("user Himanshu");
        String balance = venmo.handle("balance Himanshu");
        if (balance.equals("$0")) {
            System.out.println(STARTING_BALANCE_ON_USER+" Himanshu "+balance+" : PASS");
        } else {
            System.out.println(STARTING_BALANCE_ON_USER+" Himanshu "+balance+" : FAIL");
        }

        clear();
    }

    private void testBalanceOnNonExistingUser() {
        String result = venmo.handle("balance Himanshu");
        if (result.contains(Error.USER_NOT_FOUND)) {
            System.out.println(CHECK_BALANCE_USER_NOT_FOUND+" Himanshu : PASS");
        } else {
            System.out.println(CHECK_BALANCE_USER_NOT_FOUND+" Himanshu : FAIL");
        }
    }

    private void testBalanceWithoutUser() {
        String result = venmo.handle("balance");
        if (result.contains(Error.INVALID_ARGS)) {
            System.out.println(CHECK_BALANCE_WITHOUT_USER+" : PASS");
        } else {
            System.out.println(CHECK_BALANCE_WITHOUT_USER+" : FAIL");
        }
    }

    private void testChargeOnCardAfterMakingPayment() {
        venmo.handle("user Himanshu");
        venmo.handle("add Himanshu 5454545454545454");
        venmo.handle("user Milana");
        venmo.handle("add Milana 4111111111111111");
        venmo.handle("pay Himanshu Milana $10.50 for not doing dishes");
        User himanshu = Database.getUser("Himanshu");
        Card card = himanshu.getCard();
        if (card.getCharge().equals(new BigDecimal("10.50"))) {
            System.out.println(CHARGE_ON_CARD_AFTER_PAYMENT+" : PASS");
        } else {
            System.out.println(CHARGE_ON_CARD_AFTER_PAYMENT+" : FAIL");
        }

        clear();
    }

    private void testAddAnotherUsersCard() {
        venmo.handle("user Himanshu");
        venmo.handle("add Himanshu 5454545454545454");
        venmo.handle("user Milana");
        String result = venmo.handle("add Milana 5454545454545454");
        if (result.contains(Error.CARD_BELONGS_TO_ANOTHER_USER)) {
            System.out.println(ADD_ANOTHER_USERS_CARD+" Milana 5454545454545454 : PASS");
        } else {
            System.out.println(ADD_ANOTHER_USERS_CARD+" Milana 5454545454545454 : FAIL");
        }

        clear();
    }

    private void testAddSecondCardOnUser() {
        venmo.handle("user Himanshu");
        venmo.handle("add Himanshu 5454545454545454");
        String result = venmo.handle("add Himanshu 4111111111111111");
        if (result.contains(Error.USER_ALREADY_HAS_CARD)) {
            System.out.println(ADD_SECOND_CARD+" Himanshu 4111111111111111 : PASS");
        } else {
            System.out.println(ADD_SECOND_CARD+" Himanshu 4111111111111111 : FAIL");
        }

        clear();
    }

    private void testAddNonLuhnCard() {
        venmo.handle("user Himanshu");
        String result = venmo.handle("add Himanshu 1234567890123456");
        if (result.contains(Error.CARD_NUMBER_INVALID)) {
            System.out.println(ADD_NON_LUHN_CARD+" Himanshu 1234567890123456 : PASS");
        } else {
            System.out.println(ADD_NON_LUHN_CARD+" Himanshu 1234567890123456 : FAIL");
        }

        clear();
    }

    private void testAddCardWithUser() {
        venmo.handle("user Himanshu");
        venmo.handle("add Himanshu 5454545454545454");
        Card card = Database.getCard("5454545454545454");
        if (card != null) {
            System.out.println(ADD_CARD_WITH_USER+" Himanshu 5454545454545454 : PASS");
        } else {
            System.out.println(ADD_CARD_WITH_USER+" Himanshu 5454545454545454 : FAIL");
        }

        clear();
    }

    private void testAddCardOnNonExistingUser() {
        String result = venmo.handle("add Himanshu 5454545454545454");
        if (result.contains(Error.USER_NOT_FOUND)) {
            System.out.println(ADD_CARD_ON_NON_EXISTING_USER+" Himanshu : PASS");
        } else {
            System.out.println(ADD_CARD_ON_NON_EXISTING_USER+" Himanshu : FAIL");
        }
    }

    private void testAddCardWithoutUser() {
        String result = venmo.handle("add");
        if (result.contains(Error.INVALID_ARGS)) {
            System.out.println(ADD_CARD_WITHOUT_USER+" : PASS");
        } else {
            System.out.println(ADD_CARD_WITHOUT_USER+" : FAIL");
        }
    }

    private void testBadCommand() {
        String result = venmo.handle("foobar");
        if (result.contains(Error.COMMAND_NOT_RECOGNIZED)) {
            System.out.println(BAD_COMMAND+" foobar : PASS");
        } else {
            System.out.println(BAD_COMMAND+" foobar : FAIL");
        }
    }

    private void testCreateUserWithoutName() {
        String result = venmo.handle("user");
        if (result.contains(Error.INVALID_ARGS)) {
            System.out.println(CREATE_USER_WITHOUT_NAME+" : PASS");
        } else {
            System.out.println(CREATE_USER_WITHOUT_NAME+" : FAIL");
        }
    }

    private void testCreateUserWithName() {
        venmo.handle("user Himanshu");
        User user = Database.getUser("Himanshu");
        if (user != null) {
            System.out.println(CREATE_USER_WITH_NAME+" Himanshu : PASS");
        } else {
            System.out.println(CREATE_USER_WITH_NAME+" Himanshu : FAIL");
        }

        // clear database
        clear();
    }

    private void testCreateUserWithLongerName() {
        String result = venmo.handle("user HimanshuVasantBhaisareIsMyFullName");
        if (result.contains(Error.USERNAME_INVALID)) {
            System.out.println(CREATE_USER_WITH_LONGER_NAME+" HimanshuVasantBhaisareIsMyFullName : PASS");
        } else {
            System.out.println(CREATE_USER_WITH_LONGER_NAME+" HimanshuVasantBhaisareIsMyFullName : FAIL");
        }
    }

    private void testCreateUserWithShorterName() {
        String result = venmo.handle("user Him");
        if (result.contains(Error.USERNAME_INVALID)) {
            System.out.println(CREATE_USER_WITH_SHORTER_NAME+" Him : PASS");
        } else {
            System.out.println(CREATE_USER_WITH_SHORTER_NAME+" Him : FAIL");
        }
    }

    private void testCreateUserWithUnderscoreName() {
        venmo.handle("user Him_an_shu");
        User user = Database.getUser("Him_an_shu");
        if (user != null) {
            System.out.println(CREATE_USER_WITH_UNDERSCORE_NAME+" Him_an_shu : PASS");
        } else {
            System.out.println(CREATE_USER_WITH_UNDERSCORE_NAME+" Him_an_shu : FAIL");
        }

        // clear database
        clear();
    }

    private void testCreateUserWithDashName() {
        venmo.handle("user Him-ans-shu");
        User user = Database.getUser("Him-ans-shu");
        if (user != null) {
            System.out.println(CREATE_USER_WITH_DASH_NAME+" Him-ans-shu : PASS");
        } else {
            System.out.println(CREATE_USER_WITH_DASH_NAME+" Him-ans-shu : FAIL");
        }

        // clear database
        clear();
    }

    private void testCreateUserWithAlphanumericName() {
        venmo.handle("user H1m8n3hu");
        User user = Database.getUser("H1m8n3hu");
        if (user != null) {
            System.out.println(CREATE_USER_WITH_ALPHANUM_NAME+" H1m8n3hu : PASS");
        } else {
            System.out.println(CREATE_USER_WITH_ALPHANUM_NAME+" H1m8n3hu : FAIL");
        }

        // clear database
        clear();
    }
}