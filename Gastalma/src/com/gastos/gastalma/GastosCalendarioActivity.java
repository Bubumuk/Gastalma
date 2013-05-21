package com.gastos.gastalma;

import java.util.Calendar;
import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.View;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.squareup.timessquare.CalendarPickerView;

public class GastosCalendarioActivity extends SherlockActivity {

	private CalendarPickerView calendar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gastos_calendario);
		
		// Show the Up button in the action bar.
		setupActionBar();
		
		final Calendar lastYear = Calendar.getInstance();
		lastYear.add(Calendar.YEAR, -1);
		final Calendar maxDate = Calendar.getInstance();
		maxDate.add(Calendar.DAY_OF_MONTH, 1);
		
		long fecha = getIntent().getLongExtra("dia_seleccionado", -1);
		Date cDate = new Date(fecha);

		calendar = (CalendarPickerView) findViewById(R.id.calendar_view);
		calendar.init(cDate, lastYear.getTime(), maxDate.getTime());
		//calendar.selectDate(cDate);
	}

	public void returnFecha(View view) {
		Intent intent = new Intent();
		intent.putExtra("fecha", calendar.getSelectedDate().getTime());
		setResult(RESULT_OK, intent);
		finish();
	}
	
	private void setupActionBar() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, 1, Menu.NONE, "Hoy")
			.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
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
			calendar.selectDate(new Date());
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}
