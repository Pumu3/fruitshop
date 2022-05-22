package models;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

public class Fruit {
	protected Double price;
	protected Integer quantity;
	
	public Fruit(Double price, Integer quantity) {
		this.price = price;
		this.quantity = quantity;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	public String toString() {
		return this.getClass().getSimpleName()+" -> "+StringUtils.join(Arrays.asList("Price: "+this.price, "Quantity: "+this.quantity), ", ");
	}
}
