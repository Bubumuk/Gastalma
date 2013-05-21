package com.gastos.utils.fragments.ingresos;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import net.kapati.widgets.DatePicker;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.gastos.db.GastosDBHelper;
import com.gastos.gastalma.AgregarIngresoActivity;
import com.gastos.gastalma.R;
import com.gastos.utils.Ingreso;
import com.gastos.utils.IngresosAdapter;

public final class ReporteIngresosMesFragment extends SherlockFragment {
	private String fDate;
	private ListView listView;
	private IngresosAdapter adapter;
	private GastosDBHelper dbHelper;
	private DatePicker text;
	private SimpleDateFormat sdf;

    public static ReporteIngresosMesFragment newInstance(int position) {
        ReporteIngresosMesFragment fragment = new ReporteIngresosMesFragment();
        return fragment;
    }

    @SuppressLint("SimpleDateFormat")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        dbHelper = new GastosDBHelper();
        dbHelper.abrirLecturaBD(getActivity());
        sdf = new SimpleDateFormat("yyyy-MM-dd");
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
            	populateListaIngresosMes();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        
        listView = new ListView(getActivity());
        listView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        layout.addView(text);
        layout.addView(listView);

        populateListaIngresosMes();
        
        registerForContextMenu(listView);

        return layout;
    }
	
	private void populateListaIngresosMes() {
		dbHelper.abrirLecturaBD(getActivity());
		List<Ingreso> lista_ingresos = dbHelper.fetchIngresosMes(fDate);
		
        adapter = new IngresosAdapter(getActivity(), R.layout.list_row, lista_ingresos);
        listView.setAdapter(adapter);
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
	            editarIngreso(info.position);
	            return true;
	        case R.id.eliminar:
	            eliminarIngreso(info.position);
	            return true;
	        case R.id.copiar:
	            copiarIngreso(info.position);
	            return true;
	        default:
	            return super.onContextItemSelected(item);
	    }
	}
	
	@SuppressLint("ResourceAsColor")
	private void editarIngreso(int info) {
		Intent myIntent = new Intent(getActivity(), AgregarIngresoActivity.class);
		myIntent.putExtra("Ingreso", bundleIngreso((Ingreso)listView.getItemAtPosition(info)));
		myIntent.putExtra("editar", "editar");
		startActivityForResult(myIntent, 1);
	}
	
	private void eliminarIngreso(int position) {
		Toast toast;
		Ingreso item = (Ingreso)listView.getItemAtPosition(position);
		if(dbHelper.eliminarIngreso(item.getId())) {
			toast = Toast.makeText(getActivity(), "Elemento eliminado", Toast.LENGTH_SHORT);
			toast.show();
			adapter.remove((Ingreso)listView.getItemAtPosition(position));
			adapter.notifyDataSetChanged();
		} else {
			toast = Toast.makeText(getActivity(), "ERROR al eliminar elemento", Toast.LENGTH_SHORT);
			toast.show();
		}
	}
	
	private void copiarIngreso(int position) {
		Toast toast;
		Ingreso item = (Ingreso)listView.getItemAtPosition(position);
		
		DatePicker dp1 = new DatePicker(getActivity(), null);
		
		double cantidad = Double.parseDouble(item.getCantidad());
		
		dp1.performClick();
		
		dbHelper.insertarIngreso(
				cantidad,
				dp1.getDate(),
				item.getDescripcion(),
				item.getHora());
		
		toast = Toast.makeText(getActivity(), "Elemento copiado", Toast.LENGTH_SHORT);
		toast.show();
	}

	public Bundle bundleIngreso(Ingreso ingreso){
	     Bundle bundle = new Bundle();
	     bundle.putInt("id", ingreso.getId());
	     bundle.putString("cantidad", ingreso.getCantidad());
	     bundle.putString("descripcion", ingreso.getDescripcion());
	     bundle.putString("fecha", ingreso.getFecha());
	   
	     return bundle;
	}
}
