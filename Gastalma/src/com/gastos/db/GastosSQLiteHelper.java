package com.gastos.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class GastosSQLiteHelper extends SQLiteOpenHelper {
 
    //Sentencia SQL para crear la tabla de Gastos
    private String sqlCreateGastos = "CREATE TABLE Gastos (" +
    		"id INTEGER PRIMARY KEY AUTOINCREMENT," +
    		"nombre TEXT," +
    		"fecha TEXT," +
    		"dia TEXT," +
    		"mes TEXT," +
    		"año TEXT," +
    		"costo NUMERIC," +
    		"descripcion TEXT," +
    		"tipo TEXT" +
    		"hora TEXT)";
    
    //Sentencia SQL para crear la tabla de Ingresos
    private String sqlCreateIngresos = "CREATE TABLE Ingresos (" +
    		"id INTEGER PRIMARY KEY AUTOINCREMENT," +
    		"cantidad NUMERIC," +
    		"fecha TEXT," +
    		"dia TEXT," +
    		"mes TEXT," +
    		"año TEXT," +
    		"descripcion TEXT)";
    
    //Sentencia SQL para crear la tabla de Pagos
    private String sqlCreatePagos = "CREATE TABLE Pagos (" +
    		"id INTEGER PRIMARY KEY AUTOINCREMENT," +
    		"cantidad NUMERIC," +
    		"gasto NUMERIC" +
    		"fecha TEXT," +
    		"dia TEXT," +
    		"mes TEXT," +
    		"año TEXT)";
 
    public GastosSQLiteHelper(Context contexto, String nombre, CursorFactory factory, int version) {
        super(contexto, nombre, factory, version);
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) {
        //Se ejecuta la sentencia SQL de creación de la tabla
        db.execSQL(sqlCreateGastos);
        db.execSQL(sqlCreateIngresos);
        db.execSQL(sqlCreatePagos);
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
        //NOTA: Por simplicidad del ejemplo aquí utilizamos directamente la opción de
        //      eliminar la tabla anterior y crearla de nuevo vacía con el nuevo formato.
        //      Sin embargo lo normal será que haya que migrar datos de la tabla antigua
        //      a la nueva, por lo que este método debería ser más elaborado.
    	
    	//Se copia la tabla a una nueva
    	db.execSQL("CREATE TABLE Gastos_copia AS SELECT * FROM Gastos");
    	db.execSQL("CREATE TABLE Ingresos_copia AS SELECT * FROM Ingresos");
    	db.execSQL("CREATE TABLE Pagos_copia AS SELECT * FROM Pagos");
 
        //Se elimina la versión anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS Gastos");
        db.execSQL("DROP TABLE IF EXISTS Ingresos");
        db.execSQL("DROP TABLE IF EXISTS Pagos");
        
        //Se renombran las copias
        db.execSQL("ALTER TABLE Gastos_copia RENAME TO Gastos");
        db.execSQL("ALTER TABLE Ingresos_copia RENAME TO Ingresos");
        db.execSQL("ALTER TABLE Pagos_copia RENAME TO Pagos");
        
        /*//Se crea la nueva versión de la tabla
        db.execSQL(sqlCreateGastos);
        db.execSQL(sqlCreateIngresos);
        db.execSQL(sqlCreatePagos);*/
    }
}