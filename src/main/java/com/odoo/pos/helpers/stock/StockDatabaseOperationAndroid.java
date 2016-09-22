package com.odoo.pos.helpers.stock;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.util.Log;

import com.odoo.pos.controllers.stock.StockProduct;
import com.odoo.pos.controllers.stock.StockProductLot;
import com.odoo.pos.helpers.DatabaseOperation;
import com.odoo.pos.helpers.DatabaseContents;

/**
 * DAO used by android for StockServices.
 * 
 * @author odoo Team
 *
 */
public class StockDatabaseOperationAndroid implements StockDatabaseOperation {

	private DatabaseOperation databaseOperation;
	
	/**
	 * Constructs StockDatabaseOperationAndroid.
	 * @param databaseOperation databaseOperation for use in StockDatabaseOperationAndroid.
	 */
	public StockDatabaseOperationAndroid(DatabaseOperation databaseOperation) {
		this.databaseOperation = databaseOperation;
	}

	@Override
	public int addProduct(StockProduct stockProduct) {
		ContentValues content = new ContentValues();
        content.put("name", stockProduct.getName());
        content.put("barcode", stockProduct.getBarcode());
        content.put("unit_price", stockProduct.getUnitPrice());
        content.put("status", "ACTIVE");
        
        int id = databaseOperation.insert(DatabaseContents.TABLE_PRODUCT_CATALOG.toString(), content);
        
        
    	ContentValues content2 = new ContentValues();
        content2.put("_id", id);
        content2.put("quantity", 0);
        databaseOperation.insert(DatabaseContents.TABLE_STOCK_SUM.toString(), content2);
        
        return id;
	}
	
	/**
	 * Converts list of object to list of product.
	 * @param objectList list of object.
	 * @return list of product.
	 */
	private List<StockProduct> toProductList(List<Object> objectList) {
		List<StockProduct> list = new ArrayList<StockProduct>();
        for (Object object: objectList) {
        	ContentValues content = (ContentValues) object;
                list.add(new StockProduct(
                		content.getAsInteger("_id"),
                        content.getAsString("name"),
                        content.getAsString("barcode"),
                        content.getAsDouble("unit_price"))
                );
        }
        return list;
	}

	@Override
	public List<StockProduct> getAllProduct() {
        return getAllProduct(" WHERE status = 'ACTIVE'");
	}
	
	/**
	 * Returns list of all products in stock.
	 * @param condition specific condition for getAllProduct.
	 * @return list of all products in stock.
	 */
	private List<StockProduct> getAllProduct(String condition) {
		String queryString = "SELECT * FROM " + DatabaseContents.TABLE_PRODUCT_CATALOG.toString() + condition + " ORDER BY name";
        List<StockProduct> list = toProductList(databaseOperation.select(queryString));
        return list;
	}
	
	/**
	 * Returns product from stock finds by specific reference.
	 * @param reference reference value.
	 * @param value value for search.
	 * @return list of product.
	 */
	private List<StockProduct> getProductBy(String reference, String value) {
        String condition = " WHERE " + reference + " = " + value + " ;";
        return getAllProduct(condition);
	}
	
	/**
	 * Returns product from stock finds by similar name.
	 * @param reference reference value.
	 * @param value value for search.
	 * @return list of product.
	 */
	private List<StockProduct> getSimilarProductBy(String reference, String value) {
        String condition = " WHERE " + reference + " LIKE '%" + value + "%' ;";
        return getAllProduct(condition);
	}

	@Override
	public StockProduct getProductByBarcode(String barcode) {
		List<StockProduct> list = getProductBy("barcode", barcode);
        if (list.isEmpty()) return null;
        return list.get(0);
	}

	@Override
	public StockProduct getProductById(int id) {
		return getProductBy("_id", id+"").get(0);
	}

