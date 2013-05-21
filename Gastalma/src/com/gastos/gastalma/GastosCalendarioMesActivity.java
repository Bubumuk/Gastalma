package com.gastos.gastalma;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.actionbarsherlock.app.SherlockActivity;
import com.gastos.utils.MonthCellDescriptor;
import com.squareup.timessquare.CalendarCellView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class GastosCalendarioMesActivity extends SherlockActivity implements OnClickListener {

	private CalendarCellView enero;
	private CalendarCellView febrero;
	private CalendarCellView marzo;
	private CalendarCellView abril;
	private CalendarCellView mayo;
	private CalendarCellView junio;
	private CalendarCellView julio;
	private CalendarCellView agosto;
	private CalendarCellView septiembre;
	private CalendarCellView octubre;
	private CalendarCellView noviembre;
	private CalendarCellView diciembre;

	private List<CalendarCellView> meses_view;
	private List<MonthCellDescriptor> meses;
	private int mes_seleccionado, mes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gastos_calendario_mes);

		meses_view = new ArrayList<CalendarCellView>(0);
		meses = new ArrayList<MonthCellDescriptor>(0);

		inicializarMeses();
		setListeners();
		inicializarListaDeMesesView();
		inicializarListaDeMeses();
		
		mes = getIntent().getIntExtra("mes_seleccionado", -1);

		habilitarMeses();
	}

	private void inicializarListaDeMeses() {
		int mes_actual = getMesActual();

		for (int mes = 0; mes < 12; mes++) {

			if (mes <= mes_actual) {
				if (mes == mes_actual) {
					meses.add(new MonthCellDescriptor(mes, true, true, true, true));
				} else {
					meses.add(new MonthCellDescriptor(mes, true, true, false, false));
				}
			} else {
				meses.add(new MonthCellDescriptor(mes, false, false, false, false));
			}
		}
	}

	private void inicializarListaDeMesesView() {
		meses_view.add(enero);
		meses_view.add(febrero);
		meses_view.add(marzo);
		meses_view.add(abril);
		meses_view.add(mayo);
		meses_view.add(junio);
		meses_view.add(julio);
		meses_view.add(agosto);
		meses_view.add(septiembre);
		meses_view.add(octubre);
		meses_view.add(noviembre);
		meses_view.add(diciembre);
	}

	private void setListeners() {
		enero.setOnClickListener(this);
		febrero.setOnClickListener(this);
		marzo.setOnClickListener(this);
		abril.setOnClickListener(this);
		mayo.setOnClickListener(this);
		junio.setOnClickListener(this);
		julio.setOnClickListener(this);
		agosto.setOnClickListener(this);
		septiembre.setOnClickListener(this);
		octubre.setOnClickListener(this);
		noviembre.setOnClickListener(this);
		diciembre.setOnClickListener(this);

	}

	private void habilitarMeses() {

		CalendarCellView mes_view;
		for (MonthCellDescriptor mes : meses) {
			mes_view = meses_view.get(mes.getMonth());
			if (mes.isSelectable()) {
				mes_view.setSelectable(true);
				mes_view.setCurrentMonth(true);
				if(mes.isToday()) {
					mes_view.setToday(true);
				}
				if (mes.getMonth() == this.mes) {
					mes_view.setSelected(true);
					mes_seleccionado = mes.getMonth();
				}
			} else {
				mes_view.setSelectable(false);
				meses_view.get(mes.getMonth()).setClickable(false);
			}
		}
	}

	private int getMesActual() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.MONTH);
	}

	private void inicializarMeses() {
		enero = (CalendarCellView) findViewById(R.id.enero);
		febrero = (CalendarCellView) findViewById(R.id.febrero);
		marzo = (CalendarCellView) findViewById(R.id.marzo);
		abril = (CalendarCellView) findViewById(R.id.abril);
		mayo = (CalendarCellView) findViewById(R.id.mayo);
		junio = (CalendarCellView) findViewById(R.id.junio);
		julio = (CalendarCellView) findViewById(R.id.julio);
		agosto = (CalendarCellView) findViewById(R.id.agosto);
		septiembre = (CalendarCellView) findViewById(R.id.septiembre);
		octubre = (CalendarCellView) findViewById(R.id.octubre);
		noviembre = (CalendarCellView) findViewById(R.id.noviembre);
		diciembre = (CalendarCellView) findViewById(R.id.diciembre);

	}

	@Override
	public void onClick(View view) {
		seleccionarMes((CalendarCellView) view);
	}

	private void seleccionarMes(CalendarCellView view) {
		CalendarCellView mes_view;
		for (MonthCellDescriptor mes : meses) {
			mes_view = meses_view.get(mes.getMonth());
			if (mes.isSelectable()) {
				if (mes_view == view) {
					mes_view.setSelected(true);
					mes_seleccionado = mes.getMonth();
				} else {
					mes_view.setSelected(false);
				}
			}
		}

	}
	
	public void returnMes(View view) {
		Intent intent = new Intent();
		intent.putExtra("mes", mes_seleccionado);
		setResult(RESULT_OK, intent);
		finish();
	}
}
