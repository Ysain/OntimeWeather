package com.example.ontimeweather.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ontimeweather.R;
import com.example.ontimeweather.model.City;
import com.example.ontimeweather.model.County;
import com.example.ontimeweather.model.OntimeWeatherDB;
import com.example.ontimeweather.model.Province;
import com.example.ontimeweather.util.HttpCallbackListener;
import com.example.ontimeweather.util.HttpUtil;
import com.example.ontimeweather.util.Utility;
//����ʡ�������ݵĻ
public class ChooseAreaActivity extends Activity{

	public static final int LEVEL_PROVINCE = 0;
	public static final int LEVEL_CITY = 1;
	public static final int LEVEL_COUNTY =2;
	
	private ProgressDialog progressDialog;
	private TextView titleText;
	private ListView listView;
	private ArrayAdapter<String> adapter;
	private OntimeWeatherDB ontimeWeatherDB;
	private List<String> dataList = new ArrayList<String>();
	
	//ʡ�б�
	private List<Province> provinceList;
	
	//���б�
	private List<City> cityList;
	
	//���б�
	private List<County> countyList;
	
	//ѡ�е�ʡ��
	private Province selectedProvince;
	
	//ѡ�еĳ���
	private City selectedCity;
	
	//ѡ�еļ���
	private int currentLevel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//��ȡ�ؼ�
		setContentView(R.layout.choose_area);
		listView = (ListView)findViewById(R.id.list_view);
		titleText = (TextView)findViewById(R.id.title_text);
		//��ʼ��adapter������Ϊlistview������
		adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dataList);
		listView.setAdapter(adapter);
		ontimeWeatherDB = OntimeWeatherDB.getInstance(this);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(currentLevel==LEVEL_PROVINCE){
					selectedProvince = provinceList.get(arg2);
					queryCities();
				}
				else if (currentLevel==LEVEL_CITY){
					selectedCity = cityList.get(arg2);
					queryCounties();
				}
			}
		});
		queryProvinces();//����ʡ������
	}
	
	//��ѯȫ������ʡ�����ȴ����ݿ��ѯ�����û�оʹӷ������ϲ�ѯ
	private void queryProvinces(){
		provinceList = ontimeWeatherDB.loadProvinces();
		if(provinceList.size()>0){
			dataList.clear();
			for(Province province:provinceList){
				dataList.add(province.getProvinceName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText("�й�");
			currentLevel = LEVEL_PROVINCE;
		}
		else{
			queryFromServer(null,"province");
		}
	}
	
	//��ѯʡ�����е���
	private void queryCities(){
		cityList = ontimeWeatherDB.loadCity(selectedProvince.getId());
		if(cityList.size()>0){
			dataList.clear();
			for(City city:cityList){
				dataList.add(city.getCityName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectedCity.getCityName());
			currentLevel = LEVEL_CITY;
		}
		else{
			queryFromServer(selectedProvince.getProvinceCode(),"city");
		}
	}
	
	//��ѯ�������е���
	private void queryCounties(){
		countyList = ontimeWeatherDB.loadCounty(selectedCity.getId());
		if(countyList.size()>0){
			dataList.clear();
			for(County coun:countyList){
				dataList.add(coun.getCountyName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectedCity.getCityName());
			currentLevel = LEVEL_COUNTY;
		}
		else{
			queryFromServer(selectedCity.getCityCode(),"county");
		}
	}
	
	//���ݴ���Ĵ��ź����ʹӷ������ϲ�ѯ��������
	private void queryFromServer(final String code,final String type){
		String address;
		if(!TextUtils.isEmpty(code)){
			address = "http://www.weather.com.cn/data/list3/city"+code+".xml";
		}
		else{
			address = "http://www.weather.com.cn/data/list3/city.xml";
		}
		showProgressDialog();
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			@Override
			public void onFinish(String response) {
				boolean result = false;
				if("province".equals(type)){
					result = Utility.handleProvinceResponse(ontimeWeatherDB, response);
				}
				else if("city".equals(type)){
					result = Utility.handleCitiesResponse(ontimeWeatherDB, response, selectedProvince.getId());
				}
				else if("county".equals(type)){
					result = Utility.handleCountiesResponse(ontimeWeatherDB, response, selectedCity.getId());
				}
				
				if(result){
					//ͨ��runOnUiThread()�����ص����̴߳����߼�
					runOnUiThread(new Runnable(){//����handle֮����һ�ָ���UI����
						@Override
						public void run() {
							closeProgressDialog();
							if("province".equals(type)){
								queryProvinces();
							}
							else if("city".equals(type)){
								queryCities();
							}
							else if("county".equals(type)){
								queryCounties();
							}
						}
					});
				}
			}
			@Override
			public void onError(Exception e) {
				//ͨ��runOnUiThread()�����ص����̴߳����߼�
				runOnUiThread(new Runnable(){
					@Override
					public void run() {
						closeProgressDialog();
						Toast.makeText(ChooseAreaActivity.this, "����ʧ��", Toast.LENGTH_SHORT);
					}
				});
			}
		});
	}
	
	//��ʾ���ȶԻ���
	private void showProgressDialog(){
		if(progressDialog==null){
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("���ڼ���...");
			progressDialog.setCanceledOnTouchOutside(false);//���õ����ĻDialog�Ƿ���ʧ
		}
		progressDialog.show();
	}
	
	//�رս��ȶԻ���
	private void closeProgressDialog(){
		if(progressDialog!=null){
			progressDialog.dismiss();
		}
	}

	
	//back���񰴼������ݵ�ǰ�ļ������жϣ���ʱӦ�÷������б�ʡ�б���ֱ���˳�

	@Override
	public void onBackPressed() {
		if(currentLevel==LEVEL_COUNTY){
			queryCities();
		}
		else if(currentLevel==LEVEL_CITY){
			queryProvinces();
		}
		else{
			finish();
		}
	}
}
