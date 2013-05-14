package com.gastos.utils;

public class Ingreso {
	private String cantidad;
	private String fecha;
	private String mes;
	private String descripcion;
	private String hora;
	private int id;
	
	public Ingreso(String cantidad, String descripcion, String fecha, String hora, int id) {
		this.id = id;
		this.cantidad = cantidad;
		this.fecha = fecha;
		this.descripcion = descripcion;
		this.hora = hora;
	}
	
	public Ingreso() {
		
	}
	
	public String getCantidad() {
		return cantidad;
	}
	public void setCantidad(String cantidad) {
		this.cantidad = cantidad;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getMes() {
		return mes;
	}
	public void setMes(String mes) {
		this.mes = mes;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getHora() {
		return hora;
	}
	public void setHora(String hora) {
		this.hora = hora;
	}
}
