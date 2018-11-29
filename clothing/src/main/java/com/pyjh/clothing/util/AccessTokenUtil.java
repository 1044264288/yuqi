package com.pyjh.clothing.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 提供获取access_token的方�?
 * 提供发�?�http请求的方�?
 * @author Administrator
 *
 */
public class AccessTokenUtil {

	// 凭证获取（GET）�?��?�access_token
	public final static String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	

	public static Integer toInt(String str) {
		if (str == null || str.equals("")) {
			return null;
		}
		return Integer.valueOf(str);
	}
	
	/**
	 * 发�?�https请求
	 * 
	 * @param requestUrl
	 *            请求地址
	 * @param requestMethod
	 *            请求方式（GET、POST�?
	 * @param outputStr
	 *            提交的数�?
	 * @return rootNode(通过rootNode.get(key)的方式获取json对象的属性�??)
	 */
	public static JsonNode httpsRequest(String requestUrl, String requestMethod, String outputStr) {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = null;
		StringBuffer buffer = new StringBuffer();
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始�?
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");

			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setSSLSocketFactory(ssf);

			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			// 设置请求方式（GET/POST�?
			conn.setRequestMethod(requestMethod);
			//conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			if ("GET".equalsIgnoreCase(requestMethod))
				conn.connect();

			// 当outputStr不为null时向输出流写数据
			if (null != outputStr) {
				OutputStream outputStream = conn.getOutputStream();
				// 注意编码格式
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			// 从输入流读取返回内容
			InputStream inputStream = conn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}

			// 释放资源
			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			inputStream = null;
			conn.disconnect();
			rootNode = mapper.readTree(buffer.toString());
			/*
			InputStream is = conn.getInputStream(); 
			int size = is.available(); 
			byte[] b = new byte[size];
			is.read(b);

			String message = new String(b, "UTF-8");

			JSONObject json = JSONObject.fromObject(message);
			AccessToken accessToken = new AccessToken();
			accessToken.setAccessToken(json.getString("access_token"));
			accessToken.setExpiresIn(new Integer(json.getString("expires_in")));
			*/
		} catch (ConnectException ce) {			
			
//			log.error("连接超时：{}", ce);
		} catch (Exception e) {
			e.printStackTrace();
//			log.error("https请求异常：{}", e);
		}
		return rootNode;
	}
	
}
