//package ATM;

/**
* @author Brian Perel
* @version 1.0
*
* -OOP app using Java
* -ATM functions: display balance, withdrawal, deposit, transfer funds, terminate account
* -ATM withdrawal should be prevented if balance is below withdrawal amount
* -ATM deposit should be prevented if deposit amount is extreme
* -Keep track of 3 attributes: account number, PIN number, account balance
* -Validate all user input: exception, type and condition handling
* -Save information to receipt txt file, at end of program ask if client wants a receipt or not, if not receipt file is deleted
* -All currency is in USD $
*/

/*
GUI design: window 1 = appears, enter acctno, if acctno is correct proceed to 2nd window
			window 2 = appears, enter pin, if pin is correct proceed to 3rd window
			window 3 = show 6 options
			sub windows = 1 appears for each option entered
*/

import java.util.Scanner;
import java.io.Console;
import java.util.InputMismatchException;
import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Date;
import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;



class Account {

	private int number;
	private String acctNo;
	private String pin;
	private double balance;
	private String acctType;

	public Account() {
		this(null, null, 0.00, null);
	}
	public Account(String acctNo, String pin) {
		this.acctNo = acctNo;
		this.pin = pin;
	}
	public Account(String acctNo, String pin, double balance, String acctType) throws IllegalArgumentException {
		this.acctNo = acctNo;
		this.pin = pin;
		this.acctType = acctType;

		if(balance < 0)
			throw new IllegalArgumentException("Value less than 0!");

		this.balance = balance;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}
	public void setPIN(String pin) {
		this.pin = pin;
	}
	public void setBalance(double balance) throws IllegalArgumentException{
		this.balance = balance;

		if(balance < 0)
			throw new IllegalArgumentException("Value less than 0!");
	}
	public void setType(String acctType) {
		this.acctType = acctType;
	}
	public int getNumber() {
		return this.number;
	}
	public String getAcctNo() {
		return this.acctNo;
	}
	public String getPIN() {
		return this.pin;
	}
	public double getBalance() {
		return this.balance;
	}
	public String getType() {
		return this.acctType;
	}
	public String toString() {
		return String.format("\tAccount number: %s\n\tPIN number: %s" +
		"\n\tAccount balance: $%,.2f\n\tAccount type: %s", this.acctNo, this.pin, this.balance, this.acctType);
	}
	public boolean equals(Object o) {
		if(o == null || !(o instanceof Account)) {
			return false;
		}
		else return true;
	}
	public int hashCode() {
		return number * 12;
	}
}



abstract class ATM {
	private Account account; // we need this so that we can call methods of account class

	public ATM(Account account) {
		this.account = account;
	}
	public void displayBalance() {
		System.out.println("Balance: " + account.getBalance());
	}
	public abstract void depositCash(PrintWriter file) throws IOException;
	public abstract void withdraw(PrintWriter file) throws IOException;
	public abstract void transferFunds(String acctNo2, PrintWriter file) throws IOException;
}




class DepositFunds extends ATM {
	static DecimalFormat df = new DecimalFormat("$###,###.00");
	private Account account;

	public DepositFunds(Account account) {
		super(account);
		this.account = account;
	}

	@Override
	public void depositCash(PrintWriter file) {
		Scanner input = new Scanner(System.in);

		String money0;

		do {
			money0 = JOptionPane.showInputDialog("\n\nDeposit amount: $");

			if(money0.matches("[0-9]+") == false)
				JOptionPane.showMessageDialog(null, "Invalid amount!");

		}while(money0.matches("[0-9]+") == false);

		double money  = Double.parseDouble(money0);
		file.println("\n\tDeposit amount: $" + money);

		if(money >= 1 && money < 1000000000) {
			account.setBalance(account.getBalance() + money);
			file.print("\nDepositing...");
			JOptionPane.showMessageDialog(null, "Deposit Complete! Your New Balance is: " + df.format(account.getBalance()));
			file.printf("Deposit Complete! Your New Balance is: $%,.2f\n", account.getBalance());
		}
		else {
			try {
				JOptionPane.showMessageDialog(null, "\tError: You don't have sufficient funds!");

				if(money == 0) {
					JOptionPane.showMessageDialog(null, "\nDeposit operation cancelled...");
					file.printf("Deposit operation cancelled!");
				} else {
					depositCash(file);
				}
			}
			catch(InputMismatchException inputMismatchException) {
				JOptionPane.showMessageDialog(null, "\tError! Enter a number choice\n");
				depositCash(file);
			}
		}
	}

