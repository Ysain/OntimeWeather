package com.example.ontimeweather.util;

import android.text.TextUtils;

import com.example.ontimeweather.model.City;
import com.example.ontimeweather.model.County;
import com.example.ontimeweather.model.OntimeWeatherDB;
import com.example.ontimeweather.model.Province;
//�����ʹ���������Ϣ
public class Utility {

	//�����ʹ�����������ص�ʡ����Ϣ
	public synchronized static boolean handleProvinceResponse(
			OntimeWeatherDB ontimeWeatherDB,String response){
		if(!TextUtils.isEmpty(response)){
			String[] allProvince = response.split(",");//�ã��ŷָ�
			if(allProvince!=null&&allProvince.length>0){
				for(String p:allProvince){
					String[] array = p.split("\\|");//�õ����߷ָ�
					Province province = new Province();//�������������õ�ʵ������
					province.setProvinceCode(array[0]);
					province.setProvinceName(array[1]);
					//��������������ݴ洢��Province��
					ontimeWeatherDB.saveProvince(province);
					
				}
				return true;
			}
		}
		return false;
  }
	
	//�����ʹ�����������ص��м���Ϣ
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
						//��������������ݴ洢��Province��
						ontimeWeatherDB.saveCity(city);
						
					}
					return true;
				}
			}
			return false;
	  }
		
		//�����ؼ���Ϣ
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