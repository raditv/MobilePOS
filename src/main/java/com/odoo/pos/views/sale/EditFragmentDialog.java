package com.odoo.pos.views.sale;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.odoo.pos.R;
import com.odoo.pos.controllers.stock.StockLineItem;
import com.odoo.pos.controllers.sale.SaleServices;
import com.odoo.pos.helpers.NoDaoSetException;
import com.odoo.pos.views.component.UpdatableFragment;

/**
 * A dialog for edit a StockLineItem of sale,
 * overriding price or set the quantity.
 * @author odoo Team
 *
 */
@SuppressLint("ValidFragment")
public class EditFragmentDialog extends DialogFragment {
	private SaleServices saleServices;
	private UpdatableFragment saleFragment;
	private UpdatableFragment reportFragment;
	private EditText quantityBox;
	private EditText priceBox;
	private Button comfirmButton;
	private String saleId;
	private String position;
	private StockLineItem stockLineItem;
	private Button removeButton;
	
	/**
	 * Construct a new  EditFragmentDialog.
	 * @param saleFragment
	 * @param reportFragment
	 */
	public EditFragmentDialog(UpdatableFragment saleFragment, UpdatableFragment reportFragment) {
		super();
		this.saleFragment = saleFragment;
		this.reportFragment = reportFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.dialog_saleedit, container, false);
		try {
			saleServices = SaleServices.getInstance();
		} catch (NoDaoSetException e) {
			e.printStackTrace();
		}
		
		quantityBox = (EditText) v.findViewById(R.id.quantityBox);
		priceBox = (EditText) v.findViewById(R.id.priceBox);
		comfirmButton = (Button) v.findViewById(R.id.confirmButton);
		removeButton = (Button) v.findViewById(R.id.removeButton);
		
		saleId = getArguments().getString("sale_id");
		position = getArguments().getString("position");

		stockLineItem = saleServices.getCurrentSale().getLineItemAt(Integer.parseInt(position));
		quantityBox.setText(stockLineItem.getQuantity()+"");
		priceBox.setText(stockLineItem.getStockProduct().getUnitPrice()+"");
		removeButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Log.d("remove", "id=" + stockLineItem.getId());
				saleServices.removeItem(stockLineItem);
				end();
			}
		});

		comfirmButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view) {
				saleServices.updateItem(
						Integer.parseInt(saleId),
						stockLineItem,
						Integer.parseInt(quantityBox.getText().toString()),
						Double.parseDouble(priceBox.getText().toString())
				);
				
				end();
			}
			
		});
		return v;
	}
	
	/**
	 * End.
	 */
	private void end(){
		saleFragment.update();
		reportFragment.update();
		this.dismiss();
	}
	
	
}
