package ru.waxera.vanishtags.command;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.waxera.vanishtags.VanishService;
import ru.waxera.vanishtags.VanishTags;

import java.util.ArrayList;
import java.util.List;

public class MainCommand extends AbstractCommand {
    public MainCommand() { super(VanishTags.getInstance(), "vanishtags", "vt", "vtags"); }

    private static final VanishService service = VanishService.getInstance();

    @Override
    public void execute(CommandSender sender, String s, String[] args){
        if(args[0].equalsIgnoreCase("reload")){
            if(!sender.hasPermission("vtags.reload")){
                sendPermissionWarn(sender);
                return;
            }

            VanishTags.getInstance().reloadConfig();
            sendMessage(sender, "&a" + VanishTags.getInstance().translate("config-reloaded"));
            return;
        }

        if(args[0].equalsIgnoreCase("mode")){
            if(service == null){
                sendMessage(sender, "&c" + VanishTags.getInstance().translate("vtags-disabled"));
                return;
            }
            if(!sender.hasPermission("vtags.mode")){
                sendPermissionWarn(sender);
                return;
            }

            String playerName = (args.length == 1 || (args.length == 2 && args[1].isEmpty())) ? sender.getName() : args[1];

            boolean self = playerName.equalsIgnoreCase(sender.getName());

            if(!self && !sender.hasPermission("vtags.mode.others")) { sendPermissionWarn(sender); return; }

            Player player = Bukkit.getPlayer(playerName);

            StringBuilder mode = new StringBuilder();
            assert player != null;
            if(service.isVanished(player)){
                service.visiblePlayer(player);
                mode.append("&b").append(VanishTags.getInstance().translate("mode-visible"));
            }
            else{
                service.vanishPlayer(player, true);
                mode.append("&a").append(VanishTags.getInstance().translate("mode-vanished"));
            }

            StringBuilder vanishedMessage = new StringBuilder();
            sendMessage(sender, vanishedMessage.append("&f")
                    .append(VanishTags.getInstance().translate("change-mode-successfully")
                            .replace("%mode%", mode.toString())
                            .replace("%target%", self ? VanishTags.getInstance().translate("you") : playerName)
                    )
                    .toString());

            if(!self){
                StringBuilder vanishedMessageOther = new StringBuilder();
                player.sendMessage(vanishedMessageOther.append("&f")
                        .append(VanishTags.getInstance().translate("change-mode-successfully")
                                .replace("%mode%", mode.toString())
                                .replace("%target%", VanishTags.getInstance().translate("you")))
                        .toString().replace("&", "§"));
            }

            return;
        }

        sendMessage(sender, "&c" + VanishTags.getInstance().translate("unknown-command"));
        return;
    }

    private void sendPermissionWarn(CommandSender sender){
        sendMessage(sender, "&c" + VanishTags.getInstance().translate("no-permissions"));
    }
    private void sendMessage(CommandSender sender, String message){
        sender.sendMessage(message.replace("&", "§"));
    }

    private List<String> onlineList(){
        List<String> result = new ArrayList<>();
        for(Player player: Bukkit.getOnlinePlayers()){
            result.add(player.getName());
        }
        return result;
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args){
        if(args.length == 1) return Lists.newArrayList("reload", "mode");
        if(args.length == 2 && args[0].equalsIgnoreCase("mode")) return onlineList();
        return Lists.newArrayList();
    }
}

