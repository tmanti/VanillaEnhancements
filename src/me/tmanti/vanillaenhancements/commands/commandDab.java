package me.tmanti.vanillaenhancements.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class commandDab implements CommandExecutor {

    private commandSendTitle st = new commandSendTitle();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender.isOp())) {
            sender.sendMessage("you dont have permission to run this command");
            return true;
        }

        String col = null;

        if(args.length > 0){
            col = ChatColor.valueOf(args[0]).name();
        }

        for(Object x : Bukkit.getOnlinePlayers().toArray()){
            Player p = (Player) x;
            st.sendTitle(sender.getName() + " dabs! <o/", p, true);
        }
        return true;
    }
}
