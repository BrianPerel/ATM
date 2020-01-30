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


* GUI design: window 1 = appears, enter acctno, if acctno is correct proceed to 2nd window
			window 2 = appears, enter pin, if pin is correct proceed to 3rd window
			window 3 = show 6 options
			sub windows = 1 appears for each option entered

*/


import java.util.Scanner;
import java.util.InputMismatchException;
import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JPasswordField;
import javax.swing.JOptionPane;
import java.awt.Button;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;


/*
* -Class to setup Account object, use Serializable interface to use serialize operations
* -Attributes: account type, number, pin, balance
*/
class Account implements java.io.Serializable {

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

		if (balance < 0)
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

	public void setBalance(double balance) throws IllegalArgumentException {
		this.balance = balance;

		if (balance < 0)
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

	@Override
	public String toString() {
		return String.format(
				"\tAccount number: %s\n\tPIN number: %s" + "\n\tAccount balance: $%,.2f\n\tAccount type: %s\n",
				this.acctNo, this.pin, this.balance, this.acctType);
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Account) || o != this) {
			return false;
		} else
			return true;
	}

	@Override
	public int hashCode() {
		return number * 12;
	}
}



/*
* -Abstract class, since it's methods will be overwritten by sub classes
* -We need the account field so that we can call methods of account class
* -Has method to display the balance, holds abstract methods for basic app operations
*/
abstract class ATM {
	private final Account account;

	public ATM(Account account) {
		this.account = account;
	}

	public void displayBalance() {
		System.out.println("Balance: " + this.account.getBalance());
	}

	public abstract void depositCash(PrintWriter file) throws IOException;

	public abstract void withdraw(PrintWriter file) throws IOException;

	public abstract void transferFunds(String acctNo2, PrintWriter file) throws IOException;
}


/*
* -Inherit ATM program for primary methods / operations
* -Performs deposit operation
*/
class DepositFunds extends ATM {
	static DecimalFormat df = new DecimalFormat("$###,###.00");   // for decimal rounding (to 2 places and comma insertion)
	private final Account account;

	public DepositFunds(Account account) {
		super(account);
		this.account = account;
	}

	@Override
	public void depositCash(PrintWriter file) {

		String money0;

		do {
			money0 = JOptionPane.showInputDialog(null, "\n\nDeposit amount: $", "Deposit",
					JOptionPane.QUESTION_MESSAGE);

			if (money0.matches("[0-9]+") == false)
				JOptionPane.showMessageDialog(null, "Invalid amount!", "Warning", JOptionPane.WARNING_MESSAGE);

		} while (money0.matches("[0-9]+") == false);

		double money = Double.parseDouble(money0);
		file.println("\n\tDeposit amount: $" + money);

		if (money >= 1 && money < 1000000000) {
			account.setBalance(account.getBalance() + money);
			file.print("\nDepositing...");
			JOptionPane.showMessageDialog(null,
					"Deposit Complete! Your New Balance is: " + df.format(this.account.getBalance()));
			file.printf("Deposit Complete! Your New Balance is: $%,.2f\n", this.account.getBalance());
		} else {
			try {
				JOptionPane.showMessageDialog(null, "\tError: You don't have sufficient funds!", "Warning",
						JOptionPane.WARNING_MESSAGE);

				if (money == 0) {
					JOptionPane.showMessageDialog(null, "\nDeposit operation cancelled...");
					file.printf("Deposit operation cancelled!");
				} else {
					depositCash(file);
				}
			} catch (InputMismatchException inputMismatchException) {
				JOptionPane.showMessageDialog(null, "\tError! Enter a number choice\n", "Warning",
						JOptionPane.WARNING_MESSAGE);
				depositCash(file);
			}
		}
	}

	@Override
	public void withdraw(PrintWriter file) throws IOException {
	}

	@Override
	public void transferFunds(String acctNo2, PrintWriter file) throws IOException {
	}
}

/*
* -Inherit ATM program for primary methods / operations
* -Performs withdraw operations
*/
class WithdrawalFunds extends ATM {

	private final Account account;
	static DecimalFormat df = new DecimalFormat("$###,###.00");

	public WithdrawalFunds(Account account) {
		super(account);
		this.account = account;
	}

