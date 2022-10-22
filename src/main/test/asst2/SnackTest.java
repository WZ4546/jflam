package asst2;

import org.junit.Test;
import static org.junit.Assert.*;

import java.time.*;
import java.util.*;

public class SnackTest {
    VendingMachine vm = new VendingMachine();
    Snack s = vm.getSnacks().get(0);
    User currentUser = new User("anon", null, "customer"); 


    @Test
    public void testPrice() {
        s.setPrice(3.98);
        s.setPrice(-3.40);
        double output = s.getPrice();
        assertEquals(output, 3.98, 0.01);
    }

    @Test
    public void testSold() {
        s.setSold(1000L);
        s.setSold(-3L);
        Long output = s.getSold();
        assertTrue(output == 1000L);
    }

    @Test
    public void testID(){
        s.setID(1234L);
        Long output = s.getID();
        assertTrue(output == 1234L);
    }

    @Test
    public void testCategory(){
        s.setCategory("Drink");
        String output = s.getCategory();
        assertEquals(output, "Drink");
    }

    @Test
    public void testName() {
        s.setName("Coke");
        String output = s.getName();
        assertEquals(output, "Coke");
    }

    @Test
    public void testQuantity() {
        s.setQuantity(13L);
        s.setQuantity(16L);
        s.setQuantity(-4L);
        Long output = s.getQuantity();
        assertTrue(output == 13L);
    }
}
