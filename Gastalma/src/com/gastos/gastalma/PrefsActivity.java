package com.gastos.gastalma;

import group.pals.android.lib.ui.lockpattern.LockPatternActivity;

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
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceScreen;
import android.preference.TwoStatePreference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.support.v4.app.NavUtils;

public class PrefsActivity extends SherlockPreferenceActivity implements OnPreferenceChangeListener {
	
	SharedPreferences prefs;
	ListPreference dia_pago;
	SeekBarPreference porciento_pago;
	Preference lock_pattern;
	PreferenceScreen seguridad;
	Preference lock_ninguno;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.preferences);
		setupActionBar();
		
		prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);
		
		setPrefs();
		setChangeListeners();
		setPreferenceOnClickListener();
		
		setSeguridadSummary();
	}
	
	private void setSeguridadSummary() {
		String savedPattern = prefs.getString("_Pattern", "");
		seguridad.setSummary(savedPattern.equals("") ? "Ninguno" : "Patrón");
	}
	
	@SuppressWarnings("deprecation")
	private void setPrefs() {
		dia_pago = (ListPreference)findPreference("dia_pago");
		porciento_pago = (SeekBarPreference)findPreference("porciento_pago");
		lock_pattern = (Preference)findPreference("lock_pattern");
		seguridad = (PreferenceScreen)findPreference("seguridad");
		lock_ninguno = (Preference)findPreference("lock_ninguno");
	}
	
	private void setChangeListeners() {
		dia_pago.setOnPreferenceChangeListener(this);
		porciento_pago.setOnPreferenceChangeListener(this);
		lock_pattern.setOnPreferenceChangeListener(this);
	}
	
	private void setPreferenceOnClickListener() {
		lock_pattern.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				Intent i = new Intent(LockPatternActivity._ActionCreatePattern,
	                    null, PrefsActivity.this, LockPatternActivity.class);
	            i.putExtra(LockPatternActivity._Theme, R.style.Alp_Theme_Dialog_Dark);
	            i.putExtra(LockPatternActivity._AutoSave, true);
	            i.putExtra(LockPatternActivity._MinWiredDots, 4);
	            startActivityForResult(i, 1);
				return true;
			}
		});
		
		lock_ninguno.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				seguridad.setSummary("Ninguno");
				prefs.edit().putString("_Pattern", "").commit();
				getListView().invalidateViews();
				seguridad.getDialog().dismiss();
				return true;
			}
		});
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    switch (requestCode) {
	    case 1:
	        if (resultCode == RESULT_OK) {
	            String pattern = data.getStringExtra(LockPatternActivity._Pattern);
	            seguridad.setSummary("Patrón");
	            prefs.edit().putString("_Pattern", pattern).commit();
	            getListView().invalidateViews();
	            seguridad.getDialog().dismiss();
	        }
	        if(resultCode == RESULT_CANCELED) {
	        	seguridad.getDialog().dismiss();
	        }
	        break;
	    }
	}
}