	@Override
	public void withdraw(PrintWriter file) throws IOException {}
	public void transferFunds(String acctNo2, PrintWriter file) throws IOException {}
}





class WithdrawalFunds extends ATM {

	private Account account;
	static DecimalFormat df = new DecimalFormat("$###,###.00");

	public WithdrawalFunds(Account account) {
		super(account);
		this.account = account;
	}

	@Override
	public void withdraw(PrintWriter file) throws IOException {
		Scanner input = new Scanner(System.in);

		String money0;

		do {
			money0 = JOptionPane.showInputDialog("\n\tWithdraw amount: $");

			if(money0.matches("[0-9]+") == false)
				JOptionPane.showMessageDialog(null, "Invalid amount!");

		}while(money0.matches("[0-9]+") == false);

		double money  = Double.parseDouble(money0);
		file.print("\n\tWithdraw amount: $" + money);

		if(money > 0 && money < account.getBalance()) {
			account.setBalance(account.getBalance() - money);
			file.print("\nWithdrawing...");
			JOptionPane.showMessageDialog(null, "Withdraw Complete! Your New Balance is: " + df.format(account.getBalance()));
			file.printf("Withdraw complete! Your New Balance is: $%,.2f\n", account.getBalance());
		}
		else if(money <= 0) {

			try {
				JOptionPane.showMessageDialog(null, "\tAmount entered is too little!");

				if(money == 0) {
					JOptionPane.showMessageDialog(null, "\nWithdraw operation cancelled...");
					file.println("Withdraw operation cancelled...");

				} else {
					withdraw(file);
				}
			}
			catch(InputMismatchException inputMismatchException) {
				JOptionPane.showMessageDialog(null, "\tError! Enter a number choice. Invalid option!\n");
				withdraw(file);
			}

			if(money == 0) {
				JOptionPane.showMessageDialog(null, "\nWithdraw operation cancelled...");
				file.println("Withdraw opertion cancelled...");
			} else {
				withdraw(file);
			}
		}

		else if(money > account.getBalance()) {
			JOptionPane.showMessageDialog(null, "\tError: You don't have sufficient funds!");

			if(money == 0) {
				JOptionPane.showMessageDialog(null, "\nWithdraw operation cancelled...");
				file.printf("Withdraw operation cancelled...");
			} else {
				withdraw(file);
			}
		}
	}

	@Override
	public void depositCash(PrintWriter file) throws IOException {}
	public void transferFunds(String acctNo2, PrintWriter file) throws IOException {}
}




class TransferFunds extends ATM {

	private Account account;
	private Account account2;
	static DecimalFormat df = new DecimalFormat("$###,###.00");

	public TransferFunds(Account account, Account account2) {
		super(account);
		this.account = account;
		this.account2 = account2;
	}
	@Override
	public void transferFunds(String acctNo2, PrintWriter file) throws IOException {
		Scanner input = new Scanner(System.in);

		String money0;

		do {
			money0 = JOptionPane.showInputDialog("\n\nTransfer amount: $");

			if(money0.matches("[0-9]+") == false)
				JOptionPane.showMessageDialog(null, "Invalid amount!");

		}while(money0.matches("[0-9]+") == false);

		double money = Double.parseDouble(money0);
		file.print("\n\tTransfer amount: $" + money);

			if(money > 0 && money < account.getBalance()) {
				account.setBalance(account.getBalance() - money);
				file.print("\nTransferring...");
				JOptionPane.showMessageDialog(null, "\nTransfer complete!\nYour New Balance for Account 1 (" +
				account.getAcctNo() + ") is: " + df.format(account.getBalance()) + "\nYour New Balance for Account 2 (" +
				account2.getAcctNo() + ") is: " + df.format(account2.getBalance()));

				file.printf("Transfer complete! Your New Balance for Account " +
				account.getAcctNo() + " is: " + df.format(account.getBalance()) + "\nYour New Balance for Account " +
				account2.getAcctNo() + " is: " + df.format(account2.getBalance()));
			}
			else if(money <= 0) {

				try {
					money0 = JOptionPane.showInputDialog(null, "Amount entered is too little!");
					money  = Double.parseDouble(money0);

					if(money == 0) {
						JOptionPane.showMessageDialog(null, "\nTransfer operation cancelled...");
						file.println("Transfer operation cancelled");
					} else {
						transferFunds(acctNo2, file);
					}
				}
				catch(InputMismatchException inputMismatchException) {
					JOptionPane.showMessageDialog(null, "\tError! Enter a number choice. Invalid option!\n");
					transferFunds(acctNo2, file);
				}

				if(money == 0) {
					JOptionPane.showMessageDialog(null, "\nTransfer operation cancelled...");
					file.println("Transfer operation cancelled...");
				} else {
					transferFunds(acctNo2, file);
				}
			}

			else if(money > account.getBalance()) {
				JOptionPane.showMessageDialog(null, "\tError: You don't have sufficient funds!");

				if(money == 0) {
					JOptionPane.showMessageDialog(null, "\nTransfer operation cancelled...");
					file.println("Transfer operation cancelled...");
				} else {
					transferFunds(acctNo2, file);
				}
		}
	}

