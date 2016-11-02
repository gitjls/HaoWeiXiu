package com.haoweixiu.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class GetUpdateInfo {
	public static String getUpdataVerJSON(String serverPath) throws Exception{
		StringBuilder newVerJSON = new StringBuilder();
		HttpClient client = new DefaultHttpClient();//�½�http�ͻ���
		HttpParams httpParams = client.getParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 3000);//�������ӳ�ʱ��Χ
		HttpConnectionParams.setSoTimeout(httpParams, 5000);
		//serverPath��version.json��·��
		HttpResponse response = client.execute(new HttpGet(serverPath));
		HttpEntity entity = response.getEntity();
			if(entity != null){
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(entity.getContent(),"UTF-8"),8192);
			String line = null;
			while((line = reader.readLine()) != null){
				newVerJSON.append(line+"\n");//���ж�ȡ����StringBuilder��
			}
			reader.close();
		}
		return newVerJSON.toString();
	}
}
