package asst2;

import java.io.IOException;
import java.io.File;
import java.io.FileWriter;

public class Snack {
    private String category; 
    private String name; 
    private Long id; 
    private Long quantity; 
    private double price; 
    private Long sold; 

    public Snack(Long id, String category, String name, Double price, Long quantity) { 
        this.id = id; 
        this.category = category; 
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public void setSold(Long sold) {
        this.sold = sold; 
    }

    public Long getSold() { 
        return this.sold;
    }

    public Long getID() { 
        return this.id; 
    }

    public void setID(Long id) {
        this.id = id; 
    }

    public String getCategory() {
        return this.category; 
    }

    public void setCategory(String category) { 

        this.category = category; 
    }

    public String getName() { 
        return this.name;
    }

    public void setName(String name) { 
        this.name = name; 
    }

    public Long getQuantity() { 
        return this.quantity;
    }

    public void setQuantity(Long quantity) {
        if (quantity < 0) { 
            System.out.println("Quantity cannot be negative");
            return; 
        } 

        if (quantity > 15) { 
            System.out.println("Max Quantity is 15");
            return; 
        }

        this.quantity = quantity;
    }

    public double getPrice() { 
        return this.price; 
    }

    public void setPrice(Double price) { 
        if (price < 0) { 
            System.out.println("Price cannot be negative"); 
            return;  
        }
        this.price = price; 
    }
}
