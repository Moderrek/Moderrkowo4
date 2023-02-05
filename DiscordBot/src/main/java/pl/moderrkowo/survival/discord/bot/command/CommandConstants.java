package pl.moderrkowo.survival.discord.bot.command;

import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import pl.moderrkowo.survival.api.util.ColorUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CommandConstants {

    private static final CommandData POLACZ = Commands.slash("polacz", "Połącz konto Minecraft").addOption(OptionType.STRING, "kod", "kod", true);
    private static final CommandData POLACZ_INFO = Commands.slash("polacz_info", "Sprawdź z jakim kontem jesteś połączony");
    private static final CommandData ODLACZ = Commands.slash("odlacz", "Odłącz konto Minecraft");

    public static final Map<CommandData, ICommand> COMMANDS = new HashMap<>(){
        {
            put(POLACZ, (bot, event) -> {
                event.deferReply().setEphemeral(true).queue();
                long id = event.getMember().getIdLong();
                if(bot.getLinkingManager().isLinked(id)){
                    event.getHook().sendMessage("Jesteś już połączony! Aby sprawdzić /polacz_info").queue();
                    return;
                }
                String code = event.getOption("kod", OptionMapping::getAsString);
                if(bot.getLinkingManager().isCodeGenerated(code)){

                    UUID uuid = bot.getLinkingManager().getPlayerByCode(code);
                    bot.getLinkingManager().setLinked(id, uuid);
                    bot.getLinkingManager().removeCode(code);

                    event.getHook().sendMessage("Pomyślnie połączono!").setEphemeral(true).queue();

                    Player p = Bukkit.getPlayer(uuid);
                    if(p != null){
                        p.sendMessage(Component.text("Pomyślnie połączono!").color(ColorUtil.green));
                        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1,1);
                    }
                }else{
                    event.getHook().sendMessage("Nieprawidłowy kod").setEphemeral(true).queue();
                }
            });
            put(POLACZ_INFO, (bot, event) -> {
                event.deferReply().setEphemeral(true).queue();
                long id = event.getMember().getIdLong();
                if(bot.getLinkingManager().isLinked(id)){
                    String name = Bukkit.getOfflinePlayer(bot.getLinkingManager().getLinked(id)).getName();
                    event.getHook().sendMessage("Połączony z **" + name + "**").queue();
                }else{
                    event.getHook().sendMessage("Nie jesteś połączony! Aby połączyć konto Minecraft wejdź na serwer i wpisz komendę /polacz").queue();
                }
            });
            put(ODLACZ, (bot, event) -> {
                event.deferReply().setEphemeral(true).queue();
                long id = event.getMember().getIdLong();
                if(bot.getLinkingManager().isLinked(id)){
                    UUID uuid = bot.getLinkingManager().getLinked(id);
                    bot.getLinkingManager().removeLinked(id, uuid);
                    event.getHook().sendMessage("Pomyślnie odłączono.").queue();
                    Player p = Bukkit.getPlayer(uuid);
                    if(p != null){
                        p.sendMessage(Component.text("Pomyślnie odłączono!").color(ColorUtil.green));
                        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1,1);
                    }
                }else{
                    event.getHook().sendMessage("Nie jesteś połączony! Aby połączyć konto Minecraft wejdź na serwer i wpisz komendę /polacz").queue();
                }
            });
        }
    };

}
