package asst2;

import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException; 
import org.json.simple.JSONArray;

public class Card extends Transaction {
    HashMap<String, Integer> CCInfo = new HashMap<String, Integer>(); 

    public Card(User buyer, UserDatabase db, VendingMachine vm, Snack product, int amount) { 
        super(buyer, db, vm, product, amount);
        parseCCInfo();
    }

    public void parseCCInfo() {
        JSONParser parser = new JSONParser(); 
        try {
            FileReader reader = new FileReader("src/main/resources/credit_cards.json");
            JSONArray array = (JSONArray) parser.parse(reader);
            for (Object obj: array) { 
                JSONObject info = (JSONObject) obj; 
                CCInfo.put(((String)info.get("name")).toLowerCase(), 
                    (Integer) Integer.parseInt((String)info.get("number")));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public boolean doesMatch(String name, Integer number) {
        Set<String> names = CCInfo.keySet();
        for (String nameSet: names) { 
            if (name.equals(nameSet) && number.equals(CCInfo.get(name)))
                return true; 
        }
        return false; 
    }

    @Override
    public void processPayment(App app, Scanner sc) { 
        String cardholder; 
        Integer ccNumber; 
        while (true) {
            System.out.println("Card Holder Name: ");
            cardholder = sc.nextLine().toLowerCase().replaceAll("//s+", "");
            if (cardholder.equals("cancel")) {
                app.help();
                app.parseInput();
            }

            System.out.println("Credit Card Number: ");
            String input = sc.nextLine().replaceAll("//s+", "");
            if (input.equals("cancel")) {
                updateCancelled("user cancelled");
                app.help();
                app.parseInput();
            }
            
            try {
                ccNumber = Integer.parseInt(input);
                if (doesMatch(cardholder, ccNumber)) { 
                    System.out.printf("Paid $%.2f for %s\n", snack.getPrice(), snack.getName());
                    super.paid = snack.getPrice();
                    super.method = "card"; 
                    super.adjustStock();
                    app.parseInput();
                } else { 
                    throw new Exception();
                }
            } catch (Exception e) { 
                System.out.println("Invalid name and/or credit card number. Try again");
            }; 
        }
    }
}