	@Override
	public void withdraw(PrintWriter file) throws IOException {

		String money0;

		do {
			money0 = JOptionPane.showInputDialog(null, "\nWithdraw amount: $", "Withdraw", JOptionPane.QUESTION_MESSAGE);

			if (money0.matches("[0-9]+") == false)
				JOptionPane.showMessageDialog(null, "Invalid amount!", "Warning", JOptionPane.WARNING_MESSAGE);

		} while (money0.matches("[0-9]+") == false);

		final double money = Double.parseDouble(money0);
		file.print("\n\tWithdraw amount: $" + money);

		if (money > 0 && money < account.getBalance()) {
			this.account.setBalance(this.account.getBalance() - money);
			file.print("\nWithdrawing...");
			JOptionPane.showMessageDialog(null,
					"Withdraw Complete! Your New Balance is: " + df.format(account.getBalance()), "Withdraw",
					JOptionPane.QUESTION_MESSAGE);
			file.printf("Withdraw complete! Your New Balance is: $%,.2f\n", account.getBalance());
		} else if (money <= 0) {

			try {
				JOptionPane.showMessageDialog(null, "\tAmount entered is too little!", "Warning",
						JOptionPane.WARNING_MESSAGE);

				if (money == 0) {
					JOptionPane.showMessageDialog(null, "\nWithdraw operation cancelled...");
					file.println("Withdraw operation cancelled...");
				} else {
					withdraw(file);
				}
			} catch (InputMismatchException inputMismatchException) {
				JOptionPane.showMessageDialog(null, "\tError! Enter a number choice. Invalid option!\n", "Warning",
						JOptionPane.WARNING_MESSAGE);
				withdraw(file);
			}
		}

		else if (money > account.getBalance()) {
			JOptionPane.showMessageDialog(null, "\tError: You don't have sufficient funds!", "Warning",
					JOptionPane.QUESTION_MESSAGE);

			if (money == 0) {
				JOptionPane.showMessageDialog(null, "\nWithdraw operation cancelled...");
				file.printf("Withdraw operation cancelled...");
			} else {
				withdraw(file);
			}
		}
	}

	@Override
	public void depositCash(PrintWriter file) throws IOException {
	}

	@Override
	public void transferFunds(String acctNo2, PrintWriter file) throws IOException {
	}
}


/*
* -Inherit ATM program for primary methods / operations
* -Performs transfer operations
*/
class TransferFunds extends ATM {

	private final Account account;
	private final Account account2;
	static DecimalFormat df = new DecimalFormat("$###,###.00");

	public TransferFunds(Account account, Account account2) {
		super(account);
		this.account = account;
		this.account2 = account2;
	}

	@Override
	public void transferFunds(String acctNo2, PrintWriter file) throws IOException {

		String money0;

		do {
			money0 = JOptionPane.showInputDialog(null, "\nTransfer amount: $", "ATM", JOptionPane.QUESTION_MESSAGE);

			if (money0.matches("[0-9]+") == false)
				JOptionPane.showMessageDialog(null, "Invalid amount!", "Warning", JOptionPane.WARNING_MESSAGE);

		} while (money0.matches("[0-9]+") == false);

		double money = Double.parseDouble(money0);
		file.print("\n\tTransfer amount: $" + money);

		if (money > 0 && money < this.account.getBalance()) {
			this.account.setBalance(this.account.getBalance() - money);
			file.print("\nTransferring...");
			JOptionPane.showMessageDialog(null,
					"\nTransfer complete!\nYour New Balance for Account 1 (" + account.getAcctNo() + ") is: "
							+ df.format(this.account.getBalance()) + "\nYour New Balance for Account 2 ("
							+ this.account2.getAcctNo() + ") is: " + df.format(this.account2.getBalance()),
					"ATM - City Central Bank", JOptionPane.QUESTION_MESSAGE);

			file.printf("Transfer complete! Your New Balance for Account " + account.getAcctNo() + " is: "
					+ df.format(this.account.getBalance()) + "\nYour New Balance for Account " + this.account2.getAcctNo()
					+ " is: " + df.format(this.account2.getBalance()));
		} else if (money <= 0) {

			try {
				money0 = JOptionPane.showInputDialog(null, "Amount entered is too little!", "Warning",
						JOptionPane.QUESTION_MESSAGE);
				money = Double.parseDouble(money0);

				if (money == 0) {
					JOptionPane.showMessageDialog(null, "\nTransfer operation cancelled...", "Cancelled",
							JOptionPane.QUESTION_MESSAGE);
					file.println("Transfer operation cancelled");
				} else {
					transferFunds(acctNo2, file);
				}
			} catch (InputMismatchException inputMismatchException) {
				JOptionPane.showMessageDialog(null, "\tError! Enter a number choice. Invalid option!\n", "Warning",
						JOptionPane.QUESTION_MESSAGE);

				transferFunds(acctNo2, file);
			}
		}

		else if (money > this.account.getBalance()) {
			JOptionPane.showMessageDialog(null, "\tError: You don't have sufficient funds!", "Warning",
					JOptionPane.WARNING_MESSAGE);

			if (money == 0) {
				JOptionPane.showMessageDialog(null, "\nTransfer operation cancelled...", "Cancelled",
						JOptionPane.WARNING_MESSAGE);

				file.println("Transfer operation cancelled...");
			} else {
				transferFunds(acctNo2, file);
			}
		}
	}

