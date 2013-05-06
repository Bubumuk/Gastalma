package com.gastos.utils;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.gastos.gastalma.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

public class IngresosHistorialAdapter extends ArrayAdapter<Ingreso> {
    
    private Context context;
    private NumberFormat nf;
    private List<Ingreso> ingresosList;
    private List<Ingreso> origIngresosList;
	private IngresosFilter ingresosFilter;
    
    public IngresosHistorialAdapter(Context context, int resourceId, List<Ingreso> items) {
        super(context, resourceId, items);
        this.context = context;
        this.ingresosList = items;
        this.origIngresosList = items;
    }
 
    /*private view holder class*/
    private class ViewHolder {
        TextView txtCant;
        TextView txtDesc;
        TextView txtDate;
    }
    
    @Override
    public int getCount() {
    	return ingresosList.size();
    }

    @Override
    public Ingreso getItem(int position) {
    	return ingresosList.get(position);
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

        holder.txtCant.setText(nf.format(Double.parseDouble(rowItem.getCantidad())));
        holder.txtDesc.setText(rowItem.getDescripcion());
        holder.txtDate.setText(rowItem.getFecha());

        return convertView;
    }
    
    @Override
    public Filter getFilter() {
    	if (ingresosFilter == null)
    		ingresosFilter = new IngresosFilter();

    	return ingresosFilter;
    }

    private class IngresosFilter extends Filter {

		@Override
    	protected FilterResults performFiltering(CharSequence constraint) {
    		FilterResults results = new FilterResults();
    		// We implement here the filter logic
    		if (constraint == null || constraint.length() == 0) {
    			// No filter implemented we return all the list
    			results.values = origIngresosList;
    			results.count = origIngresosList.size();
    		}
    		else {
    			// We perform filtering operation
    			List<Ingreso> nIngresosList = new ArrayList<Ingreso>();

    			for (Ingreso i : origIngresosList) {
    				String nombreGasto = i.getDescripcion().toUpperCase();
    				String constraintText = constraint.toString().toUpperCase();
    				
    				if (nombreGasto.startsWith(constraintText))
    					nIngresosList.add(i);
    			}

    			results.values = nIngresosList;
    			results.count = nIngresosList.size();

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
    			ingresosList = (List<Ingreso>) results.values;
    			notifyDataSetChanged();
    		}
    	}
    }
}