package com.gastos.utils;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class GastosAdapter extends ArrayAdapter<Gasto> {

	Context context;
	NumberFormat nf;

	public GastosAdapter(Context context, int resourceId, List<Gasto> items) {
		super(context, resourceId, items);
		this.context = context;
	}

	/* private view holder class */
	private class ViewHolder {
		TextView txtCant;
		TextView txtNom;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		Gasto rowItem = getItem(position);

		nf = NumberFormat.getCurrencyInstance(Locale.US);

		LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(android.R.layout.simple_list_item_2, null);
			holder = new ViewHolder();
			holder.txtCant = (TextView) convertView.findViewById(android.R.id.text1);
			holder.txtNom = (TextView) convertView.findViewById(android.R.id.text2);
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();

		holder.txtCant.setText(nf.format(Double.parseDouble(rowItem.getCosto())));
		holder.txtNom.setText(rowItem.getNombre());

		return convertView;
	}
}