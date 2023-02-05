package pl.moderrkowo.survival.discord.command;

import net.kyori.adventure.text.Component;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.moderrkowo.survival.api.util.ColorUtil;
import pl.moderrkowo.survival.discord.bot.LinkingManager;

import java.util.UUID;

public class PolaczCommand implements CommandExecutor {

    private LinkingManager linkingManager;
    public PolaczCommand(LinkingManager linkingManager){
        this.linkingManager = linkingManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage("Aby uzyc tej komendy musisz byc graczem!");
            return false;
        }

        final Player player = (Player) sender;
        final UUID uuid = player.getUniqueId();

        if(linkingManager.isLinked(uuid)){
            player.sendMessage(Component.text("Już jesteś podłączony!").color(ColorUtil.red));
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 0.5f, 1);
            return false;
        }

        String code = null;
        try {
            code = linkingManager.getCodeOrGenerate(uuid);
        } catch (Exception e) {
            final Component component = Component.text("Wystąpił problem podczas łączenia konta. Spróbuj ponownie później.").color(ColorUtil.red);
            player.sendMessage(component);
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 0.5f, 1);
            return false;
        }
        final Component component = Component
                .text("Aby połączyć konto Discord wejdź na naszego discorda /discord i uzyj komendy /polacz <kod>")
                .color(ColorUtil.yellow)
                .appendNewline()
                .append(
                        Component.text("Twoj kod: ")
                                 .color(ColorUtil.yellow)
                                 .append(Component.text(code))
                );
        player.sendMessage(component);
        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES,0.5f, 1);
        return true;
    }
}
