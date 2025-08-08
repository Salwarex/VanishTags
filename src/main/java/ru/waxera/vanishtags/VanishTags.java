package ru.waxera.vanishtags;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import ru.waxera.vanishtags.command.MainCommand;
import ru.waxera.vanishtags.display.DisplayTagsListener;
import ru.waxera.vanishtags.storage.Storage;

public final class VanishTags extends JavaPlugin {

    private static VanishTags instance;
    private static Storage language;
    private static Storage visiblePlayers;

    @Override
    public void onEnable() {
        System.out.println("Loading started...");

        instance = this;
        saveDefaultConfig();

        Bukkit.getPluginManager().registerEvents(new ConnectListener(), this);
        Bukkit.getPluginManager().registerEvents(new DisplayTagsListener(), this);

        language = new Storage("languages.yml", "", this);
        visiblePlayers = new Storage("visible_players.yml", "", this);

        new MainCommand();

        System.out.println("Loading finished!");
    }

    public static VanishTags getInstance() {
        return instance;
    }


    public String translate(String str){
        String lang = getConfig().getString("language", "en");
        return language.getConfig().getString(lang + "." + str, str);
    }

    public static Storage getVisiblePlayersStorage() {
        return visiblePlayers;
    }
}
