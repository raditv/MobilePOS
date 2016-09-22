package com.odoo.pos.controllers.stock;

import java.util.HashMap;
import java.util.Map;

import com.odoo.pos.controllers.DateTimeStrategy;

/**
 * Lot or bunch of stockProduct that import to stock.
 * 
 * @author odoo Team
 * 
 */
public class StockProductLot {
	
	private int id;
	private String dateAdded;
	private int quantity;
	private StockProduct stockProduct;
	private double unitCost;
	
	/**
	 * Static value for UNDEFINED ID.
	 */
	public static final int UNDEFINED_ID = -1;

	/**
	 * Constructs a new StockProductLot.
	 * @param id ID of the StockProductLot, This value should be assigned from database.
	 * @param dateAdded date and time of adding this lot.
	 * @param quantity quantity of stockProduct.
	 * @param stockProduct a stockProduct of this lot.
	 * @param unitCost cost (of buy) of each unit in this lot.
	 */
	public StockProductLot(int id, String dateAdded, int quantity, StockProduct stockProduct, double unitCost) {
		this.id = id;
		this.dateAdded = dateAdded;
		this.quantity = quantity;
		this.stockProduct = stockProduct;
		this.unitCost = unitCost;
	}
	
	/**
	 * Returns date added of this StockProductLot.
	 * @return date added of this StockProductLot.
	 */
	public String getDateAdded() {
		return dateAdded;
	}
	
	/**
	 * Returns quantity of this StockProductLot.
	 * @return quantity of this StockProductLot.
	 */
	public int getQuantity() {
		return quantity;
	}
	
	/**
	 * Returns cost of this StockProductLot.
	 * @return cost of this StockProductLot.
	 */
	public double unitCost() {
		return unitCost;
	}

	/**
	 * Returns id of this StockProductLot.
	 * @return id of this StockProductLot.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns stockProduct in this StockProductLot.
	 * @return stockProduct in this StockProductLot.
	 */
	public StockProduct getStockProduct() {
		return stockProduct;
	}

	/**
	 * Returns the description of this StockProductLot in Map format.
	 * @return the description of this StockProductLot in Map format.
	 */
	public Map<String, String> toMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", id + "");
		map.put("dateAdded", DateTimeStrategy.format(dateAdded));
		map.put("quantity", quantity + "");
		map.put("productName", stockProduct.getName());
		map.put("cost", unitCost + "");
		return map;
	}
}
