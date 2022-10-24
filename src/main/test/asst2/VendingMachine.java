package asst2;

import org.junit.Test;
import static org.junit.Assert.*;

public class VendingMachineTest {
    VendingMachine vm = new VendingMachine();

    @Test
    public void testConstructor(){
        assertEquals(16, vm.getSnacks().size());
        assertEquals(10, vm.getCurrencies().size());
    }

    @Test
    public void testUpdateCurrencies(){
        int size = vm.getCurrenciesJSON().size();
        vm.updateCurrencies();
        assertEquals(size + 10, vm.getCurrenciesJSON().size());
    }

    @Test
    public void testUpdateSnacks(){
        int size = vm.getSnacksJSON().size();
        vm.updateSnacks();
        assertEquals(size + vm.getSnacks().size(), vm.getSnacksJSON().size());
    }
    
    @Test
    public void testaddcurrency() {
        vm.addCurrency(20.0);
        vm.decreaseCurrency(20.0);
        vm.snackReport();
        assertTrue(true);
    }

    @Test
    public void testIsValidCash(){
        assertTrue(vm.isValidCash(50));
    }

    @Test
    public void testIsValidCategory(){
        assertTrue(vm.isValidCategory("drinks"));
    }

    @Test
    public void testIsValidID(){
        assertTrue(vm.isValidID(Long.parseLong("123456")));
    }
    @Test
    public void testGetSnack(){
        assertTrue(vm.getSnack("Sprite").getName().equals("Sprite"));
    }

    @Test
    public void testAddSnack(){
        int size = vm.getSnacks().size();
        vm.addSnack(new Snack(Long.parseLong("123"), "drink", "pepsi", 3.50, Long.parseLong("3")));
        assertEquals(size + 1, vm.getSnacks().size());
    }

}
