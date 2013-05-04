package com.gastos.gastalma;

import net.saik0.android.unifiedpreference.UnifiedPreferenceFragment;
import net.saik0.android.unifiedpreference.UnifiedSherlockPreferenceActivity;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NavUtils;

public class PrefsActivity extends UnifiedSherlockPreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Set header resource MUST BE CALLED BEFORE super.onCreate 
		setHeaderRes(R.xml.pref_headers);
		super.onCreate(savedInstanceState);

		// Set desired preference file and mode (optional)
		setSharedPreferencesName("prefs");
		setSharedPreferencesMode(Context.MODE_PRIVATE);
		setupActionBar();
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
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
	
	public static class GeneralPreferenceFragment extends UnifiedPreferenceFragment {}

}
