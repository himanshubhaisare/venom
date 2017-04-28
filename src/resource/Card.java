package resource;

import java.math.BigDecimal;

public class Card {

    private String number;

    private User user;

    private BigDecimal charge;

    public Card(String number, User user) {
        this.number = number;
        this.user = user;
        this.charge = BigDecimal.ZERO;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getCharge() {
        return charge;
    }

    public void setCharge(BigDecimal charge) {
        this.charge = charge;
    }
}
