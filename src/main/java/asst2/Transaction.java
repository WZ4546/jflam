package asst2;

import java.util.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.io.FileWriter;
import java.io.IOException;

public class Transaction { 
    protected String method; 
    protected VendingMachine vm; 
    protected User buyer; 
    protected UserDatabase db; 
    protected Snack snack; 
    protected int amount; 
    protected LocalDateTime date; 
    protected Map<Double, Integer> change; 
    protected double paid; 

    public Transaction(User buyer, UserDatabase db, VendingMachine vm, Snack snack, int amount) {
        this.db = db; 
        this.buyer = buyer; 
        this.vm = vm; 
        this.snack = snack; 
        this.change = new LinkedHashMap<Double, Integer>();
        this.amount = amount; 
    }

    public void updateTransactions() { 
        try {
            FileWriter writer = new FileWriter("src/main/resources/transactions.csv", true);
            writer.write("\n" + 
                date.toString() + "," 
            +   snack.getName() + ","
            +   amount          + ","
            +   paid            + ","
            +   method          + ","
            +   change.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<Double, Integer> getChange() { 
        return this.change; 
    }

    public void setDate(LocalDateTime date) { 
        this.date = date; 
    }

    public LocalDateTime getDate() {
        return this.date; 
    }

    public void adjustStock() { 
        snack.setQuantity(snack.getQuantity() - amount);
        snack.setSold(snack.getSold() + amount);
        for (int i = 0; i < amount; i++) { 
            buyer.addPurchase(snack.getName());
            db.updatePurchases(buyer, snack.getName());
        }
        vm.updateSnacks();
        setDate(LocalDateTime.now());
        updateTransactions();
    }

    public void updateCancelled(String reason) { 
            try {
                FileWriter writer = new FileWriter("src/main/resources/cancelled.csv", true);
                writer.write("\n" + 
                    date.toString() + "," 
                +   buyer.getUsername() + ","
                +   reason);
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public void processPayment(App app, Scanner sc) { 
        Transaction paymentMethod = null; 
        System.out.println("Please select payment method (Card/Cash): ");
        while (sc.hasNext()) { 
            String method = sc.nextLine().replaceAll("//s+", "").toLowerCase();
            switch (method) { 
                case "card":
                    paymentMethod = new Card(buyer, db, vm, snack, amount); 
                    break; 
                case "cash":
                    paymentMethod = new Cash(buyer, db, vm, snack, amount);
                    break; 
                case "cancel":
                    updateCancelled("user cancelled");
                    app.help();
                    app.parseInput();
                    return; 
                default:
                    System.out.println("Invalid payment method");
                    System.out.println("Please select payment method (Card/Cash): ");
                    continue; 
            }
            paymentMethod.processPayment(app, sc);
        }
    }

    public double round(double value, int places) {
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public void calculateChange(Double paidAmount) { 
        Set<Double> currencies = vm.getCurrencies().keySet();

        for (Double currency: currencies) { 
            currency = round(currency, 2);
            int i = 1; 

            while (Double.compare(paidAmount, currency) >= 0 && vm.getCurrencies().get(currency) > 0) {
                vm.decreaseCurrency(currency);
                change.put(currency, i);
                paidAmount -= currency; 
                paidAmount = round(paidAmount, 2);
                i++;
            }
        }

        if (paidAmount != 0) { 
            // Put the notes back in 
            for (int i = 0; i < change.keySet().size(); i++) { 
                Double currency = (Double) change.keySet().toArray()[i];
                int quantity = change.get(currency);
                for (int j = 0; j < quantity; j++) { 
                    vm.addCurrency(currency);
                }
            }
            
            change = null; 
            System.out.println("The vending machine does not have enough change.");
            System.out.println("Please try a different payment amount");
            updateCancelled("not enough change");
        }
    }
}
