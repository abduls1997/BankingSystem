package bankingProject;

import java.io.*;
import java.util.*;

public class Account {
    private String accountNumber;
    private String username;
    private double balance;
    private String accountType;

    public Account(String accountNumber, String username, double balance, String accountType) {
        this.accountNumber = accountNumber;
        this.username = username;
        this.balance = balance;
        this.accountType = accountType.toUpperCase();
        
        // Create first transaction
        new Transaction(accountNumber, "Opening Balance", balance, balance);
    }

    public String getAccountNumber() { return accountNumber; }
    public String getUsername() { return username; }
    public double getBalance() { return balance; }
    public String getAccountType() { return accountType; }

    public void deposit(double amount) {
        if (amount <= 0) {
            System.out.println("❌ Error: Please enter a positive amount.");
            return;
        }

        this.balance = this.balance + amount;
        new Transaction(accountNumber, "Deposit", amount, balance);
        
        System.out.println("\n✅ Deposit Successful!");
        System.out.println("💵 Amount: $" + amount);
        System.out.println("💰 New Balance: $" + balance);
    }

    public boolean withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("❌ Error: Please enter a positive amount.");
            return false;
        }

        if (amount > balance) {
            System.out.println("❌ Error: Insufficient funds!");
            return false;
        }

        this.balance = this.balance - amount;
        new Transaction(accountNumber, "Withdrawal", -amount, balance);
        
        System.out.println("\n✅ Withdrawal Successful!");
        System.out.println("💵 Amount: $" + amount);
        System.out.println("💰 Remaining Balance: $" + balance);
        return true;
    }

    public String toCSV() {
        return String.format("%s,%s,%.2f,%s", accountNumber, username, balance, accountType);
    }

    @Override
    public String toString() {
        return String.format("Account #%s | Type: %s | Balance: $%.2f",
            accountNumber, accountType, balance);
    }
}