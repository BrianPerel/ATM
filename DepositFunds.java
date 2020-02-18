import java.text.DecimalFormat;
import java.io.PrintWriter;
import java.io.IOException;
import javax.swing.JOptionPane;
import java.util.InputMismatchException;


/**
 * Inherit ATM program for primary methods / operations
 * Performs deposit operation
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

			if (money0.matches("[0-9.]+") == false)
				JOptionPane.showMessageDialog(null, "Invalid amount!", "Warning", JOptionPane.WARNING_MESSAGE);

		} while (money0.matches("[0-9.]+") == false);

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
