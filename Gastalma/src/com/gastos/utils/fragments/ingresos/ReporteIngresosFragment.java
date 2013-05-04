package com.gastos.utils.fragments.ingresos;

import com.actionbarsherlock.app.SherlockFragment;
import com.gastos.gastalma.R;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public final class ReporteIngresosFragment extends SherlockFragment {
	private EditText text;

    public static ReporteIngresosFragment newInstance(int position) {
        ReporteIngresosFragment fragment = new ReporteIngresosFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		text = new EditText(getActivity());
        text.setText("No implementado .D");
        text.setTextColor(Color.WHITE);
        text.setBackgroundResource(R.color.alert);
        text.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        text.setGravity(Gravity.CENTER);
        text.setFocusable(false);

        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        layout.addView(text);

        return layout;
    }
}
