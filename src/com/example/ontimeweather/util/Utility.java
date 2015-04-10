package com.example.ontimeweather.util;

import android.text.TextUtils;

import com.example.ontimeweather.model.City;
import com.example.ontimeweather.model.County;
import com.example.ontimeweather.model.OntimeWeatherDB;
import com.example.ontimeweather.model.Province;
//解析和处理数据信息
public class Utility {

	//解析和处理服务器返回的省级信息
	public synchronized static boolean handleProvinceResponse(
			OntimeWeatherDB ontimeWeatherDB,String response){
		if(!TextUtils.isEmpty(response)){
			String[] allProvince = response.split(",");//用，号分割
			if(allProvince!=null&&allProvince.length>0){
				for(String p:allProvince){
					String[] array = p.split("\\|");//用单竖线分割
					Province province = new Province();//解析的数据设置到实体类中
					province.setProvinceCode(array[0]);
					province.setProvinceName(array[1]);
					//将解析处理的数据存储到Province表
					ontimeWeatherDB.saveProvince(province);
					
				}
				return true;
			}
		}
		return false;
  }
	
	//解析和处理服务器返回的市级信息
		public synchronized static boolean handleCitiesResponse(
				OntimeWeatherDB ontimeWeatherDB,String response,int provinceId){
			if(!TextUtils.isEmpty(response)){
				String[] allCities = response.split(",");
				if(allCities!=null&&allCities.length>0){
					for(String p:allCities){
						String[] array = p.split("\\|");
						City city = new City();
						city.setCityCode(array[0]);
						city.setCityName(array[1]);
						//将解析处理的数据存储到Province表
						ontimeWeatherDB.saveCity(city);
						
					}
					return true;
				}
			}
			return false;
	  }
		
		//解析县级信息
		public synchronized static boolean handleCountiesResponse(
				OntimeWeatherDB ontimeWeatherDB,String county,int cityId){
			if(TextUtils.isEmpty(county)){
				String[] allCounty = county.split(",");
				if(allCounty!=null&&allCounty.length>0){
					for(String c:allCounty){
						String[] array = c.split("\\|");
						County coun = new County();
						coun.setCountyCode(array[0]);
						coun.setCountyName(array[1]);
						ontimeWeatherDB.saveCountry(coun);
					}
					return true;
				}
			}
			return false;
		}
}