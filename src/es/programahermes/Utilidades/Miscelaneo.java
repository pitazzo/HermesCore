package es.programahermes.Utilidades;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.shininet.bukkit.itemrenamer.api.RenamerAPI;

import es.programahermes.MySQL;
import es.programahermes.Health.HealthSQL;

public class Miscelaneo implements Listener, CommandExecutor {

	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		MySQL.removePoints(player, 40);
	}

	@EventHandler
	public void onCraft(CraftItemEvent event) {
		for (ItemStack item : event.getInventory().getMatrix()) {
			if (Batteries.isCharged(item)) {
				ItemStack eBatt = new ItemStack(Material.COAL, item.getAmount());
				ItemMeta im = eBatt.getItemMeta();
				im.setDisplayName("Bater�a descargada");
				eBatt.setItemMeta(im);
				Player player = (Player) event.getInventory().getHolder();
				Block craftingTable = player.getTargetBlock(null, 10);
				craftingTable.getWorld().dropItemNaturally(
						craftingTable.getLocation(), eBatt);
			}
		}
	}

	/*
	 * @EventHandler public void onLogin(PlayerLoginEvent event) { Player player
	 * = event.getPlayer(); if (!MySQL.dbContanisPlayer(player)) {
	 * event.disallow( Result.KICK_OTHER, ChatColor.GREEN +
	 * "�Acceso denegado, no cuentas con un perfil de jugador! Si crees que es un error contacta con el staff!"
	 * ); } }
	 */
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("emergency")) {
			if (sender instanceof Player) {
				if (sender.isOp()) {
					Player player = (Player) sender;
					MySQL.setFatiga(player, 0);
					MySQL.setOxygen(player, 700);
					MySQL.setResidual(player, 0);
					MySQL.setSed(player, 100);
					player.setHealth(20);
					player.setFoodLevel(20);
					player.getInventory().setHelmet(
							new ItemStack(Material.GOLD_HELMET, 1));
					player.getInventory().setChestplate(
							new ItemStack(Material.GOLD_CHESTPLATE, 1));
					player.getInventory().setLeggings(
							new ItemStack(Material.GOLD_LEGGINGS, 1));
					player.getInventory().setBoots(
							new ItemStack(Material.GOLD_BOOTS, 1));
					player.sendMessage(ChatColor.GREEN
							+ "Modo de emergencia activado. Gracias por confiar en industrias ACME.");
					return true;

				} else {
					sender.sendMessage(ChatColor.RED + "Permisos insuficientes");
					return false;
				}
			} else {
				return false;
			}
		}
		return false;
	}

	public static void setWalkSpeed(Player player, double d) {
		if (!HealthSQL.FracturaTI(player)) {
			player.setWalkSpeed((float) d);
		} else {
			return;
		}
	}

	public static boolean equalsName(ItemStack item, String name) {
		if (item != null) {
			if (item.getItemMeta() != null) {
				if (item.getItemMeta().getDisplayName() != null) {
					if (item.getItemMeta().getDisplayName().equals(name)) {

						return true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			} else {
				return false;
			}

		} else {
			return false;
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if (event.getPlayer().getItemInHand().getType()
				.equals(Material.INK_SACK)
				&& event.getPlayer().getItemInHand().getData().equals(12)) {
			if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
				if (event.getClickedBlock().getType().equals(Material.WOOD)) {
					event.getClickedBlock().setType(Material.WOOL);
					event.getClickedBlock().setData((byte) 2);
					event.getPlayer()
							.getItemInHand()
							.setAmount(
									event.getPlayer().getItemInHand()
											.getAmount() - 1);
					event.getPlayer().playSound(
							event.getPlayer().getLocation(), Sound.FIZZ, 1.0F,
							1.0F);
				}
			}
		}
	}

	public static String getDisplayName(ItemStack item) {
		if (item != null) {
			if (item.getItemMeta() != null) {
				if (item.getItemMeta().getDisplayName() != null) {
					return item.getItemMeta().getDisplayName();
				}else{
					return "ERROR";
				}
			}
		}
		return null;
	}

	public static String getIRName(ItemStack item) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			int counter = 0;
			counter++;
			if (counter < 2) {
				String name = RenamerAPI.getAPI().getRule(RenamerAPI.getAPI().getRenamePack(player),item).getName().toString();
				if (name != null) {
					return name;

				}else{
					return "ERROR";
				}
				
			} else {
				break;
			}
		}
		return null;

	}

	public static String getName(ItemStack item) {
		if (item != null) {
			if (item.getItemMeta().getDisplayName() != null) {
				return getDisplayName(item);
			} else {
				if (getIRName(item) != null) {
					return getIRName(item);
				} else {
					return "ERROR";
				}

			}
		} else {
			return "ERROR";
		}

	}
}
