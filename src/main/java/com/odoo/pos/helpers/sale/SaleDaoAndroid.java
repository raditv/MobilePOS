package com.odoo.pos.helpers.sale;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.ContentValues;

import com.odoo.pos.controllers.DateTimeStrategy;
import com.odoo.pos.controllers.stock.StockLineItem;
import com.odoo.pos.controllers.stock.StockProduct;
import com.odoo.pos.controllers.sale.SaleQuickLoad;
import com.odoo.pos.controllers.sale.Sale;
import com.odoo.pos.helpers.DatabaseOperation;
import com.odoo.pos.helpers.DatabaseContents;


/**
 * DAO used by android for Sale process.
 * 
 * @author odoo Team
 *
 */
public class SaleDaoAndroid implements SaleDao {

	DatabaseOperation databaseOperation;
	public SaleDaoAndroid(DatabaseOperation databaseOperation) {
		this.databaseOperation = databaseOperation;
	}

	@Override
	public Sale initiateSale(String startTime) {
		ContentValues content = new ContentValues();
        content.put("start_time", startTime.toString());
        content.put("status", "ON PROCESS");
        content.put("payment", "n/a");
        content.put("total", "0.0");
        content.put("orders", "0");
        content.put("end_time", startTime.toString());
        
        int id = databaseOperation.insert(DatabaseContents.TABLE_SALE.toString(), content);
		return new Sale(id,startTime);
	}

	@Override
	public void endSale(Sale sale, String endTime) {
		ContentValues content = new ContentValues();
        content.put("_id", sale.getId());
        content.put("status", "ENDED");
        content.put("payment", "n/a");
        content.put("total", sale.getTotal());
        content.put("orders", sale.getOrders());
        content.put("start_time", sale.getStartTime());
        content.put("end_time", endTime);
		databaseOperation.update(DatabaseContents.TABLE_SALE.toString(), content);
	}
	
	@Override
	public int addLineItem(int saleId, StockLineItem stockLineItem) {
		ContentValues content = new ContentValues();
        content.put("sale_id", saleId);
        content.put("product_id", stockLineItem.getStockProduct().getId());
        content.put("quantity", stockLineItem.getQuantity());
        content.put("unit_price", stockLineItem.getPriceAtSale());
        int id = databaseOperation.insert(DatabaseContents.TABLE_SALE_LINEITEM.toString(), content);
        return id;
	}

	@Override
	public void updateLineItem(int saleId, StockLineItem stockLineItem) {
		ContentValues content = new ContentValues();		
		content.put("_id", stockLineItem.getId());
		content.put("sale_id", saleId);
		content.put("product_id", stockLineItem.getStockProduct().getId());
		content.put("quantity", stockLineItem.getQuantity());
		content.put("unit_price", stockLineItem.getPriceAtSale());
		databaseOperation.update(DatabaseContents.TABLE_SALE_LINEITEM.toString(), content);
	}

	@Override
	public List<Sale> getAllSale() {
		return getAllSale(" WHERE status = 'ENDED'");
	}
	
	@Override
	public List<Sale> getAllSaleDuring(Calendar start, Calendar end) {
		String startBound = DateTimeStrategy.getSQLDateFormat(start);
		String endBound = DateTimeStrategy.getSQLDateFormat(end);
		List<Sale> list = getAllSale(" WHERE end_time BETWEEN '" + startBound + " 00:00:00' AND '" + endBound + " 23:59:59' AND status = 'ENDED'"); 
		return list;
	}
	
	/**
	 * This method get all Sale *BUT* no StockLineItem will be loaded.
	 * @param condition
	 * @return
	 */
	public List<Sale> getAllSale(String condition) {
		String queryString = "SELECT * FROM " + DatabaseContents.TABLE_SALE + condition;
        List<Object> objectList = databaseOperation.select(queryString);
        List<Sale> list = new ArrayList<Sale>();
        for (Object object: objectList) {
        	ContentValues content = (ContentValues) object;
        	list.add(new SaleQuickLoad(
        			content.getAsInteger("_id"),
        			content.getAsString("start_time"),
        			content.getAsString("end_time"),
        			content.getAsString("status"),
        			content.getAsDouble("total"),
        			content.getAsInteger("orders")      
        			)
        	);
        }
        return list;
	}
	
	/**
	 * This load complete data of Sale.
	 * @param id Sale ID.
	 * @return Sale of specific ID.
	 */
	@Override
	public Sale getSaleById(int id) {
		String queryString = "SELECT * FROM " + DatabaseContents.TABLE_SALE + " WHERE _id = " + id;
        List<Object> objectList = databaseOperation.select(queryString);
        List<Sale> list = new ArrayList<Sale>();
        for (Object object: objectList) {
        	ContentValues content = (ContentValues) object;
        	list.add(new Sale(
        			content.getAsInteger("_id"),
        			content.getAsString("start_time"),
        			content.getAsString("end_time"),
        			content.getAsString("status"),
        			getLineItem(content.getAsInteger("_id")))
        			);
        }
        return list.get(0);
	}

	@Override
	public List<StockLineItem> getLineItem(int saleId) {
		String queryString = "SELECT * FROM " + DatabaseContents.TABLE_SALE_LINEITEM + " WHERE sale_id = " + saleId;
		List<Object> objectList = databaseOperation.select(queryString);
		List<StockLineItem> list = new ArrayList<StockLineItem>();
		for (Object object: objectList) {
			ContentValues content = (ContentValues) object;
			int productId = content.getAsInteger("product_id");
			String queryString2 = "SELECT * FROM " + DatabaseContents.TABLE_PRODUCT_CATALOG + " WHERE _id = " + productId;
			List<Object> objectList2 = databaseOperation.select(queryString2);
			
			List<StockProduct> stockProductList = new ArrayList<StockProduct>();
			for (Object object2: objectList2) {
				ContentValues content2 = (ContentValues) object2;
				stockProductList.add(new StockProduct(productId, content2.getAsString("name"), content2.getAsString("barcode"), content2.getAsDouble("unit_price")));
			}
			list.add(new StockLineItem(content.getAsInteger("_id") , stockProductList.get(0), content.getAsInteger("quantity"), content.getAsDouble("unit_price")));
		}
		return list;
	}

	@Override
	public void clearSaleLedger() {
		databaseOperation.execute("DELETE FROM " + DatabaseContents.TABLE_SALE);
		databaseOperation.execute("DELETE FROM " + DatabaseContents.TABLE_SALE_LINEITEM);
	}

	@Override
	public void cancelSale(Sale sale,String endTime) {
		ContentValues content = new ContentValues();
        content.put("_id", sale.getId());
        content.put("status", "CANCELED");
        content.put("payment", "n/a");
        content.put("total", sale.getTotal());
        content.put("orders", sale.getOrders());
        content.put("start_time", sale.getStartTime());
        content.put("end_time", endTime);
		databaseOperation.update(DatabaseContents.TABLE_SALE.toString(), content);
		
	}

	@Override
	public void removeLineItem(int id) {
		databaseOperation.delete(DatabaseContents.TABLE_SALE_LINEITEM.toString(), id);
	}



}
