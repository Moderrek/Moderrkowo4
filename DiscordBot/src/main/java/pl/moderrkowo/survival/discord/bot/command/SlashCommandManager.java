package pl.moderrkowo.survival.discord.bot.command;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;
import pl.moderrkowo.survival.discord.bot.DiscordBot;

public class SlashCommandManager extends ListenerAdapter {

    private final DiscordBot bot;
    public SlashCommandManager(DiscordBot bot){
        this.bot = bot;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event){
        if(event.getGuild().getIdLong() != bot.getGuildId())
            return;
        for(CommandData data : CommandConstants.COMMANDS.keySet()){
            if(event.getName().equalsIgnoreCase(data.getName())){
                CommandConstants.COMMANDS.get(data).execute(bot, event);
                break;
            }
        }
    }

}
