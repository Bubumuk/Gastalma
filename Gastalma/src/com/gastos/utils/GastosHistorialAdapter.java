package com.gastos.utils;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class GastosHistorialAdapter extends ArrayAdapter<Gasto> implements Filterable {
    
    private Context context;
    private NumberFormat nf;
    private List<Gasto> gastosList;
    private List<Gasto> origGastosList;
	private GastosFilter gastosFilter;
    
    public GastosHistorialAdapter(Context context, int resourceId, List<Gasto> items) {
        super(context, resourceId, items);
        this.context = context;
        this.gastosList = items;
        this.origGastosList = items;
    }
 
    /*private view holder class*/
    private class ViewHolder {
        TextView txtCant;
        TextView txtNom;
    }

    @Override
    public int getCount() {
    	return gastosList.size();
    }

    @Override
    public Gasto getItem(int position) {
    	return gastosList.get(position);
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Gasto rowItem = getItem(position);
        
        nf = NumberFormat.getCurrencyInstance(Locale.US);
 
        LayoutInflater mInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
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

    @Override
    public Filter getFilter() {
    	if (gastosFilter == null)
    		gastosFilter = new GastosFilter();

    	return gastosFilter;
    }

    private class GastosFilter extends Filter {

		@Override
    	protected FilterResults performFiltering(CharSequence constraint) {
    		FilterResults results = new FilterResults();
    		// We implement here the filter logic
    		if (constraint == null || constraint.length() == 0) {
    			// No filter implemented we return all the list
    			results.values = origGastosList;
    			results.count = origGastosList.size();
    		}
    		else {
    			// We perform filtering operation
    			List<Gasto> nGastosList = new ArrayList<Gasto>();

    			for (Gasto g : origGastosList) {
    				String nombreGasto = g.getNombre().toUpperCase();
    				String constraintText = constraint.toString().toUpperCase();
    				
    				if (nombreGasto.startsWith(constraintText))
    					nGastosList.add(g);
    			}

    			results.values = nGastosList;
    			results.count = nGastosList.size();

    		}
    		return results;
    	}

    	@SuppressWarnings("unchecked")
		@Override
    	protected void publishResults(CharSequence constraint,
    			FilterResults results) {

    		// Now we have to inform the adapter about the new list filtered
    		if (results.count == 0)
    			notifyDataSetInvalidated();
    		else {
    			gastosList = (List<Gasto>) results.values;
    			notifyDataSetChanged();
    		}
    	}
    }
}