package com.gastos.utils;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.emilsjolander.components.stickylistheaders.StickyListHeadersAdapter;
import com.gastos.gastalma.R;

public class ReporteGastosAñoAdapter extends ArrayAdapter<MesGastos> implements StickyListHeadersAdapter {

	Context context;
	NumberFormat nf;

	public ReporteGastosAñoAdapter(Context context, int resourceId, List<MesGastos> items) {
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
		MesGastos rowItem = getItem(position);

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

        holder.txtCant.setText(nf.format(rowItem.getCosto()));
        //holder.txtNom.setText(rowItem.getNombre());
        //holder.txtDate.setText(rowItem.getFecha());

		return convertView;
	}
	
	@Override
	public long getHeaderId(int position) {
	    long mes = getItem(position).getMes();
	    return mes;
	}

	class HeaderViewHolder {
	    TextView text1;
	}
	
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override 
	public View getHeaderView(int position, View convertView, ViewGroup parent) {
	    HeaderViewHolder holder;
	    MesGastos rowItem = getItem(position);
	    
	    LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	    
	    if (convertView == null) {
	        holder = new HeaderViewHolder();
	        convertView = mInflater.inflate(R.layout.header, parent, false);
	        holder.text1 = (TextView) convertView.findViewById(R.id.text1);
	        convertView.setTag(holder);
	    } else {
	        holder = (HeaderViewHolder) convertView.getTag();
	    }
	    
	    String headerText = rowItem.getMesString();
	    holder.text1.setText(headerText);
	    return convertView;
	}
}