import java.io.PrintWriter;
import java.io.IOException;


/**
 * Abstract class, since it's methods will be overwritten by sub classes
 * We need the account field so that we can call methods of account class
 * Has method to display the balance, holds abstract methods for basic app operations
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