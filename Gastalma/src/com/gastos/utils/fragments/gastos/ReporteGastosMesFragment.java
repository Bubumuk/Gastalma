package com.gastos.utils.fragments.gastos;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
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

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.emilsjolander.components.stickylistheaders.StickyListHeadersListView;
import com.gastos.db.GastosDBHelper;
import com.gastos.gastalma.GastosCalendarioMesActivity;
import com.gastos.gastalma.R;
import com.gastos.utils.MesGastos;
import com.gastos.utils.ReporteGastosMesAdapter;

public final class ReporteGastosMesFragment extends SherlockFragment {
	private String fDate;
	private Date cDate;
	private ListView listView;
	private ReporteGastosMesAdapter adapter;
	private GastosDBHelper dbHelper;
	private TextView text;
	private SimpleDateFormat sdf;
	private String lDate;
	private Locale loc_mx;

    public static ReporteGastosMesFragment newInstance(int position) {
        ReporteGastosMesFragment fragment = new ReporteGastosMesFragment();
        return fragment;
    }

    @SuppressLint("SimpleDateFormat")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        dbHelper = GastosDBHelper.getInstance();
        dbHelper.abrirLecturaBD(getActivity());
        
        cDate = new Date();
        loc_mx = new Locale("es","MX");
        sdf = new SimpleDateFormat("yyyy-MM-dd");
		fDate = sdf.format(cDate);
		lDate = new SimpleDateFormat("MMMM", loc_mx).format(cDate);
		
		setHasOptionsMenu(true);
    }

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		text = new TextView(getActivity());
        text.setPadding(6, 6, 6, 6);
        text.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        text.setText(lDate);
        text.setBackgroundResource(R.color.abs__holo_blue_light);
        
        View line = new View(getActivity());
        line.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 3));
        line.setBackgroundColor(0xFF3C3C3C);
        
        listView = new StickyListHeadersListView(getActivity());
        listView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        layout.addView(text);
        layout.addView(listView);
        
        listView.setOnItemClickListener(new OnItemClickListener() {

			@TargetApi(Build.VERSION_CODES.GINGERBREAD)
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//TODO: Crear click event
			}
		});
        
        populateListaGastosMes();
        
        registerForContextMenu(listView);

        return layout;
    }
	
	private void populateListaGastosMes() {
		dbHelper.abrirLecturaBD(getActivity());
		List<MesGastos> lista_gastos = dbHelper.fetchGastosMesPorDia(fDate);
        
		adapter = new ReporteGastosMesAdapter(getActivity(), android.R.layout.simple_list_item_2, lista_gastos);
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
				ViewCalendarioGastos();
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void ViewCalendarioGastos() {
		Intent myIntent = new Intent(getActivity(), GastosCalendarioMesActivity.class);
		myIntent.putExtra("mes_seleccionado", cDate.getMonth());
		startActivityForResult(myIntent, 1);
		//overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
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
	        /*case R.id.editar:
	            editarGasto(info.position);
	            return true;
	        case R.id.eliminar:
	            eliminarGasto(info.position);
	            return true;
	        case R.id.copiar:
	            copiarGasto(info.position);
	            return true;*/
	        default:
	            return super.onContextItemSelected(item);
	    }
	}
	
	@SuppressLint("SimpleDateFormat")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
	    super.onActivityResult(requestCode, resultCode, intent);
	    
	    if(resultCode == Activity.RESULT_OK) {
	    	int mes = intent.getIntExtra("mes", -1);
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.clear(Calendar.MINUTE);
			cal.clear(Calendar.SECOND);
			cal.clear(Calendar.MILLISECOND);

			cal.set(Calendar.MONTH, mes);

			cDate = cal.getTime();
			String fecha = new SimpleDateFormat("yyyy-MM-dd").format(cDate);

			fDate = fecha;
			lDate = new SimpleDateFormat("MMMM", loc_mx).format(cDate);
			text.setText(lDate);
    		populateListaGastosMes();
	    }
	}
}
