package resource;

import java.math.BigDecimal;

/**
 * Created by hbhaisare on 15/11/2016.
 */
public class User {

    public String name;

    public BigDecimal balance;

    public Card card;

    public User(String name) {
        this.name = name;
        this.balance = BigDecimal.ZERO;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
