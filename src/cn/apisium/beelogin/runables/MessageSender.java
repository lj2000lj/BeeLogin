package cn.apisium.beelogin.runables;

import java.io.UnsupportedEncodingException;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import cn.apisium.beelogin.BeeLogin;

public class MessageSender extends BukkitRunnable {
	private Player player;
	private BeeLogin plugin;

	public MessageSender(Player player, BeeLogin plugin) {
		this.player = player;
		this.plugin = plugin;
	}

	public void run() {
		try {
			this.player.sendPluginMessage(this.plugin, "BeeLogin", new String("START").getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		BeeLogin.instance.getLogger().info("正在获取玩家" + this.player.getName() + "的请求");
	}
}