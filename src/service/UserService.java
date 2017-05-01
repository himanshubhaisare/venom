package service;

import constants.Error;
import database.Database;
import resource.Card;
import resource.User;
import validator.Username;
import validator.Validation;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserService {

    /**
     * Validate and create a user
     *
     * @param args
     * @return
     */
    public String create(String[] args) {
        String result = "";
        Validation validation = validateCreate(args);
        if (validation.isValid()) {
            String name = args[0];
            User user = new User(name);
            Database.setUser(user);
        } else {
            result = validation.getErrorString();
        }

        return result;
    }

    /**
     * Validate username
     *
     * @param args
     * @return
     */
    private Validation validateCreate(String[] args) {
        Validation validation = new Validation();
        if (args.length != 3) {
            validation.addError(Error.INVALID_ARGS);
            return validation;
        }

        String name = args[0];
        if (!Username.validate(name)) {
            validation.addError(Error.USERNAME_INVALID);
            return validation;
        }

        return validation;
    }

    /**
     * Show user's balance
     *
     * @param args
     * @return
     */
    public String showBalance(String[] args) {
        String result;
        BigDecimal balance;
        Validation validation = validateBalance(args);
        if (validation.isValid()) {
            User user = Database.getUser(args[0]);
            balance = user.getCard().getBalance();
            result = "$"+balance.toString();
        } else {
            result = validation.getErrorString();
        }

        return result;
    }

    /**
     * Validate show balance
     *
     * @param args
     * @return
     */
    private Validation validateBalance(String[] args) {
        Validation validation = new Validation();
        if (args.length != 1) {
            validation.addError(Error.INVALID_ARGS);
            return validation;
        }

        User user = Database.getUser(args[0]);
        if (user == null) {
            validation.addError(Error.USER_NOT_FOUND);
            return validation;
        }

        Card card = user.getCard();
        if (card == null) {
            validation.addError(Error.CARD_NOT_FOUND);
            return validation;
        }

        return validation;
    }

    /**
     * return balances of all users
     *
     * @return
     */
    public String summary() {
        String summary = "";
        Map<String, User> users = Database.getUsers();
        List<String> balances = users.entrySet().stream().map(userEntry -> {
            User user = userEntry.getValue();
            Card card = user.getCard();
            String userName = user.getName();
            String balance = "error";
            if (card != null) {
                balance = "$" + card.getBalance().toString();
            }
            return userName + ": " + balance;
        }).collect(Collectors.toList());

        int count = 0;
        for (String userAndBalance : balances) {
            count++;
            summary += userAndBalance;
            if (count < balances.size()) {
                summary += "\n";
            }
        }

        return summary;
    }
}
