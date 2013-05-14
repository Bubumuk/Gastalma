package com.gastos.utils;

import com.gastos.utils.fragments.ingresos.ReporteIngresosAñoFragment;
import com.gastos.utils.fragments.ingresos.ReporteIngresosDiaFragment;
import com.gastos.utils.fragments.ingresos.ReporteIngresosFragment;
import com.gastos.utils.fragments.ingresos.ReporteIngresosMesFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ReporteIngresosFragmentAdapter extends FragmentPagerAdapter {

	private int mCount = 5;

	public ReporteIngresosFragmentAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		switch(position) {
			case 0:
				return ReporteIngresosDiaFragment.newInstance(position, this);
			case 1:
				return ReporteIngresosMesFragment.newInstance(position);
			case 3:
				return ReporteIngresosAñoFragment.newInstance(position);
			default:
				return ReporteIngresosFragment.newInstance(position);
		}
	}
	
	public int getItemPosition(Object object) {
	    return POSITION_NONE;
	}
	
	@Override
	public int getCount() {
		return mCount;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		switch(position) {
			case 0: return "Día";
			case 1: return "Mes";
			case 2: return "Semana";
			case 3: return "Año";
			case 4: return "Personalizado";
			default: return "";
		}
	}
	
	
}
