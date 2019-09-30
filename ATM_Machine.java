/**
-OOP app using Java
-ATM functions: display balance, withdrawal, deposit
-ATM withdrawal should be prevented if balance is below withdrawal amount
-Keep track of 3 attributes: account number, PIN number, account balance
-save information to receipt file, at end if user doesn't want receipt program
should delete file
*/

import java.util.Scanner;
import java.io.Console;
import java.util.regex.Matcher;
import java.io.*;
import java.nio.file.*;
import java.util.Date;


class Client {
	private String acctNo;
	private String PIN;
	private double balance;

	public Client(String acctNo, String PIN, double balance) {
		this.acctNo = acctNo;
		this.PIN = PIN;
		this.balance = balance;
	}
	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}
	public void setPIN(String PIN) {
		this.PIN = PIN;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public String getAcctNo() {
		return this.acctNo;
	}
	public String getPIN() {
		return this.PIN;
	}
	public double getBalance() {
		return this.balance;
	}
	public String toString() {
		return "\tAccount number: " + getAcctNo() +
		"\n\t" + "PIN number: " + getPIN() +
		"\n\t" + String.format("Account balance: $%.2f\n", getBalance());
	}
}


class ATM {
	private Client client; // we need this so that we can call methods of Client class

	public ATM(Client c) {
		this.client = c;
	}

	public void displayBalance() {
		System.out.println("Balance: " + client.getBalance());
	}

	public void depositCash(double money, PrintWriter file) throws IOException {}

	public void withdraw(double money, PrintWriter file) throws IOException {}
}




class DepositATM extends ATM {
	private Client client;
	private double balance;

	public DepositATM(Client client) {
		super(client);
		this.client = client;
		this.balance = balance;
	}

	@Override
	public void depositCash(double money, PrintWriter file) {
		Scanner input = new Scanner(System.in);

		if(money >= 1 && money < 1000000000) {
				client.setBalance(client.getBalance() + money);
				System.out.println("\nDepositing...");
				file.print("\nDepositing...");
				System.out.printf("Deposit Complete! Your New Balance is: $%,.2f\n", client.getBalance());
				file.printf("Deposit Complete! Your New Balance is: $%,.2f\n", client.getBalance());
		}

		else if(money <= 0 || money > 1000000000) {
			System.out.print("\tPlease enter a different amount to deposit or enter 0 to cancel depositing: ");
			double d = input.nextDouble();

			if(d == 0) {
				System.out.println("\nDeposit operation cancelled...");
			}

			else {
				depositCash(d, file);
			}
		}
	}
}



class WithdrawalATM extends ATM {

	private Client client;
	private double balance;

	public WithdrawalATM(Client client) {
		super(client);
		this.client = client;
		this.balance = balance;
	}

	@Override
	public void withdraw(double money, PrintWriter file) throws IOException {
		Scanner input = new Scanner(System.in);

		if(money > 0 && money < client.getBalance()) {
			client.setBalance(client.getBalance() - money);
			System.out.println("\nWithdrawing...");
			file.print("\nWithdrawing...");
			System.out.printf("Withdraw complete! Your New Balance is: $%,.2f\n", client.getBalance());
			file.printf("Withdraw complete! Your New Balance is: $%,.2f\n", client.getBalance());
		}

		else if(money <= 0) {
			System.out.print("\tPlease enter a different amount to withdraw or 0 to cancel withdraw: ");
			double w = input.nextDouble();

			if(w == 0) {
				System.out.println("\tWithdraw operation cancelled...");
			}

			else {
				withdraw(w, file);
			}
		}

		else if(money > client.getBalance()) {
			System.out.println("\tError: You don't have sufficient funds!");
			System.out.print("\tPlease enter a different amount to withdraw or 0 to cancel withdraw: ");
			double w = input.nextDouble();

			if(w == 0) {
				System.out.println("\tWithdraw operation cancelled...");
			}

			else {
				withdraw(w, file);
			}
		}
	}
}


public class ATM_Machine {
	public static void main(String[] args) throws IOException {
		Scanner input = new Scanner(System.in);
		PrintWriter file = new PrintWriter("C:\\Users\\brian\\Desktop\\Brian's Folder\\FSU Courses\\My Projects\\ATM\\Console Version\\Receipt.txt");
		Date date = new Date();

		file.println("Today is: " + date + "\n");

		Console console = System.console();

		String acctNo, PIN;
		char[] PIN1;
		int track = 0;
		int attempt = 0;

		do {
			if(attempt == 3) {
				System.out.println("ATM System locked! Restart to unlock");
				System.exit(0);
			}

			if(track > 0)
				System.out.println("Invalid Account!");

		System.out.print("Account number (or 'cancel' to quit): ");
		acctNo = input.next();
		track++;
		attempt++;

		if(acctNo.equals("cancel"))
		System.exit(0);


		} while(acctNo.length() < 6 || acctNo.length() > 6 || (acctNo.matches("[0-9]+") == false)
		|| (!(acctNo.equals("123456"))));

		track = 0;
		attempt = 0;

		do {
			if(attempt == 3) {
				System.out.println("ATM System locked! Restart to unlock");
				System.exit(0);
			}

			if(track > 0)
				System.out.println("Invalid PIN!");

			System.out.print("PIN: ");
			PIN1 = console.readPassword();
			PIN = new String(PIN1);

			track++;
			attempt++;

		} while(PIN.length() < 4 || PIN.length() > 4 || (PIN.matches("[0-9]+") == false) || (!PIN.equals("1111")));

		System.out.println("Welcome, Brian...");

		Client client = new Client(acctNo, PIN, 10.00);

		int select;

		do {
			System.out.print("\nEnter:\n\t1. (1) for balance inquiry\n\t2. (2) for cash withdrawal" +
			"\n\t3. (3) for cash deposit\n\t4. (4) to quit\n\tWhat would you like to do: ");

				select = input.nextInt();

				switch(select) {

					case 1: {
						System.out.println("\nBalance inquiry...\n" + client);
						file.print("Balance inquiry...\n" + client);
						break;
					}

					case 2:  {
						ATM w1 = new WithdrawalATM(client);
						System.out.print("\n\tWithdraw amount: $");
						double w = input.nextDouble();
						file.print("\n\tWithdraw amount: $" + w);
						w1.withdraw(w, file); break;
					}

					case 3: {
						ATM d1 = new DepositATM(client);
						System.out.print("\n\tDeposit amount: $");
						double d = input.nextDouble();
						file.print("\n\tDeposit amount: $" + d);
						d1.depositCash(d, file); break;
					}

					case 4: {
						System.out.println("\nHave a nice day!");
						file.println("\nHave a nice day!"); break;
					}

					default:
						System.out.println("Invalid option!"); break;

				}

		} while(select != 4);

		file.close();

	}
}