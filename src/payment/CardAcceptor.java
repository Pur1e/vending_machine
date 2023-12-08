package payment;

import java.util.Random;


public class CardAcceptor extends PaymentMethod {
	public CardAcceptor(int amount) {
		super(amount);
	}	private final String code = randomCode();
	
	private String randomCode() {
		return String.valueOf(new Random().nextInt(9000) + 1000);
	}
	
	public String getCode() {
		return code;
	}
}
