package me.tmanti.vanillaenhancements.HorsesPlus;

import net.minecraft.server.v1_13_R2.NBTTagByteArray;
import net.minecraft.server.v1_13_R2.NBTTagCompound;
import net.minecraft.server.v1_13_R2.NBTTagList;
import net.minecraft.server.v1_13_R2.NBTTagString;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import sun.rmi.runtime.NewThreadAction;

import java.util.ArrayList;

public class HorsePickup implements CommandExecutor, Listener {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("this command can only be used by a player");
            return true;
        }

        sender.sendMessage("H o r s e");

        return true;
    }

    @EventHandler
    private void playerInteractEntity(PlayerInteractEntityEvent event){
        Entity entity = event.getRightClicked();
        Player player = event.getPlayer();
        if(entity.getType() == EntityType.HORSE && player.isSneaking()){
            event.setCancelled(true);
            player.getInventory().addItem(horseSummon((Horse) entity));
            entity.remove();
        }
    }

    private void playerInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
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

        System.out.println(horse.getColor().toString() + "/" + horse.getStyle().toString() + "/" + horse.getInventory().toString());

        saddle.setItemMeta(itemmeta);

        return saddle;
    }

}
