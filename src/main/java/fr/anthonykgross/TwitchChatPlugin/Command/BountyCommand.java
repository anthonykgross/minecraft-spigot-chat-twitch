package fr.anthonykgross.TwitchChatPlugin.Command;

import fr.anthonykgross.TwitchChatPlugin.App;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BountyCommand implements CommandExecutor {
    private App app;

    public static final int ACTION_HELP = 0;
    public static final int ACTION_ADD = 1;
    public static final int ACTION_LIST = 2;
    public static final int ACTION_RM = 3;

    public BountyCommand(App app) {
        this.app = app;
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        return this.app.onBountyCommand(commandSender, command, s, strings);
    }

    public static Integer getAction(String action) {
        if (action.equals("a") || action.equals("add")) {
            return BountyCommand.ACTION_ADD;
        }
        if (action.equals("l") || action.equals("ls") || action.equals("list") || action.equals("board")) {
            return BountyCommand.ACTION_LIST;
        }
        if (action.equals("r") || action.equals("rm") || action.equals("remove")) {
            return BountyCommand.ACTION_RM;
        }
        return BountyCommand.ACTION_HELP;
    }
}
