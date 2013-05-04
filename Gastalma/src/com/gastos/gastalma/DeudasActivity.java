package com.gastos.gastalma;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.actionbarsherlock.app.SherlockActivity;
import com.gastos.db.GastosDBHelper;
import com.gastos.utils.CroutonAlert;

import de.neofonie.mobile.app.android.widget.crouton.Crouton;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
	SharedPreferences prefs;
	DateTime dt, dt_pago;
	double pago_min;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_deudas);
		// Show the Up button in the action bar.
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
        
        calcularPago();
	}
	
	private void calcularPago() {
		deuda = prefs.getString("deuda", dbHelper.fetchDeuda());
        
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
		//Used to put dark icons on light action bar


		menu.add(Menu.NONE, 1, Menu.NONE, "pagar")
		.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		
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
				/*
				if(deudaDouble > 0) {
					pagarDeuda();
				} else {
					Toast.makeText(this, "No hay deudas .D", Toast.LENGTH_SHORT).show();
				}
				*/
				Crouton.makeText(
						this, 
						CroutonAlert.alert_string, 
						CroutonAlert.alertStyle())
						.show();
	            break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void pagarDeuda() {
		
		final AlertDialog editalert = new AlertDialog.Builder(this)
			.setTitle("Pagar deuda")
			.setMessage("Cantidad mínima por pagar:")
			.setView(pagar_view)
			.setPositiveButton("Aceptar", null)
			.create();
		
		editalert.setOnShowListener(new DialogInterface.OnShowListener() {

		    @Override
		    public void onShow(DialogInterface dialog) {

		        Button b = editalert.getButton(AlertDialog.BUTTON_POSITIVE);
		        b.setOnClickListener(new View.OnClickListener() {

		            @Override
		            public void onClick(View view) {
		            	//Set new deuda to prefs
		            	SharedPreferences.Editor editor = prefs.edit();
		            	
		            	double pago = Double.parseDouble(input.getText().toString());
		            	double deuda_nueva = deudaDouble - pago;
		            	if(deuda_nueva < 0) {
		            		//set error
		            		input.setError("Número negativo");
		            	} else {
		            		editor.putString("deuda", deudaDouble + "");
			                editor.commit();
			                calcularPago();
			                
			                //Dismiss once everything is OK.
			                editalert.dismiss();
		            	}
		            }
		        });
		    }
		});

		chmin.setOnCheckedChangeListener(
				new CheckBox.OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							input.setText(pago_min + "");
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
