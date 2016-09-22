package com.odoo.pos.controllers.sale;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.odoo.pos.controllers.stock.StockLineItem;
import com.odoo.pos.controllers.stock.StockProduct;

/**
 * Sale represents sale operation.
 * 
 * @author odoo Team
 *
 */
public class Sale {
	
	private final int id;
	private String startTime;
	private String endTime;
	private String status;
	private List<StockLineItem> items;
	

	public Sale(int id, String startTime) {
		this(id, startTime, startTime, "", new ArrayList<StockLineItem>());
	}
	
	/**
	 * Constructs a new Sale.
	 * @param id ID of this Sale.
	 * @param startTime start time of this Sale.
	 * @param endTime end time of this Sale.
	 * @param status status of this Sale.
	 * @param items list of StockLineItem in this Sale.
	 */
	public Sale(int id, String startTime, String endTime, String status, List<StockLineItem> items) {
		this.id = id;
		this.startTime = startTime;
		this.status = status;
		this.endTime = endTime;
		this.items = items;
	}
	
	/**
	 * Returns list of StockLineItem in this Sale.
	 * @return list of StockLineItem in this Sale.
	 */
	public List<StockLineItem> getAllLineItem(){
		return items;
	}
	
	/**
	 * Add StockProduct to Sale.
	 * @param stockProduct stockProduct to be added.
	 * @param quantity quantity of stockProduct that added.
	 * @return StockLineItem of Sale that just added.
	 */
	public StockLineItem addLineItem(StockProduct stockProduct, int quantity) {
		
		for (StockLineItem stockLineItem : items) {
			if (stockLineItem.getStockProduct().getId() == stockProduct.getId()) {
				stockLineItem.addQuantity(quantity);
				return stockLineItem;
			}
		}
		
		StockLineItem stockLineItem = new StockLineItem(stockProduct, quantity);
		items.add(stockLineItem);
		return stockLineItem;
	}
	
	public int size() {
		return items.size();
	}
	
	/**
	 * Returns a StockLineItem with specific index.
	 * @param index of specific StockLineItem.
	 * @return a StockLineItem with specific index.
	 */
	public StockLineItem getLineItemAt(int index) {
		if (index >= 0 && index < items.size())
			return items.get(index);
		return null;
	}

	/**
	 * Returns the total price of this Sale.
	 * @return the total price of this Sale.
	 */
	public double getTotal() {
		double amount = 0;
		for(StockLineItem stockLineItem : items) {
			amount += stockLineItem.getTotalPriceAtSale();
		}
		return amount;
	}

	public int getId() {
		return id;
	}

	public String getStartTime() {
		return startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public String getStatus() {
		return status;
	}
	
	/**
	 * Returns the total quantity of this Sale.
	 * @return the total quantity of this Sale.
	 */
	public int getOrders() {
		int orderCount = 0;
		for (StockLineItem stockLineItem : items) {
			orderCount += stockLineItem.getQuantity();
		}
		return orderCount;
	}

	/**
	 * Returns the description of this Sale in Map format. 
	 * @return the description of this Sale in Map format.
	 */
	public Map<String, String> toMap() {	
		Map<String, String> map = new HashMap<String, String>();
		map.put("id",id + "");
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		map.put("status", getStatus());
		map.put("total", getTotal() + "");
		map.put("orders", getOrders() + "");
		
		return map;
	}

	/**
	 * Removes StockLineItem from Sale.
	 * @param stockLineItem stockLineItem to be removed.
	 */
	public void removeItem(StockLineItem stockLineItem) {
		items.remove(stockLineItem);
	}

}