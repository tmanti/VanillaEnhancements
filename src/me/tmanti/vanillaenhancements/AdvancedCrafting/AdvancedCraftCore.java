package me.tmanti.vanillaenhancements.AdvancedCrafting;

import me.tmanti.vanillaenhancements.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.jline.internal.ShutdownHooks;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.StandardSocketOptions;
import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Level;

public class AdvancedCraftCore implements Listener, CommandExecutor {

    public void checkRecipe(Inventory inv){
        for(String y : recipeList){
            Integer count = 0;
            for(int x = 0; x < toAvoid.length-1; x++) {
                try {
                    if (inv.getItem(toAvoid[x]).equals(plugin.getConfig().getItemStack("Recipes." + y + "." + Integer.toString(x)))) {
                        count++;
                    }
                } catch(Exception e){
                    if(plugin.getConfig().getString("Recipes." + y + "." + Integer.toString(x)).equals("null")){
                        count++;
                    }
                }
            }
            if(count == 9){
                ItemStack output;
                try {
                    output = plugin.getConfig().getItemStack("Recipes." + y + ".output");
                } catch(Exception e){
                    System.out.println("Either item to craft is null or invalid itemstack");
                    return;
                }


                inv.setItem(toAvoid[9], output);

                for (int z = 0; z < toAvoid.length - 1; z++) {
                    inv.setItem(toAvoid[z], null);
                }
            }
        }
    }

    private Plugin plugin = Main.getPlugin(Main.class);
    Set<String> recipeList;
    Integer[] toAvoid = {3,4,5,12,13,14,21,22,23,16};
    ArrayList<String> names = new ArrayList<>();

    public static Main m;
    public AdvancedCraftCore(Main m){
        AdvancedCraftCore.m = m;
    }

    public void openGui(Player player){
        Inventory inv = Bukkit.createInventory(null, 27, "Advanced Crafting"); //3,4,5, 12,13,14 21,22,23 16

        for(int i = 0; i < inv.getSize(); i++){
            boolean tf = true;
            for(int z = 0; z < toAvoid.length; z++){
                if(toAvoid[z] == i){
                    tf = false;
                    break;
                }
            }
            if(tf == true){
                inv.setItem(i, new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1));
            }
        }

        recipeList = plugin.getConfig().getConfigurationSection("Recipes").getKeys(false);

        player.openInventory(inv);
        names.add(player.getDisplayName());
        runnable(player);
    }

    @EventHandler
    public void openAdvancedCrafting(PlayerInteractEvent event){
        Action action = event.getAction();
        Block clickedBlock = event.getClickedBlock();
        ItemStack item = event.getItem();
        Player player = event.getPlayer();
        if(action == Action.RIGHT_CLICK_BLOCK && item == null && clickedBlock.getBlockData().getMaterial() == Material.CRAFTING_TABLE  && player.isSneaking()){
            event.setCancelled(true);
            openGui(event.getPlayer());
        }
    }

    @EventHandler
    public void InventoryInteract(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        try {
            Inventory inv = event.getClickedInventory();
            if (inv.getName().equals("Advanced Crafting")) {
                if (item.getType().equals(Material.GRAY_STAINED_GLASS_PANE)) {
                    event.setCancelled(true);
                }
            }
        } catch (Exception e) {}
    }

    @EventHandler
    public void closeGui(InventoryCloseEvent event){
        HumanEntity p = event.getPlayer();
        String name = p.getName();
        if(names.contains(name)){
            names.remove(name);
        }
    }

    public void runnable(Player player){
        new BukkitRunnable(){
            @Override
            public void run(){
                if(names.contains(player.getDisplayName())) {
                    checkRecipe(player.getOpenInventory().getTopInventory());
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 5);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player && sender.isOp()){
            Player player = (Player) sender;
            openGui(player);
        }
        return true;
    }
}
