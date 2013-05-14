package com.gastos.gastalma;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.LayoutParams;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.internal.widget.IcsAdapterView;
import com.actionbarsherlock.internal.widget.IcsAdapterView.OnItemSelectedListener;
import com.actionbarsherlock.internal.widget.IcsLinearLayout;
import com.actionbarsherlock.internal.widget.IcsSpinner;
import com.gastos.utils.fragments.gastos.ReporteGastosAñoFragment;
import com.gastos.utils.fragments.gastos.ReporteGastosDiaFragment;
import com.gastos.utils.fragments.gastos.ReporteGastosFragment;
import com.gastos.utils.fragments.gastos.ReporteGastosMesFragment;

public class ReportesGastosActivity extends SherlockFragmentActivity implements OnItemSelectedListener {
	
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
        	//.setIcon(android.R.drawable.ic_menu_add)
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
		setTitle("Reporte Gastos");
		
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
        listNavLayout.setGravity(Gravity.RIGHT);        // <-- align the spinner to the right

        // configure action bar
        getSupportActionBar().setCustomView(listNavLayout, new ActionBar.LayoutParams(Gravity.RIGHT));
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public void onItemSelected(IcsAdapterView<?> parent, View view, int position, long id) {

		//Toast.makeText(ReportesGastosActivity.this, "Item selected: " + mLocations[position], Toast.LENGTH_SHORT).show();
		Fragment newFragment;

		// Create new fragment
		switch(position) {
		case 0:
			newFragment = new ReporteGastosDiaFragment();
			break;
		case 1:
			newFragment = new ReporteGastosMesFragment();
			break;
		case 2:
			newFragment = new ReporteGastosAñoFragment();
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

	}

	@Override
	public void onNothingSelected(IcsAdapterView<?> parent) {		
	}
	
	@Override
	public void onBackPressed() {
		NavUtils.navigateUpFromSameTask(this);
	}
}