	@Override
	public boolean editProduct(StockProduct stockProduct) {
		ContentValues content = new ContentValues();
		content.put("_id", stockProduct.getId());
		content.put("name", stockProduct.getName());
        content.put("barcode", stockProduct.getBarcode());
        content.put("status", "ACTIVE");
        content.put("unit_price", stockProduct.getUnitPrice());
		return databaseOperation.update(DatabaseContents.TABLE_PRODUCT_CATALOG.toString(), content);
	}
	
	
	@Override
	public int addProductLot(StockProductLot stockProductLot) {
		 ContentValues content = new ContentValues();
         content.put("date_added", stockProductLot.getDateAdded());
         content.put("quantity",  stockProductLot.getQuantity());
         content.put("product_id",  stockProductLot.getStockProduct().getId());
         content.put("cost",  stockProductLot.unitCost());
         int id = databaseOperation.insert(DatabaseContents.TABLE_STOCK.toString(), content);
         
         int productId = stockProductLot.getStockProduct().getId();
         ContentValues content2 = new ContentValues();
         content2.put("_id", productId);
         content2.put("quantity", getStockSumById(productId) + stockProductLot.getQuantity());
         Log.d("stock dao android","" + getStockSumById(productId) + " " + productId + " " + stockProductLot.getQuantity() );
         databaseOperation.update(DatabaseContents.TABLE_STOCK_SUM.toString(), content2);
         
         return id;
	}


	@Override
	public List<StockProduct> getProductByName(String name) {
		return getSimilarProductBy("name", name);
	}

	@Override
	public List<StockProduct> searchProduct(String search) {
		String condition = " WHERE name LIKE '%" + search + "%' OR barcode LIKE '%" + search + "%' ;";
        return getAllProduct(condition);
	}
	
	/**
	 * Returns list of all StockProductLot in stock.
	 * @param condition specific condition for get StockProductLot.
	 * @return list of all StockProductLot in stock.
	 */
	private List<StockProductLot> getAllProductLot(String condition) {
		String queryString = "SELECT * FROM " + DatabaseContents.TABLE_STOCK.toString() + condition;
        List<StockProductLot> list = toProductLotList(databaseOperation.select(queryString));
        return list;
	}

	/**
	 * Converts list of object to list of StockProductLot.
	 * @param objectList list of object.
	 * @return list of StockProductLot.
	 */
	private List<StockProductLot> toProductLotList(List<Object> objectList) {
		List<StockProductLot> list = new ArrayList<StockProductLot>();
		for (Object object: objectList) {
			ContentValues content = (ContentValues) object;
			int productId = content.getAsInteger("product_id");
			StockProduct stockProduct = getProductById(productId);
					list.add( 
					new StockProductLot(content.getAsInteger("_id"),
							content.getAsString("date_added"),
							content.getAsInteger("quantity"),
							stockProduct,
							content.getAsDouble("cost"))
					);
		}
		return list;
	}

	@Override
	public List<StockProductLot> getProductLotByProductId(int id) {
		return getAllProductLot(" WHERE product_id = " + id);
	}
	
	@Override
	public List<StockProductLot> getProductLotById(int id) {
		return getAllProductLot(" WHERE _id = " + id);
	}

	@Override
	public List<StockProductLot> getAllProductLot() {
		return getAllProductLot("");
	}

	@Override
	public int getStockSumById(int id) {
		String queryString = "SELECT * FROM " + DatabaseContents.TABLE_STOCK_SUM + " WHERE _id = " + id;
		List<Object> objectList = (databaseOperation.select(queryString));
		ContentValues content = (ContentValues) objectList.get(0);
		int quantity = content.getAsInteger("quantity");
		Log.d("inventoryDaoAndroid", "stock sum of "+ id + " is " + quantity);
		return quantity;
	}

	@Override
	public void updateStockSum(int productId, double quantity) {
		 ContentValues content = new ContentValues();
         content.put("_id", productId);
         content.put("quantity", getStockSumById(productId) - quantity);
         databaseOperation.update(DatabaseContents.TABLE_STOCK_SUM.toString(), content);
	}

	@Override
	public void clearProductCatalog() {		
		databaseOperation.execute("DELETE FROM " + DatabaseContents.TABLE_PRODUCT_CATALOG);
	}

	@Override
	public void clearStock() {
		databaseOperation.execute("DELETE FROM " + DatabaseContents.TABLE_STOCK);
		databaseOperation.execute("DELETE FROM " + DatabaseContents.TABLE_STOCK_SUM);
	}

	@Override
	public void suspendProduct(StockProduct stockProduct) {
		ContentValues content = new ContentValues();
		content.put("_id", stockProduct.getId());
		content.put("name", stockProduct.getName());
		content.put("barcode", stockProduct.getBarcode());
		content.put("status", "INACTIVE");
		content.put("unit_price", stockProduct.getUnitPrice());
		databaseOperation.update(DatabaseContents.TABLE_PRODUCT_CATALOG.toString(), content);
	}
	



}
