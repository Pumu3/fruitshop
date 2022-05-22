package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import CSV.ReadCSV;
import models.Apple;
import models.Fruit;
import models.Orange;
import models.Pear;

public class Main {
	public static final String PRICES_FILE = "resources/prices.csv";
	public static final String QUANTITY_FILE = "resources/quantity.csv";

	public static void main(String[] args) {
		ReadCSV read = new ReadCSV();
		Map<String, Double> prices = new HashMap<>();
		Map<String, Integer> quantities = new HashMap<>();
		
		// Read files
		List<String[]> lines = read.readCSVFile(PRICES_FILE);
		lines.stream().forEach(l -> {
			prices.put(l[0], Double.valueOf(l[1]));
		});
		
		lines = read.readCSVFile(QUANTITY_FILE);
		lines.stream().forEach(l -> {
			quantities.put(l[0], Integer.valueOf(l[1]));
		});
		
		List<Fruit> cart = new ArrayList<>();
		prices.keySet().stream().forEach(fruitName -> {
			try {
				Fruit fruit = (Fruit) Class.forName("models."+fruitName).newInstance();
				fruit.setPrice(prices.get(fruitName));
				fruit.setQuantity(quantities.get(fruitName));
				
				if(fruit instanceof Apple) {
					fruit.setQuantity(fruit.getQuantity()-(fruit.getQuantity()/3));
				}
				
				cart.add(fruit);
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				System.out.println("Problem adding fruit to cart. This is the error: "+e.getMessage());
			}
		});
		
//		System.out.println("Cart before discounts: "+cart);
		
		// Init total cart price
		Map<String, Double> totalPerFruit = new HashMap<>();
		AtomicReference<Double> totalPrice = new AtomicReference<Double>(0.0); 
		Double discountApplied = 0.0;
		cart.stream().forEach(fruit -> {
			totalPerFruit.put(fruit.getClass().getName(), fruit.getQuantity()*fruit.getPrice());
			totalPrice.set(totalPrice.get()+totalPerFruit.get(fruit.getClass().getName()));
		});
		
//		System.out.println("total prices before discounts: "+totalPrice);
		discountApplied += totalPrice.get();
//		System.out.println("total prices before discounts per fruit: "+totalPerFruit);

		// Discounts
		totalPrice.set(totalPrice.get()-(Math.ceil(totalPerFruit.get(Pear.class.getName())/4)));
		discountApplied -= totalPrice.get();
		
		Optional<Fruit> orangeInCart = cart.parallelStream().filter(fruit -> fruit instanceof Orange).findFirst();
		Optional<Fruit> pearInCart = cart.parallelStream().filter(fruit -> fruit instanceof Pear).findFirst();
		if(orangeInCart.isPresent() && pearInCart.isPresent()) {
			Orange orange = (Orange) orangeInCart.get();
			Pear pear = (Pear) pearInCart.get();
			orange.setQuantity(orange.getQuantity()+(pear.getQuantity()/2));
		}
		
//		System.out.println("Cart after discounts: "+cart);
//		System.out.println("total prices after discounts: "+totalPrice);

		
		System.out.println("Total price: "+totalPrice);
		System.out.println("Products purchased: "+cart);
		System.out.println("Offers applied: "+discountApplied);
	}

}
