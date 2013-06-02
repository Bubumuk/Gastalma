package com.gastos.gastalma;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.actionbarsherlock.app.SherlockActivity;
import com.gastos.db.GastosDBHelper;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

public class DeudasActivity extends SherlockActivity {

	private GastosDBHelper dbHelper;
	private DecimalFormat nf;
	private TextView txt2, txt4, txt5, txt6, txt7;
	private EditText input;
	private CheckBox chmin;
	private String deuda, dia;
	private double deudaDouble;
	private int porciento;
	private View pagar_view;
	private SharedPreferences prefs;
	private DateTime dt, dt_pago;
	private double pago_min;
	private AlertDialog editalert;
	private Toast toast;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_deudas);
		
		setupActionBar();
		
		dbHelper = new GastosDBHelper();
        dbHelper.abrirLecturaBD(this);
        
        nf = (DecimalFormat)NumberFormat.getCurrencyInstance(Locale.US);
        nf.setNegativePrefix("-"+nf.getCurrency().getSymbol());
        
        pagar_view = getLayoutInflater().inflate(R.layout.pagar, null);
        
        txt2 = (TextView)findViewById(R.id.textView2);
        txt4 = (TextView)findViewById(R.id.textView4);
        txt5 = (TextView)findViewById(R.id.textView5);
        txt6 = (TextView)findViewById(R.id.textView6);
        txt7 = (TextView)findViewById(R.id.textView7);
        
        input = (EditText)pagar_view.findViewById(R.id.txtpago);
		chmin = (CheckBox)pagar_view.findViewById(R.id.chminimo);
        
        dt = new DateTime();
        
        prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        dia = prefs.getString("dia_pago", "24");
        porciento = prefs.getInt("porciento_pago", 15);
        
        editalert = new AlertDialog.Builder(this)
		.setTitle("Pagar deuda")
		.setMessage("Cantidad mínima por pagar:")
		.setView(pagar_view)
		.setPositiveButton("Aceptar", null)
		.setNegativeButton("Cancelar", null)
		.create();
        
        calcularPago();
	}
	
	private void calcularPago() {
		deuda = prefs.getString("deuda", "0");
        
        txt2.setText(nf.format(Double.parseDouble(deuda)));
        
        txt6.setText("Por pagar: (" + porciento + "%)");
        
        dt_pago = new DateTime(dt.getYear(), dt.getMonthOfYear(), Integer.parseInt(dia), 0, 0);
        
        //Ver si la fecha se pasa al siguiente mes
        dt_pago = calcularMesDePago(dt_pago, DateTime.now());
        String fecha_pago = 
        		(dt_pago.getYear() == DateTime.now().getYear() && dt_pago.getDayOfYear() == DateTime.now().getDayOfYear())
        		? "HOY"
        		: formatoFecha(dt_pago);
        
        txt4.setText(fecha_pago);
        
        txt5.setText("Faltan " + calcularDias(dt_pago) + " dias");
        Double porcentaje = (double) (porciento);
        porcentaje /= 100;
        deudaDouble = Double.parseDouble(deuda);
        pago_min = deudaDouble * porcentaje;
        txt7.setText(nf.format(pago_min));
	}
	
	private int calcularDias(DateTime dia_pago) {
		return Days.daysBetween(DateTime.now(), dia_pago).getDays();
	}
	
	private DateTime calcularMesDePago(DateTime dia_pago, DateTime hoy) {
		if(hoy.getDayOfMonth() > dia_pago.getDayOfMonth()) {
			// Se pasa al siguiente mes
			return dia_pago.plusMonths(1);
		}
		return dia_pago;
	}
	
	private String formatoFecha(DateTime fecha) {
		Locale loc = new Locale("es","MX");
		DateTimeFormatter fmt = DateTimeFormat.forPattern("d 'de' MMMM 'de' y").withLocale(loc);
        return fmt.print(fecha);
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {

		menu.add(Menu.NONE, 1, Menu.NONE, "Historial")
			.setIcon(R.drawable.ic_action_time)
			.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		
		if(deudaDouble > 0) {
			menu.add(Menu.NONE, 2, Menu.NONE, "pagar")
			.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		}
		
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
				ViewHistorialPagos();
				break;
			case 2:
				pagarDeuda();
	            break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void ViewHistorialPagos() {
		Intent myIntentP = new Intent(this, PagosHistorialActivity.class);
		startActivity(myIntentP);
	}

	@SuppressLint("SimpleDateFormat")
	public String getTime() {
    	Calendar cal = Calendar.getInstance();
    	cal.getTime();
    	SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
    	return sdf.format(cal.getTime());
    }
	
	@SuppressLint("SimpleDateFormat")
	public String getDate() {
		Date cDate = new Date();
		String fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
		return fDate;
	}
	
	private void agregarPago(double cantidad) {
		dbHelper.insertarPago(cantidad, getDate(), getTime());
		
		toast = Toast.makeText(getApplicationContext(), "Elemento agregado", Toast.LENGTH_SHORT);
		toast.show();
	}
	
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	private void pagarDeuda() {
		
		editalert.setOnShowListener(new DialogInterface.OnShowListener() {

		    @Override
		    public void onShow(DialogInterface dialog) {
		    	
		    	input.setText(String.format("%.2f", pago_min));
		    	chmin.setChecked(true);

		        Button b = editalert.getButton(AlertDialog.BUTTON_POSITIVE);
		        b.setOnClickListener(new View.OnClickListener() {

		            @Override
		            public void onClick(View view) {
		            	//Set new deuda to prefs
		            	
		            	String pagoStr = input.getText().toString();

		            	if(!pagoStr.isEmpty()) {
		            		SharedPreferences.Editor editor = prefs.edit();

		            		double pago = Double.parseDouble(input.getText().toString());
		            		double deuda_nueva = deudaDouble - pago;
		            		
		            		editor.putString("deuda", deuda_nueva + "");
		            		editor.commit();
		            		calcularPago();
		            		
		            		agregarPago(pago);

		            		//Dismiss once everything is OK.
		            		editalert.dismiss();
		            	}
		            }
		        });
		        
		        Button c = editalert.getButton(AlertDialog.BUTTON_NEGATIVE);
		        c.setOnClickListener(new View.OnClickListener() {

		            @Override
		            public void onClick(View view) {
		            	//Dismiss once everything is OK.
		            	editalert.dismiss();
		            }
		        });
		    }
		});

		chmin.setOnCheckedChangeListener(
				new CheckBox.OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						if (isChecked) {
							input.setText(String.format("%.2f", pago_min));
							input.setEnabled(false);
						}
						else {
							input.setText("");
							input.setEnabled(true);
						}
					}
				});
		
		editalert.show();
	}

}
