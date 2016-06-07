package io.exoticcode.libs;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class AnvilRecipe implements Listener {

	private static AnvilRecipe instance = null;
	private final List<ItemSet> recipes = new ArrayList<>();
	private final List<ItemSet> amountSpecificRecipes = new ArrayList<>();
	private final JavaPlugin plugin;

	public AnvilRecipe(JavaPlugin plugin) {
		AnvilRecipe.instance = this;
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public final void onClick(InventoryClickEvent event) {
		if (!(event.getView().getTopInventory().getType() == InventoryType.ANVIL))
			return;
		if (event.getRawSlot() == 2) {
			ItemStack slot1 = event.getInventory().getItem(0).clone();
			ItemStack slot2 = event.getInventory().getItem(1).clone();
			for (ItemSet itemSet : recipes) {
				ItemStack test1 = slot1.clone();
				ItemStack test2 = slot2.clone();
				test1.setAmount(1);
				test2.setAmount(2);
				ItemStack[] items = itemSet.items;
				RecipeType type = itemSet.type;
				if (type == RecipeType.META_SPECIFIC 
						&& items[0].equals(test1) 
						&& items[1].equals(test2)) {
					event.getWhoClicked().setItemOnCursor(items[2]);
					if (slot1.getAmount() == 1) {
						event.getInventory().setItem(0, new ItemStack(Material.AIR));
					} else {
						event.getInventory().getItem(0).setAmount(slot1.getAmount() - 1);
					}
					if (slot2.getAmount() == 1) {
						event.getInventory().setItem(1, new ItemStack(Material.AIR));
					} else {
						event.getInventory().getItem(1).setAmount(slot2.getAmount() - 1);
					}
					((Player) event.getWhoClicked()).updateInventory();
					new BukkitRunnable() {
						@Override
						public void run() {
							checkInventory(event.getInventory(), false);
							((Player) event.getWhoClicked()).updateInventory();
						}
					}.runTaskLater(plugin, 10L);
					return;
				} else if (type == RecipeType.META_UNSPECIFIC 
						&& items[0].getType().equals(test1.getType())
						&& items[1].getType().equals(test2.getType())) {
					event.getWhoClicked().setItemOnCursor(items[2]);
					if (slot1.getAmount() == 1) {
						event.getInventory().setItem(0, new ItemStack(Material.AIR));
					} else {
						event.getInventory().getItem(0).setAmount(slot1.getAmount() - 1);
					}
					if (slot2.getAmount() == 1) {
						event.getInventory().setItem(1, new ItemStack(Material.AIR));
					} else {
						event.getInventory().getItem(1).setAmount(slot2.getAmount() - 1);
					}
					((Player) event.getWhoClicked()).updateInventory();
					new BukkitRunnable() {
						@Override
						public void run() {
							checkInventory(event.getInventory(), false);
							((Player) event.getWhoClicked()).updateInventory();
						}
					}.runTaskLater(plugin, 10L);
					return;
				}
			}
			for (ItemSet itemSet : amountSpecificRecipes) {
				ItemStack test1 = slot1.clone();
				ItemStack test2 = slot2.clone();
				ItemStack[] items = itemSet.items;
				RecipeType type = itemSet.type;
				if (type == RecipeType.META_SPECIFIC 
						&& items[0].equals(test1) 
						&& items[1].equals(test2)) {
					event.getWhoClicked().setItemOnCursor(items[2]);
					event.getInventory().clear();
					((Player) event.getWhoClicked()).updateInventory();
					return;
				} else if (type == RecipeType.META_UNSPECIFIC 
						&& items[0].getType().equals(test1.getType())
						&& items[1].getType().equals(test2.getType()) 
						&& items[0].getAmount() == test1.getAmount()
						&& items[1].getAmount() == test2.getAmount()) {
					event.getWhoClicked().setItemOnCursor(items[2]);
					event.getInventory().clear();
					((Player) event.getWhoClicked()).updateInventory();
					return;
				}
			}
			return;
		}
		new BukkitRunnable() {
			@Override
			public void run() {
				ItemStack slot1 = event.getInventory().getItem(0);
				ItemStack slot2 = event.getInventory().getItem(1);
				if (slot1 == null || slot2 == null)
					return;
				checkInventory(event.getView().getTopInventory(), true);
				if (event.getWhoClicked() instanceof Player)
					((Player) event.getWhoClicked()).updateInventory();
			}
		}.runTaskLater(plugin, 10L);
	}

	private final void checkInventory(Inventory inv, boolean deep) {
		if (inv.getType() != InventoryType.ANVIL)
			return;
		ItemStack slot1 = inv.getItem(0);
		ItemStack slot2 = inv.getItem(1);
		if (slot1 == null || slot2 == null)
			return;
		for (ItemSet itemSet : recipes) {
			ItemStack test1 = slot1.clone();
			ItemStack test2 = slot2.clone();
			test1.setAmount(1);
			test2.setAmount(2);
			ItemStack[] items = itemSet.items;
			RecipeType type = itemSet.type;
			if (type == RecipeType.META_SPECIFIC 
					&& items[0].equals(test1) 
					&& items[1].equals(test2)) {
				inv.setItem(2, items[2]);
				return;
			} else if (type == RecipeType.META_UNSPECIFIC 
					&& items[0].getType().equals(test1.getType())
					&& items[1].getType().equals(test2.getType())) {
				inv.setItem(2, items[2]);
				return;
			}
		}
		if (deep) {
			for (ItemSet itemSet : amountSpecificRecipes) {
				ItemStack test1 = slot1.clone();
				ItemStack test2 = slot2.clone();
				ItemStack[] items = itemSet.items;
				RecipeType type = itemSet.type;
				if (type == RecipeType.META_SPECIFIC && items[0].equals(test1) && items[1].equals(test2)) {
					inv.setItem(2, items[2]);
					return;
				} else if (type == RecipeType.META_UNSPECIFIC && items[0].getType().equals(test1.getType())
						&& items[1].getType().equals(test2.getType()) && items[0].getAmount() == test1.getAmount()
						&& items[1].getAmount() == test2.getAmount()) {
					inv.setItem(2, items[2]);
					return;
				}
			}
		}
	}

	public final static void registerNewAmountSpecificRecipe(RecipeType type, ItemStack... items) {
		if (items.length < 3)
			throw new IllegalArgumentException("You cannot have less than 3 items.");
		ItemStack[] registered = { items[0], items[1], items[2] };
		if (items[0].getAmount() == 1 && items[1].getAmount() == 1) {
			AnvilRecipe.instance.recipes.add(instance.new ItemSet(type, registered));
		} else {
			AnvilRecipe.instance.amountSpecificRecipes.add(instance.new ItemSet(type, registered));
		}
	}

	public final static void registerNewRecipe(RecipeType type, ItemStack... items) {
		if (items.length < 3)
			throw new IllegalArgumentException("You cannot have less than 3 items.");
		ItemStack first = items[0].clone();
		ItemStack second = items[1].clone();
		ItemStack third = items[2].clone();
		first.setAmount(1);
		second.setAmount(1);
		third.setAmount(1);
		ItemStack[] registered = { first, second, third };
		AnvilRecipe.instance.recipes.add(instance.new ItemSet(type, registered));
	}

	public final static void register(JavaPlugin plugin) {
		if (instance != null) {
			plugin.getLogger().warning("Anvil Recipe has already been registered.");
			return;
		}
		Bukkit.getPluginManager().registerEvents(new AnvilRecipe(plugin), plugin);
	}

	class ItemSet {
		private final RecipeType type;
		private final ItemStack[] items;

		private ItemSet(RecipeType type, ItemStack[] items) {
			this.type = type;
			this.items = items;
		}
	}

	public enum RecipeType {
		META_SPECIFIC, META_UNSPECIFIC
	}

}