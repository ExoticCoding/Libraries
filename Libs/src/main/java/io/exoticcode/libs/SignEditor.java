package io.exoticcode.libs;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class SignEditor implements Listener {

	private static SignEditor instance;
	private final String version;
	private final Map<UUID, SignResponse> inEdit = new HashMap<UUID, SignResponse>();

	private SignEditor(JavaPlugin plugin) {
		SignEditor.instance = this;
		this.version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onDisconnect(PlayerQuitEvent event) {
		UUID id = event.getPlayer().getUniqueId();
		if (inEdit.containsKey(id))
			inEdit.remove(id);
	}

	@EventHandler
	public void onSignChange(SignChangeEvent event) {
		UUID id = event.getPlayer().getUniqueId();
		if (inEdit.containsKey(id)) {
			inEdit.get(id).onSignFinish(event);
			inEdit.remove(id);
		}
	}

	private final void open(Player player, Sign sign, SignResponse response) {
		for (int i = 0; i < 4; ++i)
			sign.setLine(i, sign.getLine(i).replace("§", "&"));
		sign.update();

		try {
			Object handle = player.getClass().getMethod("getHandle").invoke(player);
			Object connection = handle.getClass().getField("playerConnection").get(handle);

			Field tileField = sign.getClass().getDeclaredField("sign");
			tileField.setAccessible(true);
			Object tileSign = tileField.get(sign);

			Field editable = tileSign.getClass().getDeclaredField("isEditable");
			editable.setAccessible(true);
			editable.set(tileSign, true);

			Field handler = tileSign.getClass().getDeclaredField("h");
			handler.setAccessible(true);
			handler.set(tileSign, handle);

			Object position = getNMSClass("BlockPosition$PooledBlockPosition")
					.getMethod("d", double.class, double.class, double.class)
					.invoke(null, sign.getX(), sign.getY(), sign.getZ());

			Object packet = getNMSClass("PacketPlayOutOpenSignEditor").getConstructor(getNMSClass("BlockPosition"))
					.newInstance(position);

			connection.getClass().getDeclaredMethod("sendPacket", getNMSClass("Packet")).invoke(connection, packet);
			inEdit.put(player.getUniqueId(), response);
		} catch (Exception x) {
			x.printStackTrace();
		}
	}

	public Class<?> getNMSClass(String clazz) {
		try {
			return Class.forName("net.minecraft.server." + version + "." + clazz);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static final void openEditor(Player player, Sign sign, SignResponse response) {
		instance.open(player, sign, response);
	}

	public static final void register(JavaPlugin plugin) {
		new SignEditor(plugin);
	}

	public interface SignResponse {

		void onSignFinish(SignChangeEvent event);

	}

}