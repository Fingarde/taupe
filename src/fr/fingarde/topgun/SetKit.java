package fr.fingarde.topgun;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class SetKit implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {

        if(!(sender instanceof Player)) return false;

        Player player = (Player) sender;

        File stuff = new File(Main.getInstance().getDataFolder(), "stuff.yml");

        YamlConfiguration config = new YamlConfiguration();

        try {
            config.load(stuff);

            config.options().copyDefaults(true);

            config.set("inventory", player.getInventory().getContents());

            config.save(stuff);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }

        return false;
    }
}
