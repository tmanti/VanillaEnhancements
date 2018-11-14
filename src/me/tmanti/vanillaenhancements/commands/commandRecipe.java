package me.tmanti.vanillaenhancements.commands;

import me.tmanti.vanillaenhancements.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class commandRecipe implements CommandExecutor, Listener {

    private Plugin plugin = Main.getPlugin(Main.class);

    public boolean doName = false;
    public ItemStack[] recipeItems = {null, null, null, null, null, null, null, null, null, null};
    public Integer[] toAvoid = {3,4,5,12,13,14,21,22,23,16,26};

    public void openGui(Player player){
        Inventory inv = Bukkit.createInventory(null, 27, "Create custom recipe"); //3,4,5, 12,13,14 21,22,23 16

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

        ItemStack item = new ItemStack(Material.GREEN_STAINED_GLASS_PANE, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&l&aConfirm"));
        item.setItemMeta(meta);

        inv.setItem(26, item);

        player.openInventory(inv);
    }

    public void createRecipe(){

    }

    @EventHandler
    public void preventInventoryInteraction(InventoryClickEvent event){
        ItemStack item = event.getCurrentItem();
        try {
            Inventory inv = event.getClickedInventory();
            if(inv.getName().equals("Create custom recipe")) {
                if (item.getType().equals(Material.GRAY_STAINED_GLASS_PANE) || item.getType().equals(Material.GREEN_STAINED_GLASS_PANE)) {
                    event.setCancelled(true);
                    if(item.getType().equals(Material.GREEN_STAINED_GLASS_PANE)){
                        for(int i = 0; i < 10; i++) {
                            try {
                                recipeItems[i] = inv.getItem(toAvoid[i]);
                            } catch (Exception e) {
                                recipeItems[i] = null;
                            }
                        }
                        event.getWhoClicked().closeInventory();
                        doName = true;
                        event.getWhoClicked().sendMessage("Enter the name of the recipe");
                        event.getWhoClicked().sendMessage("Type 'exit' to stop");
                    }
                }
            }
        } catch(Exception e){ }
    }

    @EventHandler
    public void playerNameRecipe(AsyncPlayerChatEvent event){
        String msg = event.getMessage();
        Player player = event.getPlayer();
        if(doName){
            event.setCancelled(true);
            if(msg.equals("exit")){
                return;
            } else {
                for(int x = 0; x < 9; x++){
                    if(recipeItems[x] != null) {
                        plugin.getConfig().set("Recipes." + msg + "." + x, recipeItems[x]);
                    } else {
                        plugin.getConfig().set("Recipes." + msg + "." + x, "null");
                    }
                }
                plugin.getConfig().set("Recipes." + msg + ".output", recipeItems[9]);
                plugin.saveConfig();
                player.sendMessage("recipe created");
                doName = false;
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage("only players can send this message");
            return true;
        }

        Player player = (Player) sender;

        if(!(player.isOp())){
            sender.sendMessage("you do not have permission to use this command");
            return true;
        }

        openGui(player);

        return true;
    }



}
