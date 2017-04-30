package validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Money {

    /**
     * allow US dollar amount
     */
    private static final String MONEY_PATTERN = "^[+-]?[0-9]{1,3}(?:,?[0-9]{3})*(?:\\.[0-9]{2})?$";

    /**
     * Validate dollar amount
     *
     * @param username
     * @return
     */
    public static boolean validate(String username) {
        Pattern pattern = Pattern.compile(MONEY_PATTERN);
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }
}
