import java.text.DecimalFormat;
import javax.swing.JOptionPane;
import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.io.IOException;


/**
 * Inherit ATM program for primary methods / operations
 * Performs transfer operations
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

			if (money0.matches("[0-9.]+") == false)
				JOptionPane.showMessageDialog(null, "Invalid amount!", "Warning", JOptionPane.WARNING_MESSAGE);

		} while (money0.matches("[0-9.]+") == false);

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
