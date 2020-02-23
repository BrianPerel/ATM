import java.text.DecimalFormat;
import javax.swing.JOptionPane;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.InputMismatchException;


/**
 * Inherit ATM program for primary methods / operations
 * Performs withdraw operations
 */
class WithdrawFunds extends ATM {

	private final Account account;
	static DecimalFormat df = new DecimalFormat("$###,###.00");

	public WithdrawFunds(Account account) {
		super(account);
		this.account = account;
	}

	@Override
	public void withdraw(PrintWriter file) throws IOException {

		String money0;

		do {
			money0 = JOptionPane.showInputDialog(null, "\nWithdraw amount: $", "Withdraw", JOptionPane.QUESTION_MESSAGE);

			if (money0.matches("[0-9.]+") == false)
				JOptionPane.showMessageDialog(null, "Invalid amount!", "Warning", JOptionPane.WARNING_MESSAGE);

		} while (money0.matches("[0-9.]+") == false);

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