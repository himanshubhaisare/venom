package validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hbhaisare on 15/11/2016.
 */
public class Username {

    /**
     * allow alphanumeric, dashes, underscores, min 4 & max 15 characters
     */
    private static final String USERNAME_PATTERN = "^[a-zA-Z0-9_-]{4,15}$";

    /**
     * Validate user name
     *
     * @param username
     * @return
     */
    public static boolean validate(String username) {
        Pattern pattern = Pattern.compile(USERNAME_PATTERN);
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }
}
