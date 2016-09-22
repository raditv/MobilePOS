package com.odoo.pos.helpers;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.util.Log;

import com.odoo.pos.R;
import com.odoo.pos.controllers.stock.StockServices;
import com.odoo.pos.controllers.stock.StockProductCatalog;

/**
 * Reads a demo products from CSV in res/raw/
 * 
 * @author odoo Team
 *
 */
public class Demo {

	/**
	 * Adds the demo product to stock.
	 * @param context The current stage of the application.
	 */
	public static void testProduct(Context context) {
        InputStream instream = context.getResources().openRawResource(R.raw.products);
		BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
		String line;
		try {
			StockProductCatalog catalog = StockServices.getInstance().getStockProductCatalog();
			while (( line = reader.readLine()) != null ) {
				String[] contents = line.split(",");
				Log.d("Demo", contents[0] + ":" + contents[1] + ": " + contents[2]);
				catalog.addProduct(contents[1], contents[0], Double.parseDouble(contents[2]));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
