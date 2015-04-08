package com.example.ontimeweather.model;

import java.util.ArrayList;
import java.util.List;

import com.example.ontimeweather.db.OntimeWeatherOpenHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class OntimeWeatherDB {

	//���ݿ�����
	public static final String DB_NAME = "OntimeWeather";
	
	//���ݿ�汾
	public static final int VERSION = 1;
	private static OntimeWeatherDB ontimeWeatherDB;
	private SQLiteDatabase db;//���һ�����ݿ����
	
	//�����췽��˽�л�
	private OntimeWeatherDB(Context context){
		OntimeWeatherOpenHelper dbHelper = new OntimeWeatherOpenHelper(context, DB_NAME, null, VERSION);
	    db = dbHelper.getWritableDatabase();
	}
	
	//��ȡOntimeWeatherDB��ʵ��,ʹ��һ����������֤�������߳�ʹ���������
	public synchronized static OntimeWeatherDB getInstance(Context context){
		if(ontimeWeatherDB == null){
			ontimeWeatherDB = new OntimeWeatherDB(context);
		}
		return ontimeWeatherDB;
	}
	
	//��provinceʵ���洢�����ݿ�
	public void saveProvince(Province province){
		if(province!=null){
			ContentValues values = new ContentValues();//ʹ��һ��contentValue��ֵ�Դ洢
			values.put("province_name", province.getProvinceName());
			values.put("province_code", province.getProvinceCode());
			db.insert("province", null, values);
		}
	}
	
	//�����ݿ��ȡȫ������ʡ�ݵ���Ϣ
	public List<Province> loadProvinces(){
		List<Province> list = new ArrayList<Province>();
		Cursor cursor = db.query("Province", null, null, null, null, null, null);
		if(cursor.moveToFirst()){
			do{
				Province province = new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
				province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
				list.add(province);
			}while(cursor.moveToNext());
		}
		if(cursor!=null){
			cursor.close();
		}
		return list;
	}
	
	//��cityʵ���洢�����ݿ�
	public void saveCity(City city){
		if(city!=null){
			ContentValues values = new ContentValues();
			values.put("city_name", city.getCityName());
			values.put("city_code", city.getCityCode());
			values.put("province_Id", city.getProvinceId());
			db.insert("city", null, values);
		}
	}
	
	//���ݿ��ȡĳʡʡ�����г�����Ϣ
	public List<City> loadCity(int provinceId){
		List<City> list = new ArrayList<City>();
		Cursor cursor = db.query("City", null, "province_id = ?", new String[]{String.valueOf("provinceId")}, null, null, null);
		if(cursor.moveToFirst()){
			do{
				City city = new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCityName(cursor.getString(cursor.getColumnIndex("City_name")));
				city.setCityCode(cursor.getString(cursor.getColumnIndex("City_code")));
				city.setProvinceId(provinceId);
				list.add(city);
			}while(cursor.moveToNext());
		}
		if(cursor!=null){
			cursor.close();
		}
		return list;
	}
	
	//��Countyʵ���洢�����ݿ�
	public void saveCountry(County county){
		if(county!=null){
			ContentValues values = new ContentValues();
			values.put("county_name", county.getCountyName());
			values.put("county_code", county.getCountyCode());
			values.put("city_Id", county.getCityId());
			db.insert("County", null, values);
		}
	}
	
	//�����ݿ��ȡĳ�����������ص���Ϣ
	public List<County> loadCounty(int CityId){
		List<County> list = new ArrayList<County>();
		Cursor cursor = db.query("county", null, "city_id = ?", new String[]{String.valueOf("cityId")}, null, null, null);
		if(cursor.moveToFirst()){
			do{
				County county = new County();
				county.setId(cursor.getInt(cursor.getColumnIndex("id")));
				county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
				county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
				county.setCityId(CityId);
				list.add(county);
			}while(cursor.moveToNext());
		}
		if(cursor!=null){
			cursor.close();
		}
		return list;
	}
}
