package com.gastos.utils;

public class Pago {
	
	private double cantidad;
	private String fecha;
	private String hora;
	
	public Pago(double cantidad, String fecha, String hora) {
		this.setCantidad(cantidad);
		this.setFecha(fecha);
		this.setHora(hora);
	}

	public double getCantidad() {
		return cantidad;
	}

	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}
}
