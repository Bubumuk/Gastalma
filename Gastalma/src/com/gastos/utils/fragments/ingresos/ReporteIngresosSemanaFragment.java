package com.gastos.utils.fragments.ingresos;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import net.kapati.widgets.DatePicker;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.gastos.db.GastosDBHelper;
import com.gastos.gastalma.AgregarGastoActivity;
import com.gastos.gastalma.AgregarIngresoActivity;
import com.gastos.gastalma.GastosCalendarioActivity;
import com.gastos.gastalma.IngresosCalendarioActivity;
import com.gastos.gastalma.R;
import com.gastos.utils.Gasto;
import com.gastos.utils.GastosAdapter;
import com.gastos.utils.Ingreso;
import com.gastos.utils.IngresosAdapter;
import com.gastos.utils.fragments.ingresos.ReporteIngresosFragment;

public final class ReporteIngresosSemanaFragment extends SherlockFragment {
	private String fDate;
	private Date cDate;
	private ListView listView;
	private IngresosAdapter adapter;
	private GastosDBHelper dbHelper;
	private TextView text;
	private SimpleDateFormat sdf;
	private SharedPreferences prefs;
	private String lDate;
	private Locale loc_mx;
	private Date start;
	private Date end;
	private String lDate2;

    public static ReporteIngresosSemanaFragment newInstance(int position) {
    	ReporteIngresosSemanaFragment fragment = new ReporteIngresosSemanaFragment();
        return fragment;
    }

    @SuppressLint("SimpleDateFormat")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        dbHelper = new GastosDBHelper();
        dbHelper.abrirLecturaBD(getActivity());
        
        cDate = new Date();
        loc_mx = new Locale("es","MX");
        sdf = new SimpleDateFormat("yyyy-MM-dd");
		fDate = sdf.format(cDate);
		
		setHasOptionsMenu(true);
		
		calcularSemana();
		
		lDate = new SimpleDateFormat("EEEE, d 'de' MMMM 'de' y", loc_mx).format(end);
		lDate2 = new SimpleDateFormat("EEEE, d 'de' MMMM", loc_mx).format(start);
    }

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		//text = (DatePicker)inflater.inflate(R.layout.datepicker, null);
		//text.setDateFormat(DateFormat.getLongDateFormat(getActivity()));
		text = new TextView(getActivity());
        text.setPadding(6, 6, 6, 6);
        text.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        text.setText(lDate2 + " al " + lDate);
        text.setBackgroundResource(R.color.abs__holo_blue_light);
        /*
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
        });*/
        
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
        
        populateListaIngresosSemana();
        
        registerForContextMenu(listView);

        return layout;
    }
	
	private void calcularSemana() {

		// get today and clear time of day
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the
											// hour of day !
		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);

		// get start of this week in milliseconds
		cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());

		start = cal.getTime();

		// start of the next week
		cal.add(Calendar.DAY_OF_WEEK, 6);
		end = cal.getTime();
	}
	
	private void populateListaIngresosSemana() {
		dbHelper.abrirLecturaBD(getActivity());
		List<Ingreso> lista_ingresos = dbHelper.fetchIngresosSemana(sdf.format(start), sdf.format(end));
		
        adapter = new IngresosAdapter(getActivity(), R.layout.list_row, lista_ingresos);
        listView.setAdapter(adapter);
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void onCreateOptionsMenu(Menu menu, com.actionbarsherlock.view.MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		
		/*menu.add(Menu.NONE, 3, Menu.NONE, "fecha")
		.setShowAsAction(com.actionbarsherlock.view.MenuItem.SHOW_AS_ACTION_IF_ROOM);*/
		
		menu.add(Menu.NONE, 3, Menu.NONE, "fecha")
    	.setIcon(R.drawable.ic_action_go_to_today)
    	.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
	}
	
	@Override
	public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
		switch (item.getItemId()) {
			case 3:
				//simulateTouchEvent();
				ViewCalendarioIngresos();
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void ViewCalendarioIngresos() {
		Intent myIntent = new Intent(getActivity(), IngresosCalendarioActivity.class);
		myIntent.putExtra("dia_seleccionado", cDate.getTime());
		startActivityForResult(myIntent, 1);
		//overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
	}
	
	/*
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
	*/
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
		
		String fecha = dp1.getDate();
		java.text.DateFormat df = DateFormat.getDateFormat(getActivity());
		Date f = null;
		try {
			f = df.parse(fecha);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		dbHelper.insertarIngreso(
				cantidad,
				new SimpleDateFormat("yyyy-MM-dd").format(f),
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
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
	    super.onActivityResult(requestCode, resultCode, intent);
	    
	    if(resultCode == Activity.RESULT_OK) {
	    	long fecha_long = intent.getLongExtra("fecha", -1);
    		cDate = new Date(fecha_long);
			String fecha = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
    		fDate = fecha;
    		lDate = new SimpleDateFormat("EEEE, d 'de' MMMM 'de' y", loc_mx).format(cDate);
    		text.setText(lDate);
	    	populateListaIngresosSemana();
	    }
	}
}
