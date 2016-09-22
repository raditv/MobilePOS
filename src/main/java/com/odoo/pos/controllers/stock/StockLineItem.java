package com.odoo.pos.controllers.stock;

import java.util.HashMap;
import java.util.Map;

/**
 * StockLineItem of Sale.
 * 
 * @author odoo Team
 * 
 */
public class StockLineItem {

	private final StockProduct stockProduct;
	private int quantity;
	private int id;
	private double unitPriceAtSale;

	/**
	 * Static value for UNDEFINED ID.
	 */
	public static final int UNDEFINED = -1;

	/**
	 * Constructs a new StockLineItem.
	 * @param stockProduct stockProduct of this StockLineItem.
	 * @param quantity stockProduct quantity of this StockLineItem.
	 */
	public StockLineItem(StockProduct stockProduct, int quantity) {
		this(UNDEFINED, stockProduct, quantity, stockProduct.getUnitPrice());
	}

	/**
	 * Constructs a new StockLineItem.
	 * @param id ID of this StockLineItem, This value should be assigned from database.
	 * @param stockProduct stockProduct of this StockLineItem.
	 * @param quantity stockProduct quantity of this StockLineItem.
	 * @param unitPriceAtSale unit price at sale time. default is price from StockProductCatalog.
	 */
	public StockLineItem(int id, StockProduct stockProduct, int quantity,
						 double unitPriceAtSale) {
		this.id = id;
		this.stockProduct = stockProduct;
		this.quantity = quantity;
		this.unitPriceAtSale = unitPriceAtSale;
	}

	/**
	 * Returns stockProduct in this StockLineItem.
	 * @return stockProduct in this StockLineItem.
	 */
	public StockProduct getStockProduct() {
		return stockProduct;
	}

	/**
	 * Return quantity of stockProduct in this StockLineItem.
	 * @return quantity of stockProduct in this StockLineItem.
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * Sets quantity of stockProduct in this StockLineItem.
	 * @param quantity quantity of stockProduct in this StockLineItem.
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	/**
	 * Adds quantity of stockProduct in this StockLineItem.
	 * @param amount amount for add in quantity.
	 */
	public void addQuantity(int amount) {
		this.quantity += amount;
	}

	/**
	 * Returns total price of this StockLineItem.
	 * @return total price of this StockLineItem.
	 */
	public double getTotalPriceAtSale() {
		return unitPriceAtSale * quantity;
	}

	/**
	 * Returns the description of this StockLineItem in Map format.
	 * @return the description of this StockLineItem in Map format.
	 */
	public Map<String, String> toMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", stockProduct.getName());
		map.put("quantity", quantity + "");
		map.put("price", getTotalPriceAtSale() + "");
		return map;

	}

	/**
	 * Returns id of this StockLineItem.
	 * @return id of this StockLineItem.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets id of this StockLineItem.
	 * @param id of this StockLineItem.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Sets price stockProduct of this StockLineItem.
	 * @param unitPriceAtSale price stockProduct of this StockLineItem.
	 */
	public void setUnitPriceAtSale(double unitPriceAtSale) {
		this.unitPriceAtSale = unitPriceAtSale;
	}

	/**
	 * Returns price stockProduct of this StockLineItem.
	 * @return unitPriceAtSale price stockProduct of this StockLineItem.
	 */
	public Double getPriceAtSale() {
		return unitPriceAtSale;
	}

	/**
	 * Determines whether two objects are equal or not.
	 * @return true if Object is a StockLineItem with same ID ; otherwise false.
	 */
	@Override
	public boolean equals(Object object) {
		if (object == null)
			return false;
		if (!(object instanceof StockLineItem))
			return false;
		StockLineItem stockLineItem = (StockLineItem) object;
		return stockLineItem.getId() == this.getId();
	}
}
