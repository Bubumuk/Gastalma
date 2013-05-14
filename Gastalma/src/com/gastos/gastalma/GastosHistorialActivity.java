package com.gastos.gastalma;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.OnQueryTextListener;
import com.gastos.db.GastosDBHelper;
import com.gastos.utils.Gasto;
import com.gastos.utils.GastosHistorialAdapter;

import android.os.Build;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.database.Cursor;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class GastosHistorialActivity extends SherlockActivity {

	private GastosDBHelper dbHelper;
	private ListView listView;
	private ArrayAdapter<Gasto> adapter;

	private Toast toast;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gastos_historial);
		// Show the Up button in the action bar.
		setupActionBar();
		
		listView = (ListView)findViewById(R.id.listView1);
		
		listView.setEmptyView(findViewById(R.id.textViewEmpty));
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
				Gasto gasto = (Gasto) adapter.getItemAtPosition(position);
				agregarGasto(gasto);
				
				setResult(RESULT_OK);
				finish();
			}
		});
		
		dbHelper = new GastosDBHelper();
        dbHelper.abrirLecturaBD(this);
        
        populateListaGastos();
	}

	private void setupActionBar() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //Create the search view
        SearchView searchView = new SearchView(getSupportActionBar().getThemedContext());
        searchView.setQueryHint("Buscar..");

        menu.add("Search")
            .setIcon(R.drawable.abs__ic_search)
            .setActionView(searchView)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        
        searchView.setOnQueryTextListener(new OnQueryTextListener() {
			
			@Override
			public boolean onQueryTextSubmit(String query) {
				return false;
			}
			
			@Override
			public boolean onQueryTextChange(String newText) {
				adapter.getFilter().filter(newText);
				return false;
			}
		});

        return true;
    }
	
	@Override
	public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				// This ID represents the Home or Up button. In the case of this
				// activity, the Up button is shown. Use NavUtils to allow users
				// to navigate up one level in the application structure. For
				// more details, see the Navigation pattern on Android Design:
				//
				// http://developer.android.com/design/patterns/navigation.html#up-vs-back
				//
				NavUtils.navigateUpFromSameTask(this);
			
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void populateListaGastos() {
		List<Gasto> lista_gastos = new ArrayList<Gasto>();
		Cursor c = dbHelper.fetchGastosHistorial();
		//Nos aseguramos de que existe al menos un registro
		if (c.moveToFirst()) {
		     //Recorremos el cursor hasta que no haya más registros
		     do {
		    	 lista_gastos.add(new Gasto(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5), c.getInt(6)));
		     } while(c.moveToNext());
		}
		
		adapter = new GastosHistorialAdapter(this, R.layout.list_row, lista_gastos);
		listView.setAdapter(adapter);
	}
	
	@SuppressLint("SimpleDateFormat")
	public String getTime() {
    	Calendar cal = Calendar.getInstance();
    	cal.getTime();
    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss a");
    	return sdf.format(cal.getTime());
    }
	
	@SuppressLint("SimpleDateFormat")
	public String getDate() {
		Date cDate = new Date();
		String fDate = new SimpleDateFormat("dd/MM/yyyy").format(cDate);
		return fDate;
	}
	
	private void agregarGasto(Gasto gasto) {
		
		dbHelper.insertarGasto(
				gasto.getNombre(),
				getDate(),
				Double.parseDouble(gasto.getCosto()),
				gasto.getDescripcion(),
				gasto.getTipo(),
				getTime());
		
		toast = Toast.makeText(getApplicationContext(), "Elemento agregado", Toast.LENGTH_SHORT);
		toast.show();
	}

}
