package com.gastos.gastalma;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.gastos.utils.fragments.gastos.ReporteGastosAñoFragment;
import com.gastos.utils.fragments.gastos.ReporteGastosDiaFragment;
import com.gastos.utils.fragments.gastos.ReporteGastosFragment;
import com.gastos.utils.fragments.gastos.ReporteGastosMesFragment;
import com.gastos.utils.fragments.gastos.ReporteGastosSemanaFragment;

public class ReportesGastosActivity extends SherlockFragmentActivity {
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reporte_fragments);
        
        setupActionBar();

        // get an instance of FragmentTransaction from your Activity
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //add a fragment
        ReporteGastosDiaFragment myFragment = new ReporteGastosDiaFragment();
        fragmentTransaction.add(R.id.myfragment, myFragment);
        fragmentTransaction.commit();
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		//Used to put dark icons on light action bar

        /*menu.add(Menu.NONE, 1, Menu.NONE, "fecha")
        	.setIcon(R.drawable.ic_action_go_to_today)
        	.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);*/
        
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
	            break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void setupActionBar() {
		setTitle("");
		/*
		Context context = getSupportActionBar().getThemedContext();
        ArrayAdapter<CharSequence> list = ArrayAdapter.createFromResource(context, R.array.locations, R.layout.sherlock_spinner_item);
        list.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);

        // create ICS spinner
        IcsSpinner spinner = new IcsSpinner(this, null, R.attr.actionDropDownStyle);
        spinner.setAdapter(list);
        spinner.setOnItemSelectedListener(this);

        // configure custom view
        IcsLinearLayout listNavLayout = (IcsLinearLayout)
        getLayoutInflater().inflate(R.layout.abs__action_bar_tab_bar_view, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        listNavLayout.addView(spinner, params);
        listNavLayout.setGravity(Gravity.LEFT);        // <-- align the spinner to the right

        // configure action bar
        getSupportActionBar().setCustomView(listNavLayout, new ActionBar.LayoutParams(Gravity.LEFT));
        getSupportActionBar().setDisplayShowCustomEnabled(true);*/
		
		SpinnerAdapter adapter = ArrayAdapter.createFromResource(getSupportActionBar().getThemedContext(), R.array.locations, R.layout.sherlock_spinner_dropdown_item);
		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		
		getSupportActionBar().setListNavigationCallbacks(adapter, new OnNavigationListener() {
			
			@Override
			public boolean onNavigationItemSelected(int itemPosition, long itemId) {
				//Toast.makeText(ReportesGastosActivity.this, "Item selected: " + mLocations[position], Toast.LENGTH_SHORT).show();
				Fragment newFragment;

				// Create new fragment
				switch(itemPosition) {
				case 0:
					newFragment = new ReporteGastosDiaFragment();
					break;
				case 1:
					newFragment = new ReporteGastosMesFragment();
					break;
				case 2:
					newFragment = new ReporteGastosAñoFragment();
					break;
				case 3:
					newFragment = new ReporteGastosSemanaFragment();
					break;
				default:
					newFragment = new ReporteGastosFragment();
					break;
				}

				// Create new transaction
				// get an instance of FragmentTransaction from your Activity
		        FragmentManager fragmentManager = getSupportFragmentManager();
		        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

				// Replace whatever is in the fragment_container view with this fragment
		        fragmentTransaction.replace(R.id.myfragment, newFragment);

				// Commit the transaction
		        fragmentTransaction.commit();

				return true;
			}
		});
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	@Override
	public void onBackPressed() {
		NavUtils.navigateUpFromSameTask(this);
	}
}
