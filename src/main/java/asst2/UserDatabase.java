package asst2;

import java.util.List;

import java.io.IOException;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException; 
import org.json.simple.JSONArray;

public class UserDatabase {
    private ArrayList<User> users; 
    private User anon; 
    
    public UserDatabase() { 
        this.users = new ArrayList<User>();
        loadDb();
    }

    public JSONArray getDB() { 
        JSONParser parser = new JSONParser(); 
        try {
            FileReader reader = new FileReader("src/main/resources/users.json");
            return (JSONArray) parser.parse(reader);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null; 
    }

    public User getAnon() { 
        return this.anon;
    }

    public void loadDb() {
        for (Object obj: getDB()) { 
            JSONObject info = (JSONObject) obj;

            String name =  ((String) info.get("name")).toLowerCase();
            String password = ((String) info.get("password"));
            Long role = (Long) info.get("role"); 
            User user = new User(name, password, role); 

            if (name.equals("anon")) 
                anon = user; 

            JSONArray purchases = (JSONArray) info.get("purchases");
            for (Object purchase: purchases)
                user.addPurchase((String) purchase);

            if ((boolean) info.get("cardSaved")) 
                user.saveCard();

            users.add(user);
        }
    }

    public void addUser(User newUser) {
        JSONArray dbJSON = getDB();

        JSONObject user = new JSONObject();
        user.put("name", newUser.getUsername());
        user.put("password", newUser.getPassword());

        JSONArray purchases = new JSONArray();
        for (String purchase: newUser.getPurchases())
            purchases.add(purchase);
        user.put("purchases", purchases);
        user.put("cardSaved", newUser.hasCard());

        dbJSON.add(user);

        try {
            FileWriter writer = new FileWriter("src/main/resources/users.json");
            writer.write(dbJSON.toJSONString());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JSONObject getUser(User user) { 
        JSONParser parser = new JSONParser(); 
        try {
            FileReader reader = new FileReader("src/main/resources/users.json");
            JSONArray array = (JSONArray) parser.parse(reader);
            for (Object obj: array) { 
                JSONObject info = (JSONObject) obj;

                String name =  ((String)info.get("name")).toLowerCase();
                if (name.equals(user.getUsername().toLowerCase())) {
                    return info; 
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null; 
    }

    public void updatePurchases(User user, String newPurchase) { 
        JSONArray db = getDB(); 
        JSONObject userJSON = getUser(user);
        db.remove(userJSON);

        JSONArray purchasesJSON = (JSONArray) userJSON.get("purchases");
        purchasesJSON.add(newPurchase);
        userJSON.remove("purchases");
        userJSON.put("purchases", purchasesJSON);
        db.add(userJSON);

        try {
            FileWriter writer = new FileWriter("src/main/resources/users.json");
            writer.write(db.toJSONString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<User> getUsers() { 
        return this.users;
    }
}
