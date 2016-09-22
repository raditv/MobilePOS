package com.odoo.pos.helpers;

/**
 * Enum for name of tables in database.
 * 
 * @author odoo Team
 *
 */
public enum DatabaseContents {
	
	DATABASE("com.odoo.db1"),
	TABLE_PRODUCT_CATALOG("product_catalog"),
	TABLE_STOCK("stock"),
	TABLE_SALE("sale"),
	TABLE_SALE_LINEITEM("sale_lineitem"),
	TABLE_STOCK_SUM("stock_sum"),
	LANGUAGE("language");
	
	private String name;
	
	/**
	 * Constructs DatabaseContents.
	 * @param name name of this content for using in database.
	 */
	private DatabaseContents(String name) {
		this.name = name;
	}
	
	@Override 
	public String toString() {
		return name;
	}
}
