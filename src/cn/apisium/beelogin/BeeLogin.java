package cn.apisium.beelogin;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import cn.apisium.beelogin.handler.EventCanceler;
import cn.apisium.beelogin.handler.LoginEventHandler;
import cn.apisium.beelogin.handler.MessageHandler;
import cn.apisium.util.DefaultMap;

public class BeeLogin extends JavaPlugin implements Listener {
	public static BeeLogin instance;
	public static Logger logger;
	private final EventCanceler eventCanceler = new EventCanceler();
	private final LoginEventHandler eventHandler = new LoginEventHandler();

	public final static DefaultMap<String, BukkitTask> kickers = new DefaultMap<String, BukkitTask>(null);
	public final static DefaultMap<String, Boolean> loginStatus = new DefaultMap<String, Boolean>(false);

	public void onEnable() {
		instance = this;
		logger = this.getLogger();
		Config.load(this.getConfig());
		if (Config.usingBeeLoginMod) {
			Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BeeLogin");
			Bukkit.getMessenger().registerIncomingPluginChannel(this, "BeeLogin", new MessageHandler());
			Bukkit.getPluginManager().registerEvents(eventCanceler, this);
		}
		Bukkit.getPluginManager().registerEvents(this, this);
		Bukkit.getPluginManager().registerEvents(eventHandler, this);
		logger.info("BeeLogin加载成功");
		logger.info("====================");
		logger.info("蜜蜂工作室 BeeStudio");
		logger.info("      荣誉出品");
		logger.info("讨论QQ群:  367428642");
		logger.info("====================");
		logger.info("当前网页端验证URL:" + Config.checkUrl);
		logger.info("当前网页端IP获取URL:" + Config.sendIpUrl);
	}

	public void onDisable() {
		getLogger().info("BeeLogin卸载成功");
	}

}
