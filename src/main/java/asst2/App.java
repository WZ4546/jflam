package asst2;

import java.util.Scanner;
import java.util.List;
import javax.swing.*;

public class App {
    UserDatabase db = new UserDatabase();
    VendingMachine vm = new VendingMachine();
    User currentUser = db.getAnon(); 

    public void menu() {
        System.out.println("Available Snacks: ");
        System.out.println("    Drinks: Mineral Water, Sprite, Coke, Pepsi, Juice.");
        System.out.println("    Chocolates: Mars, M&M, Bounty, Snickers.");
        System.out.println("    Chips: Smiths, Pringles, Kettle, Thins.");
        System.out.println("    Candies: Mentos, Sour Patch, Skittles.");
    }

    public void help() { 
        System.out.println("Menu                            --Prints the menu");
        System.out.println("List [Category]                 --Prints more detail about the snacks in a specific category");
        System.out.println("Buy [Snack] [Amount]            --To make a purchase");
        System.out.println("Login  [Username] [Password]    --To log in");
        System.out.println("Signup [Username] [Password]    --To sign up");
        System.out.println("History                         --To view the latest purchases made by you, or other users");

        // Seller Commands: 
        if (currentUser.getRole() == 1 || currentUser.getRole() == 3)  {
            System.out.println("    Modify [Snack] [Id/Name/Category/Price/Quantity] [New Value]");
            System.out.println("    Summary");
        }

        // Cashier Commands: 
        if (currentUser.getRole() == 2 || currentUser.getRole() == 3) {
            System.out.println("    Add    [Cash] [Amount]");
            System.out.println("    Remove [Cash] [Amount]");
        }

        //Owner Commands: 
        if (currentUser.getRole() == 3) { 

        }

        System.out.println("Cancel");
        System.out.println("Logout");
    }

    public String[] getDetails(boolean login, String winTitle){
        final String password;
        final String username;
        final String cp;
        final JTextField unf = new JTextField(10);
        final JPasswordField pf = new JPasswordField(10);
        final JPasswordField cpf = new JPasswordField(10);

        JPanel panel = new JPanel();
        ImageIcon icon = new ImageIcon(new ImageIcon("src/main/resources/images/vending_machine.png").getImage().
        getScaledInstance(64, 64, java.awt.Image.SCALE_SMOOTH));
        panel.add(new JLabel("Username:"));
        panel.add(unf);
        panel.add(Box.createHorizontalStrut(15)); // a spacer
        panel.add(new JLabel("Password:"));
        panel.add(pf);
        if(login == false){ // User is signing up
            panel.add(new JLabel("Confirm password:"));
            panel.add(cpf);
        }
        
        
        int result = JOptionPane.showConfirmDialog(null, panel, 
               winTitle, JOptionPane.OK_CANCEL_OPTION, JOptionPane.NO_OPTION, icon);
        if (result == JOptionPane.OK_OPTION) {
            username = unf.getText();
            password = new String(pf.getPassword());
            if(login == false){
                cp = new String(cpf.getPassword());
            }
            else{
                cp = "";
            }
        }
        else{
            username = "";
            password = "";
            cp = "";
        }
        String[] details = { username, password, cp };
        return details;
        
    }

    public void buy(Scanner sc, String name, String amountStr) {
        int amount; 
        try {
            amount = Integer.parseInt(amountStr);
            if (amount <= 0) 
                throw new Exception();
        } catch (Exception e) { 
            System.out.println("Amount must be a positive integer");
            return; 
        }

        List<Snack> snacks = vm.getSnacks();
        Transaction payment = null; 

        for (int i = 0; i < snacks.size(); i++) {
            if (snacks.get(i).getName().toLowerCase().equals(name.toLowerCase())) { 
                if (snacks.get(i).getQuantity() == 0) {
                    System.out.printf("%s is out of stock!\n", name);
                    return; 
                }
                payment = new Transaction(currentUser, db, vm, snacks.get(i), amount);
                break; 
            }
        }

        if (payment == null) {
            System.out.println("Snack not found");
            return; 
        }

        payment.processPayment(this, sc);
    }

    public void printCategory(String category) { 
        if (!vm.isValidCategory(category)) {
            System.out.println("Invalid Category");
            return; 
        }

        category = category.toLowerCase();
        for (Snack snack : vm.getSnacks())
            if (snack.getCategory().toLowerCase().equals(category))
                System.out.printf(" %s\n  Quantity: %d\n  Price: $%.2f\n", 
                    snack.getName(), snack.getQuantity(), snack.getPrice());
    }

    public void signup(String username, String password) { 
        // Check if duplicate username 
        for (User user: db.getUsers()) { 
            if (username.toLowerCase().equals(user.getUsername())) { 
                System.out.println("Username must be unique. Please try again");
                return; 
            }
        }

        User user = new User(username, password, Long.parseLong("0"));
        db.addUser(user);
        System.out.println("Signed up successfully!");
    }

