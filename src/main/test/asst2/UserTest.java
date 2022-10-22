package asst2;

import org.junit.Test;
import static org.junit.Assert.*;

public class UserTest {
    User user1 = new User("Amy", "hello", "NPC");
  
    @Test public void testUsername() {
        assertEquals("Amy", user1.getUsername());
    }
  
    @Test public void testRole() {
        assertEquals("NPC", user1.getRole());
    }
  
    @Test public void testPassword() {
        assertEquals("hello", user1.getPassword());
    }
  
    @Test public void testCardInfo() {
        assertFalse(user1.hasCard());
    }
  
    @Test public void testSavedCard() {
        user1.saveCard();
        assertTrue(user1.hasCard());
    }
  
    //TODO: Re-write to make testable
    @Test public void testLatestPurchases() {
        User user1 = new User("Amy", "hello", "NPC");
        user1.addPurchase("first");
        user1.addPurchase("first");
        user1.addPurchase("first");
        user1.addPurchase("first");
        user1.latestPurchases();
    }
  
    @Test public void testPurchases() {
        User user1 = new User("Amy", "hello", "NPC");
        user1.addPurchase("first");
        user1.addPurchase("first");
        user1.addPurchase("first");
        user1.addPurchase("first");
        assertEquals(user1.getPurchases().size(), 4);
    }
}
