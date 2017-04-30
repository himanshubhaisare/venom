import service.CardService;
import service.UserService;
import service.Braintree;
import tests.BraintreeTest;

public class BraintreeTestApplication {

    /**
     * Braintree tests
     *
     * @param args
     */
    public static void main(String[] args) {

        UserService userService = new UserService();
        CardService cardService = new CardService();
        Braintree Braintree = new Braintree(userService, cardService);
        BraintreeTest BraintreeTest = new BraintreeTest(Braintree);

        BraintreeTest.run();
    }
}
