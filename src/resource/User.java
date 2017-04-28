package resource;

import java.math.BigDecimal;

public class User {

    private String name;

    private BigDecimal balance;

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
