package asst2;

import java.util.*;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileWriter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;

public class VendingMachine {
    private List<Snack> snacks;  
    private Map<Double, Long> currencies;

    public VendingMachine() { 
        this.snacks = new ArrayList<Snack>();
        this.currencies = new LinkedHashMap<Double,  Long>();

        initializeCurrencies();
        initializeSnacks();
    }

    public void initializeCurrencies() { 
        JSONParser parser = new JSONParser(); 
        try {
            FileReader reader = new FileReader("src/main/resources/currencies.json");
            JSONObject currenciesJSON = (JSONObject) parser.parse(reader);
            currencies.put(50.0, (Long) currenciesJSON.get("50"));
            currencies.put(20.0, (Long) currenciesJSON.get("20"));
            currencies.put(10.0, (Long) currenciesJSON.get("10"));
            currencies.put(5.0, (Long) currenciesJSON.get("5"));
            currencies.put(2.0, (Long) currenciesJSON.get("2"));
            currencies.put(1.0, (Long) currenciesJSON.get("1"));
            currencies.put(0.5, (Long) currenciesJSON.get("0.5"));
            currencies.put(0.2, (Long) currenciesJSON.get("0.2"));
            currencies.put(0.1, (Long) currenciesJSON.get("0.1"));
            currencies.put(0.05, (Long) currenciesJSON.get("0.05"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void initializeSnacks() { 
        JSONParser parser = new JSONParser(); 
        try {
            FileReader snacksJSON = new FileReader("src/main/resources/snacks.json");
            JSONArray snacksArray = (JSONArray) parser.parse(snacksJSON); 

            for (Object obj: snacksArray) { 
                JSONObject snack = (JSONObject) obj; 

                Long id = (Long) snack.get("ID");
                String category = (String) snack.get("Category");
                String name = (String) snack.get("Name");
                Double price = (Double) snack.get("Price");
                Long quantity = (Long) snack.get("Quantity");
                Long sold = (Long) snack.get("Sold"); 

                Snack snackObj = new Snack(id, category, name, price, quantity); 
                snackObj.setSold(sold);
                snacks.add(snackObj);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void updateCurrencies() { 
        JSONObject currenciesJSON = new JSONObject();
        currenciesJSON.put("50", currencies.get(50.0));
        currenciesJSON.put("20", currencies.get(20.0));
        currenciesJSON.put("10", currencies.get(10.0));
        currenciesJSON.put("5", currencies.get(5.0));
        currenciesJSON.put("2", currencies.get(2.0));
        currenciesJSON.put("1", currencies.get(1.0));
        currenciesJSON.put("0.5", currencies.get(0.5));
        currenciesJSON.put("0.2", currencies.get(0.2));
        currenciesJSON.put("0.1", currencies.get(0.1));
        currenciesJSON.put("0.05", currencies.get(0.05));

        try {
            FileWriter writer = new FileWriter("src/main/resources/currencies.json");
            writer.write(currenciesJSON.toJSONString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void snackReport() { 
        File file = new File("src/main/resources/sellerReport1.csv");
        try { 
            FileWriter writer = new FileWriter(file);
            writer.write("ID,Category,Name,Price,Quantity,Sold");
            for (Snack snack: getSnacks()) { 
                writer.write("\n" +
                    snack.getID() + ","
                +   snack.getCategory() + ","
                +   snack.getName() + ","
                +   snack.getPrice() + ","
                +   snack.getQuantity() + ","
                +   snack.getSold());
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred with printing summary"); 
            return; 
        }
        System.out.println("Report generated in resources");
    }
    
    public void updateSnacks() { 
        JSONArray snacksJSON = new JSONArray();
        for (Snack snack: this.snacks) {
            JSONObject snackJSON = new JSONObject();
            snackJSON.put("ID", snack.getID());
            snackJSON.put("Category", snack.getCategory());
            snackJSON.put("Name", snack.getName());
            snackJSON.put("Price", snack.getPrice());
            snackJSON.put("Quantity", snack.getQuantity());
            snackJSON.put("Sold", snack.getSold());
            snacksJSON.add(snackJSON);
        }

        try {
            FileWriter writer = new FileWriter("src/main/resources/snacks.json");
            writer.write(snacksJSON.toJSONString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public Map<Double, Long> getCurrencies() { 
        return this.currencies;
    }

    public void decreaseCurrency(Double currency) { 
        if (!isValidCash(currency)) { 
            System.out.println("Error: Invalid cash");
            return; 
        }
        this.currencies.put(currency, this.currencies.get(currency) - 1);
        updateCurrencies();
    }

    public void addCurrency(Double currency) { 
        if (!isValidCash(currency)) { 
            System.out.println("Error: Invalid cash");
            return; 
        }
        this.currencies.put(currency, this.currencies.get(currency) + 1);
        updateCurrencies();
    }

    public List<Snack> getSnacks() { 
        return this.snacks;
    }

    public boolean isValidCash(double cash) { 
        Set<Double> validPayments = getCurrencies().keySet();
        return validPayments.contains(cash);
    }

    public boolean isValidCategory(String category) { 
        category = category.toLowerCase();
        String[] tmp  = {"drinks", "chocolates", "chips", "candies"};
        ArrayList<String> validCategories = new ArrayList<>(Arrays.asList(tmp));
        return validCategories.contains(category);
    }

    public boolean isValidID(Long id) { 
        for (Snack snack: snacks)
            if (snack.getID() == id)
                return false; 
        return true; 
    }
    
    public Snack getSnack(String name) { 
        for (Snack snack: snacks) { 
            if (snack.getName().toLowerCase().equals(name.toLowerCase())) {
                return snack; 
            }
        }
        return null; 
    }

    public void addSnack(Snack snack) { 
        this.snacks.add(snack);
        updateSnacks();
    }
}
