package com.odoo.pos.helpers.stock;

import java.util.List;

import com.odoo.pos.controllers.stock.StockProduct;
import com.odoo.pos.controllers.stock.StockProductLot;

/**
 * DAO for StockServices.
 * 
 * @author odoo Team
 *
 */
public interface StockDatabaseOperation {

	/**
	 * Adds stockProduct to stock.
	 * @param stockProduct the stockProduct to be added.
	 * @return id of this stockProduct that assigned from database.
	 */
	int addProduct(StockProduct stockProduct);
	
	/**
	 * Adds StockProductLot to stock.
	 * @param stockProductLot the StockProductLot to be added.
	 * @return id of this StockProductLot that assigned from database.
	 */
	int addProductLot(StockProductLot stockProductLot);

	/**
	 * Edits stockProduct.
	 * @param stockProduct the stockProduct to be edited.
	 * @return true if stockProduct edits success ; otherwise false.
	 */
	boolean editProduct(StockProduct stockProduct);

	/**
	 * Returns product from stock finds by id.
	 * @param id id of product.
	 * @return product
	 */
	StockProduct getProductById(int id);
	
	/**
	 * Returns product from stock finds by barcode.
	 * @param barcode barcode of product.
	 * @return product
	 */
	StockProduct getProductByBarcode(String barcode);
	
	/**
	 * Returns list of all products in stock.
	 * @return list of all products in stock.
	 */
	List<StockProduct> getAllProduct();
	
	/**
	 * Returns list of product in stock finds by name.
	 * @param name name of product.
	 * @return list of product in stock finds by name.
	 */
	List<StockProduct> getProductByName(String name);
	
	/**
	 * Search product from string in stock.
	 * @param search string for searching.
	 * @return list of product.
	 */
	List<StockProduct> searchProduct(String search);
	
	/**
	 * Returns list of all products in stock.
	 * @return list of all products in stock.
	 */
	List<StockProductLot> getAllProductLot();
	
	/**
	 * Returns list of product in stock finds by id.
	 * @param id id of product.
	 * @return list of product in stock finds by id.
	 */
	List<StockProductLot> getProductLotById(int id);
	
	/**
	 * Returns list of StockProductLot in stock finds by id.
	 * @param id id of StockProductLot.
	 * @return list of StockProductLot in stock finds by id.
	 */
	List<StockProductLot> getProductLotByProductId(int id);
	
	/**
	 * Returns Stock in stock finds by id.
	 * @param id id of Stock.
	 * @return Stock in stock finds by id.
	 */
	int getStockSumById(int id);
	
	/**
	 * Updates quantity of product.
	 * @param productId id of product.
	 * @param quantity quantity of product.
	 */
	void updateStockSum(int productId, double quantity);
	
	/**
	 * Clears StockProductCatalog.
	 */
	void clearProductCatalog();
	
	/**
	 * Clear Stock.
	 */
	void clearStock();
	
	/**
	 * Hidden stockProduct from stock.
	 * @param stockProduct The stockProduct to be hidden.
	 */
	void suspendProduct(StockProduct stockProduct);

}
