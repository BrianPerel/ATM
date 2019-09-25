/**
-OOP app using Java
-ATM functions: display balance, withdrawal, deposit
-ATM withdrawal should be prevented if balance is below withdrawal amount
-Keep track of 3 attributes: account number, PIN number, account balance
*/

package ATM;

import java.util.Scanner;
import java.util.regex.Matcher;

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
	private Client client;

	public ATM(Client c) {
		this.client = c;
	}

	public void displayBalance() {
		System.out.println("Balance: " + client.getBalance());
	}
	public void depositCash(double money) {}

	public void withdraw(double money) {}
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
	public void depositCash(double money) {
		Scanner input = new Scanner(System.in);

		if(money >= 1 && money < 1000000000) {
				client.setBalance(client.getBalance() + money);
				System.out.println("\nDepositing...");
				System.out.printf("Deposit Complete! Your New Balance is: $%,.2f\n", client.getBalance());
		}

		else if(money <= 0) {
			System.out.print("\tPlease enter a different amount to deposit: ");
			double d = input.nextDouble();
			depositCash(d);
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
	public void withdraw(double money) {
		Scanner input = new Scanner(System.in);

		if(money > 0 && money < client.getBalance()) {
			client.setBalance(client.getBalance() - money);
			System.out.println("\nWithdrawing...");
			System.out.printf("Withdraw complete! Your New Balance is: $%,.2f\n", client.getBalance());
		}

		else if(money <= 0) {
			System.out.print("\tPlease enter a different amount to withdraw: ");
			double w = input.nextDouble();
			withdraw(w);
		}

		else if(money > client.getBalance()) {
			System.out.println("\tError: You don't have sufficient funds!");
			System.out.print("\tPlease enter a different amount to withdraw: ");
			double w = input.nextDouble();
			withdraw(w);
		}
	}
}



public class ATM_Machine {
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);

		String acctNo, PIN;
		int track = 0;
		int attempt = 0;

		do {
			if(attempt == 3) {
				System.out.println("ATM System locked! Restart to unlock");
				System.exit(0);
			}

			if(track > 0)
				System.out.println("Invalid Account!");

		System.out.print("Account number: ");
		acctNo = input.next();
		track++;
		attempt++;
		} while(acctNo.length() < 6 || acctNo.length() > 6 || (acctNo.matches("[0-9]+") == false)
		|| (acctNo.equals("123456") == false));

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
		PIN = input.next();
		track++;
		attempt++;
		} while(PIN.length() < 4 || PIN.length() > 4 || (PIN.matches("[0-9]+") == false) || (PIN.equals("1958") == false));

		System.out.println("Welcome, Brian...");

		Client client = new Client(acctNo, PIN, 0.00);
		ATM c1 = new ATM(client);

		char select;


		do {
			System.out.print("\nEnter:\n\t1. (b) for balance inquiry\n\t2. (w) for cash withdrawal" +
			"\n\t3. (d) for cash deposit\n\t4. (q) to quit\n\tWhat would you like to do: ");

			select = input.next().charAt(0);

				if(select == 'b') {
					System.out.println("\nBalance inquiry...\n" + client);
				}

				else if(select == 'w') {
					// Polymorphism, sub class withdraw method overrides the super class withdraw method, because sub has one
					ATM w1 = new WithdrawalATM(client);
					System.out.print("\n\tWithdraw amount: $");
					double w = input.nextDouble();
					w1.withdraw(w);
				}

				else if(select == 'd') {
					ATM d1 = new DepositATM(client);
					System.out.print("\n\tDeposit amount: $");
					double d = input.nextDouble();
					d1.depositCash(d);
				}

				else if(select != 'b' && select != 'w' && select != 'd' && select != 'q')
					System.out.println("Invalid option!");

		} while(select != 'q');
		System.out.println();
	}
}