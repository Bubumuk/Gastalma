package com.gastos.gastalma;

import group.pals.android.lib.ui.lockpattern.LockPatternActivity;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnDismissListener;

public class AppActivity extends Activity {

	private SharedPreferences prefs;
	private String savedPattern;
	private Intent menu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		menu = new Intent(this, MenuActivity.class);
		menu.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		
		prefs = getSharedPreferences("prefs", Context.MODE_PRIVATE);
		savedPattern = prefs.getString("_Pattern", "");
		
		if(!savedPattern.equals("")) {
			
			prefs.edit().putBoolean("inicio", false).commit();
			
			Intent intent = new Intent(LockPatternActivity._ActionComparePattern, null,
			        this, LockPatternActivity.class);
			intent.putExtra(LockPatternActivity._Pattern, savedPattern);
			startActivityForResult(intent, 1);
		} else {
			startActivity(menu);
			finish();
		}
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1: {
			switch (resultCode) {
			case RESULT_OK:
				// The user passed
				startActivity(menu);
				finish();
				break;
			case RESULT_CANCELED:
				// The user cancelled the task
				finish();          
	            moveTaskToBack(true);
				break;
			case LockPatternActivity._ResultFailed:
				// The user failed to enter the pattern
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				
				builder.setTitle("Patrón erroneo")
					.setMessage("La aplicación se cerrará")
					.setPositiveButton("Aceptar", null);
				
				AlertDialog dialog = builder.create();
				
				dialog.setOnDismissListener(new OnDismissListener() {
					
					@Override
					public void onDismiss(DialogInterface arg0) {
						moveTaskToBack(true);
						finish();
					}
				});
				
				dialog.show();
				break;
			}

			/*
			 * In any case, there's always a key _ExtraRetryCount, which holds
			 * the number of tries that the user did.
			 */
			//int retryCount = data.getIntExtra(LockPatternActivity._ExtraRetryCount, 0);

			break;
			}
		}
	}
	
	@Override
	protected void onDestroy() {
		
		super.onDestroy();
	}

}
