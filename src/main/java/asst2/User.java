package asst2;

import java.util.*;

public class User {
    private String username; 
    private String password; 
    private boolean hasCard; 
    private Long role; 
    private List<String> purchases; 

    public User(String username, String password, Long role) { 
        this.username = username; 
        this.password = password; 
        this.hasCard = false; 
        this.role = role;  
        this.purchases = new ArrayList<String>(); 
    }

    public String getUsername() { 
        return this.username;
    }

    public Long getRole() { 
        return this.role; 
    }

    public String getPassword() { 
        return this.password;
    }

    public void addPurchase(String purchase) { 
        purchases.add(purchase);
    }

    public List<String> getPurchases() {
        return this.purchases;
    }

    public void saveCard() { 
        this.hasCard = true; 
    }
    
    public boolean hasCard() { 
        return this.hasCard; 
    }

    public void latestPurchases() { 
        int j = 0; 
        for (int i = purchases.size() - 1; i >= 0; i--) { 
            if (j == 4) break; 
            System.out.println(purchases.get(i));
            j++;
        }
    }
}