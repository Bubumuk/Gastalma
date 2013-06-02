package com.gastos.utils;

import java.text.DateFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.emilsjolander.components.stickylistheaders.StickyListHeadersAdapter;
import com.gastos.gastalma.R;
import com.gastos.utils.ReporteIngresosAñoAdapter.HeaderViewHolder;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ReporteGastosMesAdapter extends ArrayAdapter<Gasto> implements StickyListHeadersAdapter {

	Context context;
	NumberFormat nf;

	public ReporteGastosMesAdapter(Context context, int resourceId, List<Gasto> items) {
		super(context, resourceId, items);
		this.context = context;
	}

	/* private view holder class */
	private class ViewHolder {
		TextView txtCant;
		TextView txtNom;
		TextView txtDate;
		TextView txtHour;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		Gasto rowItem = getItem(position);

		nf = NumberFormat.getCurrencyInstance(Locale.US);

		LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.list_row_mes, null);
			holder = new ViewHolder();
			holder.txtCant = (TextView) convertView.findViewById(R.id.row_title);
			holder.txtNom = (TextView) convertView.findViewById(R.id.row_subtitle);
			holder.txtDate = (TextView) convertView.findViewById(R.id.row_date);
			holder.txtHour = (TextView) convertView.findViewById(R.id.row_hour);
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();

		holder.txtCant.setText(nf.format(Double.parseDouble(rowItem.getCosto())));
		holder.txtNom.setText(rowItem.getNombre());
		//holder.txtDate.setText(rowItem.getFecha());
		holder.txtHour.setText(formatHour(rowItem.getHora()));

		return convertView;
	}
	
	@Override
	public long getHeaderId(int position) {
		String fecha = getItem(position).getFecha();
		long mes = Long.parseLong(fecha.substring(8, 10));
		return mes;
	}

	class HeaderViewHolder {
		TextView text1;
	}
	
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	public View getHeaderView(int position, View convertView, ViewGroup parent) {
		HeaderViewHolder holder;
		Gasto rowItem = getItem(position);

		LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

		if (convertView == null) {
			holder = new HeaderViewHolder();
			convertView = mInflater.inflate(R.layout.header, parent, false);
			holder.text1 = (TextView) convertView.findViewById(R.id.text1);
			convertView.setTag(holder);
		} else {
			holder = (HeaderViewHolder) convertView.getTag();
		}

		String fecha = rowItem.getFecha();
		Date d = new Date(fecha.replace("-", "/"));
		
		int dia = separarFechaN(fecha, 1);
		
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
		String dayOfTheWeek = sdf.format(d);

		String headerText = dayOfTheWeek + " " + dia;
		holder.text1.setText(headerText);
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
	
	private int separarFechaN(String fecha, int caso) {
		switch (caso) {
		case 3: // año
			return Integer.parseInt(fecha.substring(0, fecha.indexOf("-")));
		case 2: // mes
			return Integer.parseInt(fecha.substring(fecha.indexOf("-") + 1, fecha.lastIndexOf("-")));
		case 1: // dia
			return Integer.parseInt(fecha.substring(fecha.lastIndexOf("-") + 1));
		}
		return 0;
	}
}