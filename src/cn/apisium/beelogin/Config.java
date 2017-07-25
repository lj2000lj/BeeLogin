package cn.apisium.beelogin;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config {
	public static String serverIp = Bukkit.getIp();
	public static String kickMessage;
	public static String badIpMessage;
	public static String unknowErrorMessage;
	public static String serverPassword;
	public static String badIpResult;
	public static String allowJoinResult;
	public static String checkUrl;
	public static String sendIpUrl;
	public static String queryMode;
	public static String encodeMode;
	public static Boolean skipOnLoopbackAddress;
	public static Boolean skipOnSiteLocalAddress;
	public static Boolean usingBeeLoginMod;
	public static Boolean enableExperimentalFunctions;
	public static long timeOutLimited;
	public static String timeOutMessage;
	public static long verifyDelay;

	public static void load(FileConfiguration config) {
		config.addDefault("ConfigVersion", "0");
		config.addDefault("KickMessage", "请修改配置文件!并确定文件编码！");
		config.addDefault("BadIpMessage", "请修改配置文件!并确定文件编码！您的IP是：%UserIp%");
		config.addDefault("UnknowErrorMessage", "请修改配置文件!并确定文件编码！您的IP是：%UserIp%");
		config.addDefault("ServerPassword", "");
		config.addDefault("BadIpResult", "badip");
		config.addDefault("AllowJoinResult", "yes");
		config.addDefault("CheckUrl", "");
		config.addDefault("SendIpUrl", "");
		config.addDefault("QueryMode", "GET");
		config.addDefault("EncodeMode", "MD5");
		config.addDefault("SkipOnLoopbackAddress", false);
		config.addDefault("SkipOnSiteLocalAddress", false);
		config.addDefault("UsingBeeLoginMod", false);
		config.addDefault("EnableExperimentalFunctions", false);
		config.addDefault("TimeOutLimited", 100);
		config.addDefault("TimeOutMessage", "登录超时");
		config.addDefault("VerifyDelay", 20);
		kickMessage = config.getString("KickMessage");
		badIpMessage = config.getString("BadIpMessage");
		unknowErrorMessage = config.getString("UnknowErrorMessage");
		serverPassword = config.getString("ServerPassword");
		badIpResult = config.getString("BadIpResult");
		allowJoinResult = config.getString("AllowJoinResult");
		checkUrl = config.getString("CheckUrl");
		sendIpUrl = config.getString("SendIpUrl");
		queryMode = config.getString("QueryMode");
		encodeMode = config.getString("EncodeMode");
		skipOnLoopbackAddress = config.getBoolean("SkipOnLoopbackAddress");
		skipOnSiteLocalAddress = config.getBoolean("SkipOnSiteLocalAddress");
		usingBeeLoginMod = config.getBoolean("UsingBeeLoginMod");
		enableExperimentalFunctions = config.getBoolean("EnableExperimentalFunctions");
		timeOutLimited = config.getLong("TimeOutLimited");
		timeOutMessage = config.getString("TimeOutMessage");
		verifyDelay = config.getLong("VerifyDelay");
	}

	public static void save() {
		YamlConfiguration config = new YamlConfiguration();
		config.set("KickMessage", Config.kickMessage);
		config.set("BadIpMessage", Config.badIpMessage);
		config.set("UnknowErrorMessage", Config.unknowErrorMessage);
		config.set("ServerPassword", Config.serverPassword);
		config.set("BadIpResult", Config.badIpResult);
		config.set("AllowJoinResult", Config.allowJoinResult);
		config.set("CheckUrl", Config.checkUrl);
		config.set("SendIpUrl", Config.sendIpUrl);
		config.set("QueryMode", Config.queryMode);
		config.set("EncodeMode", Config.encodeMode);
		config.set("SkipOnLoopbackAddress", Config.skipOnLoopbackAddress);
		config.set("SkipOnSiteLocalAddress", Config.skipOnSiteLocalAddress);
		config.set("UsingBeeLoginMod", Config.usingBeeLoginMod);
		config.set("EnableExperimentalFunctions", Config.enableExperimentalFunctions);
		config.set("TimeOutLimited", Config.timeOutLimited);
		config.set("TimeOutMessage", Config.timeOutMessage);
		config.set("VerifyDelay", Config.verifyDelay);
		try {
			config.save(new File(BeeLogin.instance.getDataFolder(), "config.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
