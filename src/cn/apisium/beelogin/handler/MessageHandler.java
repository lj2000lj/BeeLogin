package cn.apisium.beelogin.handler;

import static cn.apisium.beelogin.BeeLogin.logger;
import static cn.apisium.beelogin.BeeLogin.loginStatus;
import static cn.apisium.beelogin.Config.usingBeeLoginMod;

import java.nio.charset.StandardCharsets;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

import cn.apisium.beelogin.BeeLogin;
import cn.apisium.beelogin.Config;
import cn.apisium.beelogin.LoginResult;

public class MessageHandler implements PluginMessageListener {
	@Override
	public void onPluginMessageReceived(final String channel, final Player player, final byte[] message) {
		if (!usingBeeLoginMod)
			return;
		logger.info("已接收到玩家" + player.getName() + "发出的登录请求");
		final String playerToken = new String(message, StandardCharsets.UTF_8);
		new Thread(new Runnable() {
			@Override
			public void run() {
				final LoginResult result = new LoginResult(Config.checkUrl, Config.queryMode, Config.encodeMode,
						player.getName(), player.getAddress().getAddress().getHostAddress().replace("/", ""),
						Config.serverIp, Config.serverPassword, playerToken);
				new BukkitRunnable() {
					@Override
					public void run() {
						if (result.getResult()) {
							if (BeeLogin.kickers.get(player.getName().toLowerCase()) == null) {
								player.kickPlayer(Config.unknowErrorMessage);
								logger.warning("在玩家" + player.getName() + "登录时踢出器发生错误");
								return;
							}
							BeeLogin.kickers.get(player.getName().toLowerCase()).cancel();
							loginStatus.put(player.getName().toLowerCase(), true);
							logger.info(player.getName() + "成功登录");
						} else {
							player.kickPlayer(Config.kickMessage);
							if (BeeLogin.kickers.get(player.getName().toLowerCase()) == null) {
								return;
							}
							BeeLogin.kickers.get(player.getName().toLowerCase()).cancel();
						}
					}
				}.runTask(BeeLogin.instance);
			}
		}).start();
	}
}
