package com.gastos.gastalma;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.gastos.db.GastosDBHelper;

import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.content.Intent;
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
        subMenu.add(Menu.NONE, 2, Menu.NONE, "Acerca de");

        MenuItem subMenuItem = subMenu.getItem();
        subMenuItem.setIcon(com.actionbarsherlock.R.drawable.abs__ic_menu_moreoverflow_normal_holo_dark);
        subMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			ViewConfiguracion();
			break;
		case 2:
			ViewAbout();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		menu.add(Menu.NONE, 3, Menu.NONE, "Gastos");
		menu.add(Menu.NONE, 4, Menu.NONE, "Ingresos");
		menu.add(Menu.NONE, 5, Menu.NONE, "Pagos");
	}

	@Override
	public boolean onContextItemSelected(android.view.MenuItem item) {
		switch (item.getItemId()) {
		case 3:
			Intent myIntentG = new Intent(this, ReportesGastosActivity.class);
			startActivity(myIntentG);
			break;
		case 4:
			Intent myIntentI = new Intent(this, ReportesIngresosActivity.class);
			startActivity(myIntentI);
			break;
		case 5:
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
	
	public void ViewAbout() {
		Intent myIntent = new Intent(this, AboutActivity.class);
		startActivityForResult(myIntent, 0);
	}
	
	@Override
	public void onBackPressed() {
		moveTaskToBack(true);
		finish();
	}
}
