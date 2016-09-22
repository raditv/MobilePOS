package com.odoo.pos.views.stock;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentIntegratorSupportV4;
import com.google.zxing.integration.android.IntentResult;
import com.odoo.pos.R;
import com.odoo.pos.controllers.sale.SaleServices;
import com.odoo.pos.controllers.stock.StockProduct;
import com.odoo.pos.controllers.stock.StockProductCatalog;
import com.odoo.pos.controllers.stock.StockServices;
import com.odoo.pos.helpers.DatabaseExecutor;
import com.odoo.pos.helpers.Demo;
import com.odoo.pos.helpers.NoDaoSetException;
import com.odoo.pos.views.MainActivity;
import com.odoo.pos.views.component.ButtonAdapter;
import com.odoo.pos.views.component.UpdatableFragment;

/**
 * UI for StockServices, shows list of StockProduct in the StockProductCatalog.
 * Also use for a sale process of adding StockProduct into sale.
 * 
 * @author odoo Team
 *
 */
@SuppressLint("ValidFragment")
public class StockFragment extends UpdatableFragment {

	protected static final int SEARCH_LIMIT = 0;
	private ListView inventoryListView;
	private StockProductCatalog stockProductCatalog;
	private List<Map<String, String>> inventoryList;
	private Button addProductButton;
	private EditText searchBox;
	private Button scanButton;

	private ViewPager viewPager;
	private SaleServices saleServices;
	private MainActivity main;

	private UpdatableFragment saleFragment;
	private Resources res;

	/**
	 * Construct a new StockFragment.
	 * @param saleFragment
	 */
	public StockFragment(UpdatableFragment saleFragment) {
		super();
		this.saleFragment = saleFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		try {
			stockProductCatalog = StockServices.getInstance().getStockProductCatalog();
			saleServices = SaleServices.getInstance();
		} catch (NoDaoSetException e) {
			e.printStackTrace();
		}

		View view = inflater.inflate(R.layout.layout_inventory, container, false);

		res = getResources();
		inventoryListView = (ListView) view.findViewById(R.id.productListView);
		addProductButton = (Button) view.findViewById(R.id.addProductButton);
		scanButton = (Button) view.findViewById(R.id.scanButton);
		searchBox = (EditText) view.findViewById(R.id.searchBox);

		main = (MainActivity) getActivity();
		viewPager = main.getViewPager();

		initUI();
		return view;
	}

	/**
	 * Initiate this UI.
	 */
	private void initUI() {

		addProductButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showPopup(v);
			}
		});

		searchBox.addTextChangedListener(new TextWatcher(){
			public void afterTextChanged(Editable s) {
				if (s.length() >= SEARCH_LIMIT) {
					search();
				}
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after){}
			public void onTextChanged(CharSequence s, int start, int before, int count){}
		});

		inventoryListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> myAdapter, View myView, int position, long mylng) {
				int id = Integer.parseInt(inventoryList.get(position).get("id").toString());

				saleServices.addItem(stockProductCatalog.getProductById(id), 1);
				saleFragment.update();
				viewPager.setCurrentItem(1);
			}     
		});

		scanButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				IntentIntegratorSupportV4 scanIntegrator = new IntentIntegratorSupportV4(StockFragment.this);
				scanIntegrator.initiateScan();
			}
		});

	}

	/**
	 * Show list.
	 * @param list
	 */
	private void showList(List<StockProduct> list) {

		inventoryList = new ArrayList<Map<String, String>>();
		for(StockProduct stockProduct : list) {
			inventoryList.add(stockProduct.toMap());
		}

		ButtonAdapter sAdap = new ButtonAdapter(getActivity().getBaseContext(), inventoryList,
				R.layout.listview_inventory, new String[]{"name"}, new int[] {R.id.name}, R.id.optionView, "id");
		inventoryListView.setAdapter(sAdap);
	}

	/**
	 * Search.
	 */
	private void search() {
		String search = searchBox.getText().toString();

		if (search.equals("/demo")) {
			testAddProduct();
			searchBox.setText("");
		} else if (search.equals("/clear")) {
			DatabaseExecutor.getInstance().dropAllData();
			searchBox.setText("");
		}
		else if (search.equals("")) {
			showList(stockProductCatalog.getAllProduct());
		} else {
			List<StockProduct> result = stockProductCatalog.searchProduct(search);
			showList(result);
			if (result.isEmpty()) {

			}
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		IntentResult scanningResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, intent);

		if (scanningResult != null) {
			String scanContent = scanningResult.getContents();
			searchBox.setText(scanContent);
		} else {
			Toast.makeText(getActivity().getBaseContext(), res.getString(R.string.fail),
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Test adding product
	 */
	protected void testAddProduct() {
		Demo.testProduct(getActivity());
		Toast.makeText(getActivity().getBaseContext(), res.getString(R.string.success),
				Toast.LENGTH_SHORT).show();
	}

	/**
	 * Show popup.
	 * @param anchorView
	 */
	public void showPopup(View anchorView) {
		AddProductDialogFragment newFragment = new AddProductDialogFragment(StockFragment.this);
		newFragment.show(getFragmentManager(), "");
	}

	@Override
	public void update() {
		search();
	}

	@Override
	public void onResume() {
		super.onResume();
		update();
	}

}