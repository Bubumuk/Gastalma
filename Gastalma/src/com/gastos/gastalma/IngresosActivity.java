package com.gastos.gastalma;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.actionbarsherlock.app.SherlockActivity;
import com.gastos.db.GastosDBHelper;
import com.gastos.utils.Ingreso;
import com.gastos.utils.IngresosAdapter;

public class IngresosActivity extends SherlockActivity {

	private final int INGRESO_AGREGADO = 1;
	private final int FECHA_SELECCIONADA = 2;
	
	private String fDate, lDate;
	private ListView listView;
	private IngresosAdapter adapter;
	private GastosDBHelper dbHelper;
	private TextView textDia;
	private Locale loc_mx;
	private Date cDate;
	
	@SuppressLint({ "SimpleDateFormat", "DefaultLocale" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ingresos);
		
		loc_mx = new Locale("es","MX");
		cDate = new Date();
		fDate = new SimpleDateFormat("MMMM", loc_mx).format(cDate);
		lDate = new SimpleDateFormat("dd/MM/yyyy", loc_mx).format(cDate);
		textDia = (TextView)findViewById(R.id.textView1);
		textDia.setText(fDate.toUpperCase());
		
		listView = (ListView)findViewById(R.id.listView1);
        
        dbHelper = new GastosDBHelper();
        dbHelper.abrirLecturaBD(this);
        
        populateListaIngresos();
		
		// Show the Up button in the action bar.
		setupActionBar();
        
        registerForContextMenu(listView);
	}
	
	private void editarIngreso(int info) {
		Intent myIntent = new Intent(this, AgregarIngresoActivity.class);
		myIntent.putExtra("Ingreso", bundleIngreso((Ingreso)listView.getItemAtPosition(info)));
		myIntent.putExtra("editar", "editar");
		startActivityForResult(myIntent, 1);
	}
	
	public Bundle bundleIngreso(Ingreso ingreso){
	     Bundle bundle = new Bundle();
	     bundle.putInt("id", ingreso.getId());
	     bundle.putString("cantidad", ingreso.getCantidad());
	     bundle.putString("descripcion", ingreso.getDescripcion());
	     bundle.putString("fecha", ingreso.getFecha());
	   
	     return bundle;
	}
	
	private void eliminarIngreso(int position) {
		Toast toast;
		Ingreso item = (Ingreso)listView.getItemAtPosition(position);
		dbHelper.abrirLecturaBD(this);
		if(dbHelper.eliminarIngreso(item.getId())) {
			toast = Toast.makeText(this, "Elemento eliminado", Toast.LENGTH_SHORT);
			toast.show();
			adapter.remove(item);
			adapter.notifyDataSetChanged();
		} else {
			toast = Toast.makeText(this, "ERROR al eliminar elemento" + item.getCantidad(), Toast.LENGTH_SHORT);
			toast.show();
		}
	}
	
	private void populateListaIngresos() {
		
		List<Ingreso> lista_ingresos = new ArrayList<Ingreso>();
		Cursor c = dbHelper.fetchIngresosMes(lDate);
		//Nos aseguramos de que existe al menos un registro
		if (c.moveToFirst()) {
		     //Recorremos el cursor hasta que no haya más registros
		     do {
		    	 lista_ingresos.add(new Ingreso(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getInt(4)));
		     } while(c.moveToNext());
		}
		
        adapter = new IngresosAdapter(this,R.layout.list_row, lista_ingresos);
        listView.setAdapter(adapter);
	}

	private void setupActionBar() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		//Used to put dark icons on light action bar

		menu.add(Menu.NONE, 3, Menu.NONE, "")
			.setIcon(R.drawable.ic_action_go_to_today)
			.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		menu.add(Menu.NONE, 2, Menu.NONE, "Historial")
			.setIcon(R.drawable.ic_action_time)
			.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		menu.add(Menu.NONE, 1, Menu.NONE, "Agregar")
			.setIcon(R.drawable.ic_action_new)
			.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		//SubMenu subMenu = menu.addSubMenu("Agregar");
		/*subMenu.add(Menu.NONE, 1, Menu.NONE, "Nuevo");
    	subMenu.add(Menu.NONE, 2, Menu.NONE, "Desde historial");*/

		//com.actionbarsherlock.view.MenuItem subMenuItem = subMenu.getItem();
		//subMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

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
			case 1:
				ViewAgregarIngresos();
	            break;
			case 2:
				ViewReporteIngresos();
				break;
			case 3:
				ViewCalendarioIngresos();
	            break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
	    super.onCreateContextMenu(menu, v, menuInfo);
	    listView.setSelected(false);
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.gastos, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
	    AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	    
	    switch (item.getItemId()) {
	        case R.id.editar:
	            editarIngreso(info.position);
	            return true;
	        case R.id.eliminar:
	            eliminarIngreso(info.position);
	            return true;
	        default:
	            return super.onContextItemSelected(item);
	    }
	}
	
	public void ViewAgregarIngresos() {
		Intent myIntent = new Intent(this, AgregarIngresoActivity.class);
		startActivityForResult(myIntent, INGRESO_AGREGADO);
	}
	
	public void ViewIngresosHistorial() {
		Intent myIntent = new Intent(this, IngresosHistorialActivity.class);
		startActivityForResult(myIntent, 0);
	}
	
	public void ViewReporteIngresos() {
		Intent myIntentI = new Intent(this, ReportesIngresosActivity.class);
		startActivityForResult(myIntentI, 0);
	}
	
	private void ViewCalendarioIngresos() {
		Intent myIntent = new Intent(this, GastosCalendarioActivity.class);
		myIntent.putExtra("dia_seleccionado", cDate.getTime());
		startActivityForResult(myIntent, FECHA_SELECCIONADA);
		//overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
	}
	
	@SuppressLint({ "SimpleDateFormat", "DefaultLocale" })
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
	    super.onActivityResult(requestCode, resultCode, intent);
	    if(resultCode == RESULT_OK) {
	    	switch(requestCode) {
	    	case INGRESO_AGREGADO:
	    		populateListaIngresos();
	    		break;
	    	case FECHA_SELECCIONADA:	    		
	    		long fecha_long = intent.getLongExtra("fecha", -1);
	    		cDate = new Date(fecha_long);
				String fecha = new SimpleDateFormat("MMMM", loc_mx).format(cDate);
	    		fDate = fecha;
	    		lDate = new SimpleDateFormat("dd/MM/yyyy", loc_mx).format(cDate);
	    		textDia.setText(fDate.toUpperCase());
	    		populateListaIngresos();
	    		break;
	    	}
	    }
	}

}

