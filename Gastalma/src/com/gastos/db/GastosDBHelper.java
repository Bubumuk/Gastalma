package com.gastos.db;

import java.util.ArrayList;
import java.util.List;

import com.gastos.utils.Ingreso;

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
	
	public boolean insertarGasto(String nombre, String fecha, Double costo, String descripcion, String tipo) {
		
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
		 
		//Insertamos el registro en la base de datos
		int exito = (int)db.insert("Gastos", null, gasto);
		
		db.close();
		
		return exito > 0 ? true : false;
	}
	
	public boolean eliminarGasto(String nombre, String fecha) {
		
		//Creamos el registro a insertar como objeto ContentValues
		String[] args = new String[] {nombre, fecha};
		 
		//Eliminamos el registro en la base de datos
		int exito = db.delete("Gastos", "nombre=? AND fecha=?", args);
		
		db.close();
		
		return exito > 0 ? true : false;
	}
	
	public boolean actualizarGasto(String nombre, String costo, String descripcion, String fecha, int id) {
		
		//Creamos el registro a insertar como objeto ContentValues
		String[] args = new String[] { String.valueOf(id) };
		
		ContentValues values = new ContentValues();
		values.put("nombre", nombre);
		values.put("costo", costo);
		values.put("descripcion", descripcion);
		values.put("fecha", fecha);
		 
		//Actualizamos el registro en la base de datos
		int exito = db.update("Gastos", values, "id=?", args);
		
		db.close();
		
		return exito > 0 ? true : false;
	}

	public Cursor fetchGastos(String fecha) {
		
		Cursor c = db.query("Gastos", new String[] { "nombre", "costo", "tipo", "descripcion", "id" }, "fecha=?", new String[] { fecha }, null, null, null);
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
	
	public boolean insertarIngreso(Double cantidad, String fecha, String descripcion) {
		
		//Creamos el registro a insertar como objeto ContentValues
		ContentValues ingreso = new ContentValues();
		ingreso.put("cantidad", cantidad);
		ingreso.put("fecha", fecha);
		ingreso.put("dia", separarFecha(fecha, 1));
		ingreso.put("mes", separarFecha(fecha, 2));
		ingreso.put("año", separarFecha(fecha, 3));
		ingreso.put("descripcion", descripcion);
		 
		//Insertamos el registro en la base de datos
		int exito = (int)db.insert("Ingresos", null, ingreso);
		
		db.close();
		
		return exito > 0 ? true : false;
	}
	
	public boolean eliminarIngreso(String cantidad, String fecha) {
		
		//Creamos el registro a insertar como objeto ContentValues
		String[] args = new String[] {cantidad, fecha};
		 
		//Eliminamos el registro en la base de datos
		int exito = db.delete("Ingresos", "cantidad=? AND fecha=?", args);
		
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
	
	public Cursor fetchIngresos(String mes) {
		
		Cursor c = db.query("Ingresos", new String[] { "cantidad", "descripcion", "fecha", "id" }, "mes=?", new String[] { mes }, null, null, "fecha DESC");
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

	public String[] fetchDatos() {
		String ingresos, gastos;
		Cursor c;
		c = db.rawQuery("SELECT SUM(cantidad) FROM Ingresos", null);
		ingresos = c.moveToNext() ? (c.getString(0) != null) ? c.getString(0) : "0" : "0";
		c = db.rawQuery("SELECT SUM(costo) FROM Gastos WHERE tipo=\'Crédito\'", null);
		gastos = c.moveToNext() ? (c.getString(0) != null) ? c.getString(0) : "0" : "0";
		    
		return new String[] {ingresos,gastos};
	}
	
	public boolean insertarPago(Double cantidad, String fecha, int id) {
		
		//Creamos el registro a insertar como objeto ContentValues
		ContentValues pago = new ContentValues();
		pago.put("cantidad", cantidad);
		pago.put("gasto", id);
		pago.put("fecha", fecha);
		pago.put("dia", separarFecha(fecha, 1));
		pago.put("mes", separarFecha(fecha, 2));
		pago.put("año", separarFecha(fecha, 3));
		 
		//Insertamos el registro en la base de datos
		int exito = (int)db.insert("Pagos", null, pago);
		
		db.close();
		
		return exito > 0 ? true : false;
	}
	
	public Cursor fetchPagos() {
		
		Cursor c = db.query("Pagos", new String[] { "cantidad", "fecha" }, null, null, null, null, null);
		return c;
	}
	
	public String fetchDeuda() {
		Cursor c = db.rawQuery("SELECT SUM(Costo) FROM Gastos WHERE tipo='Crédito'", null);
		return c.moveToNext() ? (c.getString(0) != null) ? c.getString(0) : "0" : "0";
	}
	
	private Ingreso[][] separarList(List<ArrayList<Ingreso>> fechas) {
		Ingreso[][] array = new Ingreso[fechas.size()][];
		Ingreso[] ar;
		for(int i=0; i < fechas.size(); i++) {
			ar = new Ingreso[fechas.get(i).size()];
		    array[i] = (Ingreso[])fechas.get(i).toArray(ar);
		}
		return array;
	}
	
	public String[] fetchIngresosDiasHeaders(String mes) {
		List<String> fechas = new ArrayList<String>();
		Cursor c;
		c = db.rawQuery("SELECT DISTINCT fecha FROM Ingresos WHERE mes='"+mes+"'", null);
		
		if (c.moveToFirst()) {
		     do {
		    	 fechas.add(c.getString(0));
		     } while(c.moveToNext());
		}
		String []strArray = new String[fechas.size()];
		return (String[]) fechas.toArray(strArray);
	}
	
	public int obtenerNumHeadersDia(String mes) {
		Cursor c;
		c = db.rawQuery("SELECT DISTINCT fecha FROM Ingresos where mes='"+mes+"'", null);
		return c.getCount();
	}
	 
	public Ingreso[][] fetchIngresosPorDia(String mes) {
		List<ArrayList<Ingreso>> fechas = new ArrayList<ArrayList<Ingreso>>();
		ArrayList<Ingreso> temp = new ArrayList<Ingreso>();
		Cursor c;
		c = db.rawQuery("SELECT cantidad,descripcion,fecha,id FROM Ingresos WHERE mes='"+mes+"' ORDER BY fecha DESC", null);
		String fecha = "";
		if (c.moveToFirst()) {
			fecha=c.getString(2);
			do {
				if(fecha.equals(c.getString(2))) {
					temp.add(new Ingreso(c.getString(0),c.getString(1),c.getString(2),c.getInt(3)));
		    	 } else {
		    		 fechas.add(temp);
		    		 temp = new ArrayList<Ingreso>();
		    		 temp.add(new Ingreso(c.getString(0),c.getString(1),c.getString(2),c.getInt(3)));
		    		 fecha=c.getString(2);
		    	 }
			} while(c.moveToNext());
			fechas.add(temp);
		}
		    
		return separarList(fechas);
	}

	public int obtenerNumHeadersAño(String año) {
		Cursor c;
		c = db.rawQuery("SELECT DISTINCT mes FROM Ingresos where año='"+año+"'", null);
		return c.getCount();
	}

	public String[] fetchIngresosMesHeaders(String año) {
		List<String> fechas = new ArrayList<String>();
		Cursor c;
		c = db.rawQuery("SELECT DISTINCT mes FROM Ingresos WHERE año='"+año+"'", null);
		
		if (c.moveToFirst()) {
		     do {
		    	 fechas.add(c.getString(0));
		     } while(c.moveToNext());
		}
		String []strArray = new String[fechas.size()];
		return (String[]) fechas.toArray(strArray);
	}
	
	public int obtenerNumHeadersMes(String mes) {
		Cursor c;
		c = db.rawQuery("SELECT DISTINCT fecha FROM Ingresos where mes='"+mes+"'", null);
		return c.getCount();
	}
	 
	public Ingreso[][] fetchIngresosPorMes(String mes) {
		List<ArrayList<Ingreso>> fechas = new ArrayList<ArrayList<Ingreso>>();
		ArrayList<Ingreso> temp = new ArrayList<Ingreso>();
		Cursor c;
		c = db.rawQuery("SELECT cantidad,descripcion,fecha,id FROM Ingresos WHERE mes='"+mes+"' ORDER BY fecha DESC", null);
		String fecha = "";
		if (c.moveToFirst()) {
			fecha=c.getString(2);
			do {
				if(fecha.equals(c.getString(2))) {
					temp.add(new Ingreso(c.getString(0),c.getString(1),c.getString(2),c.getInt(3)));
		    	 } else {
		    		 fechas.add(temp);
		    		 temp = new ArrayList<Ingreso>();
		    		 temp.add(new Ingreso(c.getString(0),c.getString(1),c.getString(2),c.getInt(3)));
		    		 fecha=c.getString(2);
		    	 }
			} while(c.moveToNext());
			fechas.add(temp);
		}
		    
		return separarList(fechas);
	}
}
