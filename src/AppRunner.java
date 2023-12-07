import enums.ActionLetter;
import model.*;
import util.UniversalArray;
import util.UniversalArrayImpl;

import java.util.Scanner;

public class AppRunner {
	
	private static boolean isExit = false;
	private final UniversalArray<Product> products = new UniversalArrayImpl<>();
	private final CoinAcceptor coinAcceptor;
	private int minPriceOfProducts;
	
	private AppRunner() {
		products.addAll(new Product[] {
				new Water(ActionLetter.B, 20),
				new CocaCola(ActionLetter.C, 50),
				new Soda(ActionLetter.D, 30),
				new Snickers(ActionLetter.E, 80),
				new Mars(ActionLetter.F, 80),
				new Pistachios(ActionLetter.G, 130)
		});
		coinAcceptor = new CoinAcceptor(0);
	}
	
	public static void run() {
		AppRunner app = new AppRunner();
		while (! isExit) {
			app.startSimulation();
		}
	}
	
	private void startSimulation() {
		findMinPrice();
		
		print("В автомате доступны:");
		showProducts(products);
		
		marking();
		print("В автомате монет на сумму: " + coinAcceptor.getAmount() + " coм");
		
		UniversalArray<Product> allowProducts = new UniversalArrayImpl<>();
		allowProducts.addAll(getAllowedProducts().toArray());
		
		choosePaymentType(allowProducts);
		
	}
	
	private void choosePaymentType(UniversalArray<Product> products) {
		
		if (coinAcceptor.getAmount() < minPriceOfProducts) {
			System.out.println("\u001B[33m" + "Ничего не купить, пополните баланс" + "\u001B[0m");
		}
		
		print("Выберите способ оплаты: ");
		print("a - Монетами");
		print("b - Банкнотами");
		String action = fromConsole().substring(0, 1);
		switch (action) {
			case "a":
				payWithCoins(products);
				break;
			case "b":
				break;
			default:
				System.err.println("Недопустимая буква. Попрбуйте еще раз.");
				choosePaymentType(products);
		}
	}
	
	private UniversalArray<Product> getAllowedProducts() {
		UniversalArray<Product> allowProducts = new UniversalArrayImpl<>();
		for (int i = 0; i < products.size(); i++) {
			if (coinAcceptor.getAmount() >= products.get(i).getPrice()) {
				allowProducts.add(products.get(i));
			}
		}
		return allowProducts;
	}
	
	private void payWithCoins(UniversalArray<Product> products) {
		marking();
		print("Выберите действие");
		print(" a - Пополнить баланс (10сом)");
		showActions(products);
		print(" h - Выйти");
		String action = fromConsole().substring(0, 1);
		
		if ("a".equalsIgnoreCase(action)) {
			coinAcceptor.setAmount(coinAcceptor.getAmount() + 10);
			print("\u001B[32m" + "Вы пополнили баланс на 10 coм" + "\u001B[0m");
			return;
		}
		
		try {
			for (int i = 0; i < products.size(); i++) {
				if (products.get(i).getActionLetter().equals(ActionLetter.valueOf(action.toUpperCase()))) {
					coinAcceptor.setAmount(coinAcceptor.getAmount() - products.get(i).getPrice());
					print("Вы купили " + products.get(i).getName());
					break;
				} else if ("h".equalsIgnoreCase(action)) {
					isExit = true;
					break;
				}
			}
		} catch (IllegalArgumentException e) {
			print("Недопустимая буква. Попрбуйте еще раз.");
			payWithCoins(products);
		}
	}
	
	private void showActions(UniversalArray<Product> products) {
		for (int i = 0; i < products.size(); i++) {
			print(String.format(" %s - %s", products.get(i).getActionLetter().getValue(), products.get(i).getName()));
		}
	}
	
	private String fromConsole() {
		return new Scanner(System.in).nextLine();
	}
	
	private void showProducts(UniversalArray<Product> products) {
		for (int i = 0; i < products.size(); i++) {
			print(products.get(i).toString());
		}
	}
	
	private void findMinPrice() {
		int maxInt = Integer.MAX_VALUE;
		for (int i = 0; i < products.size(); i++) {
			maxInt = Integer.min(products.get(i).getPrice(), maxInt);
		}
		minPriceOfProducts = maxInt;
	}
	
	private void print(String msg) {
		System.out.println(msg);
	}
	
	private void marking() {
		System.out.println("================================");
	}
}
