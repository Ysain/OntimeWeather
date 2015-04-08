package com.example.ontimeweather.db;
//所有数据相关代码
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class OntimeWeatherOpenHelper extends SQLiteOpenHelper{

	//Province表建表语句
	public static final String CREATE_PROVINCE = "create table Province("
			+"id integer primary key autoincrement,"
			+"province_name text,"
			+"province_code text)";
	//City表建表语句
	public static final String CREATE_City = "create table City("
	        +"id integer primary key autoincrement,"
			+"city_name text,"
	        +"city_code text,"
			+"province_id integer)";
	//Country表建表语句
	public static final String CREATE_COUNTRY ="create table Country("
			+"id integer primary key autoincrement,"
			+"country_name text,"
			+"country_code text,"
			+"city_id integer)";
	public OntimeWeatherOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		arg0.execSQL(CREATE_PROVINCE);
		arg0.execSQL(CREATE_City);
		arg0.execSQL(CREATE_PROVINCE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		
	}

}
