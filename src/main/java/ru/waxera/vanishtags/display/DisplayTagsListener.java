package ru.waxera.vanishtags.display;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import ru.waxera.vanishtags.VanishService;
import ru.waxera.vanishtags.VanishTags;

public class DisplayTagsListener implements Listener {

    private final VanishService service = VanishService.getInstance();

    @EventHandler
    public void playerClick(PlayerInteractAtEntityEvent e){
        if(service == null) return;

        Player player = e.getPlayer();
        Entity targetEntity = e.getRightClicked();
        if(!(targetEntity instanceof Player target)) return;

        DisplayTagsType type = service.getDisplayType();
        String message = createMessage(target);

        switch (type){
            case TITLE -> displayTitle(player, message, false);
            case SUBTITLE -> displayTitle(player, message, true);
            default -> displayActionBar(player, message);
        }
    }

    private void displayActionBar(Player player, String message){
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
    }
    private void displayTitle(Player player, String message, boolean subtitle){
        player.sendTitle(subtitle ? "" : message, subtitle ? message : "", 10, 20 * VanishTags.getInstance().getConfig().getInt("display-tag.seconds", 5), 10);
    }

    private String createMessage(Player target){
        String result = VanishTags.getInstance().getConfig().getString("display-tag.format", "&6%nickname%");
        return result.replace("&", "§").replace("%nickname%", target.getName());
    }
}