	@Override
	public void depositCash(PrintWriter file) throws IOException {}
	public void withdraw(PrintWriter file) throws IOException {}
}



public class ATM_Machine extends JFrame {

	static String acctNo;
	static String pin = "";
	static Scanner input = new Scanner(System.in);
	private Button button_1;

	public static void main(String[] args) throws IOException {

		File fileMain = new File("Receipt.txt");
		PrintWriter file = new PrintWriter(fileMain);

		Date date = new Date();
		file.println("\t\tCity Central Bank\n\nToday is: " + date + "\n");

		Console console = System.console();

		int attempt = 0;

		do {
			if(attempt == 3) {
				JOptionPane.showMessageDialog(null, "Max tries exceeded, ATM System locked! Restart to unlock");
				System.exit(0);
			}
			acctNo = JOptionPane.showInputDialog("City Central Bank\nToday is: " + date + "\n\nAccount Number: ");
			acctNo = acctNo.toLowerCase();

			attempt++;

			if(acctNo.equals("cancel")) {
				JOptionPane.showMessageDialog(null, "Have a nice day!");
				System.exit(0);
			}

			if(acctNo.length() != 6 || (acctNo.matches("[0-9]+") == false))
				JOptionPane.showMessageDialog(null, "Invalid Account Number!");

		} while(acctNo.length() != 6 || (acctNo.matches("[0-9]+") == false));

		attempt = 0;

		do {
			if(attempt == 3) {
				JOptionPane.showMessageDialog(null, "Max tries exceeded, ATM System locked! Restart to unlock");
				System.exit(0);
			}
		//	JOptionPane.showInputDialog("City Central Bank\nToday is: " + date);



			JPanel panel = new JPanel();
			JLabel label = new JLabel("Pin:");
			JPasswordField pass = new JPasswordField(10);
			panel.add(label);
			panel.add(pass);
			String[] options = new String[]{"OK", "Cancel"};


			int option = JOptionPane.showOptionDialog(null, panel, "Input",
											 JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
											 null, options, null);

			if(option == 0) { // pressing OK button

				char[] password = pass.getPassword();
				pin = new String(password);
			}
			else if(option == 1) { // pressing Cancel button
				System.exit(0);
			}

			attempt++;

			if(pin.length() != 4 || (pin.matches("[0-9]+") == false))
				JOptionPane.showMessageDialog(null, "Invalid Pin Number!");

		}while(pin.length() != 4 || (pin.matches("[0-9]+") == false));

		String savCheck0, savCheck;

		do {
			do {
			savCheck0 = JOptionPane.showInputDialog("Savings (s) or Checkings (c): ");
			}while(savCheck0.equals(""));

			savCheck = Character.toUpperCase(savCheck0.charAt(0)) + savCheck0.substring(1);

			if(savCheck.length() != 1 || (savCheck.matches("[A-Za-z]") == false) || !(savCheck.equals("S")) && !(savCheck.equals("C")))
				JOptionPane.showMessageDialog(null, "Invalid option!");

		} while(savCheck.length() != 1 || (savCheck.matches("[A-Za-z]") == false) || !(savCheck.equals("S")) && !(savCheck.equals("C")));

		String savCheck2 = "";
		if(savCheck.equals("C"))
			savCheck2 = "Checkings";

		else if(savCheck.equals("S"))
			savCheck2 = "Savings";

		Account account = new Account(acctNo, pin, (Math.random() * 100000), savCheck2);

		String select = "0";
		menu(account, file, select, savCheck, fileMain);
	}
	public static void menu(Account account, PrintWriter file, String select, String savCheck, File fileMain) throws IOException {
		boolean acctTerminated = false;

		do {

			try {
				select = JOptionPane.showInputDialog("Enter:\n\t1. (1) for balance inquiry\n\t2. (2) for cash withdrawal" +
				"\n\t3. (3) for cash deposit\n\t4. (4) to terminate account\n\t5." +
				" (5) to transfer funds\n\t6. (6) to quit\n\n\tSelect your transaction: \n");

					switch(select) {
						case "1": {
							if(account != null) {
								JOptionPane.showMessageDialog(null, account, "Balance Inquiry", JOptionPane.INFORMATION_MESSAGE);
								file.print("Balance inquiry...\n" + account);
							}
							else if(account == null) {
								JOptionPane.showMessageDialog(null, "Account is empty", "Warning!", JOptionPane.INFORMATION_MESSAGE);
								file.print("Balance inquiry...\n\tAccount doesn't exist");
							}
							break;
						}

						case "2": {
							if(acctTerminated == true) {
								JOptionPane.showMessageDialog(null, "Account is empty, can't withdraw!", "Warning!", JOptionPane.INFORMATION_MESSAGE);
								continue;
							}
							ATM w1 = new WithdrawalFunds(account);
							w1.withdraw(file);
							break;
						}

						case "3": {
							if(acctTerminated == true) {
								JOptionPane.showMessageDialog(null, "Account is empty, can't deposit!", "Warning!", JOptionPane.INFORMATION_MESSAGE);
								continue;
							}
							ATM d1 = new DepositFunds(account);
							d1.depositCash(file);
							break;
						}

						case "4": {
							if(acctTerminated == true) {
								JOptionPane.showMessageDialog(null, "Account is already empty, can't transfer!", "Warning!", JOptionPane.INFORMATION_MESSAGE);
								continue;
							}

							account = null;
							JOptionPane.showMessageDialog(null, "\nAccount has been terminated\n", "Account Termination", JOptionPane.INFORMATION_MESSAGE);
							file.println("\nAccount has been terminated");
							acctTerminated = true;
							break;
						}

						case "5": {
							if(acctTerminated == true) {
								JOptionPane.showMessageDialog(null, "Account is empty, can't transfer!", "Warning!", JOptionPane.INFORMATION_MESSAGE);
								continue;
							}
							String acctNo2;

							do {
								acctNo2 = JOptionPane.showInputDialog("\nAccount Number 2: ");
								if(acctNo.equals(acctNo2) || acctNo2.length() < 6 || acctNo2.length() > 6 || (acctNo2.matches("[0-9]+") == false)) {
									JOptionPane.showMessageDialog(null, "Invalid Account!", "Warning!", JOptionPane.INFORMATION_MESSAGE);
								}

							} while(acctNo.equals(acctNo2) || (acctNo2.length() < 6 || acctNo2.length() > 6 || (acctNo2.matches("[0-9]+") == false)));
							Account account2 = new Account(acctNo2, pin, Math.random() * 100000, savCheck);
							ATM t1 = new TransferFunds(account, account2);
							t1.transferFunds(acctNo2, file);
							break;
						}

						case "6": {
							file.print("\n\nHave a nice day!");
							String in = JOptionPane.showInputDialog("\nWould you like a receipt? ");
							file.close();

							if(in.equals("No") || in.equals("no")) {
								fileMain.delete();
							}
							else {
								JOptionPane.showMessageDialog(null, "Receipt saved as txt file: " + fileMain.getName(), "Want a receipt?", JOptionPane.INFORMATION_MESSAGE);
							}

							JOptionPane.showMessageDialog(null, "\nHave a nice day!");
							System.exit(0);
							break;
						}

						default: {
							JOptionPane.showMessageDialog(null, "Invalid option!");
							break;
						}
					}

			} catch(InputMismatchException inputMismatchException) {
				JOptionPane.showMessageDialog(null, "\tError! Enter a number choice. Invalid option!\n");
				input.nextLine();
			}
		}while(select != "6");
	}
}