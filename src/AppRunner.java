import enums.ActionLetter;
import model.*;
import payment.CardAcceptor;
import payment.CashAcceptor;
import util.UniversalArray;
import util.UniversalArrayImpl;

import java.util.Scanner;

public class AppRunner {
	
	private static boolean isExit = false;
	private final UniversalArray<Product> products = new UniversalArrayImpl<>();
	private final CashAcceptor cashAcceptor;
	private final CardAcceptor cardAcceptor;
	private int minPriceOfProducts;
	private int balance;
	
	
	private AppRunner() {
		products.addAll(new Product[] {
				new Water(ActionLetter.B, 20),
				new CocaCola(ActionLetter.C, 50),
				new Soda(ActionLetter.D, 30),
				new Snickers(ActionLetter.E, 80),
				new Mars(ActionLetter.F, 80),
				new Pistachios(ActionLetter.G, 130)
		});
		cashAcceptor = new CashAcceptor(0);
		cardAcceptor = new CardAcceptor(0);
		
	}
	
	public static void run() {
		AppRunner app = new AppRunner();
		while (! isExit) {
			app.startSimulation();
		}
	}
	
	private static boolean isThereAllDigits(String s) {
		for (int i = 0; i < s.length(); i++) {
			if (Character.isDigit(s.charAt(i))) {
				return true;
			}
		}
		return false;
	}
	
	private void startSimulation() {
		balance = cashAcceptor.getAmount() + cardAcceptor.getAmount();
		System.out.println("ballance" + balance);
		
		findMinPrice();
		
		print("В автомате доступны:");
		showProducts(products);
		
		marking();
		print("В автомате монет на сумму: " + balance + " coм");
		
		UniversalArray<Product> allowProducts = new UniversalArrayImpl<>();
		allowProducts.addAll(getAllowedProducts().toArray());
		
		choosePaymentType(allowProducts);
	}
	
	private void choosePaymentType(UniversalArray<Product> products) {
		
		if (balance < minPriceOfProducts) {
			System.out.println("\u001B[33m" + "Ничего не купить, пополните баланс" + "\u001B[0m");
		}
		
		print("Выберите способ пополнения: ");
		print(" 1 - Монетами");
		print(" 2 - Банкнотами");
		print(" 3 - Картой");
		print(" h - Выйти");
		
		if (balance > minPriceOfProducts) {
			marking();
			print("Вы можете купить:");
			showActions(products);
			print("\u001B[33m Вы можете купить товар или" +
					" пополнить баланс выбрав способ пополнения \u001B[0m");
		}
		
		
		try {
			String action = fromConsole().substring(0, 1);
			
			switch (action) {
				case "1":
					payWithCoins(products);
					break;
				case "2":
					payWithBanknotes(products);
					break;
				case "3":
					payWithCard(products);
					break;
				default:
					buyingAction(action, products);
			}
		} catch (StringIndexOutOfBoundsException e) {
			System.err.println("Пустая строка!");
			choosePaymentType(products);
		}
	}
	
	private UniversalArray<Product> getAllowedProducts() {
		UniversalArray<Product> allowProducts = new UniversalArrayImpl<>();
		for (int i = 0; i < products.size(); i++) {
			if (cashAcceptor.getAmount() >= products.get(i).getPrice()) {
				allowProducts.add(products.get(i));
			}
		}
		return allowProducts;
	}
	
	private void payWithCoins(UniversalArray<Product> products) {
		marking();
		print("Выберите действие");
		print(" 1 - Пополнить баланс (3сом)");
		print(" 2 - Пополнить баланс (5сом)");
		print(" 3 - Пополнить баланс (10сом)");
		print(" h - Выйти");
		String action = fromConsole().substring(0, 1);
		
		switch (action) {
			case "1":
				cashAcceptor.setAmount(cashAcceptor.getAmount() + 3);
				formattingOfReplenishment(3);
				break;
			case "2":
				cashAcceptor.setAmount(cashAcceptor.getAmount() + 5);
				formattingOfReplenishment(5);
				break;
			case "3":
				cashAcceptor.setAmount(cashAcceptor.getAmount() + 10);
				formattingOfReplenishment(10);
				break;
			default:
				buyingAction(action, products);
				break;
		}
	}
	
