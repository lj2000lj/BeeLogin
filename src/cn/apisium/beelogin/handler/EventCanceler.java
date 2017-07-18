package cn.apisium.beelogin.handler;

import static cn.apisium.beelogin.BeeLogin.loginStatus;
import static cn.apisium.beelogin.Config.usingBeeLoginMod;

import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class EventCanceler implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void anyPlayerEvent(PlayerEvent event){
		if(!(event instanceof Cancellable))
			return;
		if (!usingBeeLoginMod)
			return;
		Player player = event.getPlayer();
		if (!loginStatus.get(player.getName().toLowerCase())) {
			((Cancellable) event).setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerInteract(PlayerInteractEvent evt) {
		if (!usingBeeLoginMod)
			return;
		Player player = evt.getPlayer();
		if (!loginStatus.get(player.getName().toLowerCase())) {
			evt.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerInteractenEntity(PlayerInteractEntityEvent evt) {
		if (!usingBeeLoginMod)
			return;
		Player player = evt.getPlayer();
		if (!loginStatus.get(player.getName().toLowerCase())) {
			evt.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onAsyncPlayerChat(AsyncPlayerChatEvent evt) {
		if (!usingBeeLoginMod)
			return;
		Player player = evt.getPlayer();
		if (!loginStatus.get(player.getName().toLowerCase())) {
			evt.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent evt) {
		if (!usingBeeLoginMod) {
			return;
		}
		Player player = evt.getPlayer();
		if (!loginStatus.get(player.getName().toLowerCase())) {
			evt.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerEditBook(PlayerEditBookEvent evt) {
		if (!usingBeeLoginMod)
			return;
		Player player = evt.getPlayer();
		if (!loginStatus.get(player.getName().toLowerCase())) {
			evt.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerItemConsume(PlayerItemConsumeEvent evt) {
		if (!usingBeeLoginMod)
			return;
		Player player = evt.getPlayer();
		if (!loginStatus.get(player.getName().toLowerCase())) {
			evt.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerDropItem(PlayerDropItemEvent evt) {
		if (!usingBeeLoginMod)
			return;
		Player player = evt.getPlayer();
		if (!loginStatus.get(player.getName().toLowerCase())) {
			evt.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerPickupItem(PlayerPickupItemEvent evt) {
		if (!usingBeeLoginMod)
			return;
		Player player = evt.getPlayer();
		if (!loginStatus.get(player.getName().toLowerCase())) {
			evt.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onInventoryClick(InventoryClickEvent evt) {
		if (!usingBeeLoginMod)
			return;
		HumanEntity entity = evt.getWhoClicked();
		if (!(entity instanceof Player))
			return;
		if (!loginStatus.get(((Player) entity).getName().toLowerCase())) {
			evt.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityDamage(EntityDamageEvent evt) {
		if (!usingBeeLoginMod)
			return;
		Entity entity = evt.getEntity();
		if (!(entity instanceof Player))
			return;
		Player player = (Player) entity;
		if (!loginStatus.get(player.getName().toLowerCase())) {
			evt.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onFoodLevelChange(FoodLevelChangeEvent evt) {
		if (!usingBeeLoginMod)
			return;
		HumanEntity EPlayer = evt.getEntity();
		if (EPlayer instanceof Player) {
			Player player = (Player) EPlayer;
			if (!loginStatus.get(player.getName().toLowerCase())) {
				evt.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onMove(PlayerMoveEvent evt) {
		if (!usingBeeLoginMod)
			return;
		Player player = evt.getPlayer();
		if (!loginStatus.get(player.getName().toLowerCase())) {
			evt.setCancelled(true);

		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityRegainHealth(EntityRegainHealthEvent event) {
		if (!usingBeeLoginMod)
			return;
		Entity entity = event.getEntity();
		if (entity instanceof Player) {
			if (!loginStatus.get(((Player) entity).getName().toLowerCase())) {
				event.setCancelled(true);
			}
		}
	}
}
