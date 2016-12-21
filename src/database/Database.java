package database;

import resource.Card;
import resource.Payment;
import resource.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hbhaisare on 16/11/2016.
 */
public class Database {

    public static Map<String, User> users = new HashMap<String, User>();

    public static Map<String, Card> cards = new HashMap<String, Card>();

    public static List<Payment> payments = new ArrayList<Payment>();

    public static Map<String, User> getUsers() {
        return users;
    }

    public static void setUsers(Map<String, User> users) {
        Database.users = users;
    }

    public static User getUser(String name) {
        return users.get(name);
    }

    public static void setUser(User user) {
        Database.users.put(user.getName(), user);
    }

    public static Map<String, Card> getCards() {
        return cards;
    }

    public static Card getCard(String number) {
        return cards.get(number);
    }

    public static void setCards(Map<String, Card> cards) {
        Database.cards = cards;
    }

    public static void setCard(Card card) {
        Database.cards.put(card.getNumber(), card);
    }

    public static List<Payment> getPayments() {
        return payments;
    }

    public static void setPayments(List<Payment> payments) {
        Database.payments = payments;
    }

    public static void setPayment(Payment payment) {
        payments.add(payment);
    }
}
