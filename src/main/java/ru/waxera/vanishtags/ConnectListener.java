package ru.waxera.vanishtags;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class ConnectListener implements Listener {

    private final VanishService service = VanishService.getInstance();

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        if(service == null) return;
        Player player = e.getPlayer();
        service.vanishPlayer(player, false);
    }
}
