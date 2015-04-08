package com.example.ontimeweather.model;

public class County {

	private int Id;
	private String countyName;
	private String countyCode;
	private int cityId;
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public String getCountyName() {
		return countyName;
	}
	public void setCountyName(String countryName) {
		this.countyName = countryName;
	}
	public String getCountyCode() {
		return countyCode;
	}
	public void setCountyCode(String countryCode) {
		this.countyCode = countryCode;
	}
	public int getCityId() {
		return cityId;
	}
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}
}
