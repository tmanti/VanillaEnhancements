package me.tmanti.vanillaenhancements.commands;

import me.tmanti.vanillaenhancements.AdvancedCrafting.CustomItems;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class commandRunestone implements CommandExecutor {

    private CustomItems ci = new CustomItems();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage("only players can use this command");
            return true;
        }

        Player player = (Player) sender;

        if(!(player.isOp())){
            sender.sendMessage("you do not have permission to use this command");
            return true;
        }

        //if (args.length == 0){
          //  sender.sendMessage(ChatColor.GRAY + "usage: /runestone [name]");
            //return true;
        //}

        ci.giveItem(player, ci.emptyRuneStone());

        return true;
    }
}

/*Location location = player.getLocation();
        String playerLocation = location.getBlockX() + "/" + location.getBlockY() + "/" + location.getBlockZ() + "/" + location.getWorld() + "/" + Math.round(location.getYaw()/4);

        ItemStack rs = new ItemStack(Material.REDSTONE_BLOCK);
        net.minecraft.server.v1_13_R1.ItemStack nmsRuneStone = CraftItemStack.asNMSCopy(rs);
        NBTTagCompound rscompound = (nmsRuneStone.hasTag()) ? nmsRuneStone.getTag() : new NBTTagCompound();
        rscompound.set("location", new NBTTagString(playerLocation));

        nmsRuneStone.setTag(rscompound);
        ItemStack RuneStone = CraftItemStack.asBukkitCopy(nmsRuneStone);

        ItemMeta itemmeta = RuneStone.getItemMeta();
        itemmeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c&lRuneStone"));
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.BLUE + "Hold this and channel teleport");
        lore.add(ChatColor.BLUE + "to warp to the inscribed location");
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&',"&l&1" + args[0]));
        lore.add("");
        lore.add(ChatColor.BLUE + "Inscribed by: " + player.getName());
        itemmeta.setLore(lore);

        RuneStone.setItemMeta(itemmeta);
        player.getInventory().addItem(RuneStone);*/
//String string = rscompound.getString("location");
