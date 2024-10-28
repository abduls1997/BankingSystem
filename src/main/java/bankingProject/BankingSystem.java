package bankingProject;

import java.io.*;
import java.util.*;

public class BankingSystem {
    private String WELCOME_MESSAGE = "🏦 Welcome to Simple Banking System! 🏦\n" +
                                   "Your friendly neighborhood bank.\n";

    private String LOGIN_MENU = "\n1. 🔑 Login\n" +
                               "2. 📝 Register\n" +
                               "3. 🚪 Exit\n\n" +
                               "Please choose an option (1-3): ";

    private String MAIN_MENU = "\n📋 Main Menu:\n" +
                              "1. 📝 Create New Account\n" +
                              "2. 👥 View My Accounts\n" +
                              "3. 💵 Deposit Money\n" +
                              "4. 💸 Withdraw Money\n" +
                              "5. 💰 Check Balance\n" +
                              "6. 📊 View Transaction History\n" +
                              "7. 🚪 Logout\n\n" +
                              "Please choose an option (1-7): ";

    private List<Account> accounts;
    private Scanner scanner;
    private User currentUser;

    public BankingSystem() {
        accounts = new ArrayList<>();
        scanner = new Scanner(System.in);
        System.out.println(WELCOME_MESSAGE);
    }

    public void start() {
        while (true) {
            if (currentUser == null || !currentUser.isLoggedIn()) {
                System.out.print(LOGIN_MENU);
                int choice = getValidChoice(1, 3);
                
                if (choice == 3) {
                    System.out.println("\n👋 Thank you for using Simple Banking System!");
                    break;
                }
                
                handleLoginChoice(choice);
            } else {
                System.out.print(MAIN_MENU);
                int choice = getValidChoice(1, 7);
                
                if (choice == 7) {
                    currentUser.logout();
                    currentUser = null;
                    accounts.clear();
                    continue;
                }
                
                handleMainMenuChoice(choice);
            }
        }
    }

    private void handleLoginChoice(int choice) {
        switch (choice) {
            case 1:
                login();
                break; 
            case 2:
                register();
                break; // 
            default:
                
                System.out.println("Invalid choice. Please try again.");
                break;
        }
    }


