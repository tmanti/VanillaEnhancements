package me.tmanti.vanillaenhancements;

import me.tmanti.vanillaenhancements.AdvancedCrafting.CustomItems;
import me.tmanti.vanillaenhancements.AdvancedCrafting.AdvancedCraftCore;
import me.tmanti.vanillaenhancements.HorsesPlus.HorsePickup;
import me.tmanti.vanillaenhancements.commands.*;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public String pluginFolder = getDataFolder().getAbsolutePath();

    public void disable(){
        getServer().getPluginManager().disablePlugin(this);
    }

    @Override
    public void onEnable(){

        this.getCommand("runestone").setExecutor(new commandRunestone());
        this.getCommand("home").setExecutor(new commandHome());
        this.getCommand("recipe").setExecutor(new commandRecipe());
        this.getCommand("adv").setExecutor(new AdvancedCraftCore(this));
        this.getCommand("sendtitle").setExecutor(new commandSendTitle());
        this.getCommand("dab").setExecutor(new commandDab());
        this.getCommand("horse").setExecutor(new HorsePickup());

        getServer().getPluginManager().registerEvents(new AdvancedCraftCore(this), this);
        getServer().getPluginManager().registerEvents(new CustomItems(), this);
        getServer().getPluginManager().registerEvents(new commandRecipe(), this);
        getServer().getPluginManager().registerEvents(new HorsePickup(), this);

        loadConfig();
    }

    @Override
    public void onDisable(){
        //runs when server is disabled
        saveConfig();
    }

    public void loadConfig(){
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        saveConfig();
    }

}
