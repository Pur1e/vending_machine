package payment;

public abstract class PaymentMethod {
	protected int amount;
	
	public PaymentMethod(int amount) {
		this.amount = amount;
	}
	
	public int getAmount() {
		return amount;
	}
	
	public void setAmount(int amount) {
		this.amount = amount;
	}
}
