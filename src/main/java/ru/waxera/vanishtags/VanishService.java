package ru.waxera.vanishtags;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import ru.waxera.vanishtags.display.DisplayTagsType;
import ru.waxera.vanishtags.storage.Storage;

public final class VanishService {
    private static VanishService instance;

    private Team vanishTeam;

    private VanishService(){
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        final String teamName = "vanishTags";
        vanishTeam = scoreboard.getTeam(teamName);
        if(vanishTeam == null){
            vanishTeam = scoreboard.registerNewTeam(teamName);
        }
        vanishTeam.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.FOR_OWN_TEAM);
    }

    public void vanishPlayer(Player player, boolean forced){
        if(player == null) return;

        if(isVanished(player)) return;

        String playerName = player.getName();
        Storage storage = VanishTags.getVisiblePlayersStorage();

        if(forced) {
            if(storage.getConfig().getInt(playerName, 0) == 1){
                storage.getConfig().set(player.getName(), null);
                storage.save();
            }
        }
        else if(storage.getConfig().getInt(playerName, 0) == 1) return;

        vanishTeam.addEntry(playerName);
    }

    public void visiblePlayer(Player player){
        if(player == null) return;

        String playerName = player.getName();
        Storage storage = VanishTags.getVisiblePlayersStorage();

        storage.getConfig().set(playerName, 1);
        storage.save();

        vanishTeam.removeEntry(playerName);
    }

    public boolean isVanished(Player player){
        return vanishTeam.hasEntry(player.getName());
    }

    public DisplayTagsType getDisplayType(){
        return DisplayTagsType.valueOf(
                VanishTags.getInstance().getConfig().getString("display-tag.type", "ACTION_BAR")
        );
    }

    public static VanishService getInstance(){
        if(!VanishTags.getInstance().getConfig().getBoolean("vanish-tags", true)) return null;
        return instance == null ? instance = new VanishService() : instance;
    }
}
