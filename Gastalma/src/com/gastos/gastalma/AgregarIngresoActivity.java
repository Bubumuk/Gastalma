package com.gastos.gastalma;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import net.kapati.widgets.DatePicker;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.andreabaccega.widget.FormEditText;
import com.gastos.db.GastosDBHelper;
import com.gastos.utils.DecimalDigitsInputFilter;
import com.gastos.utils.Ingreso;

import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import android.text.InputFilter;
import android.text.format.DateFormat;
import android.annotation.SuppressLint;
import android.content.Context;

public class AgregarIngresoActivity extends SherlockActivity {

	//private SQLiteDatabase db;
	private GastosDBHelper dbHelper;
	private FormEditText txt2;
	private EditText txt3;
	private DatePicker dp1;
	private RadioButton rr;
	private Toast toast;
	private boolean editar;
	private int id;
	
	@SuppressLint("SimpleDateFormat")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agregar_ingreso);
		// Show the Up button in the action bar.
		setupActionBar();
		
		editar = false;
		txt2 = (FormEditText)findViewById(R.id.editText2);
		txt3 = (EditText)findViewById(R.id.editText3);
		dp1 = (DatePicker)findViewById(R.id.datePicker1);
		
		dp1.setDateFormat(DateFormat.getLongDateFormat(this));
		
		txt2.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(8,2)});
		
		dbHelper = new GastosDBHelper();
        dbHelper.abrirEscrituraBD(this);
        
        if(getIntent().getExtras() != null)
            if(getIntent().getStringExtra("editar").equals("editar")) {
            	setUpViews(getIntent().getExtras());
            	editar = true;
            }
        
        txt2.requestFocus();                
	}
	
	private void setUpViews(Bundle extras) {
	     // Again, the key should be in a constants file!
	     Bundle extraIngreso = extras.getBundle("Ingreso");
	 
	     // Deconstruct the Bundle into the AttendeeObject
	     Ingreso ingreso = unBundleIngreso(extraIngreso);
	 
	     // The AttendeeObject fields are now populated, so we set the texts
	     ((FormEditText) findViewById(R.id.editText2)).setText(String.valueOf(ingreso.getCantidad()));
	     ((EditText) findViewById(R.id.editText3)).setText(String.valueOf(ingreso.getDescripcion()));
	     id = ingreso.getId();
	}
	
	public Ingreso unBundleIngreso(Bundle bundle){
		Ingreso ingreso = new Ingreso();
		ingreso.setId(bundle.getInt("id"));
		ingreso.setCantidad(bundle.getString("cantidad"));
		ingreso.setDescripcion(bundle.getString("descripcion"));
		ingreso.setFecha(bundle.getString("fecha"));
	   
	    return ingreso;
	}

	private void setupActionBar() {
		// Inflate a "Done/Discard" custom action bar view.
        LayoutInflater inflater = (LayoutInflater) getSupportActionBar().getThemedContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        final View customActionBarView = inflater.inflate(R.layout.actionbar_custom_view_done_discard, null);
        customActionBarView.findViewById(R.id.actionbar_done).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // "Done"
                    	if(validarCampos())
        					if(editar)
        						IngresoActualizado();
        					else
        						IngresoAgregado();
                    }
                });
        customActionBarView.findViewById(R.id.actionbar_discard).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // "Discard"
                    	hideKeyboard();
                    	
                    	setResult(Activity.RESULT_CANCELED);
        				finish();
                    }
                });

        // Show the custom action bar view and hide the normal Home icon and title.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM,
                ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setCustomView(customActionBarView, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
	}
	
	public void IngresoAgregado() {
		agregarIngreso();

		hideKeyboard();
		
		setResult(RESULT_OK);
		finish();
	}
	
	public void IngresoActualizado() {
		actualizarIngreso();
		
		hideKeyboard();
		
		setResult(RESULT_OK);
		finish();
	}
	
	private void hideKeyboard() {
		InputMethodManager inputManager = (InputMethodManager)
				getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.toggleSoftInput(0, 0);
	}
	
	private void actualizarIngreso() {
		dbHelper.actualizarIngreso(
				txt2.getText().toString(),
				txt3.getText().toString(),
				dp1.getDate(),
				id);
		
		toast = Toast.makeText(getApplicationContext(), "Elemento actualizado", Toast.LENGTH_SHORT);
		toast.show();
	}
	
	private void agregarIngreso() {
		dbHelper.insertarIngreso(
				Double.parseDouble(txt2.getText().toString()),
				dp1.getDate(),
				txt3.getText().toString(),
				getTime());
		
		toast = Toast.makeText(getApplicationContext(), "Elemento agregado" , Toast.LENGTH_SHORT);
		toast.show();
		
	}
	
	public void AgregarYReiniciar(View view) {
		agregarIngreso();
		rr.setChecked(true);
		txt2.setText("");
		txt3.setText("");
		txt2.requestFocus();
	}
	
	public boolean validarCampos() {
        FormEditText[] allFields    = { txt2 };

        boolean allValid = true;
        for (FormEditText field: allFields) {
            allValid = field.testValidity() && allValid;
        }
        
        return allValid;
    }
	
	@SuppressLint("SimpleDateFormat")
	public String getTime() {
    	Calendar cal = Calendar.getInstance();
    	cal.getTime();
    	SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
    	return sdf.format(cal.getTime());
    }

}
