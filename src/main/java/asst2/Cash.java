package asst2;

import java.util.Scanner;
import java.util.ArrayList;

public class Cash extends Transaction {

    public Cash(User buyer, UserDatabase db, VendingMachine vm, Snack product, int amount) { 
        super(buyer, db, vm, product, amount);
    }

    public void addCash(ArrayList<Double> paidCash) {
        for (Double cash: paidCash) { 
            vm.addCurrency(cash);
            super.paid += cash; 
        } 
    }

    public void printChange(App app, Transaction payment, String type) { 
        if (payment.getChange() != null) {
            super.method = "cash"; 
            super.adjustStock();
            System.out.printf("Purchased %s Succesfully\n", type); 
            if (payment.getChange().keySet().size() != 0) { 
                System.out.println("Change: ");
                for (int i = 0; i < payment.getChange().keySet().size(); i++) {
                    Double key = (Double) payment.getChange().keySet().toArray()[i]; 
                    if (payment.getChange().get(key) != 0) { 
                        System.out.printf("  $%.2f: %d\n", key, payment.getChange().get(key));
                    }
                }
            }
            System.out.println(vm.getCurrencies());
            app.parseInput();
        }
    }

    @Override
    public void processPayment(App app, Scanner sc) { 
        Double requiredPayment = snack.getPrice() * amount;
        ArrayList<Double> paidCash = new ArrayList<Double>();
        while (requiredPayment > 0.05) {
            System.out.println("Please insert cash: ");
            String input = sc.nextLine().replaceAll("//s+", "").toLowerCase();

            try {
                Double cash = Double.parseDouble(input);

                //TODO: Test inputs with 0.5000, or 0.2000 etc. 
                if (cash < 0 ) throw new Exception(); 
                if (!vm.isValidCash(cash)) throw new Exception();

                requiredPayment -= cash; 
                paidCash.add(cash);

            } catch (Exception e) { 
                if (input.equals("cancel")) { 
                    updateCancelled("user cancelled");
                    app.help();
                    app.parseInput();
                } else  {
                    System.out.println("Invalid Payment. Please try again");
                }
            }
        }
        super.calculateChange(Math.abs(requiredPayment));
        addCash(paidCash);
        printChange(app, this, snack.getName());
    }
    
}
