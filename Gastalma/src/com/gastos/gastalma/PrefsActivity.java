package com.gastos.gastalma;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.gastos.utils.SeekBarPreference;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Build;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.TwoStatePreference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.support.v4.app.NavUtils;

public class PrefsActivity extends SherlockPreferenceActivity implements OnPreferenceChangeListener {
	
	SharedPreferences prefs;
	ListPreference dia_pago;
	SeekBarPreference porciento_pago;
	Preference seguridad;
	Intent lock;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.preferences);
		setupActionBar();
		
		prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);
		
		setPrefs();
		setChangeListeners();
		setOnClickListeners();
		setSeguridadSummary();
		
		lock = new Intent(PrefsActivity.this, PrefsLockActivity.class);
		seguridad.setIntent(lock);
	}
	
	private void setOnClickListeners() {	
	}

	private void setSeguridadSummary() {
		String savedPattern = prefs.getString("_Pattern", "");
		seguridad.setSummary(savedPattern.equals("") ? "Ninguno" : "Patrón");
	}
	
	@SuppressWarnings("deprecation")
	private void setPrefs() {
		dia_pago = (ListPreference)findPreference("dia_pago");
		porciento_pago = (SeekBarPreference)findPreference("porciento_pago");
		seguridad = (Preference)findPreference("seguridad");
	}
	
	private void setChangeListeners() {
		dia_pago.setOnPreferenceChangeListener(this);
		porciento_pago.setOnPreferenceChangeListener(this);
	}
	
	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		if (preference instanceof CheckBoxPreference
				|| (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH
					&& preference instanceof TwoStatePreference)) {
			return true;
		}

		String stringValue = newValue.toString();

		if (preference instanceof ListPreference) {
			ListPreference listPreference = (ListPreference) preference;
			int index = listPreference.findIndexOfValue(stringValue);

			preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);

		} else {
			preference.setSummary(stringValue);
		}
		return true;
	}
	
	@Override
    public void startActivity(Intent intent) {
        if (intent.equals(lock)) {
            super.startActivityForResult(intent, 1);
        } else {
            super.startActivity(intent);
        }
    }
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            // should be getting called now
        	setSeguridadSummary();
        }
    }

	private void setupActionBar() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
	            break;
		}
		return super.onOptionsItemSelected(item);
	}
}