    public void login(String username, String password) { 
        for (User user: db.getUsers()) { 
            if (username.toLowerCase().equals(user.getUsername())
            && password.equals(user.getPassword())) { 
                currentUser = user; 
                System.out.printf("Logged in as %s\n", user.getUsername());
                return; 
            } 
        }
        System.out.println("Invalid username or password");
    }

    public void modify(String name, String toModify, String newValue) { 
        Snack snack = vm.getSnack(name);
        if (snack == null) { 
            System.out.println("Snack not found.");
            return; 
        }

        toModify = toModify.toLowerCase();
        switch (toModify) {
            case "id":
                try { 
                    if (vm.isValidID(Long.parseLong(newValue))) {
                        snack.setID(Long.parseLong(newValue));
                        break; 
                    }
                } catch (Exception e) {
                    System.out.println("Quantity has to be a number");
                    break; 
                }
                System.out.println("Error: Duplicate ID"); 
                System.out.printf("Updated %s's ID to %s\n", name, newValue);
                break; 
            case "category":
                snack.setCategory(newValue);
                System.out.printf("Updated %s's category to %s\n", name, newValue);
                break; 
            case "name":
                for (Snack reqSnack: vm.getSnacks()) { 
                    if (snack.getName().equals(reqSnack.getName())) { 
                        System.out.println("Error: Duplicate snack");
                        return; 
                    }
                }
                snack.setName(newValue);
                System.out.printf("Updated %s's name to %s\n", name, newValue);
                break; 
            case "quantity":
                try { 
                    snack.setQuantity(Long.parseLong(newValue));
                } catch (Exception e) {
                    System.out.println("Quantity has to be a number");
                    break; 
                }
                System.out.printf("Updated %s's quantity to %s\n", name, newValue);
                break; 
            case "price":
                try { 
                    snack.setQuantity(Long.parseLong(newValue));
                } catch (Exception e) {
                    System.out.println("Price has to be a number");
                    break; 
                }
                System.out.printf("Updated %s's price to %s\n", name, newValue);
                break;
            default: 
                System.out.println("Invalid detail to modify");
                break; 
        }
        vm.updateSnacks();
    }

    public void addCash(double cash, int amount) {
        for (int i = 0; i < amount; i++)
            vm.addCurrency(cash);
        System.out.println("Successfully added %d %.2f note/coins to the vending machine.");
    }

    public void removeCash(double cash, int amount) { 
        for (int i = 0; i < amount; i++)
            vm.decreaseCurrency(cash);
        System.out.println("Successfully removed %d %.2f note/coins to the vending machine.");
    }

    public void parseInput() { 
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) { 
            String[] input = sc.nextLine().toLowerCase().split(" ");
            for (String str: input) str.replaceAll("//s+", "");

            switch(input[0]) { 
                case "help":
                    help();
                    break; 
                case "menu":
                    menu();
                    break; 
                case "details":
                    if (input.length < 2) { 
                        System.out.println("Please specify category");
                        break; 
                    }
                    printCategory(input[1]);
                    break; 
                case "login": 
                    if (input.length < 3) { 
                        System.out.println("Please specify username and password");
                        break; 
                    }
                    login(input[1], input[2]);
                    break; 
                case "signup": 
                    if (input.length != 3) { 
                        System.out.println("Invalid format");
                        break; 
                    }
                    signup(input[1], input[2]);
                    break; 
                case "buy":
                    if (input.length == 3)
                        buy(sc, input[1], input[2]);
                    else if (input.length >= 4) 
                        buy(sc, input[1] + " " + input[2], input[3]);
                    else 
                        System.out.println("Invalid buy format");
                    break;
                case "history":
                    currentUser.latestPurchases();
                    break; 
                case "modify":
                    if (currentUser.getRole() == 1 || currentUser.getRole() == 3) { 
                        if (input.length < 4) {
                            System.out.println("Invalid modify format");
                            break; 
                        }
                        modify(input[1], input[2], input[3]);
                        break; 
                    }
                case "summary": 
                    if (currentUser.getRole() == 1 || currentUser.getRole() == 3) { 
                        vm.snackReport(); 
                        break; 
                    }
                case "add":
                    if (currentUser.getRole() == 2 || currentUser.getRole() == 3) { 
                        try {
                            if (input.length < 3) throw new Exception();

                            double currency = Double.parseDouble(input[1]);
                            int amount = Integer.parseInt(input[2]);
                            addCash(currency, amount);
                        } catch (Exception e) {
                            System.out.println("Invalid input");
                        }
                        break; 
                    } 
                case "remove":
                    if (currentUser.getRole() == 2 || currentUser.getRole() == 3) { 
                        try {
                            if (input.length < 3) throw new Exception();

                            double currency = Double.parseDouble(input[1]);
                            int amount = Integer.parseInt(input[2]);
                            removeCash(currency, amount);
                        } catch (Exception e) {
                            System.out.println("Invalid input");
                        }
                        break; 
                    } 
                default:
                    System.out.println("Invalid input");
            }
        }
        sc.close();
    }

    public static void main(String[] args) {
        App app = new App();
        app.help();
        app.getDetails(true, "test");
        app.parseInput();
    }
}
