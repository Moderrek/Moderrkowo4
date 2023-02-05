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

public class OdlaczCommand implements CommandExecutor {

    private LinkingManager linkingManager;

    public OdlaczCommand(LinkingManager linkingManager) {
        this.linkingManager = linkingManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Aby uzyc tej komendy musisz byc graczem!");
            return false;
        }
        final Player player = (Player) sender;
        final UUID uuid = player.getUniqueId();
        if(linkingManager.isLinked(uuid)){
            long id = linkingManager.getLinked(uuid);
            linkingManager.removeLinked(id, uuid);
            player.sendMessage(Component.text("Pomyślnie odłączono!").color(ColorUtil.green));
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1,1);
        }else{
            player.sendMessage(Component.text("Nie jesteś połączony! Aby połączyć konto uzyj /polacz"));
        }
        return true;
    }
}
