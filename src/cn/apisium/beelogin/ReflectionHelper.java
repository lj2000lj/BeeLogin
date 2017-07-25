package cn.apisium.beelogin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.inventivetalent.reflection.minecraft.Minecraft;
import org.inventivetalent.reflection.resolver.ClassResolver;
import org.inventivetalent.reflection.resolver.ConstructorResolver;
import org.inventivetalent.reflection.resolver.FieldResolver;
import org.inventivetalent.reflection.resolver.MethodResolver;

public class ReflectionHelper {
	Class<?> entityPlayerClass;
	Class<?> minecraftServerClass;
	Class<?> playerInteractManagerClass;
	Class<?> gameProfileClass;
	Class<?> playerConnectionClass;
	Class<?> worldServerClass;
	Class<?> enumProtocolDirectionClass;
	Class<?> networkManagerClass;
	Class<?> serverConnectionClass;
	Constructor<?> gameProfileConstructor;
	Constructor<?> entityPlayerConstructor;
	Constructor<?> playerInteractManagerConstructor;
	Constructor<?> networkManagerConstructor;
	Constructor<?> playerConnectionConstructor;
	Field connectionField;
	Field networkManagersField;
	Field serverConnectionField;
	Method getServerMethod;
	Method getWorldServerMethod;
	Method valueOfMethod;
	Method getSocketAddressMethod;

	public ReflectionHelper() {
		try {
			entityPlayerClass = new ClassResolver()
					.resolve("net.minecraft.server." + Minecraft.getVersion() + "EntityPlayer");
			minecraftServerClass = new ClassResolver()
					.resolve("net.minecraft.server." + Minecraft.getVersion() + "MinecraftServer");
			playerInteractManagerClass = new ClassResolver()
					.resolve("net.minecraft.server." + Minecraft.getVersion() + "PlayerInteractManager");
			gameProfileClass = new ClassResolver().resolve("com.mojang.authlib.GameProfile");
			playerConnectionClass = new ClassResolver()
					.resolve("net.minecraft.server." + Minecraft.getVersion() + "PlayerConnection");
			worldServerClass = new ClassResolver()
					.resolve("net.minecraft.server." + Minecraft.getVersion() + "WorldServer");
			enumProtocolDirectionClass = new ClassResolver()
					.resolve("net.minecraft.server." + Minecraft.getVersion() + "EnumProtocolDirection");
			networkManagerClass = new ClassResolver()
					.resolve("net.minecraft.server." + Minecraft.getVersion() + "NetworkManager");
			serverConnectionClass = new ClassResolver()
					.resolve("net.minecraft.server." + Minecraft.getVersion() + "ServerConnection");

			gameProfileConstructor = new ConstructorResolver(gameProfileClass)
					.resolve(new Class[] { UUID.class, String.class });
			entityPlayerConstructor = new ConstructorResolver(entityPlayerClass).resolve(new Class[] {
					minecraftServerClass, worldServerClass, gameProfileClass, playerInteractManagerClass });
			playerInteractManagerConstructor = new ConstructorResolver(playerInteractManagerClass)
					.resolveFirstConstructor();
			networkManagerConstructor = new ConstructorResolver(networkManagerClass).resolveFirstConstructor();
			playerConnectionConstructor = new ConstructorResolver(playerConnectionClass).resolveFirstConstructor();

			connectionField = new FieldResolver(entityPlayerClass).resolveByFirstType(playerConnectionClass);
			networkManagersField = new FieldResolver(serverConnectionClass).resolveByLastType(List.class);
			serverConnectionField = new FieldResolver(minecraftServerClass).resolveByFirstType(serverConnectionClass);

			getServerMethod = new MethodResolver("org.bukkit.craftbukkit." + Minecraft.getVersion() + "CraftServer")
					.resolve("getServer");
			getWorldServerMethod = new MethodResolver(minecraftServerClass).resolve("getWorldServer");
			valueOfMethod = new MethodResolver(enumProtocolDirectionClass).resolve("valueOf");
			getSocketAddressMethod = new MethodResolver(networkManagerClass).resolve("getSocketAddress");
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public Player createPlayer(UUID uuid, String name, InetAddress address) {
		try {
			Object gameProfile = gameProfileConstructor.newInstance(uuid, name);
			Object minecraftServer = getServerMethod.invoke(Bukkit.getServer(), new Object[0]);
			Object worldServer = getWorldServerMethod.invoke(minecraftServerClass.cast(minecraftServer), 0);
			Object vanillaPlayer = entityPlayerConstructor.newInstance(minecraftServer, worldServer, gameProfile,
					playerInteractManagerConstructor.newInstance(worldServer));
			Object enumPacketDirection = valueOfMethod.invoke(null, "SERVERBOUND");
			Object networkManager = networkManagerConstructor.newInstance(enumPacketDirection);
			if (!serverConnectionField.isAccessible()) {
				serverConnectionField.setAccessible(true);
			}
			if (!networkManagersField.isAccessible()) {
				networkManagersField.setAccessible(true);
			}
			for (Object networkManagerObj : (List<?>) networkManagersField
					.get(serverConnectionField.get(minecraftServer))) {
				InetSocketAddress socket = (InetSocketAddress) getSocketAddressMethod.invoke(networkManagerObj,
						new Object[0]);
				if (socket.getAddress().equals(address)) {
					networkManager = networkManagerObj;
				}

			}
			Object playerConnection = playerConnectionConstructor.newInstance(minecraftServer, networkManager,
					vanillaPlayer);
			if (!connectionField.isAccessible()) {
				connectionField.setAccessible(true);
			}
			connectionField.set(vanillaPlayer, playerConnection);
			Player player = (Player) new MethodResolver(entityPlayerClass).resolve("getBukkitEntity")
					.invoke(vanillaPlayer, new Object[0]);

			return player;
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}
}
