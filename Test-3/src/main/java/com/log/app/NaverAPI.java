package com.log.app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.UUID;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class NaverAPI {

	public String getAccessToken(String code) {
		String state = UUID.randomUUID().toString();
		String accessToken = "";
		String reqURL = "https://nid.naver.com/oauth2.0/token";
		
		try {
			URL url = new URL(reqURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			StringBuilder sb = new StringBuilder();
			sb.append("grant_type=authorization_code");
			sb.append("&client_id=HITMO18RZpwaCpSaX0Vy");
			sb.append("&client_secret=LhKoiaiay5");
			sb.append("&redirect_uri=http://localhost:8080/login/naver");
			sb.append("&code="+code);
			sb.append("&state="+state);
			bw.write(sb.toString());
			bw.flush();
			
			int responseCode = conn.getResponseCode();
			System.out.println("response code = " + responseCode);
			
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = "";
			String result = "";
			
			while((line = br.readLine())!=null) {
				result += line;
			}
			System.out.println("response body="+result);
			
			JsonParser parser = new JsonParser();
			JsonElement element = parser.parse(result);
			
			accessToken = element.getAsJsonObject().get("access_token").getAsString();
			//refreshToken = element.getAsJsonObject().get("refresh_token").getAsString();
			
			br.close();
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return accessToken;
	}

	
	public HashMap<String, Object> getUserInfo(String accessToken) {
		HashMap<String, Object> userInfo = new HashMap<String, Object>();
		String reqUrl = "https://openapi.naver.com/v1/nid/me";
		try {
			URL url = new URL(reqUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			
			conn.setRequestProperty("Authorization", "Bearer " + accessToken);
			
			int responseCode = conn.getResponseCode();
			System.out.println("responseCode =" + responseCode);
			
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			String line = "";
			String result = "";
			
			while((line = br.readLine()) != null) {
				result += line;
			}
			System.out.println("response body ="+result);
	
			JsonParser parser = new JsonParser();
			JsonElement element =  parser.parse(result);
			
			JsonObject response = element.getAsJsonObject().get("response").getAsJsonObject();
			
			String name = response.getAsJsonObject().get("name").getAsString();
	        String email = response.getAsJsonObject().get("email").getAsString();
			
			userInfo.put("name", name);
			userInfo.put("email", email);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userInfo;
	}

	public void naverLogout(String accessToken) {
		String reqURL = "https://nid.naver.com/oauth2.0/token?grant_type=delete&client_id=HITMO18RZpwaCpSaX0Vy&client_secret=LhKoiaiay5&access_token=" + accessToken + "&service_provider=NAVER";
		try {
			URL url = new URL(reqURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Authorization", "Bearer " + accessToken);
			int responseCode = conn.getResponseCode();
			System.out.println("responseCode = " + responseCode);
			
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			String result = "";
			String line = "";
			
			while((line = br.readLine()) != null) {
				result+=line;
			}
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 
}