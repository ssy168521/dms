package com.web.form;

public class Staticstis {
	private int id;
	private String satellite;
	private String sensor;
	private int sucess_count;
	private int failed_count;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSatellite() {
		return satellite;
	}
	public void setSatellite(String satellite) {
		this.satellite = satellite;
	}
	public String getSensor() {
		return sensor;
	}
	public void setSensor(String sensor) {
		this.sensor = sensor;
	}
	public int getSucess_count() {
		return sucess_count;
	}
	public void setSucess_count(int sucessCount) {
		sucess_count = sucessCount;
	}
	public int getFailed_count() {
		return failed_count;
	}
	public void setFailed_count(int failedCount) {
		failed_count = failedCount;
	}
}
