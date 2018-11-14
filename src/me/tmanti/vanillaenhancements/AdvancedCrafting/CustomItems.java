package me.tmanti.vanillaenhancements.AdvancedCrafting;

import com.mojang.brigadier.Message;
import net.minecraft.server.v1_13_R2.NBTTagCompound;
import net.minecraft.server.v1_13_R2.NBTTagInt;
import net.minecraft.server.v1_13_R2.NBTTagList;
import net.minecraft.server.v1_13_R2.NBTTagString;
import net.minecraft.server.v1_13_R2.NBTTagCompound;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class CustomItems implements Listener {

    private boolean rsCreation = false;

    @EventHandler
    public void useRunestone(PlayerInteractEvent event){
        Action action = event.getAction();
        ItemStack item = event.getItem();
        Player player = event.getPlayer();
        try {
            if (action == Action.RIGHT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR) {
                if (item.getType().equals(Material.REDSTONE_BLOCK) && item.getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', "&cEmpty Runestone"))) {
                    event.setCancelled(true);
                    player.sendMessage("Enter the name of the runestone");
                    player.sendMessage("Type 'exit' to stop");
                    rsCreation = true;
                }
            }
        } catch(Exception e){}
    }

    @EventHandler
    public void playerNameRunestone(AsyncPlayerChatEvent event){
        String msg = event.getMessage();
        Player player = event.getPlayer();
        if(rsCreation){
            event.setCancelled(true);
            if(msg.equals("exit")){
                return;
            } else {
                ItemStack item = emptyRuneStone();
                player.getInventory().remove(item);
                player.getInventory().addItem(RuneStone(player, msg));
                player.sendMessage("Runestone Inscribed");
                rsCreation = false;
            }
        }
    }

    public void giveItem(Player player, ItemStack item){
        Inventory inv = player.getInventory();
        inv.addItem(item);
    }

    public static ItemStack emptyRuneStone(){
        ItemStack rs = new ItemStack(Material.REDSTONE_BLOCK);

        ItemMeta meta = rs.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&cEmpty Runestone"));
        ArrayList<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatColor.BLUE + "An unenchanted Runestone");
        lore.add("");
        lore.add(ChatColor.DARK_BLUE + "Right Click to Inscribe your location");
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&l&o&4Warning this action is irreversible"));
        meta.setLore(lore);
        rs.setItemMeta(meta);

        return rs;
    }

    public static ItemStack RuneStone(Player player, String name){
        Location location = player.getLocation();
        String playerLocation = location.getBlockX() + "/" + location.getBlockY() + "/" + location.getBlockZ() + "/" + location.getWorld() + "/" + Math.round(location.getYaw()/4);

        ItemStack rs = new ItemStack(Material.REDSTONE_BLOCK);
        net.minecraft.server.v1_13_R2.ItemStack nmsRuneStone = CraftItemStack.asNMSCopy(rs);
        NBTTagCompound rscompound = (nmsRuneStone.hasTag()) ? nmsRuneStone.getTag() : new NBTTagCompound();
        rscompound.set("location", new NBTTagString(playerLocation));

        NBTTagList ench = new NBTTagList();
        rscompound.set("ench", ench);

        nmsRuneStone.setTag(rscompound);
        ItemStack RuneStone = CraftItemStack.asBukkitCopy(nmsRuneStone);

        ItemMeta itemmeta = RuneStone.getItemMeta();
        itemmeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c&lRunestone"));
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.BLUE + "Hold this and channel teleport");
        lore.add(ChatColor.BLUE + "to warp to the inscribed location");
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&',"&l&1" + name));
        lore.add("");
        lore.add(ChatColor.BLUE + "Inscribed by: " + player.getName());
        itemmeta.setLore(lore);

        RuneStone.setItemMeta(itemmeta);

        return RuneStone;
    }

}
