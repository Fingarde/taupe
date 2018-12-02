package fr.fingarde.topgun;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Main extends JavaPlugin
{

    private static Main instance;
    private static Boolean state;

    private static int gameID;
    private static HashMap<String, ArrayList<Player>> teams = new HashMap<String, ArrayList<Player>>();

    private static ArrayList<Player> taupes = new ArrayList<Player>();

    public static ArrayList<Player> getTaupes() {
        return taupes;
    }

    public static void setTaupes(ArrayList<Player> taupes) {
        Main.taupes = taupes;
    }

    public static HashMap<String, ArrayList<Player>> getTeams() {
        return teams;
    }

    public static void setTeams(HashMap<String, ArrayList<Player>> teams) {
        Main.teams = teams;
    }

    public static int getGameID() {
        return gameID;
    }

    public static void setGameID(int gameID) {
        Main.gameID = gameID;
    }

    public static Main getInstance() {
        return instance;
    }

    public static Boolean getState() {
        return state;
    }

    public static void setState(Boolean state) {
        Main.state = state;
    }

    @Override
    public void onEnable()
    {
        super.onEnable();

        instance = this;

        getCommand("taupe").setExecutor(new SelectTaupe());
        getCommand("setkit").setExecutor(new SetKit());
        getCommand("start").setExecutor(new StartGame());

        getServer().getPluginManager().registerEvents(new Events(), this);

        getDataFolder().mkdirs();

        genConfigs();

        state = false;
    }

    private void genConfigs()
    {
        File stuff = new File(getDataFolder(), "stuff.yml");
        File spawn = new File(getDataFolder(), "spawn.yml");
        File log = new File(getDataFolder(), "log.yml");

        try {
            if(!stuff.exists()) stuff.createNewFile();
            if(!spawn.exists()) spawn.createNewFile();
            if(!log.exists()) log.createNewFile();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDisable()
    {
        Bukkit.getPluginManager().enablePlugin(this);
        super.onDisable();
    }
}
