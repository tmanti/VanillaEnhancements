package me.tmanti.vanillaenhancements.AdvancedCrafting;

import com.mojang.brigadier.Message;
import net.minecraft.server.v1_13_R2.NBTTagCompound;
import net.minecraft.server.v1_13_R2.NBTTagInt;
import net.minecraft.server.v1_13_R2.NBTTagList;
import net.minecraft.server.v1_13_R2.NBTTagString;
import net.minecraft.server.v1_13_R2.NBTTagCompound;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class CustomItems implements Listener {

    private boolean rsCreation = false;

    @EventHandler
    private void useRunestone(PlayerInteractEvent event){
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
    private void playerNameRunestone(AsyncPlayerChatEvent event){
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

    @EventHandler
    private void playerInteractEntity(PlayerInteractEntityEvent event){
        Entity entity = event.getRightClicked();
        Player player = event.getPlayer();

        ItemStack item = player.getInventory().getItemInMainHand();
        ItemMeta itemmeta = item.getItemMeta();

        if(entity.getType() == EntityType.HORSE && player.isSneaking()) {
            event.setCancelled(true);
            player.getInventory().addItem(horseSummon((Horse) entity));
            entity.remove();
        } else if(itemmeta.getDisplayName() == ChatColor.translateAlternateColorCodes('&', "&1&lMount Summon") && item.getType() == Material.SADDLE ) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void playerInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        Action action = event.getAction();

        ItemStack item = event.getItem();
        ItemMeta itemmeta = item.getItemMeta();
        if(itemmeta.getDisplayName() == ChatColor.translateAlternateColorCodes('&', "&1&lMount Summon") && item.getType() == Material.SADDLE && (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)){
            spawnHorse(item, player);
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

    public static ItemStack horseSummon(Horse horse){
        ItemStack saddle = new ItemStack(Material.SADDLE);
        net.minecraft.server.v1_13_R2.ItemStack nmsSaddle = CraftItemStack.asNMSCopy(saddle);
        NBTTagCompound saddleCompound = (nmsSaddle.hasTag()) ? nmsSaddle.getTag() : new NBTTagCompound();

        saddleCompound.set("horseData", new NBTTagString(horse.getColor().toString() + "/" + horse.getStyle().toString() + "/" + horse.getInventory().toString()));

        NBTTagList ench = new NBTTagList();
        saddleCompound.set("ench", ench);

        nmsSaddle.setTag(saddleCompound);
        ItemStack horseSummon =  CraftItemStack.asBukkitCopy(nmsSaddle);

        ItemMeta itemmeta = horseSummon.getItemMeta();
        itemmeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&1&lMount Summon"));

        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.BLUE + "It summons a horse");

        itemmeta.setLore(lore);

        System.out.println(horse.getColor().toString() + "/" + horse.getStyle().toString() + "/" + horse.getInventory().getContents().toString());

        saddle.setItemMeta(itemmeta);

        return saddle;
    }

    protected void spawnHorse(ItemStack saddle, Player player){
        net.minecraft.server.v1_13_R2.ItemStack nmsSaddle = CraftItemStack.asNMSCopy(saddle);
        NBTTagCompound saddleCompound = (nmsSaddle.hasTag()) ? nmsSaddle.getTag() : new NBTTagCompound();
        if(saddleCompound.hasKey("horseData")){
            String data = saddleCompound.getString("horseData");
            String[] parts = data.split("/");
            Horse horse = (Horse) player.getWorld().spawn(player.getLocation(), Horse.class);
            horse.setTamed(true);
            horse.setOwner(player);
            horse.setColor(Horse.Color.valueOf(parts[0]));
            horse.setStyle(Horse.Style.valueOf(parts[1]));
            horse.getInventory().setContents();
        }
    }

}
