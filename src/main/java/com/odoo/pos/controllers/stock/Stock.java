package com.odoo.pos.controllers.stock;

import java.util.List;

import com.odoo.pos.helpers.stock.StockDatabaseOperation;

/**
 * Import log of StockProductLot come in to store.
 * 
 * @author odoo Team
 *
 */
public class Stock {

	private StockDatabaseOperation stockDatabaseOperation;

	/**
	 * Constructs Data Access Object of stock in StockProductCatalog.
	 * @param stockDatabaseOperation DAO of stock.
	 */
	public Stock(StockDatabaseOperation stockDatabaseOperation) {
		this.stockDatabaseOperation = stockDatabaseOperation;
	}
	
	/**
	 * Constructs StockProductLot and adds StockProductLot to stock.
	 * @param dateAdded date added of StockProductLot.
	 * @param quantity quantity of StockProductLot.
	 * @param stockProduct stockProduct of StockProductLot.
	 * @param cost cost of StockProductLot.
	 * @return
	 */
	public boolean addProductLot(String dateAdded, int quantity, StockProduct stockProduct, double cost) {
		StockProductLot stockProductLot = new StockProductLot(StockProductLot. UNDEFINED_ID, dateAdded, quantity, stockProduct, cost);
		int id = stockDatabaseOperation.addProductLot(stockProductLot);
		return id != -1;
	}

	/**
	 * Returns list of StockProductLot in stock finds by id.
	 * @param id id of StockProductLot.
	 * @return list of StockProductLot in stock finds by id.
	 */
	public List<StockProductLot> getProductLotByProductId(int id) {
		return stockDatabaseOperation.getProductLotByProductId(id);
	}

	/**
	 * Returns all ProductLots in stock.
	 * @return all ProductLots in stock.
	 */
	public List<StockProductLot> getAllProductLot() {
		return stockDatabaseOperation.getAllProductLot();
	}

	/**
	 * Returns Stock in stock finds by id.
	 * @param id id of Stock.
	 * @return Stock in stock finds by id.
	 */
	public int getStockSumById(int id) {
		return stockDatabaseOperation.getStockSumById(id);
	}

	/**
	 * Updates quantity of product.
	 * @param productId id of product.
	 * @param quantity quantity of product.
	 */
	public void updateStockSum(int productId, int quantity) {
		stockDatabaseOperation.updateStockSum(productId,quantity);
		
	}

	/**
	 * Clear Stock.
	 */
	public void clearStock() {
		stockDatabaseOperation.clearStock();
		
	}
	

}
