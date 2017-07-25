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
		if (!BeeLogin.loginStatus.get(player.getName().toLowerCase())) {
			player.kickPlayer(Config.timeOutMessage);
			if (BeeLogin.kickers.get(player.getName().toLowerCase()) == null) {
				player.kickPlayer(Config.unknowErrorMessage);
				BeeLogin.instance.getLogger().warning("在玩家" + player.getName() + "登录时踢出器发生错误");
				return;
			}
			BeeLogin.kickers.get(player.getName().toLowerCase()).cancel();
			BeeLogin.kickers.remove(player.getName().toLowerCase());
		}
	}
}