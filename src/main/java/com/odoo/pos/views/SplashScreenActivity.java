package com.odoo.pos.views;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.odoo.pos.R;
import com.odoo.pos.controllers.DateTimeStrategy;
import com.odoo.pos.controllers.LanguageController;
import com.odoo.pos.controllers.stock.StockServices;
import com.odoo.pos.controllers.sale.SaleServices;
import com.odoo.pos.controllers.sale.SaleLedger;
import com.odoo.pos.helpers.AndroidDatabaseOperation;
import com.odoo.pos.helpers.DatabaseOperation;
import com.odoo.pos.helpers.DatabaseExecutor;
import com.odoo.pos.helpers.stock.StockDatabaseOperation;
import com.odoo.pos.helpers.stock.StockDatabaseOperationAndroid;
import com.odoo.pos.helpers.sale.SaleDao;
import com.odoo.pos.helpers.sale.SaleDaoAndroid;

/**
 * This is the first activity page, core-app and database created here.
 * Dependency injection happens here.
 * 
 * @author odoo Team
 * 
 */
public class SplashScreenActivity extends Activity {

	public static final String POS_VERSION = "Odoo POS";
	private static final long SPLASH_TIMEOUT = 2000;
	private Button goButton;
	private boolean gone;
	
	/**
	 * Loads database and DAO.
	 */
	private void initiateCoreApp() {
		DatabaseOperation databaseOperation = new AndroidDatabaseOperation(this);
		StockDatabaseOperation stockDatabaseOperation = new StockDatabaseOperationAndroid(databaseOperation);
		SaleDao saleDao = new SaleDaoAndroid(databaseOperation);
		DatabaseExecutor.setDatabaseOperation(databaseOperation);
		LanguageController.setDatabaseOperation(databaseOperation);

		StockServices.setStockDatabaseOperation(stockDatabaseOperation);
		SaleServices.setSaleDao(saleDao);
		SaleLedger.setSaleDao(saleDao);
		
		DateTimeStrategy.setLocale("id", "ID");
		setLanguage(LanguageController.getInstance().getLanguage());

		Log.d("Core App", "INITIATE");
	}
	
	/**
	 * Set language.
	 * @param localeString
	 */
	private void setLanguage(String localeString) {
		Locale locale = new Locale(localeString);
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		getBaseContext().getResources().updateConfiguration(config,
				getBaseContext().getResources().getDisplayMetrics());
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		initiateUI(savedInstanceState);
		initiateCoreApp();
	}
	
	/**
	 * Go.
	 */
	private void go() {
		gone = true;
		Intent newActivity = new Intent(SplashScreenActivity.this,
				MainActivity.class);
		startActivity(newActivity);
		SplashScreenActivity.this.finish();	
	}

	/**
	 * Initiate this UI.
	 * @param savedInstanceState
	 */
	private void initiateUI(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_splashscreen);
		goButton = (Button) findViewById(R.id.goButton);
		goButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				go();
			}

		});
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if (!gone) go();
			}
		}, SPLASH_TIMEOUT);
	}

}