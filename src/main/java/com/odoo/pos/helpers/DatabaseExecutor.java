package com.odoo.pos.helpers;

/**
 * Uses to directly access to databaseOperation.
 * 
 * @author odoo Team
 *
 */
public class DatabaseExecutor {

	private static DatabaseOperation databaseOperation;
	private static DatabaseExecutor instance;
	
	private DatabaseExecutor() {
		
	}
	
	/**
	 * Sets databaseOperation for use in DatabaseExecutor.
	 * @param db databaseOperation.
	 */
	public static void setDatabaseOperation(DatabaseOperation db) {
		databaseOperation = db;
	}
	
	public static DatabaseExecutor getInstance() {
		if (instance == null)
			instance = new DatabaseExecutor();
		return instance;
	}
	
	/**
	 * Drops all data in databaseOperation.
	 */
	public void dropAllData() {
		execute("DELETE FROM " + DatabaseContents.TABLE_PRODUCT_CATALOG + ";");
		execute("DELETE FROM " + DatabaseContents.TABLE_SALE + ";");
		execute("DELETE FROM " + DatabaseContents.TABLE_SALE_LINEITEM + ";");
		execute("DELETE FROM " + DatabaseContents.TABLE_STOCK + ";");
		execute("DELETE FROM " + DatabaseContents.TABLE_STOCK_SUM + ";");
		execute("VACUUM;");
	}
	
	/**
	 * Directly execute to databaseOperation.
	 * @param queryString query string for execute.
	 */
	private void execute(String queryString) {
		databaseOperation.execute(queryString);
	}
	
	
}
