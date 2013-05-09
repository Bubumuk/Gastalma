package com.gastos.gastalma;

import java.util.ArrayList;
import java.util.List;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.gastos.db.GastosDBHelper;
import com.gastos.utils.Pago;
import com.gastos.utils.PagosHistorialAdapter;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.widget.ListView;

public class PagosHistorialActivity extends SherlockActivity {

	private ListView listView;
	private GastosDBHelper dbHelper;
	private PagosHistorialAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pagos_historial);
		// Show the Up button in the action bar.
		setupActionBar();
		
		listView = (ListView)findViewById(R.id.listView1);
		
		listView.setEmptyView(findViewById(R.id.textViewEmpty));
		
		dbHelper = new GastosDBHelper();
        dbHelper.abrirLecturaBD(this);
        
        populateListaPagos();
	}

	private void populateListaPagos() {
		List<Pago> lista_pagos = new ArrayList<Pago>();
		Cursor c = dbHelper.fetchPagos();
		//Nos aseguramos de que existe al menos un registro
		if (c.moveToFirst()) {
		     //Recorremos el cursor hasta que no haya más registros
		     do {
		    	 lista_pagos.add(new Pago(c.getDouble(0), c.getString(1), c.getString(2)));
		     } while(c.moveToNext());
		}
		
        adapter = new PagosHistorialAdapter(this, R.layout.list_row, lista_pagos);
        listView.setAdapter(adapter);
	}

	private void setupActionBar() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
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

}
