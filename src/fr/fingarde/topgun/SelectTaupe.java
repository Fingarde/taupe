package fr.fingarde.topgun;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SelectTaupe implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        int nbTaupe = (int) Bukkit.getOnlinePlayers().size() / 4;


        Bukkit.broadcastMessage(nbTaupe + "");

        int randomTaupe =  (int) (Math.random() * Bukkit.getOnlinePlayers().size());

        Player[] players = Bukkit.getOnlinePlayers().toArray(new Player[ Bukkit.getOnlinePlayers().size() ]);
        Bukkit.broadcastMessage(ChatColor.RED + players[randomTaupe].getDisplayName() + " est la taupe!");
        return false;
    }
}
