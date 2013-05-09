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

public class PagosHistorialAdapter extends ArrayAdapter<Pago> {
    
    private Context context;
    private NumberFormat nf;
    private List<Pago> pagosList;
    
    public PagosHistorialAdapter(Context context, int resourceId, List<Pago> items) {
        super(context, resourceId, items);
        this.context = context;
        this.pagosList = items;
    }
 
    private class ViewHolder {
        TextView txtCant;
        TextView txtDesc;
        TextView txtDate;
        TextView txtTime;
    }
    
    @Override
    public int getCount() {
    	return pagosList.size();
    }

    @Override
    public Pago getItem(int position) {
    	return pagosList.get(position);
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Pago rowItem = getItem(position);
        
        nf = NumberFormat.getCurrencyInstance(Locale.US);
 
        LayoutInflater mInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.pago_row, null);
            holder = new ViewHolder();
            holder.txtCant = (TextView) convertView.findViewById(R.id.row_title);
            holder.txtDesc = (TextView) convertView.findViewById(R.id.row_subtitle);
            holder.txtDate = (TextView) convertView.findViewById(R.id.row_date);
            holder.txtTime = (TextView) convertView.findViewById(R.id.row_time);
            convertView.setTag(holder);
        } else {
        	holder = (ViewHolder) convertView.getTag();
        }

        holder.txtCant.setText(nf.format(rowItem.getCantidad()));
        holder.txtDesc.setText("");
        holder.txtDate.setText(rowItem.getFecha());
        holder.txtTime.setText(rowItem.getHora());

        return convertView;
    }
    
}