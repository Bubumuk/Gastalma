package com.gastos.gastalma;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.SubMenu;
import com.gastos.db.GastosDBHelper;
import com.gastos.utils.Gasto;
import com.gastos.utils.GastosAdapter;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.support.v4.app.NavUtils;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;

public class GastosActivity extends SherlockActivity {
	
	private String fDate, lDate;
	private ListView listView;
	private GastosAdapter adapter;
	private GastosDBHelper dbHelper;

	@SuppressLint({ "SimpleDateFormat", "DefaultLocale" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gastos);
		
		Locale loc = new Locale("es","MX");
		
		Date cDate = new Date();
		fDate = new SimpleDateFormat("dd/MM/yyyy").format(cDate);
		lDate = new SimpleDateFormat("EEEE, d 'de' MMMM 'de' y", loc).format(cDate);
		TextView t = (TextView)findViewById(R.id.textView1);
		t.setText(lDate);
		
		listView = (ListView)findViewById(R.id.listView1);
        
        dbHelper = new GastosDBHelper();
        dbHelper.abrirLecturaBD(this);
        
        populateListaGastos();
		
		// Show the Up button in the action bar.
		setupActionBar();
        
        registerForContextMenu(listView);
        
        listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				AlertDialog.Builder builder = new AlertDialog.Builder(GastosActivity.this);
				Gasto gasto = (Gasto)parent.getItemAtPosition(position);
				
				builder.setTitle(gasto.getNombre())
					.setMessage(gasto.getDescripcion())
					.setPositiveButton("Aceptar", new OnClickListener() {
				        public void onClick(DialogInterface dialog, int which) {
				            dialog.cancel();
				        }
				    });
				
				AlertDialog dialog = builder.create();
				dialog.show();
			}
		});
	}
	
	private void populateListaGastos() {
		List<Gasto> lista_gastos = new ArrayList<Gasto>();
		Cursor c = dbHelper.fetchGastosDia(fDate);
		//Nos aseguramos de que existe al menos un registro
		if (c.moveToFirst()) {
		     //Recorremos el cursor hasta que no haya más registros
		     do {
		    	 lista_gastos.add(new Gasto(c.getString(0), c.getString(1), c.getString(2), c.getString(3), fDate, c.getInt(4)));
		     } while(c.moveToNext());
		}
        
		adapter = new GastosAdapter(this, android.R.layout.simple_list_item_2, lista_gastos);
		listView.setAdapter(adapter);
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		//Used to put dark icons on light action bar

		SubMenu subMenu = menu.addSubMenu("Agregar");
        subMenu.add(Menu.NONE, 1, Menu.NONE, "Nuevo");
        subMenu.add(Menu.NONE, 2, Menu.NONE, "Desde historial");

        com.actionbarsherlock.view.MenuItem subMenuItem = subMenu.getItem();
        subMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        
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
				ViewAgregarGastos();
	            break;
			case 2:
				ViewGastosHistorial();
	            break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
	    super.onCreateContextMenu(menu, v, menuInfo);
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.gastos, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
	    AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	    switch (item.getItemId()) {
	        case R.id.editar:
	            editarGasto(info.position);
	            return true;
	        case R.id.eliminar:
	            eliminarGasto(info.position);
	            return true;
	        default:
	            return super.onContextItemSelected(item);
	    }
	}
	
	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	@SuppressLint("ResourceAsColor")
	private void editarGasto(int info) {
		Intent myIntent = new Intent(this, AgregarGastoActivity.class);
		myIntent.putExtra("Gasto", bundleGasto((Gasto)listView.getItemAtPosition(info)));
		myIntent.putExtra("editar", "editar");
		startActivityForResult(myIntent, 1);
	}
	
	private void eliminarGasto(int position) {
		Toast toast;
		Gasto item = (Gasto)listView.getItemAtPosition(position);
		dbHelper.abrirLecturaBD(this);
		if(dbHelper.eliminarGasto(item.getId())) {
			toast = Toast.makeText(getApplicationContext(), "Elemento eliminado", Toast.LENGTH_SHORT);
			toast.show();
			adapter.remove((Gasto)listView.getItemAtPosition(position));
			adapter.notifyDataSetChanged();
		} else {
			toast = Toast.makeText(getApplicationContext(), "ERROR al eliminar elemento", Toast.LENGTH_SHORT);
			toast.show();
		}
	}
	
	public void ViewAgregarGastos() {
		Intent myIntent = new Intent(this, AgregarGastoActivity.class);
		startActivityForResult(myIntent, 0);
	}
	
	public void ViewGastosHistorial() {
		Intent myIntent = new Intent(this, GastosHistorialActivity.class);
		startActivityForResult(myIntent, 0);
	}
	
	public Bundle bundleGasto(Gasto gasto){
	     Bundle bundle = new Bundle();
	     bundle.putInt("id", gasto.getId());
	     bundle.putString("nombre", gasto.getNombre());
	     bundle.putString("costo", gasto.getCosto());
	     bundle.putString("tipo", gasto.getTipo());
	     bundle.putString("descripcion", gasto.getDescripcion());
	     bundle.putString("fecha", gasto.getFecha());
	   
	     return bundle;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
	    super.onActivityResult(requestCode, resultCode, intent);
	    
	    if(resultCode == RESULT_OK) {
	    	populateListaGastos();
	    }
	}

}