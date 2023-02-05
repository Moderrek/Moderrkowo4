package pl.moderrkowo.survival.discord.bot;

import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class LinkingManager {


    private final FileConfiguration data;
    private final HashMap<String, UUID> codes;

    public LinkingManager(FileConfiguration data){
        this.data = data;
        codes = new HashMap<>();
    }

    public boolean isLinked(@NotNull long discordMemberId){
        return data.isSet("discord." + discordMemberId);
    }
    public boolean isLinked(@NotNull UUID minecraftId){
        return data.isSet("minecraft." + minecraftId.toString());
    }
    public UUID getLinked(@NotNull long discordMemberId){
        return UUID.fromString(data.getString("discord." + discordMemberId));
    }
    public long getLinked(@NotNull UUID minecraftId){
        return data.getLong("minecraft." + minecraftId.toString());
    }
    public void setLinked(@NotNull long discordMemberId, @NotNull UUID minecraftId){
        data.set("discord." + discordMemberId, minecraftId.toString());
        data.set("minecraft." + minecraftId.toString(), discordMemberId);
    }
    public void removeLinked(@NotNull long discordMemberId, @NotNull UUID minecraftId){
        data.set("discord." + discordMemberId, null);
        data.set("minecraft." + minecraftId.toString(), null);
    }
    public @NotNull String getCodeOrGenerate(@NotNull UUID forWho) throws Exception {
        if(!isCodeGenerated(forWho))
            return generateCode(forWho);
        return getCode(forWho);
    }
    public @NotNull String generateCode(@NotNull UUID forWho) throws Exception {
        if(codes.containsValue(forWho)){
            throw new Exception("Kod juz jest wygenerowany dla tego uzytkownika");
        }
        String code = null;
        while(code == null || codes.containsKey(code)){
            code = String.format("%04d", new Random().nextInt(10000));
        }
        codes.put(code, forWho);
        return code;
    }
    public @Nullable String getCode(@NotNull UUID forWho){
        for(String code : codes.keySet()){
            if(codes.get(code).equals(forWho)){
                return code;
            }
        }
        return null;
    }
    public boolean isCodeGenerated(@NotNull UUID forWho){
        return codes.containsValue(forWho);
    }
    public boolean isCodeGenerated(@NotNull String code){
        return codes.containsKey(code);
    }
    public @Nullable UUID getPlayerByCode(@NotNull String code){
        return codes.getOrDefault(code, null);
    }
    public void removeCode(@NotNull String code){
        if(!codes.containsKey(code)){
            return;
        }
        codes.remove(code);
    }

}
