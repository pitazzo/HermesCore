package es.programahermes.SoporteVital;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.plugin.Plugin;

import es.programahermes.MySQL;
import es.programahermes.Utilidades.Scoreboard;

public class Hydratation implements Listener {

	public static void thirstUpdate(Plugin plugin) {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			@Override
			public void run() {
				for (Player player : Bukkit.getOnlinePlayers()) {
					if(player.getGameMode().equals(GameMode.SURVIVAL)){
						if (MySQL.getSed(player) > 0) {
							MySQL.removeSed(player, 1);
						}

						Scoreboard.showScore(player);
						if (MySQL.getSed(player) <= 20) {
							player.setWalkSpeed((float) 0.1);
						} else {
							player.setWalkSpeed((float) 0.2);
						}

						if (MySQL.getSed(player) <= 20) {
							player.sendMessage(ChatColor.GREEN
									+ "[Soporte Vital]"
									+ ChatColor.RED
									+ "�Bebe algo pronto, tu nivel de hidrataci�n es muy bajo!");
							player.playSound(player.getLocation(), Sound.BAT_DEATH,
									0.5F, 0.0F);
							if (MySQL.getSed(player) <= 0) {
								player.damage(100);

							}
						}

					}
					}

			}
		}, 100L, 20 * 60);
	}

	@EventHandler
	public void onConsum(PlayerItemConsumeEvent event) {
		Player player = event.getPlayer();
		if (event.getItem().getType().equals(Material.POTION)) {
			if (MySQL.getSed(player) < 100) {
				MySQL.addSed(player, 5);

			}

		}
		if (event.getItem().getType().equals(Material.MUSHROOM_SOUP)) {
			if (MySQL.getSed(player) < 100) {
				MySQL.addSed(player, 10);

			} else {
				player.sendMessage(ChatColor.RED + "Ya has bebido suficiente");
			}
		}
		if (event.getItem().getType().equals(Material.MELON)) {
			if (MySQL.getSed(player) < 100) {
				MySQL.addSed(player, 5);

			} else {
				player.sendMessage(ChatColor.RED + "Ya has bebido suficiente");
			}
		}
	}

}