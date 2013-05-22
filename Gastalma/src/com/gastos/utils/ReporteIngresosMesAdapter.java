package com.gastos.utils;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.gastos.gastalma.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ReporteIngresosMesAdapter extends ArrayAdapter<Ingreso> {

	Context context;
	NumberFormat nf;

	public ReporteIngresosMesAdapter(Context context, int resourceId, List<Ingreso> items) {
		super(context, resourceId, items);
		this.context = context;
	}

	/* private view holder class */
	private class ViewHolder {
		TextView txtCant;
		TextView txtDesc;
		TextView txtDate;
		TextView txtHour;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		Ingreso rowItem = getItem(position);

		nf = NumberFormat.getCurrencyInstance(Locale.US);

		LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.list_row_mes, null);
			holder = new ViewHolder();
			holder.txtCant = (TextView) convertView.findViewById(R.id.row_title);
			holder.txtDesc = (TextView) convertView.findViewById(R.id.row_subtitle);
			holder.txtDate = (TextView) convertView.findViewById(R.id.row_date);
			holder.txtHour = (TextView) convertView.findViewById(R.id.row_hour);
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();

		holder.txtCant.setText(nf.format(Double.parseDouble(rowItem.getCantidad())));
		holder.txtDesc.setText(rowItem.getDescripcion());
		holder.txtDate.setText(rowItem.getFecha());
		holder.txtHour.setText(formatHour(rowItem.getHora()));

		return convertView;
	}

	@SuppressLint("SimpleDateFormat")
	private String formatHour(String hour) {
		Date date = null;
		try {
			date = new SimpleDateFormat("hh:mm:ss a").parse(hour);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
		return sdf.format(date);
	}
}