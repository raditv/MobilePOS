package com.odoo.pos.controllers.sale;

import com.odoo.pos.controllers.DateTimeStrategy;
import com.odoo.pos.controllers.stock.StockLineItem;
import com.odoo.pos.controllers.stock.StockServices;
import com.odoo.pos.controllers.stock.StockProduct;
import com.odoo.pos.controllers.stock.Stock;
import com.odoo.pos.helpers.NoDaoSetException;
import com.odoo.pos.helpers.sale.SaleDao;

/**
 * Handles all Sale processes.
 * 
 * @author odoo Team
 *
 */
public class SaleServices {
	private static SaleServices instance = null;
	private static SaleDao saleDao = null;
	private static Stock stock = null;
	
	private Sale currentSale;
	
	private SaleServices() throws NoDaoSetException {
		if (!isDaoSet()) {
			throw new NoDaoSetException();
		}
		stock = StockServices.getInstance().getStock();
		
	}
	
	/**
	 * Determines whether the DAO already set or not.
	 * @return true if the DAO already set; otherwise false.
	 */
	public static boolean isDaoSet() {
		return saleDao != null;
	}
	
	public static SaleServices getInstance() throws NoDaoSetException {
		if (instance == null) instance = new SaleServices();
		return instance;
	}

	/**
	 * Injects its sale DAO
	 * @param dao DAO of sale
	 */
	public static void setSaleDao(SaleDao dao) {
		saleDao = dao;	
	}
	
	/**
	 * Initiates a new Sale.
	 * @param startTime time that sale created.
	 * @return Sale that created.
	 */
	public Sale initiateSale(String startTime) {
		if (currentSale != null) {
			return currentSale;
		}
		currentSale = saleDao.initiateSale(startTime);
		return currentSale;
	}
	
	/**
	 * Add StockProduct to Sale.
	 * @param stockProduct stockProduct to be added.
	 * @param quantity quantity of stockProduct that added.
	 * @return StockLineItem of Sale that just added.
	 */
	public StockLineItem addItem(StockProduct stockProduct, int quantity) {
		if (currentSale == null)
			initiateSale(DateTimeStrategy.getCurrentTime());
		
		StockLineItem stockLineItem = currentSale.addLineItem(stockProduct, quantity);
		
		if (stockLineItem.getId() == StockLineItem.UNDEFINED) {
			int lineId = saleDao.addLineItem(currentSale.getId(), stockLineItem);
			stockLineItem.setId(lineId);
		} else {
			saleDao.updateLineItem(currentSale.getId(), stockLineItem);
		}
		
		return stockLineItem;
	}
	
	/**
	 * Returns total price of Sale.
	 * @return total price of Sale.
	 */
	public double getTotal() {
		if (currentSale == null) return 0;
		return currentSale.getTotal();
	}

	/**
	 * End the Sale.
	 * @param endTime time that Sale ended.
	 */
	public void endSale(String endTime) {
		if (currentSale != null) {
			saleDao.endSale(currentSale, endTime);
			for(StockLineItem line : currentSale.getAllLineItem()){
				stock.updateStockSum(line.getStockProduct().getId(), line.getQuantity());
			}
			currentSale = null;
		}
	}
	
	/**
	 * Returns the current Sale of this SaleServices.
	 * @return the current Sale of this SaleServices.
	 */
	public Sale getCurrentSale() {
		if (currentSale == null)
			initiateSale(DateTimeStrategy.getCurrentTime());
		return currentSale;
	}

	/**
	 * Sets the current Sale of this SaleServices.
	 * @param id of Sale to retrieve.
	 * @return true if success to load Sale from ID; otherwise false.
	 */
	public boolean setCurrentSale(int id) {
		currentSale = saleDao.getSaleById(id);
		return false;
	}

	/**
	 * Determines that if there is a Sale handling or not.
	 * @return true if there is a current Sale; otherwise false.
	 */
	public boolean hasSale(){
		if(currentSale == null)return false;
		return true;
	}
	
	/**
	 * Cancels the current Sale.
	 */
	public void cancleSale() {
		if (currentSale != null){
			saleDao.cancelSale(currentSale,DateTimeStrategy.getCurrentTime());
			currentSale = null;
		}
	}

	/**
	 * Edit the specific StockLineItem
	 * @param saleId ID of StockLineItem to be edited.
	 * @param stockLineItem StockLineItem to be edited.
	 * @param quantity a new quantity to set.
	 * @param priceAtSale a new priceAtSale to set.
	 */
	public void updateItem(int saleId, StockLineItem stockLineItem, int quantity, double priceAtSale) {
		stockLineItem.setUnitPriceAtSale(priceAtSale);
		stockLineItem.setQuantity(quantity);
		saleDao.updateLineItem(saleId, stockLineItem);
	}

	/**
	 * Removes StockLineItem from the current Sale.
	 * @param stockLineItem stockLineItem to be removed.
	 */
	public void removeItem(StockLineItem stockLineItem) {
		saleDao.removeLineItem(stockLineItem.getId());
		currentSale.removeItem(stockLineItem);
		if (currentSale.getAllLineItem().isEmpty()) {
			cancleSale();
		}
	}
	
}