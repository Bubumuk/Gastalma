package com.gastos.utils;

public class Gasto {
	private String costo;
	private String tipo;
	private String nombre;
	private String descripcion;
	private String fecha;
	private int id;
	
	public Gasto(String nombre, String costo, String tipo, String descripcion, String fecha, int id) {
		this.id = id;
		this.nombre = nombre;
		this.costo = costo;
		this.tipo  = tipo;
		this.descripcion = descripcion;
		this.fecha = fecha;
	}
	
	public Gasto() {
		
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getCosto() {
		return costo;
	}
	public void setCosto(String costo) {
		this.costo = costo;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
