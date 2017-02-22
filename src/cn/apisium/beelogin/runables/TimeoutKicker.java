package cn.apisium.beelogin.runables;

import static cn.apisium.beelogin.Config.usingBeeLoginMod;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import cn.apisium.beelogin.BeeLogin;
import cn.apisium.beelogin.Config;

public class TimeoutKicker extends BukkitRunnable {
	Player player;

	public TimeoutKicker(Player player) {
		this.player = player;
	}

	@Override
	public void run() {
		if (!usingBeeLoginMod)
			return;
		if (!BeeLogin.loginStatus.get(player.getName().toLowerCase()))
			player.kickPlayer(Config.timeOutMessage);
	}
}