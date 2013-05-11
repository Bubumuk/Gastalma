package com.gastos.db;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class GastosDBHelper {
	
	private SQLiteDatabase db;
	
	public void abrirLecturaBD(Activity ga) {
		//Abrimos la base de datos 'DBUsuarios' en modo escritura
        GastosSQLiteHelper gastosdb = new GastosSQLiteHelper(ga, "DBGastos", null, 1);
        db = gastosdb.getReadableDatabase();
	}
	
	public void abrirEscrituraBD(Activity ga) {
		//Abrimos la base de datos 'DBUsuarios' en modo escritura
        GastosSQLiteHelper gastosdb = new GastosSQLiteHelper(ga, "DBGastos", null, 1);
        db = gastosdb.getWritableDatabase();
	}
	
	private String separarFecha(String fecha, int caso) {
		switch(caso) {
			case 1: //dia
				return fecha.substring(0,fecha.indexOf("/"));
			case 2: //mes
				return fecha.substring(fecha.indexOf("/")+1,fecha.lastIndexOf("/"));
			case 3: //año
				return fecha.substring(fecha.lastIndexOf("/")+1);
		}
		return "";
	}
	
	public boolean isClosed() {
		return !db.isOpen();
	}
	
	public boolean insertarGasto(String nombre, String fecha, Double costo, String descripcion, String tipo, String hora) {
		
		//Creamos el registro a insertar como objeto ContentValues
		ContentValues gasto = new ContentValues();
		gasto.put("nombre", nombre);
		gasto.put("fecha", fecha);
		gasto.put("dia", separarFecha(fecha, 1));
		gasto.put("mes", separarFecha(fecha, 2));
		gasto.put("año", separarFecha(fecha, 3));
		gasto.put("costo", costo);
		gasto.put("descripcion", descripcion);
		gasto.put("tipo", tipo);
		gasto.put("hora", hora);
		 
		//Insertamos el registro en la base de datos
		int exito = (int)db.insert("Gastos", null, gasto);
		
		db.close();
		
		return exito > 0 ? true : false;
	}
	
	public boolean eliminarGasto(int id) {
		
		//Creamos el registro a insertar como objeto ContentValues
		String[] args = new String[] { String.valueOf(id) };
		 
		//Eliminamos el registro en la base de datos
		int exito = db.delete("Gastos", "id=?", args);
		
		db.close();
		
		return exito > 0 ? true : false;
	}
	
	public boolean actualizarGasto(String nombre, String costo, String descripcion, String fecha, String hora, String tipo, int id) {
		
		//Creamos el registro a insertar como objeto ContentValues
		String[] args = new String[] { String.valueOf(id) };
		
		ContentValues values = new ContentValues();
		values.put("nombre", nombre);
		values.put("costo", costo);
		values.put("descripcion", descripcion);
		values.put("fecha", fecha);
		values.put("hora", hora);
		values.put("tipo", tipo);
		 
		//Actualizamos el registro en la base de datos
		int exito = db.update("Gastos", values, "id=?", args);
		
		db.close();
		
		return exito > 0 ? true : false;
	}

	public Cursor fetchGastos() {
		
		Cursor c = db.query("Gastos", new String[] { "nombre", "costo", "tipo", "descripcion", "fecha", "id" }, "", null, null, null, null);
		return c;
	}
	
	public Cursor fetchGastosHistorial() {
		
		Cursor c = db.query("Gastos", new String[] { "nombre", "costo", "tipo", "descripcion", "fecha", "id" }, "", null, "nombre", null, null);
		return c;
	}
	
	public Cursor fetchGastosDia(String fecha) {
		
		Cursor c = db.query("Gastos", new String[] { "nombre", "costo", "tipo", "descripcion", "id" }, "fecha=?", new String[] { fecha }, null, null, null);
		return c;
	}

	public Cursor fetchGastosMes(String fecha) {

		Cursor c = db.query("Gastos", new String[] { "nombre", "costo", "tipo", "descripcion", "id" }, "mes=?", new String[] { separarFecha(fecha, 2) }, null, null, null);
		return c;
	}

	public Cursor fetchGastosAño(String fecha) {

		Cursor c = db.query("Gastos", new String[] { "nombre", "costo", "tipo", "descripcion", "id" }, "año=?", new String[] { separarFecha(fecha, 3) }, null, null, null);
		return c;
	}
	
	public boolean insertarIngreso(Double cantidad, String fecha, String descripcion, String hora) {
		
		//Creamos el registro a insertar como objeto ContentValues
		ContentValues ingreso = new ContentValues();
		ingreso.put("cantidad", cantidad);
		ingreso.put("fecha", fecha);
		ingreso.put("dia", separarFecha(fecha, 1));
		ingreso.put("mes", separarFecha(fecha, 2));
		ingreso.put("año", separarFecha(fecha, 3));
		ingreso.put("descripcion", descripcion);
		ingreso.put("hora", hora);
		 
		//Insertamos el registro en la base de datos
		int exito = (int)db.insert("Ingresos", null, ingreso);
		
		db.close();
		
		return exito > 0 ? true : false;
	}
	
	public boolean eliminarIngreso(int id) {
		
		//Creamos el registro a insertar como objeto ContentValues
		String[] args = new String[] { String.valueOf(id) };
		 
		//Eliminamos el registro en la base de datos
		int exito = db.delete("Ingresos", "id=?", args);
		
		db.close();
		
		return exito > 0 ? true : false;
	}
	
	public boolean actualizarIngreso(String cantidad, String descripcion, String fecha, int id) {
		
		//Creamos el registro a insertar como objeto ContentValues
		String[] args = new String[] { String.valueOf(id) };
		
		ContentValues values = new ContentValues();
		values.put("cantidad", cantidad);
		values.put("descripcion", descripcion);
		 
		//Actualizamos el registro en la base de datos
		int exito = db.update("Ingresos", values, "id=?", args);
		
		db.close();
		
		return exito > 0 ? true : false;
	}
	
	public Cursor fetchIngresos() {
		
		Cursor c = db.query("Ingresos", new String[] { "cantidad", "descripcion", "fecha", "id" }, null, null, null, null, "fecha DESC");
		return c;
	}
	
	public Cursor fetchIngresosHistorial() {
		
		Cursor c = db.query("Ingresos", new String[] { "cantidad", "descripcion", "fecha", "id" }, null, null, null, null, null);
		return c;
	}
	
	public Cursor fetchIngresosDia(String fecha) {

		Cursor c = db.query("Ingresos", new String[] { "cantidad", "descripcion", "fecha", "id" }, "fecha=?", new String[] { fecha }, null, null, "fecha DESC");
		return c;
	}
	
	public Cursor fetchIngresosMes(String fecha) {

		Cursor c = db.query("Ingresos", new String[] { "cantidad", "descripcion", "fecha", "id" }, "mes=?", new String[] { separarFecha(fecha, 2) }, null, null, "fecha DESC");
		return c;
	}
	
	public Cursor fetchIngresosAño(String fecha) {

		Cursor c = db.query("Ingresos", new String[] { "cantidad", "descripcion", "fecha", "id" }, "año=?", new String[] { separarFecha(fecha, 3) }, null, null, "fecha DESC");
		return c;
	}
	
	public boolean insertarPago(double cantidad, String fecha, String hora) {
		
		//Creamos el registro a insertar como objeto ContentValues
		ContentValues pago = new ContentValues();
		pago.put("cantidad", cantidad);
		pago.put("fecha", fecha);
		pago.put("dia", separarFecha(fecha, 1));
		pago.put("mes", separarFecha(fecha, 2));
		pago.put("año", separarFecha(fecha, 3));
		pago.put("hora", hora);
		 
		//Insertamos el registro en la base de datos
		int exito = (int)db.insert("Pagos", null, pago);
		
		db.close();
		
		return exito > 0 ? true : false;
	}
	
	public Cursor fetchPagos() {
		
		Cursor c = db.query("Pagos", new String[] { "cantidad", "fecha", "hora" }, null, null, null, null, null);
		return c;
	}
}
