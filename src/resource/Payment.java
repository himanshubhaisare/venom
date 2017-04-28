package resource;

import java.math.BigDecimal;

public class Payment {

    private User actor;

    private User target;

    private BigDecimal amount;

    private String note;

    public Payment(User actor, User target, BigDecimal amount, String note) {
        this.actor = actor;
        this.target = target;
        this.amount = amount;
        this.note = note;
    }

    public User getActor() {
        return actor;
    }

    public void setActor(User actor) {
        this.actor = actor;
    }

    public User getTarget() {
        return target;
    }

    public void setTarget(User target) {
        this.target = target;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
