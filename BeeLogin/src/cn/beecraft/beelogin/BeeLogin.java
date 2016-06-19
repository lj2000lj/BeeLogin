package cn.beecraft.beelogin;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class BeeLogin extends JavaPlugin implements Listener {

	/**
	 * 获取字符串的MD5值的MD5值
	 * 
	 * @param string
	 *            需要计算MD5的字符串
	 * @return MD5 字符串两次计算MD5后的值
	 */
	private static String getMd5Twice(String string) {
		string = getMd5(string).toLowerCase();
		string = getMd5(string).toLowerCase();
		return string;
	}

	/**
	 * 获取字符串的MD5值
	 * 
	 * @param string
	 *            需要计算MD5的字符串
	 * @return MD5 字符串的MD5
	 */
	private static String getMd5(String string) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] strTemp = string.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return URL 所代表远程资源的响应结果
	 * @throws Exception
	 */
	private static String sendGet(String url, String param) throws Exception {
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url + "?" + param;
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接
			connection.connect();
			// 获取所有响应头字段
			// Map<String, List<String>> map = connection.getHeaderFields();
			// 遍历所有的响应头字段
			// for (String key : map.keySet()) {
			// System.out.println(key + "--->" + map.get(key));
			// }
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			throw e;
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 * @throws Exception
	 */
	private static String sendPost(String url, String param) throws Exception {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			throw e;
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	private String kickMessage;
	private String ipErrorMessage;
	private String unknowErrorMessage;
	private String serverPassword;
	private String badIpResult;
	private String allowJoinResult;
	private String checkUrl;
	private String sendIpUrl;
	private String queryMode;
	private String encodeMode;
	private Boolean skipOnLoopbackAddress;
	private Boolean skipOnSiteLocalAddress;
	private Boolean usingBeeLoginUIMod;
	private long timeOutLimited;
	private String timeOutMessage;
	private DefaultHashMap<String, BukkitTask> kickerMap = new DefaultHashMap<String, BukkitTask>(null);
	private String serverIp;
	private long sendDelay;
	public DefaultHashMap<String, Boolean> isLogin = new DefaultHashMap<String, Boolean>(false);

	public void onEnable() {
		this.getConfig().addDefault("configVersion", "0");
		this.getConfig().addDefault("kickMessage", "请修改配置文件!并确定文件编码！");
		this.getConfig().addDefault("ipErrorMessage", "请修改配置文件!并确定文件编码！您的IP是：%UserIp%");
		this.getConfig().addDefault("unknowErrorMessage", "请修改配置文件!并确定文件编码！您的IP是：%UserIp%");
		this.getConfig().addDefault("serverPassword", "");
		this.getConfig().addDefault("badIpResult", "badip");
		this.getConfig().addDefault("allowJoinResult", "yes");
		this.getConfig().addDefault("checkUrl", "");
		this.getConfig().addDefault("sendIpUrl", "");
		this.getConfig().addDefault("queryMode", "GET");
		this.getConfig().addDefault("encodeMode", "MD5");
		this.getConfig().addDefault("skipOnLoopbackAddress", false);
		this.getConfig().addDefault("skipOnSiteLocalAddress", false);
		this.getConfig().addDefault("usingBeeLoginUIMod", false);
		this.getConfig().addDefault("timeOutLimited", 20);
		this.getConfig().addDefault("timeOutMessage", "登录超时");
		this.getConfig().addDefault("sendDelay", 20);
		if (!getDataFolder().exists())
			getDataFolder().mkdir();
		File file = new File(getDataFolder(), "config.yml");
		if ((file.exists()) && !this.getConfig().getString("configVersion").equals("2"))
			file.delete();
		if (!(file.exists()))
			saveDefaultConfig();
		reloadConfig();
		kickMessage = this.getConfig().getString("kickMessage");
		ipErrorMessage = this.getConfig().getString("ipErrorMessage");
		unknowErrorMessage = this.getConfig().getString("unknowErrorMessage");
		serverPassword = this.getConfig().getString("serverPassword");
		badIpResult = this.getConfig().getString("badIpResult");
		allowJoinResult = this.getConfig().getString("allowJoinResult");
		checkUrl = this.getConfig().getString("checkUrl");
		sendIpUrl = this.getConfig().getString("sendIpUrl");
		queryMode = this.getConfig().getString("queryMode");
		encodeMode = this.getConfig().getString("encodeMode");
		serverIp = this.getServer().getIp();
		skipOnLoopbackAddress = this.getConfig().getBoolean("skipOnLoopbackAddress");
		skipOnSiteLocalAddress = this.getConfig().getBoolean("skipOnSiteLocalAddress");
		usingBeeLoginUIMod = this.getConfig().getBoolean("usingBeeLoginUIMod");
		timeOutLimited = this.getConfig().getLong("timeOutLimited");
		timeOutMessage = this.getConfig().getString("timeOutMessage");
		sendDelay = this.getConfig().getLong("sendDelay");
		if (usingBeeLoginUIMod) {
			Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BeeLogin");
			Bukkit.getMessenger().registerIncomingPluginChannel(this, "BeeLogin", new BeeLoginMessageListener());
		}
		Bukkit.getPluginManager().registerEvents(this, this);
		getLogger().info("BeeLogin加载成功");
		getLogger().info("====================");
		getLogger().info("蜜蜂工作室 BeeStudio");
		getLogger().info("      荣誉出品");
		getLogger().info("讨论QQ群:  367428642");
		getLogger().info("====================");
		getLogger().info("当前网页端验证URL:" + checkUrl);
		getLogger().info("当前网页端IP获取URL:" + sendIpUrl);
	}

	public void onDisable() {
		getLogger().info("BeeLogin卸载成功");
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerLogin(AsyncPlayerPreLoginEvent evt) {
		if (usingBeeLoginUIMod) {
			isLogin.put(evt.getName().toLowerCase(), false);
			evt.allow();
			return;
		}
		try {
			sendGet(sendIpUrl, "ip=" + evt.getAddress().getHostAddress().replace("/", "") + "&n=" + evt.getName());
			// 向网页端发送玩家的ip地址
		} catch (Exception e) {
			evt.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
			evt.setKickMessage(unknowErrorMessage);
			evt.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, unknowErrorMessage);
		}
		String playerName = evt.getName();
		String playerToken = "null";
		String serverPassword = this.serverPassword;
		String checkUrl = this.checkUrl;
		String queryMode = this.queryMode.toLowerCase();
		String encodeMode = this.encodeMode.toLowerCase();
		InetAddress playerAddress = evt.getAddress();
		String playerIp = playerAddress.getHostAddress().replace("/", "");
		String checkResult = "";
		checkResult = checkLoged(checkUrl, queryMode, encodeMode, playerName, playerIp, serverIp, serverPassword,
				playerToken);
		if (checkResult.startsWith(this.allowJoinResult)
				|| (this.skipOnLoopbackAddress && playerAddress.isLoopbackAddress())
				|| (this.skipOnSiteLocalAddress && playerAddress.isSiteLocalAddress())) {
			evt.allow();
		} else if (checkResult.startsWith(this.badIpResult)) {
			evt.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
			evt.setKickMessage(this.ipErrorMessage.replace("%UserIp%", playerIp));
			evt.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, this.ipErrorMessage.replace("%UserIp%", playerIp));
		} else {
			evt.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
			evt.setKickMessage(this.kickMessage);
			evt.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, this.kickMessage);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerLoginEvent evt) {
		if (!usingBeeLoginUIMod)
			return;
		kickerMap.put(evt.getPlayer().getName().toLowerCase(),
				Bukkit.getScheduler().runTaskLater(this, new TimeOutKick(evt.getPlayer()), timeOutLimited));
		new BeeLoginMessageSender(evt.getPlayer(), this).runTaskLater(this, sendDelay);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerQuit(PlayerQuitEvent evt) {
		if (!usingBeeLoginUIMod)
			return;
		isLogin.remove(evt.getPlayer().getName().toLowerCase());
		if (kickerMap.get(evt.getPlayer().getName().toLowerCase()) == null) {
			evt.getPlayer().kickPlayer(unknowErrorMessage);
			getLogger().log(java.util.logging.Level.WARNING, "在玩家" + evt.getPlayer().getName() + "登录时踢出器发生错误");
			return;
		}
		kickerMap.get(evt.getPlayer().getName().toLowerCase()).cancel();
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerInteract(PlayerInteractEvent evt) {
		if (!usingBeeLoginUIMod)
			return;
		Player player = evt.getPlayer();
		if (!isLogin.get(player.getName().toLowerCase())) {
			evt.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerInteractenEntity(PlayerInteractEntityEvent evt) {
		if (!usingBeeLoginUIMod)
			return;
		Player player = evt.getPlayer();
		if (!isLogin.get(player.getName().toLowerCase())) {
			evt.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onAsyncPlayerChat(AsyncPlayerChatEvent evt) {
		if (!usingBeeLoginUIMod)
			return;
		Player player = evt.getPlayer();
		if (!isLogin.get(player.getName().toLowerCase())) {
			evt.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent evt) {
		if (!usingBeeLoginUIMod) {
			return;
		}
		Player player = evt.getPlayer();
		if (!isLogin.get(player.getName().toLowerCase())) {
			evt.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerEditBook(PlayerEditBookEvent evt) {
		if (!usingBeeLoginUIMod)
			return;
		Player player = evt.getPlayer();
		if (!isLogin.get(player.getName().toLowerCase())) {
			evt.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerItemConsume(PlayerItemConsumeEvent evt) {
		if (!usingBeeLoginUIMod)
			return;
		Player player = evt.getPlayer();
		if (!isLogin.get(player.getName().toLowerCase())) {
			evt.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerDropItem(PlayerDropItemEvent evt) {
		if (!usingBeeLoginUIMod)
			return;
		Player player = evt.getPlayer();
		if (!isLogin.get(player.getName().toLowerCase())) {
			evt.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerPickupItem(PlayerPickupItemEvent evt) {
		if (!usingBeeLoginUIMod)
			return;
		Player player = evt.getPlayer();
		if (!isLogin.get(player.getName().toLowerCase())) {
			evt.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onInventoryClick(InventoryClickEvent evt) {
		if (!usingBeeLoginUIMod)
			return;
		HumanEntity entity = evt.getWhoClicked();
		if (!(entity instanceof Player))
			return;
		if (!isLogin.get(((Player) entity).getName().toLowerCase())) {
			evt.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityDamage(EntityDamageEvent evt) {
		if (!usingBeeLoginUIMod)
			return;
		Entity entity = evt.getEntity();
		if (!(entity instanceof Player))
			return;
		Player player = (Player) entity;
		if (!isLogin.get(player.getName().toLowerCase())) {
			evt.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onFoodLevelChange(FoodLevelChangeEvent evt) {
		if (!usingBeeLoginUIMod)
			return;
		HumanEntity EPlayer = evt.getEntity();
		if (EPlayer instanceof Player) {
			Player player = (Player) EPlayer;
			if (!isLogin.get(player.getName().toLowerCase())) {
				evt.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onMove(PlayerMoveEvent evt) {
		if (!usingBeeLoginUIMod)
			return;
		Player player = evt.getPlayer();
		if (!isLogin.get(player.getName().toLowerCase())) {
			evt.setCancelled(true);

		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityRegainHealth(EntityRegainHealthEvent event) {
		if (!usingBeeLoginUIMod)
			return;
		Entity entity = event.getEntity();
		if (entity instanceof Player) {
			if (!isLogin.get(((Player) entity).getName().toLowerCase())) {
				event.setCancelled(true);
			}
		}
	}

	public String checkLoged(String url, String queryMode, String encodeMode, String playerName, String playerIp,
			String serverIp, String serverPassword, String playerToken) {
		String queryResult = "";
		if ((queryMode.equalsIgnoreCase("post")) && (encodeMode.equalsIgnoreCase("md5"))) {
			System.out.println("模式:POST&MD5 请求参数:username=" + getMd5(playerName).toLowerCase() + "&ip="
					+ getMd5Twice(playerIp) + "&sip=" + getMd5Twice(serverIp) + "&spsd=" + getMd5Twice(serverPassword)
					+ "&token=" + getMd5Twice(playerToken));
			try {
				queryResult = sendPost(url,
						"username=" + getMd5(playerName).toLowerCase() + "&ip=" + getMd5Twice(playerIp) + "&sip="
								+ getMd5Twice(serverIp) + "&spsd=" + getMd5Twice(serverPassword) + "&token="
								+ getMd5Twice(playerToken));
			} catch (Exception e) {
				queryResult = "";
			} finally {

			}
			return queryResult;
		} else if ((!queryMode.equalsIgnoreCase("post")) && (!encodeMode.equalsIgnoreCase("md5"))) {
			getLogger().info("模式:GET&明文 请求参数:username=" + playerName + "&ip=" + playerIp + "&sip=" + serverIp + "&spsd="
					+ serverPassword + "&token=" + playerToken);
			try {
				queryResult = sendGet(url, "username=" + playerName + "&ip=" + playerIp + "&sip=" + serverIp + "&spsd="
						+ serverPassword + "&token=" + playerToken);
			} catch (Exception e) {
				queryResult = "";
			} finally {

			}
			return queryResult;
		} else if ((queryMode.equalsIgnoreCase("post")) && (!encodeMode.equalsIgnoreCase("md5"))) {
			getLogger().info("模式:POST&明文 请求参数:username=" + playerName + "&ip=" + playerIp + "&sip=" + serverIp
					+ "&spsd=" + serverPassword + "&token=" + playerToken);
			try {
				queryResult = sendPost(url, "username=" + playerName + "&ip=" + playerIp + "&sip=" + serverIp + "&spsd="
						+ serverPassword + "&token=" + playerToken);
			} catch (Exception e) {
				queryResult = "";
			} finally {

			}
			return queryResult;
		} else {
			getLogger().info("模式:GET&MD5 请求参数:username=" + getMd5(playerName).toLowerCase() + "&ip="
					+ getMd5Twice(playerIp) + "&sip=" + getMd5Twice(serverIp) + "&spsd=" + getMd5Twice(serverPassword)
					+ "&token=" + getMd5Twice(playerToken));
			try {
				queryResult = sendGet(url,
						"username=" + getMd5(playerName).toLowerCase() + "&ip=" + getMd5Twice(playerIp) + "&sip="
								+ getMd5Twice(serverIp) + "&spsd=" + getMd5Twice(serverPassword) + "&token="
								+ getMd5Twice(playerToken));
			} catch (Exception e) {
				queryResult = "";
			} finally {

			}
			return queryResult;
		}
	}

	class BeeLoginMessageSender extends BukkitRunnable {
		private Player player;
		private BeeLogin plugin;

		private BeeLoginMessageSender(Player player, BeeLogin plugin) {
			this.player = player;
			this.plugin = plugin;
		}

		public void run() {
			try {
				this.player.sendPluginMessage(this.plugin, "BeeLogin", new String("START").getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			getLogger().info("正在获取玩家" + this.player.getName() + "的请求");
		}
	}

	class TimeOutKick implements Runnable {
		Player player;

		public TimeOutKick(Player player) {
			this.player = player;
		}

		@Override
		public void run() {
			if (!usingBeeLoginUIMod)
				return;
			if (!isLogin.get(player.getName().toLowerCase()))
				player.kickPlayer(timeOutMessage);
		}
	}

	class DefaultHashMap<K, V> extends HashMap<K, V> {
		protected V defaultValue;

		public DefaultHashMap(V defaultValue) {
			this.defaultValue = defaultValue;
		}

		@Override
		public V get(Object k) {
			return containsKey(k) ? super.get(k) : defaultValue;
		}
	}

	class BeeLoginMessageListener implements PluginMessageListener {

		@Override
		public void onPluginMessageReceived(String channel, Player player, byte[] message) {
			if (!usingBeeLoginUIMod)
				return;
			getLogger().info("已接收到玩家" + player.getName() + "发出的登录请求");
			String playerToken = null;
			try {
				playerToken = new String(message, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				player.kickPlayer(unknowErrorMessage);
			}
			String checkResult = checkLoged(checkUrl, queryMode, encodeMode, player.getName(),
					player.getAddress().getAddress().getHostAddress().replace("/", ""), serverIp, serverPassword,
					playerToken);
			if (checkResult.equals(allowJoinResult)) {
				if (kickerMap.get(player.getName().toLowerCase()) == null) {
					player.kickPlayer(unknowErrorMessage);
					getLogger().warning("在玩家" + player.getName() + "登录时踢出器发生错误");
					return;
				}
				kickerMap.get(player.getName().toLowerCase()).cancel();
				isLogin.put(player.getName().toLowerCase(), true);
				getLogger().info(player.getName() + "成功登录");
			} else {
				player.kickPlayer(kickMessage);
				if (kickerMap.get(player.getName().toLowerCase()) == null) {
					return;
				}
				kickerMap.get(player.getName().toLowerCase()).cancel();
			}
		}
	}
}
