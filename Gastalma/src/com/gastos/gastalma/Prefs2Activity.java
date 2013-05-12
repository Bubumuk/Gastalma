package com.gastos.gastalma;

import group.pals.android.lib.ui.lockpattern.LockPatternActivity;

import com.actionbarsherlock.app.SherlockPreferenceActivity;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class Prefs2Activity extends SherlockPreferenceActivity {

	SharedPreferences prefs;
	Preference lock_pattern;
	Preference lock_ninguno;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.preferences_lock);
		setupActionBar();
		
		prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);
		
		setPrefs();
		setPreferenceOnClickListener();
	}
	
	@SuppressWarnings("deprecation")
	private void setPrefs() {
		lock_pattern = (Preference)findPreference("lock_pattern");
		lock_ninguno = (Preference)findPreference("lock_ninguno");
	}
	
	private void setPreferenceOnClickListener() {
		lock_pattern.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				Intent i = new Intent(LockPatternActivity._ActionCreatePattern,
	                    null, Prefs2Activity.this, LockPatternActivity.class);
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
				//seguridad.setSummary("Ninguno");
				prefs.edit().putString("_Pattern", "").commit();
				//seguridad.getDialog().dismiss();
				setResult(RESULT_OK);
				finish();
				return true;
			}
		});
	}

	private void setupActionBar() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			String pattern = data.getStringExtra(LockPatternActivity._Pattern);
			//seguridad.setSummary("Patrón");
			prefs.edit().putString("_Pattern", pattern).commit();
			//seguridad.getDialog().dismiss();
			setResult(RESULT_OK);
			finish();
		}
		if(resultCode == RESULT_CANCELED) {
			//seguridad.getDialog().dismiss();
			setResult(RESULT_CANCELED);
			finish();
		}
	}
}
