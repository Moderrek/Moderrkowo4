package pl.moderrkowo.survival.discord.bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.bukkit.configuration.file.FileConfiguration;
import pl.moderrkowo.survival.discord.bot.command.CommandConstants;
import pl.moderrkowo.survival.discord.bot.command.SlashCommandManager;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DiscordBot {

    private final Logger logger;
    private final FileConfiguration data;
    private final LinkingManager linkingManager;
    private final long guildId;

    private JDA jda;


    public DiscordBot(Logger logger, FileConfiguration data, LinkingManager linkingManager, long guildId){
        this.logger = logger;
        this.data = data;
        this.linkingManager = linkingManager;
        this.guildId = guildId;
    }

    public LinkingManager getLinkingManager(){
        return linkingManager;
    }

    public void setActivity(Activity activity){
        jda.getPresence().setActivity(activity);
    }

    public void setStatus(OnlineStatus status){
        jda.getPresence().setStatus(status);
    }

    private void updateCommands(Guild guild){
        for(CommandData data : CommandConstants.COMMANDS.keySet()){guild.upsertCommand(data).queue();}
    }

    private void onConnect(){
        logger.log(Level.INFO, "Połączono.");
        Guild guild = jda.getGuildById(guildId);
        if(guild != null){
            logger.log(Level.INFO, "Serwer Discord: " + guild.getName());
            updateCommands(guild);
        }else{
            logger.log(Level.WARNING, "Nie znaleziono serwera Discord!");
        }
    }

    public void login(String token) throws InterruptedException {
        long start = System.currentTimeMillis();
        logger.log(Level.INFO, "Uruchamanie bota..");
        logout();
        JDABuilder builder = JDABuilder.createDefault(token);
        builder.disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE);
        builder.setBulkDeleteSplittingEnabled(false);
        builder.addEventListeners(new SlashCommandManager(this));
        jda = builder.build().awaitReady();
        onConnect();
        logger.log(Level.INFO, "Uruchomiono bota w " + (System.currentTimeMillis() - start) + " ms");
    }

    public void logout(){
        if(jda != null) {
            logger.log(Level.INFO, "Odłączanie..");
            jda.shutdownNow();
            logger.log(Level.INFO, "Odłączono");
        }
    }

    public long getGuildId() {
        return guildId;
    }
}
