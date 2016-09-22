package com.odoo.pos.controllers.stock;

import java.util.List;

import com.odoo.pos.helpers.stock.StockDatabaseOperation;

/**
 * Book that keeps list of StockProduct.
 * 
 * @author odoo Team
 *
 */
public class StockProductCatalog {

	private StockDatabaseOperation stockDatabaseOperation;

	/**
	 * Constructs Data Access Object of stock in StockProductCatalog.
	 * @param stockDatabaseOperation DAO of stock.
	 */
	public StockProductCatalog(StockDatabaseOperation stockDatabaseOperation) {
		this.stockDatabaseOperation = stockDatabaseOperation;
	}

	/**
	 * Constructs product and adds product to stock.
	 * @param name name of product.
	 * @param barcode barcode of product.
	 * @param salePrice price of product.
	 * @return true if product adds in stock success ; otherwise false.
	 */
	public boolean addProduct(String name, String barcode, double salePrice) {
		StockProduct stockProduct = new StockProduct(name, barcode, salePrice);
		int id = stockDatabaseOperation.addProduct(stockProduct);
		return id != -1;
	}

	/**
	 * Edits stockProduct.
	 * @param stockProduct the stockProduct to be edited.
	 * @return true if stockProduct edits success ; otherwise false.
	 */
	public boolean editProduct(StockProduct stockProduct) {
		boolean respond = stockDatabaseOperation.editProduct(stockProduct);
		return respond;
	}
		
	/**
	 * Returns product from stock finds by barcode.
	 * @param barcode barcode of product.
	 * @return product
	 */
	public StockProduct getProductByBarcode(String barcode) {
		return stockDatabaseOperation.getProductByBarcode(barcode);
	}
	
	/**
	 * Returns product from stock finds by id.
	 * @param id id of product.
	 * @return product
	 */
	public StockProduct getProductById(int id) {
		return stockDatabaseOperation.getProductById(id);
	}
	
	/**
	 * Returns list of all products in stock.
	 * @return list of all products in stock.
	 */
	public List<StockProduct> getAllProduct() {
		return stockDatabaseOperation.getAllProduct();
	}

	/**
	 * Returns list of product in stock finds by name.
	 * @param name name of product.
	 * @return list of product in stock finds by name.
	 */
	public List<StockProduct> getProductByName(String name) {
		return stockDatabaseOperation.getProductByName(name);
	}

	/**
	 * Search product from string in stock.
	 * @param search string for searching.
	 * @return list of product.
	 */
	public List<StockProduct> searchProduct(String search) {
		return stockDatabaseOperation.searchProduct(search);
	}

	/**
	 * Clears StockProductCatalog.
	 */
	public void clearProductCatalog() {
		stockDatabaseOperation.clearProductCatalog();
	}
	
	/**
	 * Hidden stockProduct from stock.
	 * @param stockProduct The stockProduct to be hidden.
	 */
	public void suspendProduct(StockProduct stockProduct) {
		stockDatabaseOperation.suspendProduct(stockProduct);
	}

	
}
