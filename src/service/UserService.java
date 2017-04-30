package service;

import constants.Error;
import database.Database;
import resource.User;
import validator.Username;

import java.math.BigDecimal;

public class UserService {

    /**
     * Validate and create a user
     *
     * @param args
     * @return
     */
    public String create(String[] args) {
        User user;
        String result = "";
        if (args.length < 3) {
            result = Error.INVALID_ARGS;
            return result;
        }

        String name = args[0];
        if (Username.validate(name)) {
            user = new User(name);
            Database.setUser(user);
        } else {
            result = Error.USERNAME_INVALID;
        }

        return result;
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
        if (args.length != 1) {
            result = Error.INVALID_ARGS;
            return result;
        }

        User user = Database.getUser(args[0]);
        if (user == null) {
            result = Error.USER_NOT_FOUND;
        } else {
            balance = user.getCard().getBalance();
            result = "$"+balance.toString();
        }

        return result;
    }
}
