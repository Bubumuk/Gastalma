package com.gastos.utils;

import java.text.DateFormatSymbols;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import com.emilsjolander.components.stickylistheaders.StickyListHeadersAdapter;
import com.gastos.gastalma.R;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ReporteGastosAñoAdapter extends ArrayAdapter<Gasto> implements StickyListHeadersAdapter {

	Context context;
	NumberFormat nf;

	public ReporteGastosAñoAdapter(Context context, int resourceId, List<Gasto> items) {
		super(context, resourceId, items);
		this.context = context;
	}

	/* private view holder class */
	private class ViewHolder {
		TextView txtCant;
		TextView txtNom;
        TextView txtDate;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		Gasto rowItem = getItem(position);

		nf = NumberFormat.getCurrencyInstance(Locale.US);

		LayoutInflater mInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_row, null);
            holder = new ViewHolder();
            holder.txtCant = (TextView) convertView.findViewById(R.id.row_title);
            holder.txtNom = (TextView) convertView.findViewById(R.id.row_subtitle);
            holder.txtDate = (TextView) convertView.findViewById(R.id.row_date);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.txtCant.setText(nf.format(Double.parseDouble(rowItem.getCosto())));
        holder.txtNom.setText(rowItem.getNombre());
        holder.txtDate.setText(rowItem.getFecha());

		return convertView;
	}
	
	@Override
	public long getHeaderId(int position) {
	    String fecha = getItem(position).getFecha();
	    long mes = Long.parseLong(fecha.substring(5, 7));
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
	    
	    int mes = Integer.parseInt(rowItem.getFecha().substring(5, 7));
	    String headerText = DateFormatSymbols.getInstance(new Locale("es","MX")).getMonths()[mes-1];
	    holder.text1.setText(headerText);
	    return convertView;
	}
}