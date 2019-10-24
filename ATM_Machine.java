package ATM;

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
* -Save information to receipt txt file
* -All currency is in USD $
*/

import java.util.Scanner;
import java.io.Console;
import java.util.InputMismatchException;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Date;

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
	public void setBalance(double balance) {
		this.balance = balance;
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
	public abstract void depositCash(double money, PrintWriter file) throws IOException;
	public abstract void withdraw(double money, PrintWriter file) throws IOException;
	public abstract void transferFunds(double money, String acctNo2, PrintWriter file) throws IOException;
}




class DepositFunds extends ATM {
	private Account account;

	public DepositFunds(Account account) {
		super(account);
		this.account = account;
	}

	@Override
	public void depositCash(double money, PrintWriter file) {
		Scanner input = new Scanner(System.in);

		if(money >= 1 && money < 1000000000) {
				account.setBalance(account.getBalance() + money);
				System.out.println("\nDepositing...");
				file.print("\nDepositing...");
				System.out.printf("Deposit Complete! Your New Balance is: $%,.2f\n", account.getBalance());
				file.printf("Deposit Complete! Your New Balance is: $%,.2f\n", account.getBalance());
		}

		else if(money <= 0 || money > 1000000000) {

			double d;

			try {
				System.out.print("\tPlease enter a different amount to deposit or enter 0 to cancel depositing: $");
				d = input.nextDouble();

				if(d == 0) {
					System.out.println("\nDeposit operation cancelled...");
				} else {
					depositCash(d, file);
				}
			}
			catch(InputMismatchException inputMismatchException) {
				System.out.println("\tError! Enter a number choice\n");
				input.nextLine();
				d = 0;
				depositCash(d, file);
			}
		}
	}

	@Override
	public void withdraw(double money, PrintWriter file) throws IOException {}
	public void transferFunds(double money, String acctNo2, PrintWriter file) throws IOException {}
}



class WithdrawalFunds extends ATM {

	private Account account;

	public WithdrawalFunds(Account account) {
		super(account);
		this.account = account;
	}

	@Override
	public void withdraw(double money, PrintWriter file) throws IOException {
		Scanner input = new Scanner(System.in);

		if(money > 0 && money < account.getBalance()) {
			account.setBalance(account.getBalance() - money);
			System.out.println("\nWithdrawing...");
			file.print("\nWithdrawing...");
			System.out.printf("Withdraw complete! Your New Balance is: $%,.2f\n", account.getBalance());
			file.printf("Withdraw complete! Your New Balance is: $%,.2f\n", account.getBalance());
		}

		else if(money <= 0) {

			double w;

			try {

				System.out.print("\tPlease enter a different amount to withdraw or 0 to cancel withdraw: $");
				w = input.nextDouble();

				if(w == 0) {
					System.out.println("\nWithdraw operation cancelled...");
				} else {
					withdraw(w, file);
				}
			}
			catch(InputMismatchException inputMismatchException) {
				System.out.println("\tError! Enter a number choice. Invalid option!\n");
				input.nextLine();
				w = 0;
				withdraw(w, file);
			}

			if(w == 0) {
				System.out.println("\tWithdraw operation cancelled...");
			} else {
				withdraw(w, file);
			}
		}

		else if(money > account.getBalance()) {
			System.out.println("\tError: You don't have sufficient funds!");
			System.out.print("\tPlease enter a different amount to withdraw or 0 to cancel withdraw: $");
			double w = input.nextDouble();

			if(w == 0) {
				System.out.println("\tWithdraw operation cancelled...");
			} else {
				withdraw(w, file);
			}
		}
	}
	public void depositCash(double money, PrintWriter file) throws IOException {}
	public void transferFunds(double money, String acctNo2, PrintWriter file) throws IOException {}
}





class TransferFunds extends ATM {

	private Account account;

	public TransferFunds(Account account) {
		super(account);
		this.account = account;
	}
	@Override
	public void transferFunds(double money, String acctNo2, PrintWriter file) throws IOException {
		Scanner input = new Scanner(System.in);

			if(money > 0 && money < account.getBalance()) {
				account.setBalance(account.getBalance() - money);
				System.out.println("\nTransferring...");
				file.print("\nTransferring...");
				System.out.printf("\nTransfer complete! Your New Balance is: $%,.2f\n", account.getBalance());
				file.printf("Transfer complete! Your New Balance is: $%,.2f\n", account.getBalance());
			}

			else if(money <= 0) {

				double t;

				try {

					System.out.print("\tPlease enter a different amount to transfer or 0 to cancel transfer operation: $");
					t = input.nextDouble();

					if(t == 0) {
						System.out.println("\nTransfer operation cancelled...");
					} else {
						transferFunds(t, acctNo2, file);
					}
				}
				catch(InputMismatchException inputMismatchException) {
					System.out.println("\tError! Enter a number choice. Invalid option!\n");
					input.nextLine();
					t = 0;
					transferFunds(t, acctNo2, file);
				}

				if(t == 0) {
					System.out.println("\nTransfer operation cancelled...");
				} else {
					transferFunds(t, acctNo2, file);
				}
			}

			else if(money > account.getBalance()) {
				System.out.println("\tError: You don't have sufficient funds!");
				System.out.print("\tPlease enter a different amount to transfer or 0 to cancel transfer operation: $");
				double t = input.nextDouble();

				if(t == 0) {
					System.out.println("\nTransfer operation cancelled...");
				} else {
					transferFunds(t, acctNo2, file);
				}
		}
	}

	public void depositCash(double money, PrintWriter file) throws IOException {}
	public void withdraw(double money, PrintWriter file) throws IOException {}
}






