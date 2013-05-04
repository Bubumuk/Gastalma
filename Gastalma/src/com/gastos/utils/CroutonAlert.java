package com.gastos.utils;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.Gravity;

import com.gastos.gastalma.R;

import de.neofonie.mobile.app.android.widget.crouton.Style;

public class CroutonAlert {
	
	public static String alert_string = "No está implementado .D";
	
	@SuppressLint("ResourceAsColor")
	public static Style alertStyle() {
		int heightInPx = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) ? 60 : 40;
		
		Style style = new Style.Builder()
			.setBackgroundColor(R.color.alert)
			.setDuration(2000)
			.setGravity(Gravity.CENTER)
			.setTextColor(android.R.color.white)
			.setHeight(heightInPx)
			.build();
		
		return style;
	}

}
