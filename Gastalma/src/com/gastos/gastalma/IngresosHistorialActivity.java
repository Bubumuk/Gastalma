package com.gastos.gastalma;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.gastos.db.GastosDBHelper;
import com.gastos.utils.Ingreso;
import com.gastos.utils.IngresosAdapter;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.support.v4.app.NavUtils;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.database.Cursor;
import android.os.Build;

public class IngresosHistorialActivity extends SherlockActivity {

	private ListView listView;
	private GastosDBHelper dbHelper;
	private IngresosAdapter adapter;
	private Toast toast;
	private EditText editText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ingresos_historial);
		// Show the Up button in the action bar.
		setupActionBar();
		
		editText = (EditText)findViewById(R.id.inputSearch);
		
		listView = (ListView)findViewById(R.id.listView1);
		
		listView.requestFocus();
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
				Ingreso ingreso = (Ingreso) adapter.getItemAtPosition(position);
				agregarIngreso(ingreso);
				
				setResult(RESULT_OK);
				finish();
			}
		});
		
		dbHelper = new GastosDBHelper();
        dbHelper.abrirLecturaBD(this);
        
        populateListaIngresos();
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
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
	
	private void populateListaIngresos() {
		
		List<Ingreso> lista_ingresos = new ArrayList<Ingreso>();
		Cursor c = dbHelper.fetchIngresosMes(getDate());
		//Nos aseguramos de que existe al menos un registro
		if (c.moveToFirst()) {
		     //Recorremos el cursor hasta que no haya más registros
		     do {
		    	 lista_ingresos.add(new Ingreso(c.getString(0), c.getString(1), c.getString(2), c.getInt(3)));
		     } while(c.moveToNext());
		}
		
        adapter = new IngresosAdapter(this,R.layout.list_row, lista_ingresos);
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
	
	@SuppressLint("SimpleDateFormat")
	public String getMes() {
		Date cDate = new Date();
		String fDate = new SimpleDateFormat("MMMM").format(cDate);
		return fDate;
	}
	
	private void agregarIngreso(Ingreso ingreso) {
		dbHelper.insertarIngreso(
				Double.parseDouble(ingreso.getDescripcion()),
				getDate(),
				ingreso.getCantidad(),
				getTime());
		
		toast = Toast.makeText(getApplicationContext(), "Elemento agregado" , Toast.LENGTH_SHORT);
		toast.show();
		
	}
}
