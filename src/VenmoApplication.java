import service.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by hbhaisare on 15/11/2016.
 */
public class VenmoApplication {

    /**
     * Venmo application runner
     *
     * @param args
     */
    public static void main(String[] args) {
        UserService userService = new UserService();
        CardService cardService = new CardService();
        PaymentService paymentService = new PaymentService();
        Venmo venmo = new Venmo(userService, cardService, paymentService);
        if (args.length > 1) {
            try {
                File output = Utils.getOutputFile(args);
                FileWriter fw = new FileWriter(output.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                Scanner scanner = new Scanner(new File(args[0]));
                while (scanner.hasNext()) {
                    String input = scanner.nextLine();
                    String result = venmo.handle(input);
                    bw.write(result);
                }
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("mini venmo started in interactive mode. Enter help to see manual.");
            String input = "";
            while (!input.equals("close")) {
                Scanner scanner = new Scanner(System.in);
                input = scanner.nextLine();
                if (input.equals("help")) {
                    System.out.println("user <name> : to create a user. e.g. user Himanshu \n" +
                            "add <user> <card number> : to add a credit card on user. e.g. add Himanshu 5555555555554444 \n" +
                            "balance <user> : to view balance of a user e.g. balance Himanshu \n" +
                            "pay <actor> <target> <$amount> <note> : pay someone e.g. pay Himanshu Lisa $10.50 for coffee \n" +
                            "feed <user> : shows activity feed of a user e.g. feed Himanshu \n" +
                            "help : brings up manual \n" +
                            "close : closes mini-venmo \n");
                } else {
                    String result = venmo.handle(input);
                    System.out.print(result);
                }
            }
        }
    }
}
