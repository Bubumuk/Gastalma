package com.gastos.db;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gastos.utils.Gasto;
import com.gastos.utils.Ingreso;
import com.gastos.utils.Pago;

public class GastosDBHelper {

	private SQLiteDatabase db;

	public void abrirLecturaBD(Activity ga) {
		// Abrimos la base de datos 'DBUsuarios' en modo escritura
		GastosSQLiteHelper gastosdb = new GastosSQLiteHelper(ga, "DBGastos", null, 1);
		db = gastosdb.getReadableDatabase();
	}

	public void abrirEscrituraBD(Activity ga) {
		// Abrimos la base de datos 'DBUsuarios' en modo escritura
		GastosSQLiteHelper gastosdb = new GastosSQLiteHelper(ga, "DBGastos", null, 1);
		db = gastosdb.getWritableDatabase();
	}

	private String separarFechaN(String fecha, int caso) {
		switch (caso) {
		case 3: // año
			return fecha.substring(0, fecha.indexOf("-"));
		case 2: // mes
			return fecha.substring(fecha.indexOf("-") + 1, fecha.lastIndexOf("-"));
		case 1: // dia
			return fecha.substring(fecha.lastIndexOf("-") + 1);
		}
		return "";
	}

	public boolean isClosed() {
		return !db.isOpen();
	}

	public void close() {
		db.close();
	}

	public boolean insertarGasto(String nombre, String fecha, Double costo, String descripcion, String tipo, String hora) {

		// Creamos el registro a insertar como objeto ContentValues
		ContentValues gasto = new ContentValues();
		gasto.put("nombre", nombre);
		gasto.put("fecha", fecha);
		gasto.put("dia", separarFechaN(fecha, 3));
		gasto.put("mes", separarFechaN(fecha, 2));
		gasto.put("año", separarFechaN(fecha, 1));
		gasto.put("costo", costo);
		gasto.put("descripcion", descripcion);
		gasto.put("tipo", tipo);
		gasto.put("hora", hora);

		// Insertamos el registro en la base de datos
		int exito = (int) db.insert("Gastos", null, gasto);

		db.close();

		return exito > 0 ? true : false;
	}

	public boolean eliminarGasto(int id) {

		// Creamos el registro a insertar como objeto ContentValues
		String[] args = new String[] { String.valueOf(id) };

		// Eliminamos el registro en la base de datos
		int exito = db.delete("Gastos", "id=?", args);

		db.close();

		return exito > 0 ? true : false;
	}

	public boolean actualizarGasto(String nombre, String fecha, Double costo, String descripcion, String tipo, String hora, int id) {

		// Creamos el registro a insertar como objeto ContentValues
		String[] args = new String[] { String.valueOf(id) };

		ContentValues values = new ContentValues();
		values.put("nombre", nombre);
		values.put("costo", costo);
		values.put("descripcion", descripcion);
		values.put("fecha", fecha);
		values.put("hora", hora);
		values.put("tipo", tipo);

		// Actualizamos el registro en la base de datos
		int exito = db.update("Gastos", values, "id=?", args);

		db.close();

		return exito > 0 ? true : false;
	}

	public List<Gasto> fetchGastos() {

		Cursor c = db.query("Gastos", new String[] { "nombre", "costo", "tipo", "descripcion", "fecha", "hora", "id" }, "", null, null, null, null);
		List<Gasto> lista_gastos = new ArrayList<Gasto>();
		// Nos aseguramos de que existe al menos un registro
		if (c.moveToFirst()) {
			// Recorremos el cursor hasta que no haya más registros
			do {
				lista_gastos.add(new Gasto(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5), c
						.getInt(6)));
			} while (c.moveToNext());
		}

		db.close();