    private void login() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                User user = User.fromCSV(line);
                if (user.getUsername().equals(username) && user.authenticate(password)) {
                    currentUser = user;
                    currentUser.login();
                    loadUserAccounts();
                    System.out.println("\n✅ Login successful! Welcome, " + username + "!");
                    return;
                }
            }
        } catch (IOException e) {
            System.out.println("⚠️ Error accessing user data.");
        }
        System.out.println("❌ Invalid username or password.");
    }

    private void register() {
        System.out.print("Choose username: ");
        String username = scanner.nextLine();
        
        if (isUsernameUnique(username)==false) {
            System.out.println("❌ Username already exists!");
            return;
        }

        System.out.print("Choose password (minimum 8 characters): ");
        String password = scanner.nextLine();
        
        if (password.length() < 8) {
            System.out.println("❌ Password must be at least 8 characters!");
            return;
        }

        User newUser = new User(username, password);
        saveUser(newUser);
        System.out.println("✅ Registration successful! Please login.");
    }

    public static boolean isUsernameUnique(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.split(",")[0].equals(username)) {
                    return false;
                }
            }
        } catch (IOException e) {
            return true;
        }
        return true;
    }

    private void saveUser(User user) {
        try (FileWriter writer = new FileWriter("users.txt", true)) {
            writer.write(user.toCSV() + "\n");
        } catch (IOException e) {
            System.out.println("⚠️ Error saving user data.");
        }
    }

    private  void loadUserAccounts() {
        accounts.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader("accounts.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4 && parts[1].equals(currentUser.getUsername())) {
                    accounts.add(new Account(
                        parts[0], parts[1],
                        Double.parseDouble(parts[2]),
                        parts[3]
                    ));
                }
            }
        } catch (IOException e) {
            // No accounts yet
        }
    }

    private void handleMainMenuChoice(int choice) {
        switch (choice) {
            case 1:
                createNewAccount();
                break;
            case 2:
                viewMyAccounts();
                break;
            case 3:
                depositMoney();
                break;
            case 4:
                withdrawMoney();
                break;
            case 5:
                checkBalance();
                break;
            case 6:
                viewTransactionHistory();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
    }


    private void createNewAccount() {
        System.out.println("\nSelect account type:");
        System.out.println("1. 💳 Checking");
        System.out.println("2. 🏦 Savings");
        int typeChoice = getValidChoice(1, 2);
        String type;
        if (typeChoice == 1) {
            type = "CHECKING";
        } else {
            type = "SAVINGS";
        }

        if (hasAccountType(currentUser.getUsername(), type)) {
            System.out.println("❌ You already have a " + type.toLowerCase() + " account!");
            return;
        }

        System.out.print("\nEnter initial deposit amount: $");
        double initialDeposit = getValidAmount();

        String accountNumber = generateAccountNumber();
        Account account = new Account(accountNumber, currentUser.getUsername(), initialDeposit, type);
        accounts.add(account);
        saveAccount(account);
    }

    private void viewMyAccounts() {
        if (accounts.isEmpty()) {
            System.out.println("\n📭 No accounts found.");
            return;
        }

        System.out.println("\n📋 Your Accounts:");
        for (int i = 0; i < accounts.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, accounts.get(i));
        }
    }

    private void depositMoney() {
        Account account = selectAccount();
        if (account == null) return;

        System.out.print("\nEnter amount to deposit: $");
        double amount = getValidAmount();
        account.deposit(amount);
        updateAccountInFile(account);
    }

    private void withdrawMoney() {
        Account account = selectAccount();
        if (account == null) return;

        System.out.print("\nEnter amount to withdraw: $");
        double amount = getValidAmount();
        if (account.withdraw(amount)) {
            updateAccountInFile(account);
        }
    }

    private void checkBalance() {
        Account account = selectAccount();
        if (account != null) {
            System.out.println("\n💰 Current Balance");
            System.out.println(account);
        }
    }

    private void viewTransactionHistory() {
        Account account = selectAccount();
        if (account == null) return;

        System.out.println("\n📊 Transaction History for " + account.getAccountNumber());
        try (BufferedReader reader = new BufferedReader(new FileReader("transactions.txt"))) {
            String line;
            boolean found = false;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(account.getAccountNumber())) {
                    System.out.println(line);
                    found = true;
                }
            }
            if (!found) {
                System.out.println("No transactions found.");
            }
        } catch (IOException e) {
            System.out.println("⚠️ Unable to read transaction history.");
        }
    }

    private Account selectAccount() {
        if (accounts.isEmpty()) {
            System.out.println("\n📭 No accounts available.");
            return null;
        }

        viewMyAccounts();
        System.out.print("\nSelect account number (1-" + accounts.size() + "): ");
        int choice = getValidChoice(1, accounts.size());
        return accounts.get(choice - 1);
    }

    private double getValidAmount() {
        while (true) {
            try {
                double amount = scanner.nextDouble();
                scanner.nextLine(); // Clear buffer
                if (amount > 0) {
                    return amount;
                }
                System.out.print("❌ Please enter a positive amount: $");
            } catch (InputMismatchException e) {
                System.out.print("❌ Please enter a valid amount: $");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }

    private int getValidChoice(int min, int max) {
        while (true) {
            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); 
                if (choice >= min && choice <= max) {
                    return choice;
                }
                System.out.printf("❌ Please enter a number between %d and %d: ", min, max);
            } catch (InputMismatchException e) {
                System.out.print("❌ Please enter a valid number: ");
                scanner.nextLine(); 
            }
        }
    }

    private String generateAccountNumber() {
        return "ACC" + String.format("%04d", accounts.size() + 1);
    }

    private boolean hasAccountType(String username, String type) {
        return accounts.stream()
            .anyMatch(a -> a.getUsername().equals(username) && 
                         a.getAccountType().equals(type));
    }

    private void saveAccount(Account account) {
        try (FileWriter writer = new FileWriter("accounts.txt", true)) {
            writer.write(account.toCSV() + "\n");
        } catch (IOException e) {
            System.out.println("⚠️ Unable to save account information.");
        }
    }

    private void updateAccountInFile(Account updatedAccount) {
        List<String> accountLines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("accounts.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (!parts[0].equals(updatedAccount.getAccountNumber())) {
                    accountLines.add(line);
                }
            }
            accountLines.add(updatedAccount.toCSV());

            FileWriter writer = new FileWriter("accounts.txt");
            for (String account : accountLines) {
                writer.write(account + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("⚠️ Unable to update account information.");
        }
    }
}