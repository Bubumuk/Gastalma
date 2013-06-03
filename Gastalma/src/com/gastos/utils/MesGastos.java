package com.gastos.utils;

public class MesGastos {
	
	private String mesString;
	private Integer mes;
	private Double costo;
	
	public MesGastos(String mesString, Integer mes, Double costo) {
		this.mesString = mesString;
		this.mes = mes;
		this.costo = costo;
	}

	public String getMesString() {
		return mesString;
	}

	public void setMesString(String mesString) {
		this.mesString = mesString;
	}

	public Integer getMes() {
		return mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}

	public Double getCosto() {
		return costo;
	}

	public void setCosto(Double costo) {
		this.costo = costo;
	}
}
