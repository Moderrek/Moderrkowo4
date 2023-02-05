package pl.moderrkowo.survival.discord.bot.command;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import pl.moderrkowo.survival.discord.bot.DiscordBot;

public interface ICommand {
    void execute(DiscordBot bot, SlashCommandInteractionEvent event);
}
