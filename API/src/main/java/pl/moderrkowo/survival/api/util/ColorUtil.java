package pl.moderrkowo.survival.api.util;

import net.kyori.adventure.text.format.TextColor;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class ColorUtil {

    /**
     * Translate text to Minecraft Legacy Color Code Text
     * @param text Uncolored text
     * @return Colored text with Minecraft Legacy codes
     */
    @Contract("_ -> new")
    @Deprecated
    public static @NotNull String legacyColor(String text){
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static final TextColor red = TextColor.fromCSSHexString("#FB6D51");
    public static final TextColor yellow = TextColor.fromCSSHexString("#FECD57");
    public static final TextColor green = TextColor.fromCSSHexString("#9ED36A");
    public static final TextColor mint = TextColor.fromCSSHexString("#46CEAD");
    public static final TextColor aqua = TextColor.fromCSSHexString("#5E9CEA");
    public static final TextColor lavender = TextColor.fromCSSHexString("#AC92EA");

}
