package com.gastos.utils.fragments.gastos;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.kapati.widgets.DatePicker;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.gastos.db.GastosDBHelper;
import com.gastos.gastalma.R;
import com.gastos.utils.Gasto;
import com.gastos.utils.GastosAdapter;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;

public final class ReporteGastosAñoFragment extends SherlockFragment {
	private String fDate;
	private ListView listView;
	private GastosAdapter adapter;
	private GastosDBHelper dbHelper;
	private DatePicker text;
	private SimpleDateFormat sdf;

    public static ReporteGastosAñoFragment newInstance(int position) {
        ReporteGastosAñoFragment fragment = new ReporteGastosAñoFragment();
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
            	populateListaGastosAño();
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
        
        populateListaGastosAño();

        return layout;
    }
	
	private void populateListaGastosAño() {
		List<Gasto> lista_gastos = new ArrayList<Gasto>();
		Cursor c = dbHelper.fetchGastosAño(fDate);
		//Nos aseguramos de que existe al menos un registro
		if (c.moveToFirst()) {
		     //Recorremos el cursor hasta que no haya más registros
		     do {
		    	 lista_gastos.add(new Gasto(c.getString(0), c.getString(1), c.getString(2), c.getString(3), fDate, c.getInt(4)));
		     } while(c.moveToNext());
		}
        
		adapter = new GastosAdapter(getActivity(), android.R.layout.simple_list_item_2, lista_gastos);
		listView.setAdapter(adapter);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		
		menu.add(Menu.NONE, 3, Menu.NONE, "fecha")
		.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
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
}