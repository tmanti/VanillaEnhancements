package me.tmanti.vanillaenhancements.commands;

import net.minecraft.server.v1_13_R2.IChatBaseComponent;
import net.minecraft.server.v1_13_R2.PacketPlayOutTitle;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class commandSendTitle implements CommandExecutor {

    public void sendTitle(String text, Player player, Boolean Dab){
        IChatBaseComponent chatTitle;

        if(Dab) {
            chatTitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + text + "\",\"color\":\"gold\"}");
        } else {
            chatTitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + text + "\"}");
        }

        PacketPlayOutTitle title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, chatTitle);
        PacketPlayOutTitle length = new PacketPlayOutTitle(5, 20, 5);


        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(title);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(length);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender.isOp())){
            sender.sendMessage("you dont have permission to run this command");
            return true;
        }
        if(args.length > 0){
            if(args[0].toLowerCase().equals("all")){
                StringBuilder builder = new StringBuilder();
                for(int y = 0; y < args.length-1; y++){
                    builder.append(args[y+1]);
                    builder.append(" ");
                }
                String msg = builder.toString();
                System.out.println(msg);

                for(Object x : Bukkit.getOnlinePlayers().toArray()){
                    Player p = (Player) x;
                    sendTitle(msg, p, false);
                }
            } else if (args.length >= 2){
                StringBuilder builder = new StringBuilder();
                for(int x = 0; x < args.length-1; x++){
                    builder.append(args[x+1]);
                }
                String msg = builder.toString();

                Player toSend = Bukkit.getPlayer(args[0]);

                if(toSend != null){
                    sendTitle(msg, toSend, false);
                } else {
                    sender.sendMessage(ChatColor.DARK_GRAY + "we cant find that player, did you spell it correctly?");
                }
            } else {
                sender.sendMessage(ChatColor.GRAY + "Incorrect Usage, Try:");
                sender.sendMessage(ChatColor.GRAY + "/sendtitle [all/{PlayerName}] [Message]");
            }
        } else {
            sender.sendMessage(ChatColor.GRAY + "Incorrect Usage, Try:");
            sender.sendMessage(ChatColor.GRAY + "/sendtitle [all/{player name}] [message]");
        }

        return true;
    }
}
