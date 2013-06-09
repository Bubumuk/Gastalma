package com.gastos.gastalma;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import net.kapati.widgets.DatePicker;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.andreabaccega.widget.FormEditText;
import com.gastos.db.GastosDBHelper;
import com.gastos.utils.DecimalDigitsInputFilter;
import com.gastos.utils.Gasto;

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
import android.content.SharedPreferences;

public class AgregarGastoActivity extends SherlockActivity {
	
	private GastosDBHelper dbHelper;
	private FormEditText txt1, txt2;
	private EditText txt3;
	private DatePicker dp1;
	private RadioButton rr;
	private Toast toast;
	private boolean editar;
	private int id;
	private SharedPreferences prefs;

	@SuppressLint("SimpleDateFormat")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agregar_gasto);
		
		editar = false;
		txt1 = (FormEditText)findViewById(R.id.editText1);
		txt2 = (FormEditText)findViewById(R.id.editText2);
		txt3 = (EditText)findViewById(R.id.editText3);
		dp1 = (DatePicker)findViewById(R.id.datePicker1);
		
		dp1.setDateFormat(DateFormat.getLongDateFormat(this));
		
		rr = (RadioButton)findViewById(R.id.radio0);
		
		txt2.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(8,2)});
		
		prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);
		
		// Show the done-discard action bar.
		setupActionBar();
		
		dbHelper = GastosDBHelper.getInstance();
        dbHelper.abrirEscrituraBD(this);
        
        if(getIntent().getExtras() != null)
	        if(getIntent().getStringExtra("editar").equals("editar")) {
	        	setUpViews(getIntent().getExtras());
	        	editar = true;
	        }
	}
	
	private void setUpViews(Bundle extras) {
	     // Again, the key should be in a constants file!
	     Bundle extraGasto = extras.getBundle("Gasto");
	 
	     // Deconstruct the Bundle into the AttendeeObject
	     Gasto gasto = unBundleGasto(extraGasto);
	     
	     String[] splitDate = gasto.getFecha().split("-");
	     int year = Integer.parseInt(splitDate[0]);
	     int month = Integer.parseInt(splitDate[1]) - 1;
	     int day = Integer.parseInt(splitDate[2]);
	 
	     // The AttendeeObject fields are now populated, so we set the texts
	     ((DatePicker) findViewById(R.id.datePicker1)).setDate(year, month, day);
	     ((FormEditText) findViewById(R.id.editText1)).setText(String.valueOf(gasto.getNombre()));
	     ((FormEditText) findViewById(R.id.editText2)).setText(gasto.getCosto());
	     if(gasto.getTipo().equals("Crédito"))
	    	 ((RadioButton)findViewById(R.id.radio1)).setChecked(true);
	     else
	    	 ((RadioButton)findViewById(R.id.radio0)).setChecked(true);
	     ((EditText) findViewById(R.id.editText3)).setText(String.valueOf(gasto.getDescripcion()));
	     id = gasto.getId();
	}
	
	public Gasto unBundleGasto(Bundle bundle){
	     Gasto gasto = new Gasto();
	     gasto.setId(bundle.getInt("id"));
	     gasto.setNombre(bundle.getString("nombre"));
	     gasto.setCosto(bundle.getString("costo"));
	     gasto.setTipo(bundle.getString("tipo"));
	     gasto.setDescripcion(bundle.getString("descripcion"));
	     gasto.setFecha(bundle.getString("fecha"));
	   
	     return gasto;
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
        						GastoActualizado();
        					else
        						GastoAgregado();
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
	
	public void GastoAgregado() {
		agregarGasto();
		
		hideKeyboard();
		setResult(Activity.RESULT_OK);
		finish();
	}
	
	private void agregarGasto() {
		dbHelper.abrirEscrituraBD(this);
		double costo = Double.parseDouble(txt2.getText().toString());
		boolean isChecked = rr.isChecked();
		String tipo = isChecked ? "Débito" : "Crédito";
		String fecha = dp1.getDate();
		java.text.DateFormat df = DateFormat.getDateFormat(this);
		Date f = null;
		try {
			f = df.parse(fecha);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		dbHelper.insertarGasto(
				txt1.getText().toString(),
				new SimpleDateFormat("yyyy-MM-dd").format(f),
				costo,
				txt3.getText().toString(),
				tipo,
				getTime());
		
		if(!isChecked) {
			agregarDeuda(Double.parseDouble(txt2.getText().toString()));
		}
		
		toast = Toast.makeText(getApplicationContext(), "Elemento agregado", Toast.LENGTH_SHORT);
		toast.show();
	}
	
	private void agregarDeuda(double costo) {
		String deuda = prefs.getString("deuda", "0");
		double deuda_nueva = Double.parseDouble(deuda) + costo;
		
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("deuda", deuda_nueva + "");
		editor.commit();
	}
	
	@SuppressLint("SimpleDateFormat")
	public String getTime() {
    	Calendar cal = Calendar.getInstance();
    	cal.getTime();
    	SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
    	return sdf.format(cal.getTime());
    }
	
	public void GastoActualizado() {
		actualizarGasto();
		
		hideKeyboard();
		setResult(RESULT_OK);
		finish();
	}
	
	private void hideKeyboard() {
		InputMethodManager inputManager = (InputMethodManager)
				getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.toggleSoftInput(0, 0);
	}
	
	private void actualizarGasto() {
		dbHelper.abrirEscrituraBD(this);
		double costo = Double.parseDouble(txt2.getText().toString());
		boolean isChecked = rr.isChecked();
		String tipo = isChecked ? "Débito" : "Crédito";
		String fecha = dp1.getDate();
		java.text.DateFormat df = DateFormat.getDateFormat(this);
		Date f = null;
		try {
			f = df.parse(fecha);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		boolean exito = dbHelper.actualizarGasto(
				txt1.getText().toString(),
				new SimpleDateFormat("yyyy-MM-dd").format(f),
				costo,
				txt3.getText().toString(),
				tipo,
				getTime(),
				id);
		
		if(!isChecked) {
			agregarDeuda(Double.parseDouble(txt2.getText().toString()));
		}

		if(exito) {
			toast = Toast.makeText(getApplicationContext(), "Elemento actualizado", Toast.LENGTH_SHORT);
			toast.show();
		} else {
			toast = Toast.makeText(getApplicationContext(), "ERROR al actualizar", Toast.LENGTH_SHORT);
			toast.show();
		}
	}
	
	public void AgregarYReiniciar() {
		agregarGasto();
		rr.setChecked(true);
		txt1.setText("");
		txt2.setText("");
		txt3.setText("");
		txt1.requestFocus();
	}
	
	public boolean validarCampos() {
        FormEditText[] allFields    = { txt1, txt2 };

        boolean allValid = true;
        for (FormEditText field: allFields) {
            allValid = field.testValidity() && allValid;
        }

        return allValid;
    }

}
