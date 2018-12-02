package fr.fingarde.topgun;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;


public class Events implements Listener
{
    @EventHandler
    public void onJoin(PlayerJoinEvent event)
    {
        event.setJoinMessage("");

        event.getPlayer().getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(8000000);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event)
    {
        event.setQuitMessage("");
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event)
    {
        if(!Main.getTaupes().contains(event.getEntity()))
        {
            for(Player onlinePlayer : Bukkit.getOnlinePlayers())
            {
                onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1 , 1);
            }

            event.setDeathMessage(ChatColor.BOLD + "" + ChatColor.GRAY + event.getEntity().getName() + " est mort.");
        }else{
            for(Player onlinePlayer : Bukkit.getOnlinePlayers())
            {
                onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.ENTITY_WITHER_SPAWN, 2 , 2);
            }

            event.setDeathMessage(ChatColor.BOLD + "" + ChatColor.GRAY + event.getEntity().getName() + " est mort.\nC\'etait en réalité une taupe.");
        }

        event.getEntity().setGameMode(GameMode.SPECTATOR);

        int nbOfPlayer = 0;
        int nbOfTaupes = 0;
        int nbOfNTo = 0;

        for(Player onlinePlayers : Bukkit.getOnlinePlayers())
        {
            if(onlinePlayers.getGameMode() == GameMode.SURVIVAL)
            {
                if(Main.getTaupes().contains(onlinePlayers))
                {
                    nbOfTaupes++;
                }else{
                    nbOfNTo++;
                }
                nbOfPlayer++;
            }
        }

        if(nbOfNTo == 0) {
            Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "Les taupes ont gagnées");

            Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
                @Override
                public void run() {
                    for(Player onlineP : Bukkit.getOnlinePlayers())
                    {
                        onlineP.setGameMode(GameMode.SURVIVAL);
                        onlineP.teleport(new Location(onlineP.getWorld(), 0, 95, 0));

                        Main.setState(false);
                        Main.setTaupes(new ArrayList<Player>());
                    }
                }
            },60);
        }
        if(nbOfTaupes == 0) {
            Bukkit.broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "Les humains ont gagnées");

            Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
                @Override
                public void run() {
                    for(Player onlineP : Bukkit.getOnlinePlayers())
                    {
                        onlineP.setGameMode(GameMode.SURVIVAL);
                        onlineP.teleport(new Location(onlineP.getWorld(), 0, 95, 0));

                        Main.setState(false);
                        Main.setTaupes(new ArrayList<Player>());
                    }
                }
            },60);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event)
    {
        if(!Main.getState())
        {
            event.setCancelled(true);
        }
    }
}
