package me.tmanti.vanillaenhancements.commands;

import net.minecraft.server.v1_13_R2.NBTTagCompound;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class commandHome implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage("this command can only be used by a player");
            return true;
        }
        Player player = (Player) sender;

        ItemStack item = player.getInventory().getItemInMainHand();

        if(item.getType().equals(Material.REDSTONE_BLOCK) && item.getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', "&c&lRunestone"))){
            net.minecraft.server.v1_13_R2.ItemStack nmsRuneStone = CraftItemStack.asNMSCopy(item);
            NBTTagCompound rscompound = (nmsRuneStone.hasTag()) ? nmsRuneStone.getTag() : new NBTTagCompound();
            if(rscompound.hasKey("location")){
                String loc = rscompound.getString("location");
                String[] parts = loc.split("/");
                World world = Bukkit.getWorld(parts[3].replace("CraftWorld{name=", "").replace("}", ""));
                double x = Integer.parseInt(parts[0]);
                double y = Integer.parseInt(parts[1]);
                double z = Integer.parseInt(parts[2]);
                float yaw = Float.valueOf(parts[4]);
                player.teleport(new Location(world, x, y, z, yaw, 0f));
            } else {
                sender.sendMessage("invalid or unavailable key");
            }
        } else {
            sender.sendMessage(ChatColor.GRAY +"Please be holding a runestone");
        }

        return true;
    }
}
