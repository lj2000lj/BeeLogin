package cn.apisium.beelogin;

import static cn.apisium.util.ChecksumUtil.md5;
import static cn.apisium.util.ChecksumUtil.md5twice;
import static cn.apisium.util.HttpUtil.send;

import org.bukkit.craftbukkit.libs.com.google.gson.Gson;

import cn.apisium.util.HttpUtil.HttpRequestType;

public class LoginResult {
	private String rawData;
	private boolean result = false;
	private GsonResult resultObj;

	public LoginResult(String url, String queryMode, String encodeMode, String playerName, String playerIp,
			String serverIp, String serverPassword) {
		this(url, queryMode, encodeMode, playerName, playerIp, serverIp, serverPassword, "");
	}

	public LoginResult(String url, String queryMode, String encodeMode, String playerName, String playerIp,
			String serverIp, String serverPassword, String playerToken) {
		rawData = getRawCheckData(url, queryMode, encodeMode, playerName, playerIp, serverIp, serverPassword,
				playerToken);
		if (rawData.equalsIgnoreCase(Config.allowJoinResult)) {
			result = true;
			return;
		}
		resultObj = new Gson().fromJson(rawData, GsonResult.class);
		result = resultObj.result;
	}

	public boolean isBadIp() {
		return rawData.equalsIgnoreCase(Config.badIpResult) || resultObj.reason.equalsIgnoreCase(Config.badIpResult);
	}

	public boolean getResult() {
		return result;
	}

	public static boolean getLoginResult() {
		return false;
	}

	public static String getRawCheckData(String url, String queryMode, String encodeMode, String playerName,
			String playerIp, String serverIp, String serverPassword) {
		return getRawCheckData(url, queryMode, encodeMode, playerName, playerIp, serverIp, serverPassword, "");
	}

	public static String getRawCheckData(String url, String queryMode, String encodeMode, String playerName,
			String playerIp, String serverIp, String serverPassword, String playerToken) {
		HttpRequestType type = queryMode.equalsIgnoreCase("post") ? HttpRequestType.POST : HttpRequestType.GET;
		EncodingType encodingType = encodeMode.equalsIgnoreCase("md5") ? EncodingType.MD5 : EncodingType.TEXT;
		return send(url, getParameter(playerName, playerIp, serverIp, serverPassword, playerToken, encodingType), type);
	}

	public static String getParameter(String playerName, String playerIp, String serverIp, String serverPassword,
			String playerToken, EncodingType encodingType) {
		switch (encodingType) {
		case MD5:
			return "username=" + md5(playerName).toLowerCase() + "&ip=" + md5twice(playerIp) + "&sip="
					+ md5twice(serverIp) + "&spsd=" + md5twice(serverPassword) + "&token=" + md5twice(playerToken);

		default:
			return "username=" + playerName + "&ip=" + playerIp + "&sip=" + serverIp + "&spsd=" + serverPassword
					+ "&token=" + playerToken;
		}
	}

	public static enum EncodingType {
		MD5, TEXT;
	}
}
