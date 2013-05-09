package com.gastos.gastalma;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.gastos.db.GastosDBHelper;

import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends SherlockActivity {

	private GastosDBHelper dbHelper;
	private Button button3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);

		dbHelper = new GastosDBHelper();
		dbHelper.abrirLecturaBD(this);

		button3 = (Button)findViewById(R.id.button3);

		registerForContextMenu(button3);
		button3.setLongClickable(false);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		//Used to put dark icons on light action bar

		SubMenu subMenu = menu.addSubMenu("Configuración");
        subMenu.add(Menu.NONE, 1, Menu.NONE, "Configuración");
        subMenu.add("Acerca de");

        MenuItem subMenuItem = subMenu.getItem();
        subMenuItem.setIcon(com.actionbarsherlock.R.drawable.abs__ic_menu_moreoverflow_normal_holo_dark);
        subMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

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
			ViewConfiguracion();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		menu.add(Menu.NONE, 2, Menu.NONE, "Gastos");
		menu.add(Menu.NONE, 3, Menu.NONE, "Ingresos");
		menu.add(Menu.NONE, 4, Menu.NONE, "Pagos");
	}

	@Override
	public boolean onContextItemSelected(android.view.MenuItem item) {
		switch (item.getItemId()) {
		case 2:
			Intent myIntentG = new Intent(this, ReportesGastosActivity.class);
			startActivity(myIntentG);
			break;
		case 3:
			Intent myIntentI = new Intent(this, ReportesIngresosActivity.class);
			startActivity(myIntentI);
			break;
		case 4:
			Intent myIntentP = new Intent(this, PagosHistorialActivity.class);
			startActivity(myIntentP);
			break;
		}
		return true;
	}

	public void ViewGastos(View view) {
		Intent myIntent = new Intent(this, GastosActivity.class);
		startActivity(myIntent);
	}

	public void ViewIngresos(View view) {
		Intent myIntent = new Intent(this, IngresosActivity.class);
		startActivity(myIntent);
	}

	public void ViewReportes(View view) {
		button3.showContextMenu();
	}

	public void ViewDeudas(View view) {
		Intent myIntent = new Intent(this, DeudasActivity.class);
		startActivity(myIntent);
	}

	public void ViewConfiguracion() {
		Intent myIntent = new Intent(this, PrefsActivity.class);
		startActivityForResult(myIntent, 0);
	}

}
