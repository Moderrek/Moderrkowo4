package pl.moderrkowo.survival.discord;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import pl.moderrkowo.survival.discord.bot.DiscordBot;
import pl.moderrkowo.survival.discord.bot.LinkingManager;
import pl.moderrkowo.survival.discord.command.OdlaczCommand;
import pl.moderrkowo.survival.discord.command.PolaczCommand;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class DiscordPlugin extends JavaPlugin {

    private final Logger logger = getLogger();
    private final FileConfiguration config = getConfig();

    private static DiscordPlugin instance;
    public static DiscordPlugin getInstance(){
        return instance;
    }

    private File dataFile;
    private FileConfiguration dataConfig;

    private LinkingManager linkingManager;
    private DiscordBot bot;
    public DiscordBot getDiscordBot(){
        return bot;
    }

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();
        instance = this;
        // config.yml
        saveDefaultConfig();
        if(!config.getBoolean("enabled")){
            logger.log(Level.WARNING, "Wyłączono z powodu: `config.yml`> \"enabled=false\" ");
            setEnabled(false);
            return;
        }
        // data.yml
        dataFile = new File(getDataFolder(), "data.yml");
        if(!dataFile.exists()){
            dataFile.getParentFile().mkdirs();
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
        // discord bot
        linkingManager = new LinkingManager(dataConfig);
        bot = new DiscordBot(logger, dataConfig, linkingManager, config.getLong("guild-id"));
        try {
            bot.login(config.getString("token"));
        } catch (InterruptedException e) {
            logger.log(Level.WARNING, "Wyłączono z powodu: " + e.getMessage());
            setEnabled(false);
            throw new RuntimeException(e);
        }
        bot.setStatus(OnlineStatus.IDLE);
        bot.setActivity(Activity.playing(config.getString("activity")));
        // commands
        getCommand("polacz").setExecutor(new PolaczCommand(linkingManager));
        getCommand("odlacz").setExecutor(new OdlaczCommand(linkingManager));
        //
        logger.log(Level.INFO, "Uruchomiono w " + (System.currentTimeMillis() - start) + " ms");
    }

    @Override
    public void onDisable() {
        long start = System.currentTimeMillis();
        if(bot != null)
            bot.logout();
        if(dataConfig != null) {
            if(dataFile != null && dataFile.exists()){
                try {
                    dataConfig.save(dataFile);
                } catch (IOException e) {
                    logger.log(Level.SEVERE, "Nie udało się zapisać `data.yml`! " + e.getMessage());
                }
            }
        }
        logger.log(Level.INFO, "Wyłączono w " + (System.currentTimeMillis() - start) + " ms");
    }
}