	@Override
	public void depositCash(PrintWriter file) throws IOException {
	}

	@Override
	public void withdraw(PrintWriter file) throws IOException {
	}
}


/*
* -ATM_Machine class will generate GUI
* -Performs logic operations for data and choices entered
*/
public class ATM_Machine extends JFrame {

	static String acctNo, savCheck0;
	static String pin = "";
	static Scanner input = new Scanner(System.in);
	private Button button_1;
	static DecimalFormat df = new DecimalFormat("$###,###.00");

	public static void main(String[] args) throws IOException {

		final File fileMain = new File("Receipt.txt");
		final PrintWriter file = new PrintWriter(fileMain);

		int attempt = 0;

		do {
			if (attempt == 3) {
				JOptionPane.showMessageDialog(null, "Max tries exceeded, ATM System locked! Restart to unlock", "ATM",
						JOptionPane.WARNING_MESSAGE);
				file.close();
				fileMain.delete();
				System.exit(0);
			}

			DateTimeFormatter tf = DateTimeFormatter.ofPattern("MMM dd, h:mm a");
			java.time.LocalDateTime now = java.time.LocalDateTime.now();


			try {
				acctNo = JOptionPane.showInputDialog(null,
					"Today is: " + now.format(tf) + "\nAccount Number: ", "ATM - City Central Bank",
					JOptionPane.QUESTION_MESSAGE);

				acctNo = acctNo.trim();

			} catch(NullPointerException e) {
				System.exit(0);
			}

			file.printf("\n\tATM - City Central Bank\nToday is: %s\n", now.format(tf));

			attempt++;

			if (acctNo.equals("cancel")) {
				JOptionPane.showMessageDialog(null, "Have a nice day!", "Goodbye", JOptionPane.QUESTION_MESSAGE);
				file.close();
				fileMain.delete();
				System.exit(0);
			}

			if (acctNo.length() != 8 || (acctNo.matches("[0-9]+") == false))
				JOptionPane.showMessageDialog(null, "Invalid Account Number!", "Warning", JOptionPane.WARNING_MESSAGE);

		} while (acctNo.length() != 8 || (acctNo.matches("[0-9]+") == false));

		attempt = 0;

		do {
			if (attempt == 3) {
				JOptionPane.showMessageDialog(null, "Max tries exceeded, ATM System locked! Restart to unlock", "ATM - City Central Bank",
						JOptionPane.WARNING_MESSAGE);
				file.close();
				fileMain.delete();
				System.exit(0);
			}

			JPanel panel = new JPanel();
			String custom = "<html><body><div float='left'>Pin: </div></body></html>";
			JLabel label = new JLabel(custom);
			JPasswordField pass = new JPasswordField(10);
			panel.add(label);
			panel.add(pass);
			String[] options = new String[] {"OK", "Cancel"};

			int option = JOptionPane.showOptionDialog(null, panel, "ATM - City Central Bank", JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, options, null);

			if (option == 0) { // pressing OK button

				char[] password = pass.getPassword();
				pin = new String(password);

			}else if (option == 1) { // pressing Cancel button
				System.exit(0);
			}else {
				System.exit(0);
			}


			attempt++;

			if(pin.length() != 4 || (pin.matches("[0-9]+") == false))
				JOptionPane.showMessageDialog(null, "Invalid Pin Number!", "Warning", JOptionPane.WARNING_MESSAGE);

		} while(pin.length() != 4 || (pin.matches("[0-9]+") == false));

		String savCheck;

		do {
			try {
				do {
					savCheck0 = JOptionPane.showInputDialog(null, "Savings (s) or Checkings (c): ", "ATM - City Central Bank",
						JOptionPane.QUESTION_MESSAGE);

				}while(savCheck0.equals(""));

			}catch(NullPointerException e) {
				System.exit(0);
			}

			savCheck0 = savCheck0.trim();
			savCheck = Character.toUpperCase(savCheck0.charAt(0)) + savCheck0.substring(1);

			if(savCheck.length() != 1 || (savCheck.matches("[A-Za-z]") == false)
					|| !(savCheck.equals("S")) && !(savCheck.equals("C")))
				JOptionPane.showMessageDialog(null, "Invalid option!", "Warning", JOptionPane.WARNING_MESSAGE);

		}while(savCheck.length() != 1 || (savCheck.matches("[A-Za-z]") == false)
				|| !(savCheck.equals("S")) && !(savCheck.equals("C")));

		String savCheck2 = "";
		if(savCheck.equals("C"))
			savCheck2 = "Checkings";

		else if (savCheck.equals("S"))
			savCheck2 = "Savings";

		Account account = new Account(acctNo, pin, ((Math.random() % 23) * 100000), savCheck2);

		String select = "0";
		menu(account, file, select, savCheck, fileMain);
	}

