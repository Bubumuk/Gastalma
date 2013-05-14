package com.gastos.utils.fragments.gastos;

import com.actionbarsherlock.app.SherlockListFragment;
import com.gastos.gastalma.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphViewStyle;
import com.jjoe64.graphview.LineGraphView;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

public final class ReporteGraficasGastosFragment extends SherlockListFragment {
	
	private CardsAdapter adapter;

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

		Context context;

		public CardsAdapter(Context context, int textViewResourceId) {
			super(context, textViewResourceId);
			this.context = context;
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			LayoutInflater mInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.recent_activity_row, null);

				// init example series data
				GraphViewSeries exampleSeries = new GraphViewSeries(new GraphViewData[] {
						new GraphViewData(1, 2.0d)
						, new GraphViewData(2, 1.5d)
						, new GraphViewData(2.5, 3.0d) // another frequency
						, new GraphViewData(3, 2.5d)
						, new GraphViewData(4, 1.0d)
						, new GraphViewData(5, 3.0d)
				});

				// graph with dynamically genereated horizontal and vertical labels
				GraphView graphView;

				graphView = new LineGraphView(getActivity(), "GraphViewDemo");
				((LineGraphView) graphView).setDrawBackground(false);
				
				GraphViewStyle graphViewStyle = new GraphViewStyle(Color.BLACK, Color.BLACK, Color.GRAY);
				graphView.setGraphViewStyle(graphViewStyle);
				
				// custom static labels
				graphView.setHorizontalLabels(new String[] {"1", "2", "3", "4"});
				graphView.setVerticalLabels(new String[] {"1", "2", "3"});
				graphView.addSeries(exampleSeries); // data

				LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.graph_layout);
				layout.addView(graphView);
			}
			return convertView;
		}
	}
}