public class ATM_Machine {

	static String acctNo;

	static Scanner input = new Scanner(System.in);

	public static void main(String[] args) throws IOException {

		PrintWriter file = new PrintWriter("C:\\Users\\brian\\Desktop\\Brian's Folder\\FSU Courses\\My Projects\\ATM\\Receipt.txt");

		Date date = new Date();

		System.out.print("\t\tCity Central Bank");
		file.println("\t\tCity Central Bank\n\nToday is: " + date + "\n");
		System.out.println("\n\nToday is: " + date + "\n");

		Console console = System.console();

		char[] pin1;
		String pin;
		int attempt = 0;

		do {
			if(attempt == 3) {
				System.out.println("Max tries exceeded, ATM System locked! Restart to unlock");
				System.exit(0);
			}

			System.out.print("Account number (or 'cancel' to quit): ");
			acctNo = input.next();

			attempt++;

			if(acctNo.equals("Cancel") || acctNo.equals("cancel") || acctNo.equals("CANCEL")) {
				System.out.println("Have a nice day!");
				System.exit(0);
			}

			if(acctNo.length() < 6 || acctNo.length() > 6 || (acctNo.matches("[0-9]+") == false))
				System.out.println("Invalid Account!");

		} while(acctNo.length() < 6 || acctNo.length() > 6 || (acctNo.matches("[0-9]+") == false));

		attempt = 0;

		do {
			if(attempt == 3) {
				System.out.println("Max tries exceeded, ATM System locked! Restart to unlock");
				System.exit(0);
			}

			System.out.print("PIN: ");
			pin1 = console.readPassword("****");

			pin = new String(pin1);

			if(pin.length() < 4 || pin.length() > 4 || (pin.matches("[0-9]+") == false))
				System.out.println("Invalid PIN!");

			attempt++;

		} while(pin.length() < 4 || pin.length() > 4 || (pin.matches("[0-9]+") == false));

		System.out.println("Welcome, Brian...\n\n");

		System.out.println("Enter:");
		String savCheck0;
		String savCheck;

		do {
			System.out.print("\tSavings (s) or Checkings (c): ");
			savCheck0 = input.next();
			savCheck = Character.toUpperCase(savCheck0.charAt(0)) + savCheck0.substring(1);

			if(savCheck.length() != 1 || (savCheck.matches("[A-Za-z]") == false))
				System.out.println("\tError! Enter 's' or 'c'");

		} while(savCheck.length() != 1 || (savCheck.matches("[A-Za-z]") == false));


		String savCheck2 = "";
		if(savCheck.equals("C"))
			savCheck2 = "Checkings";

		else if(savCheck.equals("S"))
			savCheck2 = "Savings";

		Account account = new Account(acctNo, pin, (Math.random() * 100000), savCheck2);

		int select = 0;
		menu(account, file, select, savCheck);
	}
	public static void menu(Account account, PrintWriter file, int select, String savCheck) throws IOException {
		boolean rightType = false;
		do {

			try {
				System.out.print("\nEnter:\n\t1. (1) for balance inquiry\n\t2. (2) for cash withdrawal" +
				"\n\t3. (3) for cash deposit\n\t4. (4) to terminate account\n\t5. (5) to transfer funds\n\t6. (6) to quit\n\tSelect your transaction: ");

					select = input.nextInt();
					switch(select) {
						case 1: {
							if(account != null) {
								System.out.println("\nBalance inquiry...\n" + account);
								file.print("Balance inquiry...\n" + account);
							}
							else if(account == null) {
								System.out.println("\nAccount is empty!");
								file.print("Balance inquiry...\n\tAccount doesn't exist");
							}
							rightType = true;
							break;
						}

						case 2: {
							ATM w1 = new WithdrawalFunds(account);
							System.out.print("\n\tWithdraw amount: $");
							double w = input.nextDouble();
							file.print("\n\tWithdraw amount: $" + w);
							w1.withdraw(w, file);
							rightType = true;
							break;
						}

						case 3: {
							ATM d1 = new DepositFunds(account);
							System.out.print("\n\tDeposit amount: $");
							double d = input.nextDouble();
							file.println("\n\tDeposit amount: $" + d);
							rightType = true;
							d1.depositCash(d, file);
							break;
						}

						case 4: {
							account = null;
							System.out.println("\nAccount has been terminated\n");
							file.println("\nAccount has been terminated");
							rightType = true;
							break;
						}

						case 5: {
							ATM t1 = new TransferFunds(account);
							String acctNo2;
							System.out.println("");

							do {
								System.out.print("Account Number: ");
								acctNo2 = input.next();
								if(acctNo.equals(acctNo2) || acctNo2.length() < 6 || acctNo2.length() > 6 || (acctNo2.matches("[0-9]+") == false))
									System.out.println("Invalid Account!");
							} while(acctNo.equals(acctNo2) || (acctNo2.length() < 6 || acctNo2.length() > 6 || (acctNo2.matches("[0-9]+") == false)));

							System.out.print("Transfer amount: $");
							double t = input.nextDouble();
							rightType = true;
							t1.transferFunds(t, acctNo2, file);
							break;
						}

						case 6: {
							System.out.println("\nHave a nice day!");
							file.print("\n\nHave a nice day!");
							rightType = true;
							break;
						}

						default: {
							System.out.println("Invalid option!");
							rightType = true;
							break;
						}
					}

			} catch(InputMismatchException inputMismatchException) {
				System.out.println("\tError! Enter a number choice. Invalid option!\n");
				input.nextLine();
			}
		}while(!rightType || select != 6);

	file.close();
	}
}