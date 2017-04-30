package service;

import constants.Command;
import java.util.Arrays;
import static constants.Error.*;

public class Braintree {

    private UserService userService;

    private CardService cardService;

    /**
     * Braintree Application runs with user, card and payment services
     *
     * @param userService
     * @param cardService
     */
    public Braintree(UserService userService, CardService cardService) {
        this.userService = userService;
        this.cardService = cardService;
    }

    /**
     * Parse and execute the input commands
     *
     * @param input
     */
    public String handle(String input) {
        String result;
        String[] inputs = input.split(" ");
        if (inputs.length < 1) {
            result = "";
        } else {
            String command = inputs[0];
            String[] args = Arrays.copyOfRange(inputs, 1, inputs.length);
            if (command == null || command.equals("")) {
                result = "";
            } else {
                switch (command) {
                    case Command.ADD:
                        result = this.userService.create(args);
                        result = this.cardService.create(args);
                        break;
                    case Command.CHARGE:
                        result = this.cardService.charge(args);
                        break;
                    case Command.CREDIT:
                        result = this.cardService.credit(args);
                        break;
                    case Command.BALANCE:
                        result = this.userService.showBalance(args);
						result += "\n";
                        break;
                    default:
                        result = COMMAND_NOT_RECOGNIZED;
                        break;
                }
            }
        }

		if(result.equals(COMMAND_NOT_RECOGNIZED) || result.equals(INVALID_ARGS) ||
            result.equals(USER_ALREADY_HAS_CARD) || result.equals(CARD_BELONGS_TO_ANOTHER_USER) ||
            result.equals(USER_NOT_FOUND) || result.equals(CARD_NOT_FOUND) ||
            result.equals(USERNAME_INVALID) || result.equals(CARD_NUMBER_INVALID)) {
			result += "\n";
		}

        return result;
    }
}