		return lista_gastos;
	}

	public List<Gasto> fetchGastosHistorial() {

		Cursor c = db.query("Gastos", new String[] { "nombre", "costo", "tipo", "descripcion", "fecha", "hora", "id" }, "", null, "nombre", null,
				null);
		List<Gasto> lista_gastos = new ArrayList<Gasto>();
		// Nos aseguramos de que existe al menos un registro
		if (c.moveToFirst()) {
			// Recorremos el cursor hasta que no haya más registros
			do {
				lista_gastos.add(new Gasto(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5), c
						.getInt(6)));
			} while (c.moveToNext());
		}

		db.close();

		return lista_gastos;
	}

	public List<Gasto> fetchGastosDia(String fecha) {

		Cursor c = db.query("Gastos", new String[] { "nombre", "costo", "tipo", "descripcion", "fecha", "hora", "id" }, "fecha=?",
				new String[] { fecha }, null, null, null);
		List<Gasto> lista_gastos = new ArrayList<Gasto>();
		// Nos aseguramos de que existe al menos un registro
		if (c.moveToFirst()) {
			// Recorremos el cursor hasta que no haya más registros
			do {
				lista_gastos.add(new Gasto(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5), c
						.getInt(6)));
			} while (c.moveToNext());
		}

		db.close();

		return lista_gastos;
	}

	public List<Gasto> fetchGastosMes(String fecha) {

		Cursor c = db.query("Gastos", new String[] { "nombre", "costo", "tipo", "descripcion", "fecha", "hora", "id" }, "mes=?",
				new String[] { separarFechaN(fecha, 2) }, null, null, null);
		List<Gasto> lista_gastos = new ArrayList<Gasto>();
		// Nos aseguramos de que existe al menos un registro
		if (c.moveToFirst()) {
			// Recorremos el cursor hasta que no haya más registros
			do {
				lista_gastos.add(new Gasto(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5), c
						.getInt(6)));
			} while (c.moveToNext());
		}

		db.close();

		return lista_gastos;
	}

	public List<Gasto> fetchGastosAño(String fecha) {

		Cursor c = db.query("Gastos", new String[] { "nombre", "costo", "tipo", "descripcion", "fecha", "hora", "id" }, "año=?",
				new String[] { separarFechaN(fecha, 1) }, null, null, null);
		List<Gasto> lista_gastos = new ArrayList<Gasto>();
		// Nos aseguramos de que existe al menos un registro
		if (c.moveToFirst()) {
			// Recorremos el cursor hasta que no haya más registros
			do {
				lista_gastos.add(new Gasto(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5), c
						.getInt(6)));
			} while (c.moveToNext());
		}

		db.close();

		return lista_gastos;
	}

	public List<Gasto> fetchGastosSemana(String inicio, String fin) {
		Cursor c = db.query("Gastos", new String[] { "nombre", "costo", "tipo", "descripcion", "fecha", "hora", "id" }, "fecha BETWEEN ? AND ?",
				new String[] { inicio, fin }, null, null, null);
		List<Gasto> lista_gastos = new ArrayList<Gasto>();
		// Nos aseguramos de que existe al menos un registro
		if (c.moveToFirst()) {
			// Recorremos el cursor hasta que no haya más registros
			do {
				lista_gastos.add(new Gasto(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5), c
						.getInt(6)));
			} while (c.moveToNext());
		}

		db.close();

		return lista_gastos;
	}

	public boolean insertarIngreso(Double cantidad, String fecha, String descripcion, String hora) {

		// Creamos el registro a insertar como objeto ContentValues
		ContentValues ingreso = new ContentValues();
		ingreso.put("cantidad", cantidad);
		ingreso.put("fecha", fecha);
		ingreso.put("dia", separarFechaN(fecha, 3));
		ingreso.put("mes", separarFechaN(fecha, 2));
		ingreso.put("año", separarFechaN(fecha, 1));
		ingreso.put("descripcion", descripcion);
		ingreso.put("hora", hora);

		// Insertamos el registro en la base de datos
		int exito = (int) db.insert("Ingresos", null, ingreso);

		db.close();

		return exito > 0 ? true : false;
	}

	public boolean eliminarIngreso(int id) {

		// Creamos el registro a insertar como objeto ContentValues
		String[] args = new String[] { String.valueOf(id) };

		// Eliminamos el registro en la base de datos
		int exito = db.delete("Ingresos", "id=?", args);

		db.close();

		return exito > 0 ? true : false;
	}

	public boolean actualizarIngreso(String cantidad, String descripcion, String fecha, int id) {

		// Creamos el registro a insertar como objeto ContentValues
		String[] args = new String[] { String.valueOf(id) };

		ContentValues values = new ContentValues();
		values.put("cantidad", cantidad);
		values.put("descripcion", descripcion);

		// Actualizamos el registro en la base de datos
		int exito = db.update("Ingresos", values, "id=?", args);

		db.close();

		return exito > 0 ? true : false;
	}

	public List<Ingreso> fetchIngresos() {

		Cursor c = db.query("Ingresos", new String[] { "cantidad", "descripcion", "fecha", "hora", "id" }, null, null, null, null, "fecha DESC");
		List<Ingreso> lista_ingresos = new ArrayList<Ingreso>();
		// Nos aseguramos de que existe al menos un registro
		if (c.moveToFirst()) {
			// Recorremos el cursor hasta que no haya más registros
			do {
				lista_ingresos.add(new Ingreso(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getInt(4)));
			} while (c.moveToNext());
		}

		db.close();

		return lista_ingresos;
	}

	public List<Ingreso> fetchIngresosHistorial() {

		Cursor c = db.query("Ingresos", new String[] { "cantidad", "descripcion", "fecha", "hora", "id" }, null, null, null, null, null);
		List<Ingreso> lista_ingresos = new ArrayList<Ingreso>();
		// Nos aseguramos de que existe al menos un registro
		if (c.moveToFirst()) {
			// Recorremos el cursor hasta que no haya más registros
			do {
				lista_ingresos.add(new Ingreso(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getInt(4)));
			} while (c.moveToNext());
		}

		db.close();

		return lista_ingresos;
	}

	public List<Ingreso> fetchIngresosDia(String fecha) {

		Cursor c = db.query("Ingresos", new String[] { "cantidad", "descripcion", "fecha", "hora", "id" }, "fecha=?", new String[] { fecha }, null,
				null, "fecha DESC");
		List<Ingreso> lista_ingresos = new ArrayList<Ingreso>();
		// Nos aseguramos de que existe al menos un registro
		if (c.moveToFirst()) {
			// Recorremos el cursor hasta que no haya más registros
			do {
				lista_ingresos.add(new Ingreso(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getInt(4)));
			} while (c.moveToNext());
		}

		db.close();

		return lista_ingresos;
	}

	public List<Ingreso> fetchIngresosMes(String fecha) {

		Cursor c = db.query("Ingresos", new String[] { "cantidad", "descripcion", "fecha", "hora", "id" }, "mes=?",
				new String[] { separarFechaN(fecha, 2) }, null, null, "fecha DESC");
		List<Ingreso> lista_ingresos = new ArrayList<Ingreso>();
		// Nos aseguramos de que existe al menos un registro
		if (c.moveToFirst()) {
			// Recorremos el cursor hasta que no haya más registros
			do {
				lista_ingresos.add(new Ingreso(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getInt(4)));
			} while (c.moveToNext());
		}

		db.close();

		return lista_ingresos;
	}

	public List<Ingreso> fetchIngresosAño(String fecha) {

		Cursor c = db.query("Ingresos", new String[] { "cantidad", "descripcion", "fecha", "hora", "id" }, "año=?",
				new String[] { separarFechaN(fecha, 1) }, null, null, "fecha DESC");
		List<Ingreso> lista_ingresos = new ArrayList<Ingreso>();
		// Nos aseguramos de que existe al menos un registro
		if (c.moveToFirst()) {
			// Recorremos el cursor hasta que no haya más registros
			do {
				lista_ingresos.add(new Ingreso(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getInt(4)));
			} while (c.moveToNext());
		}

		db.close();

		return lista_ingresos;
	}

	public boolean insertarPago(double cantidad, String fecha, String hora) {

		// Creamos el registro a insertar como objeto ContentValues
		ContentValues pago = new ContentValues();
		pago.put("cantidad", cantidad);
		pago.put("fecha", fecha);
		pago.put("dia", separarFechaN(fecha, 3));
		pago.put("mes", separarFechaN(fecha, 2));
		pago.put("año", separarFechaN(fecha, 1));
		pago.put("hora", hora);

		// Insertamos el registro en la base de datos
		int exito = (int) db.insert("Pagos", null, pago);

		db.close();

		return exito > 0 ? true : false;
	}

	public List<Pago> fetchPagos() {

		Cursor c = db.query("Pagos", new String[] { "cantidad", "fecha", "hora" }, null, null, null, null, null);
		List<Pago> lista_pagos = new ArrayList<Pago>();
		// Nos aseguramos de que existe al menos un registro
		if (c.moveToFirst()) {
			// Recorremos el cursor hasta que no haya más registros
			do {
				lista_pagos.add(new Pago(c.getDouble(0), c.getString(1), c.getString(2)));
			} while (c.moveToNext());
		}

		db.close();

		return lista_pagos;
	}

}
