package com.odoo.pos.helpers.sale;

import java.util.Calendar;
import java.util.List;

import com.odoo.pos.controllers.stock.StockLineItem;
import com.odoo.pos.controllers.sale.Sale;

/**
 * DAO for Sale process.
 * 
 * @author odoo Team
 *
 */
public interface SaleDao {

	/**
	 * Initiates a new Sale.
	 * @param startTime time that Sale initiated.
	 * @return Sale that initiated
	 */
	Sale initiateSale(String startTime);

	/**
	 * End Sale
	 * @param sale Sale to be ended.
	 * @param endTime time that Sale ended.
	 */
	void endSale(Sale sale, String endTime);

	/**
	 * Add StockLineItem to Sale.
	 * @param saleId ID of the Sale to add StockLineItem.
	 * @param stockLineItem StockLineItem to be added.
	 * @return ID of StockLineItem that just added.
	 */
	int addLineItem(int saleId, StockLineItem stockLineItem);
	
	/**
	 * Returns all sale in the records.
	 * @return all sale in the records.
	 */
	List<Sale> getAllSale();

	/**
	 * Returns the Sale with specific ID.
	 * @param id ID of specific Sale.
	 * @return the Sale with specific ID.
	 */
	Sale getSaleById(int id);

	/**
	 * Clear all records in SaleLedger.	
	 */
	void clearSaleLedger();

	/**
	 * Returns list of StockLineItem that belong to Sale with specific Sale ID.
	 * @param saleId ID of sale.
	 * @return list of StockLineItem that belong to Sale with specific Sale ID.
	 */
	List<StockLineItem> getLineItem(int saleId);

	/**
	 * Updates the data of specific StockLineItem.
	 * @param saleId ID of Sale that this StockLineItem belong to.
	 * @param stockLineItem to be updated.
	 */
	void updateLineItem(int saleId, StockLineItem stockLineItem);

	/**
	 * Returns list of Sale with scope of time. 
	 * @param start start bound of scope.
	 * @param end end bound of scope.
	 * @return list of Sale with scope of time.
	 */
	List<Sale> getAllSaleDuring(Calendar start, Calendar end);
	
	/**
	 * Cancel the Sale.
	 * @param sale Sale to be cancel.
	 * @param endTime time that cancelled.
	 */
	void cancelSale(Sale sale,String endTime);

	/**
	 * Removes StockLineItem.
	 * @param id of StockLineItem to be removed.
	 */
	void removeLineItem(int id);


}
