package bankingProject;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Transaction {
    private String accountNumber;
    private String type;
    private double amount;
    private double balanceAfter;
    private Date date;

    public Transaction(String accountNumber, String type, double amount, double balanceAfter) {
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.date = new Date();
        saveToFile();
    }

    private void saveToFile() {
        List<String> transactions = new ArrayList<>();
        
        // Read existing transactions
        try (BufferedReader reader = new BufferedReader(new FileReader("transactions.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith(accountNumber)) {
                    transactions.add(line);
                }
            }
        } catch (IOException e) {
            // File doesn't exist yet
        }
        
        // Add new transaction
        transactions.add(toCSV());
        
        // Write all transactions back
        try (FileWriter writer = new FileWriter("transactions.txt")) {
            for (String transaction : transactions) {
                writer.write(transaction + "\n");
            }
        } catch (IOException e) {
            System.out.println("⚠️ Unable to save transaction.");
        }
    }

    private String toCSV() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return String.format("%s,%s,%.2f,%.2f,%s",
            accountNumber, type, amount, balanceAfter, dateFormat.format(date));
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm");
        return String.format("%s | %s | Amount: $%.2f | Balance: $%.2f",
            dateFormat.format(date), type, Math.abs(amount), balanceAfter);
    }
}