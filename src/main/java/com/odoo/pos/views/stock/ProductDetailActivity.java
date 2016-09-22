package com.odoo.pos.views.stock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.odoo.pos.R;
import com.odoo.pos.controllers.DateTimeStrategy;
import com.odoo.pos.controllers.stock.StockProductLot;
import com.odoo.pos.controllers.stock.StockServices;
import com.odoo.pos.controllers.stock.StockProduct;
import com.odoo.pos.controllers.stock.StockProductCatalog;
import com.odoo.pos.controllers.stock.Stock;
import com.odoo.pos.helpers.NoDaoSetException;

/**
 * UI for shows the datails of each StockProduct.
 * @author odoo Team
 *
 */
@SuppressLint("NewApi")
public class ProductDetailActivity extends Activity {

	private StockProductCatalog stockProductCatalog;
	private Stock stock;
	private StockProduct stockProduct;
	private List<Map<String, String>> stockList;
	private EditText nameBox;
	private EditText barcodeBox;
	private TextView stockSumBox;
	private EditText priceBox;
	private Button addProductLotButton;
	private Button submitEditButton;
	private Button cancelEditButton;
	private Button openEditButton;
	private TabHost mTabHost;
	private ListView stockListView;
	private String id;
	private String[] remember;
	private AlertDialog.Builder popDialog;
	private LayoutInflater inflater ;
	private Resources res;
	private EditText costBox;
	private EditText quantityBox;
	private Button confirmButton;
	private Button clearButton;
	private View Viewlayout;
	private AlertDialog alert;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.detail_menu, menu);
	    return true;
	  } 
	
	@SuppressLint("NewApi")
	private void initiateActionBar() {
		if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setTitle(res.getString(R.string.product_detail));
			actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#33B5E5")));
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		res = getResources();
		initiateActionBar();
		
		try {
			stock = StockServices.getInstance().getStock();
			stockProductCatalog = StockServices.getInstance().getStockProductCatalog();
		} catch (NoDaoSetException e) {
			e.printStackTrace();
		}

		id = getIntent().getStringExtra("id");
		stockProduct = stockProductCatalog.getProductById(Integer.parseInt(id));

		initUI(savedInstanceState);
		remember = new String[3];
		nameBox.setText(stockProduct.getName());
		priceBox.setText(stockProduct.getUnitPrice() + "");
		barcodeBox.setText(stockProduct.getBarcode());

	}

	/**
	 * Initiate this UI.
	 * @param savedInstanceState
	 */
	private void initUI(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_productdetail_main);
		stockListView = (ListView) findViewById(R.id.stockListView);
		nameBox = (EditText) findViewById(R.id.nameBox);
		priceBox = (EditText) findViewById(R.id.priceBox);
		barcodeBox = (EditText) findViewById(R.id.barcodeBox);
		stockSumBox = (TextView) findViewById(R.id.stockSumBox);
		submitEditButton = (Button) findViewById(R.id.submitEditButton);
		submitEditButton.setVisibility(View.INVISIBLE);
		cancelEditButton = (Button) findViewById(R.id.cancelEditButton);
		cancelEditButton.setVisibility(View.INVISIBLE);
		openEditButton = (Button) findViewById(R.id.openEditButton);
		openEditButton.setVisibility(View.VISIBLE);
		addProductLotButton = (Button) findViewById(R.id.addProductLotButton);
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();
		mTabHost.addTab(mTabHost.newTabSpec("tab_test1").setIndicator(res.getString(R.string.product_detail))
				.setContent(R.id.tab1));
		mTabHost.addTab(mTabHost.newTabSpec("tab_test2").setIndicator(res.getString(R.string.stock))
				.setContent(R.id.tab2));
		mTabHost.setCurrentTab(0);
		popDialog = new AlertDialog.Builder(this);
		inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
		addProductLotButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showAddProductLot();
			}
		});

		openEditButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				edit();
			}
		});

		submitEditButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				submitEdit();
			}
		});
		
		cancelEditButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				cancelEdit();
			}
		});
	}

	/**
	 * Show list.
	 * @param list
	 */
	private void showList(List<StockProductLot> list) {

		stockList = new ArrayList<Map<String, String>>();
		for (StockProductLot stockProductLot : list) {
			stockList.add(stockProductLot.toMap());
		}

		SimpleAdapter sAdap = new SimpleAdapter(ProductDetailActivity.this, stockList,
				R.layout.listview_stock, new String[] { "dateAdded",
				"cost", "quantity" }, new int[] {
				R.id.dateAdded, R.id.cost, R.id.quantity, });
		stockListView.setAdapter(sAdap);
	}

	@Override
	protected void onResume() {
		super.onResume();
		int productId = Integer.parseInt(id);
		stockSumBox.setText(stock.getStockSumById(productId)+"");
		showList(stock.getProductLotByProductId(productId));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			return true;
		case R.id.action_edit:
			edit();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	/**
	 * Submit editing.
	 */
	private void submitEdit() {
		nameBox.setFocusable(false);
		nameBox.setFocusableInTouchMode(false);
		nameBox.setBackgroundColor(Color.parseColor("#87CEEB"));
		priceBox.setFocusable(false);
		priceBox.setFocusableInTouchMode(false);
		priceBox.setBackgroundColor(Color.parseColor("#87CEEB"));
		barcodeBox.setFocusable(false);
		barcodeBox.setFocusableInTouchMode(false);
		barcodeBox.setBackgroundColor(Color.parseColor("#87CEEB"));
		stockProduct.setName(nameBox.getText().toString());
		if(priceBox.getText().toString().equals(""))
			priceBox.setText("0.0");
		stockProduct.setUnitPrice(Double.parseDouble(priceBox.getText().toString()));
		stockProduct.setBarcode(barcodeBox.getText().toString());
		stockProductCatalog.editProduct(stockProduct);
		submitEditButton.setVisibility(View.INVISIBLE);
		cancelEditButton.setVisibility(View.INVISIBLE);
		openEditButton.setVisibility(View.VISIBLE);
	}
	
	/**
	 * Cancel editing.
	 */
	private void cancelEdit() {
		nameBox.setFocusable(false);
		nameBox.setFocusableInTouchMode(false);
		nameBox.setBackgroundColor(Color.parseColor("#87CEEB"));
		priceBox.setFocusable(false);
		priceBox.setFocusableInTouchMode(false);
		priceBox.setBackgroundColor(Color.parseColor("#87CEEB"));
		barcodeBox.setFocusable(false);
		barcodeBox.setFocusableInTouchMode(false);
		barcodeBox.setBackgroundColor(Color.parseColor("#87CEEB"));
		submitEditButton.setVisibility(View.INVISIBLE);
		cancelEditButton.setVisibility(View.INVISIBLE);
		nameBox.setText(remember[0]);
		priceBox.setText(remember[1]);
		barcodeBox.setText(remember[2]);
		openEditButton.setVisibility(View.VISIBLE);
	}
	
	/**
	 * Edit
	 */
	private void edit() {
		nameBox.setFocusable(true);
		nameBox.setFocusableInTouchMode(true);
		nameBox.setBackgroundColor(Color.parseColor("#FFBB33"));
		priceBox.setFocusable(true);
		priceBox.setFocusableInTouchMode(true);
		priceBox.setBackgroundColor(Color.parseColor("#FFBB33"));
		barcodeBox.setFocusable(true);
		barcodeBox.setFocusableInTouchMode(true);
		barcodeBox.setBackgroundColor(Color.parseColor("#FFBB33"));	
		remember[0] = nameBox.getText().toString();
		remember[1] = priceBox.getText().toString();
		remember[2] = barcodeBox.getText().toString();
		submitEditButton.setVisibility(View.VISIBLE);
		cancelEditButton.setVisibility(View.VISIBLE);
		openEditButton.setVisibility(View.INVISIBLE);
	}
	
	/**
	 * Show adding stockProduct lot.
	 */
	private void showAddProductLot(){
		Viewlayout = inflater.inflate(R.layout.layout_addproductlot,
				(ViewGroup) findViewById(R.id.addProdutlot_dialog));
		popDialog.setView(Viewlayout);
		
		costBox = (EditText) Viewlayout.findViewById(R.id.costBox);
		quantityBox = (EditText) Viewlayout.findViewById(R.id.quantityBox);
		confirmButton = (Button) Viewlayout.findViewById(R.id.confirmButton);
		clearButton = (Button) Viewlayout.findViewById(R.id.clearButton); 
		confirmButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (quantityBox.getText().toString().equals("") || costBox.getText().toString().equals("")) {
					Toast.makeText(ProductDetailActivity.this,
							res.getString(R.string.please_input_all), Toast.LENGTH_SHORT)
							.show();
				} else {
					boolean success = stock.addProductLot(
							DateTimeStrategy.getCurrentTime(), 
							Integer.parseInt(quantityBox.getText().toString()),
							stockProduct,
							Double.parseDouble(costBox.getText().toString()));

					if (success) {
						Toast.makeText(ProductDetailActivity.this, res.getString(R.string.success), Toast.LENGTH_SHORT).show();
						costBox.setText("");
						quantityBox.setText("");
						onResume();
						alert.dismiss();
						
						
					} else {
						Toast.makeText(ProductDetailActivity.this, res.getString(R.string.fail) ,Toast.LENGTH_SHORT).show();
					}
				}
				
			}
		});
		clearButton.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				if(quantityBox.getText().toString().equals("") && costBox.getText().toString().equals("")){
					alert.dismiss();
					onResume();
				}
				else{
					costBox.setText("");
					quantityBox.setText("");
				}	
			}
		});
		
		alert = popDialog.create();
		alert.show();
	}
}