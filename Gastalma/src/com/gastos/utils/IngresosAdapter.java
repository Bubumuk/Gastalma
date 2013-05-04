package com.gastos.utils;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import com.gastos.gastalma.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class IngresosAdapter extends ArrayAdapter<Ingreso> {
    
    Context context;
    NumberFormat nf;
    
    public IngresosAdapter(Context context, int resourceId, List<Ingreso> items) {
        super(context, resourceId, items);
        this.context = context;
    }
 
    /*private view holder class*/
    private class ViewHolder {
        TextView txtCant;
        TextView txtDesc;
        TextView txtDate;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Ingreso rowItem = getItem(position);
        
        nf = NumberFormat.getCurrencyInstance(Locale.US);
 
        LayoutInflater mInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_row, null);
            holder = new ViewHolder();
            holder.txtCant = (TextView) convertView.findViewById(R.id.row_title);
            holder.txtDesc = (TextView) convertView.findViewById(R.id.row_subtitle);
            holder.txtDate = (TextView) convertView.findViewById(R.id.row_date);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
 
        //if(rowItem != null) {
	        holder.txtCant.setText(nf.format(Double.parseDouble(rowItem.getCantidad())));
	        holder.txtDesc.setText(rowItem.getDescripcion());
	        holder.txtDate.setText(rowItem.getFecha());
        //}
 
        return convertView;
    }
}