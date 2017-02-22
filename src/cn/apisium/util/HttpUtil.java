package cn.apisium.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;

public class HttpUtil {
	final static Logger logger = Bukkit.getLogger();
	final static String USER_AGENT = "Mozilla/5.0 Apisium/1.0";

	public static enum HttpRequestType {
		GET, POST;
	}

	public static String send(String url, String parameters, HttpRequestType type) {
		switch (type) {
		case GET:
			return get(url, parameters);
		case POST:
			return post(url, parameters);
		default:
			return "";
		}
	}

	public static String get(String url, String parameters) {
		try {
			URL urlObject = new URL(url + "?" + parameters);
			HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("User-Agent", USER_AGENT);
			BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			StringBuffer response = new StringBuffer();
			while ((line = input.readLine()) != null) {
				response.append(line);
			}
			input.close();
			return response.toString();
		} catch (MalformedURLException e) {
			logger.log(Level.WARNING, "网址" + url + "的格式不正确", e);
		} catch (IOException e) {
			logger.log(Level.WARNING, "获取网页" + url + "的时候发生了错误", e);
		}
		return null;
	}

	public static String post(String url, String parameters) {
		try {
			URL urlObject = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("User-Agent", USER_AGENT);
			connection.setDoOutput(true);
			DataOutputStream postWrite = new DataOutputStream(connection.getOutputStream());
			postWrite.writeBytes(parameters);
			postWrite.flush();
			postWrite.close();
			BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			StringBuffer response = new StringBuffer();
			while ((line = input.readLine()) != null) {
				response.append(line);
			}
			input.close();
			return response.toString();
		} catch (MalformedURLException e) {
			logger.log(Level.WARNING, "网址" + url + "的格式不正确", e);
		} catch (IOException e) {
			logger.log(Level.WARNING, "获取网页" + url + "的时候发生了错误", e);
		}
		return null;

	}
}
