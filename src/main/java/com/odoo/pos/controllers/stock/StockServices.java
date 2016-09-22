package com.odoo.pos.controllers.stock;

import com.odoo.pos.helpers.NoDaoSetException;
import com.odoo.pos.helpers.stock.StockDatabaseOperation;

/**
 * This class is service locater for StockProduct Catalog and Stock.
 * 
 * @author odoo Team
 * 
 */
public class StockServices {

	private Stock stock;
	private StockProductCatalog stockProductCatalog;
	private static StockServices instance = null;
	private static StockDatabaseOperation stockDatabaseOperation = null;

	/**
	 * Constructs Data Access Object of stock.
	 * @throws NoDaoSetException if DAO is not exist.
	 */
	private StockServices() throws NoDaoSetException {
		if (!isDaoSet()) {
			throw new NoDaoSetException();
		}
		stock = new Stock(stockDatabaseOperation);
		stockProductCatalog = new StockProductCatalog(stockDatabaseOperation);
	}

	/**
	 * Determines whether the DAO already set or not.
	 * @return true if the DAO already set; otherwise false.
	 */
	public static boolean isDaoSet() {
		return stockDatabaseOperation != null;
	}

	/**
	 * Sets the database connector.
	 * @param dao Data Access Object of stock.
	 */
	public static void setStockDatabaseOperation(StockDatabaseOperation dao) {
		stockDatabaseOperation = dao;
	}

	/**
	 * Returns product catalog using in this stock.
	 * @return product catalog using in this stock.
	 */
	public StockProductCatalog getStockProductCatalog() {
		return stockProductCatalog;
	}

	/**
	 * Returns stock using in this stock.
	 * @return stock using in this stock.
	 */
	public Stock getStock() {
		return stock;
	}

	/**
	 * Returns the instance of this singleton class.
	 * @return instance of this class.
	 * @throws NoDaoSetException if DAO was not set.
	 */
	public static StockServices getInstance() throws NoDaoSetException {
		if (instance == null)
			instance = new StockServices();
		return instance;
	}

}
