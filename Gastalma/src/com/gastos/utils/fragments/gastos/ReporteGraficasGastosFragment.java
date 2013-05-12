package com.gastos.utils.fragments.gastos;

import com.actionbarsherlock.app.SherlockListFragment;
import com.gastos.gastalma.R;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;

public final class ReporteGraficasGastosFragment extends SherlockListFragment {
	private EditText text;
	private CardsAdapter adapter;
	private Fragment mContent;

	public static ReporteGraficasGastosFragment newInstance(int position) {
		ReporteGraficasGastosFragment fragment = new ReporteGraficasGastosFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		adapter = new CardsAdapter(getActivity().getApplicationContext(),
				android.R.id.list);
		setListAdapter(adapter);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.list_main_layout, container, false);

		return v;
	}

	private class CardsAdapter extends ArrayAdapter<String> {

		Context mContext;

		public CardsAdapter(Context context, int textViewResourceId) {
			super(context, textViewResourceId);
			this.mContext = context;
		}

		@Override
		public int getCount() {
			return 5;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			if (row == null) {
				row = getActivity().getLayoutInflater().inflate(R.layout.recent_activity_row, parent, false);
			}
			return row;
		}
	}
}
