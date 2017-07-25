package cn.apisium.beelogin.handler;

import static cn.apisium.beelogin.Config.usingBeeLoginMod;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import cn.apisium.beelogin.BeeLogin;
import cn.apisium.beelogin.Config;
import cn.apisium.beelogin.LoginResult;
import cn.apisium.beelogin.ReflectionHelper;
import cn.apisium.beelogin.runables.MessageSender;
import cn.apisium.beelogin.runables.TimeoutKicker;

public class LoginEventHandler implements Listener {
	ReflectionHelper helper = new ReflectionHelper();

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerLogin(AsyncPlayerPreLoginEvent event) {
		InetAddress playerAddress = event.getAddress();
		if ((Config.skipOnLoopbackAddress && playerAddress.isLoopbackAddress())
				|| (Config.skipOnSiteLocalAddress && playerAddress.isSiteLocalAddress())) {
			return;
		}
		if (Config.enableExperimentalFunctions) {
			if (null != Bukkit.getPlayer(event.getUniqueId())) {
				return;
			}
			Player player = helper.createPlayer(event.getUniqueId(), event.getName(), event.getAddress());
			onPlayerJoin(new PlayerLoginEvent(player, event.getAddress().getHostName(), event.getAddress()));
			while (!BeeLogin.loginStatus.get(event.getName().toLowerCase())) {
				try {
					if (!BeeLogin.loginStatus.get(event.getName().toLowerCase())
							&& !BeeLogin.kickers.containsKey(event.getName().toLowerCase())) {
						event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
						event.setKickMessage(Config.kickMessage);
						event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Config.kickMessage);
						return;
					}
					Thread.sleep(0);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if (!BeeLogin.loginStatus.get(event.getName().toLowerCase())) {
				event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
				event.setKickMessage(Config.kickMessage);
				event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Config.kickMessage);
				return;
			}
			event.allow();
			return;
		}
		if (Config.usingBeeLoginMod) {
			BeeLogin.loginStatus.put(event.getName().toLowerCase(), false);
			event.allow();
			return;
		}
		String playerName = event.getName();
		String playerIp = playerAddress.getHostAddress().replace("/", "");
		LoginResult result = new LoginResult(Config.checkUrl, Config.queryMode, Config.encodeMode, playerName, playerIp,
				Config.serverIp, Config.serverPassword);
		if (result.getResult()) {
			event.allow();
		} else if (result.isBadIp()) {
			event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
			event.setKickMessage(Config.badIpMessage.replace("%UserIp%", playerIp));
			event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER,
					Config.badIpMessage.replace("%UserIp%", playerIp));
		} else {
			event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
			event.setKickMessage(Config.kickMessage);
			event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Config.kickMessage);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoin(PlayerLoginEvent event) {
		if (!usingBeeLoginMod)
			return;
		if (!BeeLogin.loginStatus.get(event.getPlayer().getName().toLowerCase())) {
			new MessageSender(event.getPlayer(), BeeLogin.instance).runTaskLater(BeeLogin.instance, Config.verifyDelay);
			BeeLogin.kickers.put(event.getPlayer().getName().toLowerCase(), new TimeoutKicker(event.getPlayer())
					.runTaskLater(BeeLogin.instance, Config.verifyDelay + Config.timeOutLimited));
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerQuit(PlayerQuitEvent event) {
		if (!usingBeeLoginMod)
			return;
		BeeLogin.loginStatus.remove(event.getPlayer().getName().toLowerCase());
	}
}
