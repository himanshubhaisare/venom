package service;

import constants.Error;
import database.Database;
import resource.Card;
import resource.Payment;
import resource.User;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by hbhaisare on 15/11/2016.
 */
public class PaymentService {

    public PaymentService() {
    }

    /**
     * Pay someone
     *
     * @param args
     * @return
     */
    public String create(String[] args) {
        String result = "";
        Payment payment;
        if (args.length < 4) {
            result = Error.INVALID_ARGS;
            return result;
        }

        User actor = Database.getUser(args[0]);
        User target = Database.getUser(args[1]);
        BigDecimal amount = new BigDecimal(args[2].replace("$", ""));
        String note = Utils.concatenate(Arrays.copyOfRange(args, 3, args.length));

        if (actor == null) {
            result = Error.ACTOR_NOT_FOUND;
            return result;
        }
        if (target == null) {
            result = Error.TARGET_NOT_FOUND;
            return result;
        }
        if (actor.getName().equals(target.getName())) {
            result = Error.CANNOT_PAY_SELF;
            return result;
        }
        if (actor.getCard() == null) {
            result = Error.CARD_NOT_FOUND;
            return result;
        }

        // record payment
        payment = new Payment(actor, target, amount, note);
        Database.setPayment(payment);

        // record charge on actor's card
        Card actorCard = actor.getCard();
        BigDecimal charge = actorCard.getCharge().add(amount);
        actorCard.setCharge(charge);
        Database.setCard(actorCard);

        // record balance on target
        target.setBalance(target.getBalance().add(amount));
        Database.setUser(target);

        return result;
    }

    /**
     * Retrieve matching list of payments
     *
     * @param args
     * @return
     */
    public String retrieve(String[] args) {
        String result;
        List<Payment> payments;
        if (args.length < 1) {
            result = Error.INVALID_ARGS;
            return result;
        }

        User user = Database.getUser(args[0]);
        if (user == null) {
            result = Error.USER_NOT_FOUND;
            return result;
        }

        payments = Database.getPayments().stream().filter(payment -> (
            payment.getActor().getName().equals(user.getName()) || payment.getTarget().getName().equals(user.getName())
        )).collect(Collectors.toList());

        List<String> paymentsList = new ArrayList<String>();
        for (Payment payment : payments) {
            User actor = payment.getActor();
            User target = payment.getTarget();
            if (actor.getName().equals(user.getName())) {
                paymentsList.add("You paid "+target.getName()+" $"+payment.getAmount()+" for "+payment.getNote());
            } else if (target.getName().equals(user.getName())) {
                paymentsList.add(actor.getName() + " paid you $" + payment.getAmount() + " for " + payment.getNote());
            } else {
                paymentsList.add(actor.getName() + " paid " + target.getName() + " $" + payment.getAmount() + " for " + payment.getNote());
            }
        }

        result = String.join("\n", paymentsList);
        return result+"\n";
    }
}
