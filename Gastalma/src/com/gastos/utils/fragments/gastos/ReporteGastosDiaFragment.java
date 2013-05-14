package com.gastos.utils.fragments.gastos;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.kapati.widgets.DatePicker;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.gastos.db.GastosDBHelper;
import com.gastos.gastalma.AgregarGastoActivity;
import com.gastos.gastalma.R;
import com.gastos.utils.Gasto;
import com.gastos.utils.GastosAdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;

public final class ReporteGastosDiaFragment extends SherlockFragment {
	private String fDate;
	private ListView listView;
	private GastosAdapter adapter;
	private GastosDBHelper dbHelper;
	private DatePicker text;
	private SimpleDateFormat sdf;
	private SharedPreferences prefs;

    public static ReporteGastosDiaFragment newInstance(int position) {
        ReporteGastosDiaFragment fragment = new ReporteGastosDiaFragment();
        return fragment;
    }

    @SuppressLint("SimpleDateFormat")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        dbHelper = new GastosDBHelper();
        dbHelper.abrirLecturaBD(getActivity());
        sdf = new SimpleDateFormat("dd/MM/yyyy");
		fDate = sdf.format(new Date());		
		
		setHasOptionsMenu(true);
    }

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		text = (DatePicker)inflater.inflate(R.layout.datepicker, null);
		text.setDateFormat(DateFormat.getLongDateFormat(getActivity()));
        text.setPadding(6, 6, 6, 6);
        text.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        
        text.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            	fDate = text.getDate();
            	populateListaGastosDia();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        
        View line = new View(getActivity());
        line.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 3));
        line.setBackgroundColor(0xFF3C3C3C);
        
        listView = new ListView(getActivity());
        listView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        layout.addView(text);
        //layout.addView(line);
        layout.addView(listView);
        
        listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
        
        populateListaGastosDia();
        
        registerForContextMenu(listView);

        return layout;
    }
	
	private void populateListaGastosDia() {
		dbHelper.abrirLecturaBD(getActivity());
		List<Gasto> lista_gastos = new ArrayList<Gasto>();
		Cursor c = dbHelper.fetchGastosDia(fDate);
		//Nos aseguramos de que existe al menos un registro
		if (c.moveToFirst()) {
		     //Recorremos el cursor hasta que no haya más registros
		     do {
		    	 lista_gastos.add(new Gasto(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5), c.getInt(6)));
		     } while(c.moveToNext());
		}
        
		adapter = new GastosAdapter(getActivity(), android.R.layout.simple_list_item_2, lista_gastos);
		listView.setAdapter(adapter);
		dbHelper.close();
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, com.actionbarsherlock.view.MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		
		/*menu.add(Menu.NONE, 3, Menu.NONE, "fecha")
		.setShowAsAction(com.actionbarsherlock.view.MenuItem.SHOW_AS_ACTION_IF_ROOM);*/
	}
	
	@Override
	public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
		switch (item.getItemId()) {
			case 3:
				simulateTouchEvent();
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@SuppressLint("Recycle")
	private void simulateTouchEvent() {
		// Obtain MotionEvent object
		long downTime = SystemClock.uptimeMillis();
		long eventTime = SystemClock.uptimeMillis() + 100;
		float x = 0.0f;
		float y = 0.0f;
		// List of meta states found here: developer.android.com/reference/android/view/KeyEvent.html#getMetaState()
		int metaState = 0;
		MotionEvent motionEvent = MotionEvent.obtain(
		    downTime, 
		    eventTime, 
		    MotionEvent.ACTION_UP,
		    x, 
		    y, 
		    metaState
		);

		// Dispatch touch event to view
		text.dispatchTouchEvent(motionEvent);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
	    super.onCreateContextMenu(menu, v, menuInfo);
	    MenuInflater inflater = getActivity().getMenuInflater();
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
	        case R.id.copiar:
	            copiarGasto(info.position);
	            return true;
	        default:
	            return super.onContextItemSelected(item);
	    }
	}
	
	@SuppressLint("ResourceAsColor")
	private void editarGasto(int info) {
		dbHelper.abrirEscrituraBD(getActivity());
		Intent myIntent = new Intent(getActivity(), AgregarGastoActivity.class);
		myIntent.putExtra("Gasto", bundleGasto((Gasto)listView.getItemAtPosition(info)));
		myIntent.putExtra("editar", "editar");
		startActivityForResult(myIntent, 1);
	}
	
	private void eliminarGasto(int position) {
		dbHelper.abrirEscrituraBD(getActivity());
		Toast toast;
		Gasto item = (Gasto)listView.getItemAtPosition(position);
		if(dbHelper.eliminarGasto(item.getId())) {
			toast = Toast.makeText(getActivity(), "Elemento eliminado", Toast.LENGTH_SHORT);
			toast.show();
			adapter.remove((Gasto)listView.getItemAtPosition(position));
			adapter.notifyDataSetChanged();
		} else {
			toast = Toast.makeText(getActivity(), "ERROR al eliminar elemento", Toast.LENGTH_SHORT);
			toast.show();
		}
	}
	
	private void copiarGasto(int position) {
		dbHelper.abrirEscrituraBD(getActivity());
		Toast toast;
		Gasto item = (Gasto)listView.getItemAtPosition(position);
		
		DatePicker dp1 = new DatePicker(getActivity(), null);
		
		double costo = Double.parseDouble(item.getCosto());
		String tipo = item.getTipo();
		boolean isChecked = item.getTipo().equals("Crédito") ? true : false;
		
		dp1.performClick();
		
		dbHelper.insertarGasto(
				item.getNombre(),
				dp1.getDate(),
				costo,
				item.getDescripcion(),
				tipo,
				item.getHora());
		
		if(!isChecked) {
			agregarDeuda(costo);
		}
		
		toast = Toast.makeText(getActivity(), "Elemento copiado", Toast.LENGTH_SHORT);
		toast.show();
	}
	
	private void agregarDeuda(double costo) {
		prefs = getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
		String deuda = prefs.getString("deuda", "0");
		double deuda_nueva = Double.parseDouble(deuda) + costo;
		
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("deuda", deuda_nueva + "");
		editor.commit();
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
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
	    super.onActivityResult(requestCode, resultCode, intent);
	    
	    if(resultCode == Activity.RESULT_OK) {
	    	populateListaGastosDia();
	    }
	}
}
