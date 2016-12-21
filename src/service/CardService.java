package service;

import constants.Error;
import database.Database;
import resource.Card;
import resource.User;
import validator.Luhn;

/**
 * Created by hbhaisare on 15/11/2016.
 */
public class CardService {

    public CardService() {
    }

    /**
     * Create card
     *
     * @param args
     * @return
     */
    public String create(String[] args) {
        Card card;
        String result = "";
        if (args.length < 2) {
            result = Error.INVALID_ARGS;
            return result;
        }
        User user = Database.getUser(args[0]);
        card = Database.getCard(args[1]);
        if (user == null) {
            result = Error.USER_NOT_FOUND;
            return result;
        }

        if (card == null) {
            if (user.getCard() == null) {
                if (Luhn.validate(args[1])) {
                    card = new Card(args[1], user);
                    Database.setCard(card);
                    user.setCard(card);
                    Database.setUser(user);
                } else {
                    result = Error.CARD_NUMBER_INVALID;
                }
            } else {
                result = Error.USER_ALREADY_HAS_CARD;
            }
        } else {
            result = Error.CARD_BELONGS_TO_ANOTHER_USER;
        }

        return result;
    }
}
