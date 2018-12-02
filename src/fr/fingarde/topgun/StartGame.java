package fr.fingarde.topgun;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Wool;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StartGame implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {

        if(!(sender instanceof Player)) return false;

        Player player = (Player) sender;

        if(Main.getState())
        {
            Bukkit.broadcastMessage(ChatColor.RED + "Une partie est déja en cours.");
            return false;
        }

        for(Player onlinePlayers : Bukkit.getOnlinePlayers())
        {
            Block block = new Location(onlinePlayers.getWorld(), onlinePlayers.getLocation().getX(), onlinePlayers.getLocation().getY() - 1, onlinePlayers.getLocation().getZ()).getBlock();

            if(block.getType() == Material.WOOL)
            {
                Wool wool = new Wool(block.getType(), block.getData());

                onlinePlayers.setDisplayName(ChatColor.valueOf(wool.getColor().name().toUpperCase()) + onlinePlayers.getName());

                onlinePlayers.setPlayerListName(ChatColor.valueOf(wool.getColor().name().toUpperCase()) + onlinePlayers.getName());

                HashMap<String, ArrayList<Player>> teams = Main.getTeams();

                ArrayList<Player> members = new ArrayList<Player>();

                if(teams.containsKey(wool.getColor().name().toUpperCase()))
                {
                    members = teams.get(wool.getColor().name().toUpperCase());
                }
                else
                {
                    teams.put(wool.getColor().name().toUpperCase(), members);
                }

                members.add(onlinePlayers);

                teams.put(wool.getColor().name().toUpperCase(), members);

                Main.setTeams(teams);
            }
        }

        for(Map.Entry<String, ArrayList<Player>> entry : Main.getTeams().entrySet()) {
            double angle = Math.random()*Math.PI*2;

            int x = (int) Math.floor(Math.cos(angle)*40);
            int z =  (int) Math.floor(Math.sin(angle)*40);

            Location spawn = new Location(player.getWorld(), x , 50 , z);

            for(Player teamPlayers : entry.getValue())
            {
                teamPlayers.teleport(spawn);
            }
        }



        File stuff = new File(Main.getInstance().getDataFolder(), "stuff.yml");

        YamlConfiguration config = new YamlConfiguration();

        try {
            config.load(stuff);

            config.options().copyDefaults(true);

            ArrayList<ItemStack> itemStacks = (ArrayList<ItemStack>) config.get("inventory");

            for(Player onlinePlayers : Bukkit.getOnlinePlayers()) {
              onlinePlayers.getInventory().setContents(itemStacks.toArray(new ItemStack[itemStacks.size()]));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }

        Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
               Main.setState(true);
               Bukkit.broadcastMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "C\'est partit.");

               for(Player onlinePlayers : Bukkit.getOnlinePlayers())
               {
                   onlinePlayers.setGameMode(GameMode.SURVIVAL);
                   onlinePlayers.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 5 , 50));
                   onlinePlayers.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 5 , 50));
               }
            }
        }, 100);

        int nbTaupe = ((int) Bukkit.getOnlinePlayers().size() / 4) + 1;

        Player[] players = Bukkit.getOnlinePlayers().toArray(new Player[ Bukkit.getOnlinePlayers().size() ]);

        for(int i = 0 ; i < nbTaupe ; i++)
        {
            int randomTaupe =  (int) (Math.random() * Bukkit.getOnlinePlayers().size());

            if(Main.getTaupes().contains(players[randomTaupe]))
            {
                i--;
            }else {
                ArrayList<Player> taupes = Main.getTaupes();

                taupes.add(players[randomTaupe]);

                Main.setTaupes(taupes);
            }
        }

        String taupes = "";

        for(Player taupe : Main.getTaupes())
        {
            taupes += taupe.getName() + " ";
        }

        for(Player taupe : Main.getTaupes())
        {
            taupe.sendMessage(ChatColor.RED + "Vous etes une taupe \n Avec " + taupes );
        }
        return false;
    }
}
