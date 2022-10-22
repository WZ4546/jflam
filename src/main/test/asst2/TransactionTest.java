package asst2;

// import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.*;

import java.io.*;
import java.time.*;
import java.util.*;

public class TransactionTest {
    VendingMachine vm = new VendingMachine();
    Snack s = vm.getSnacks().get(0);
    User currentUser = new User("anon", null, "customer"); 
    Transaction t = new Transaction(currentUser, vm, s, 50);

    // private final InputStream systemIn = System.in;
    // private final PrintStream systemOut = System.out;

    // private ByteArrayInputStream testIn;
    // private ByteArrayOutputStream testOut;

    // @Before
    // public void setUpOutput() {
    //     testOut = new ByteArrayOutputStream();
    //     System.setOut(new PrintStream(testOut));
    // }

    // private void provideInput(String data) {
    //     testIn = new ByteArrayInputStream(data.getBytes());
    //     System.setIn(testIn);
    // }

    // private String getOutput() {
    //     return testOut.toString();
    // }

    // @After
    // public void restoreSystemInputOutput() {
    //     System.setIn(systemIn);
    //     System.setOut(systemOut);
    // }

    @Test
    public void testDate() {
        LocalDateTime testDate = LocalDateTime.of(2002, 9, 21, 2, 30);
        t.setDate(testDate);
        t.setDate(null);
        LocalDateTime output = t.getDate();
        assertEquals(output, testDate);
    }

    @Test
    public void testRound() {
        Double output = t.round(5.5678, 2);
        assertEquals(output, 5.57, 0.01);
    }

    // @Test
    // public void testProcessPayment() {
    //     App app = new App();

    //     String data = "Hello, World!\r\n";
    //     InputStream stdin = System.in;
    //     try {
    //     System.setIn(new ByteArrayInputStream(data.getBytes()));
    //     Scanner scanner = new Scanner(System.in);
    //     System.out.println(scanner.nextLine());
    //     } finally {
    //     System.setIn(stdin);
    //     }
    // }

    // @Test
    // public void testProcessPayment() {
    //     final String testString = "buy thins 1\r\n cash\r\n 10\r\n";
    //     provideInput(testString);

    //     App app = new App();

    //     assertEquals(testString, getOutput());
    // }
}
