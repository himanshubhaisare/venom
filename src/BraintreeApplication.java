import service.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class BraintreeApplication {

    /**
     * Braintree application runner
     *
     * @param args
     */
    public static void main(String[] args) {
        String result = "";
        UserService userService = new UserService();
        CardService cardService = new CardService();
        Braintree braintree = new Braintree(userService, cardService);
        if (args.length > 1) {
            try {
                File output = Utils.getOutputFile(args);
                FileWriter fw = new FileWriter(output.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                Scanner scanner = new Scanner(new File(args[0]));
                while (scanner.hasNext()) {
                    String input = scanner.nextLine();
                    result = braintree.handle(input);
                }
                bw.write(result);
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("mini braintree started in interactive mode. Enter help to see manual.");
            String input = "";
            while (!input.equals("Close")) {
                Scanner scanner = new Scanner(System.in);
                input = scanner.nextLine();
                if (input.equals("help")) {
                    System.out.print(
                            "Add <user> <card number> <$limit> : Adds a credit card on user with given credit limit e.g. Add Himanshu 5555555555554444 $1000\n" +
                            "Charge <user> <$amount> : Charge user with given amount, increases balance on card e.g. Charge Tom $500 \n" +
                            "Credit <user> <$amount> : Credit decreases balance on user's card by given amount e.g. Credit Lisa $100 \n" +
                            "Close : Closes application \n" +
                            "help : brings up manual \n");
                } else {
                    result = braintree.handle(input);
                    System.out.print(result);
                }
            }
        }
    }
}
