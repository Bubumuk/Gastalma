package com.gastos.utils;

import com.gastos.utils.fragments.gastos.ReporteGastosA�oFragment;
import com.gastos.utils.fragments.gastos.ReporteGastosDiaFragment;
import com.gastos.utils.fragments.gastos.ReporteGastosFragment;
import com.gastos.utils.fragments.gastos.ReporteGastosMesFragment;
import com.gastos.utils.fragments.gastos.ReporteGraficasGastosFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ReporteGastosFragmentAdapter extends FragmentPagerAdapter {

	private int mCount = 5;

	public ReporteGastosFragmentAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		switch(position) {
			case 0:
				return ReporteGastosDiaFragment.newInstance(position);
			case 1:
				return ReporteGastosMesFragment.newInstance(position);
			case 3:
				return ReporteGastosA�oFragment.newInstance(position);
			case 5:
				return ReporteGraficasGastosFragment.newInstance(position);
			default:
				return ReporteGastosFragment.newInstance(position);
		}
	}

	@Override
	public int getCount() {
		return mCount;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		switch(position) {
			case 0: return "D�a";
			case 1: return "Mes";
			case 2: return "Semana";
			case 3: return "A�o";
			case 4: return "Personalizado";
			//case 5: return "Estad�sticas";
			default: return "";
		}
	}
}