	public static void menu(Account account, PrintWriter file, String select, String savCheck, File fileMain) throws IOException {
		boolean acctTerminated = false;

		do {

			try {
				select = JOptionPane.showInputDialog(null,
						"Enter:\n\t1. (1) for balance inquiry\n\t2. (2) for cash withdrawal"
								+ "\n\t3. (3) for cash deposit\n\t4. (4) to terminate account\n\t5."
								+ " (5) to transfer funds\n\t6. (6) (Save) Serialize Account"
								+ "\n\t7. (7) (Load) Deserialize Account \n\t8. (8) to quit\n\n\tSelect your transaction: \n",
						"ATM- City Central Bank", JOptionPane.QUESTION_MESSAGE);

				switch (select) {

				case "1": {
					if (account != null) {
						JOptionPane.showMessageDialog(null, account, "Balance Inquiry",
								JOptionPane.INFORMATION_MESSAGE);
						file.print("\nBalance inquiry...\n" + account);
					} else if (account == null) {
						JOptionPane.showMessageDialog(null, "Account is empty", "Warning!",
								JOptionPane.WARNING_MESSAGE);
						file.print("Balance inquiry...\n\tAccount doesn't exist");
					}
					break;
				}

				case "2": {
					if (acctTerminated == true) {
						JOptionPane.showMessageDialog(null, "Account is empty, can't withdraw!", "Warning!",
								JOptionPane.WARNING_MESSAGE);
						continue;
					}
					ATM w1 = new WithdrawalFunds(account);
					w1.withdraw(file);
					break;
				}

				case "3": {
					if (acctTerminated == true) {
						JOptionPane.showMessageDialog(null, "Account is empty, can't deposit!", "Warning!",
								JOptionPane.WARNING_MESSAGE);
						continue;
					}
					ATM d1 = new DepositFunds(account);
					d1.depositCash(file);
					break;
				}

				case "4": {
					if (acctTerminated == true) {
						JOptionPane.showMessageDialog(null, "Account is already empty!", "Warning!",
								JOptionPane.WARNING_MESSAGE);
						continue;
					}

					account = null;
					JOptionPane.showMessageDialog(null, "\nAccount has been terminated\n", "Account Termination",
							JOptionPane.INFORMATION_MESSAGE);
					file.println("\nAccount has been terminated");
					acctTerminated = true;
					break;
				}

				case "5": {
					if (acctTerminated == true) {
						JOptionPane.showMessageDialog(null, "Account is empty, can't transfer!", "Warning!",
								JOptionPane.WARNING_MESSAGE);
						continue;
					}
					String acctNo2;

					do {
						acctNo2 = JOptionPane.showInputDialog(null, "\nAccount Number 2: ", "Account Terminated",
								JOptionPane.QUESTION_MESSAGE);
						if (acctNo.equals(acctNo2) || acctNo2.length() < 6 || acctNo2.length() > 6
								|| (acctNo2.matches("[0-9]+") == false)) {
							JOptionPane.showMessageDialog(null, "Invalid Account!", "Warning!",
									JOptionPane.WARNING_MESSAGE);
						}
					} while(acctNo.equals(acctNo2)
							|| (acctNo2.length() < 6 || acctNo2.length() > 6 || (acctNo2.matches("[0-9]+") == false)));
					Account account2 = new Account(acctNo2, pin, (Math.random() % 21) * 100000, savCheck);
					ATM t1 = new TransferFunds(account, account2);
					t1.transferFunds(acctNo2, file);
					break;
				}
				case "6": {
					if (acctTerminated == true) {
						JOptionPane.showMessageDialog(null, "Account is empty, can't serialize!", "Warning!",
								JOptionPane.WARNING_MESSAGE);
						continue;
					}
					// create binary file (.dat = data file) to save object state to
					String filename = "Data.dat";

					// Serialization
					try {
						// Saving of object in a file
						FileOutputStream file1 = new FileOutputStream(filename);
						ObjectOutputStream out = new ObjectOutputStream(file1);

						// method for serialization of object
						out.writeObject(account);

						// close method for serialization of object
						out.close();
						file1.close();

						JOptionPane.showMessageDialog(null, "\nObject has been serialized", "Serialize", JOptionPane.QUESTION_MESSAGE);
					}
					catch(IOException ex) {
						System.out.println("IOException is caught");
					}

					break;
				}

				case "7": {
					String filename = "Data.dat";

					Account account1 = null; // create the empty object, request os to allocate chunk of memory to store contents from file

					// Deserialization
					try {
						FileInputStream file2 = new FileInputStream(filename);
						ObjectInputStream in = new ObjectInputStream(file2);

						account1 = (Account)in.readObject(); // store the content from binary file to a reference variable (object)
															 // after reading = deserialize

						// print out the saved data from binary file
						JOptionPane.showMessageDialog(null, "\nAccount Number: " + account1.getAcctNo() + "\nAccount Pin: " +
												account1.getPIN() + "\nAccount Balance: " + df.format(account1.getBalance()) + "\nAccount type: " +
												account1.getType(), "Deserialize", JOptionPane.QUESTION_MESSAGE);
					}
					catch(IOException ex) {
						System.out.println("Deserialization error!");
					}
					catch(ClassNotFoundException ex) {
						System.out.println("Class not found error!");
					}

					break;
				}

				case "8": {
					file.print("\n\nHave a nice day!");
					String in = JOptionPane.showInputDialog(null, "\nWould you like a receipt? ", "Receipt?",
							JOptionPane.QUESTION_MESSAGE);

					file.close();
					if (in.equals("No") || in.equals("no") || in.equals("NO")) {
						fileMain.delete();
						JOptionPane.showMessageDialog(null, "\nHave a nice day!", "Goodbye", JOptionPane.QUESTION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(null, "Receipt saved as txt file: " + fileMain.getName(),
								"Receipt", JOptionPane.INFORMATION_MESSAGE);
						Runtime rt = Runtime.getRuntime();
						String file1 = "Receipt.txt";
						JOptionPane.showMessageDialog(null, "\nHave a nice day!", "Goodbye", JOptionPane.QUESTION_MESSAGE);
						Process p = rt.exec("notepad " + file1);
					}

					System.exit(0);
					break;
				}

				default: {
					JOptionPane.showMessageDialog(null, "Invalid option!", "Warning", JOptionPane.WARNING_MESSAGE);
					break;
				}
			}

			} catch (InputMismatchException inputMismatchException) {
				JOptionPane.showMessageDialog(null, "\tError! Enter a number choice. Invalid option!\n", "Warning",
						JOptionPane.WARNING_MESSAGE);

				input.nextLine();
			}
			catch(NullPointerException e) {
				System.exit(0);
			}
		} while (select != "8");
	}
}