	private void payWithBanknotes(UniversalArray<Product> products) {
		marking();
		print("Выберите действие");
		print(" 1 - Пополнить баланс (20сом)");
		print(" 2 - Пополнить баланс (50сом)");
		print(" 3 - Пополнить баланс (100сом)");
		print(" 4 - Пополнить баланс (200сом)");
		print(" h - Выйти");
		String action = fromConsole().substring(0, 1);
		
		switch (action) {
			case "1":
				cashAcceptor.setAmount(cashAcceptor.getAmount() + 20);
				formattingOfReplenishment(20);
				break;
			case "2":
				cashAcceptor.setAmount(cashAcceptor.getAmount() + 50);
				formattingOfReplenishment(50);
				break;
			case "3":
				cashAcceptor.setAmount(cashAcceptor.getAmount() + 100);
				formattingOfReplenishment(100);
				break;
			case "4":
				cashAcceptor.setAmount(cashAcceptor.getAmount() + 200);
				formattingOfReplenishment(200);
				break;
			default:
				buyingAction(action, products);
				break;
		}
	}
	
	private void payWithCard(UniversalArray<Product> products) {
		marking();
		print("Выберите действие");
		print(" a - Пополнить баланс");
		print(" h - Выйти");
		
		String action = fromConsole().substring(0, 1);
		
		if (action.equals("a")) {
			System.out.print("Введите номер карты: ");
			while (true) {
				try {
					
					String cardNum = fromConsole();
					if (cardNum.length() != 16 || ! isThereAllDigits(cardNum)) {
						throw new IllegalArgumentException();
					}
					
					break;
				} catch (IllegalArgumentException e) {
					System.err.println("Неправильный ввод. попробуйте ещё!");
				}
			}
			
			print("Код подверждения - " + cardAcceptor.getCode());
			System.out.print("Введите четырехзначный одноразовый пароль: ");
			while (true) {
				try {
					String pass = fromConsole();
					
					if (! pass.equalsIgnoreCase(cardAcceptor.getCode())) {
						throw new IllegalArgumentException();
					}
					
					break;
				} catch (IllegalArgumentException e) {
					System.err.println("Неправильный пароль. попробуйте ещё!");
				}
			}
			
			System.out.print("Введите сумму для пополнения(Однаразовый лимит полполнения 1000с): ");
			int intSum;
			while (true) {
				try {
					String sum = fromConsole();
					
					if (sum.length() > 5 || ! isThereAllDigits(sum)) {
						throw new IllegalArgumentException();
					}
					
					intSum = Integer.parseInt(sum);
					if (intSum > 1000) {
						throw new IllegalArgumentException();
					}
					
					break;
				} catch (IllegalArgumentException e) {
					System.err.println("Неправильный ввод. попробуйте ещё!");
				}
			}
			
			cardAcceptor.setAmount(cardAcceptor.getAmount() + intSum);
			formattingOfReplenishment(intSum);
		} else {
			buyingAction(action, products);
		}
	}
	
	private void buyingAction(String action, UniversalArray<Product> products) {
		try {
			for (int i = 0; i < products.size(); i++) {
				if (products.get(i).getActionLetter().equals(ActionLetter.valueOf(action.toUpperCase()))) {
					
					if (cashAcceptor.getAmount() > cardAcceptor.getAmount()) {
						cashAcceptor.setAmount(cashAcceptor.getAmount() - products.get(i).getPrice());
					} else {
						cardAcceptor.setAmount(cardAcceptor.getAmount() - products.get(i).getPrice());
					}
					
					print("\u001B[35m Вы купили " + products.get(i).getName() + "\u001B[0m");
					System.out.println();
					startSimulation();
					break;
				}
			}
			throw new IllegalArgumentException();
		} catch (IllegalArgumentException e) {
			if ("h".equalsIgnoreCase(action)) {
				isExit = true;
			} else {
				System.err.println("Недопустимая буква. Попрбуйте еще раз.");
				choosePaymentType(products);
			}
		}
	}
	
	private void showActions(UniversalArray<Product> products) {
		for (int i = 0; i < products.size(); i++) {
			print(String.format(" %s - Купить %s", products.get(i).getActionLetter().getValue(), products.get(i).getName()));
		}
	}
	
	private String fromConsole() {
		return new Scanner(System.in).nextLine().trim();
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
	
	private void formattingOfReplenishment(int price) {
		print(String.format("\u001B[32mВы пополнили баланс на %d coм\u001B[0m", price));
	}
}
