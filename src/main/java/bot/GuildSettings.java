package bot;

import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.*;

public class GuildSettings {
    public static final Color DEFAULT_COLOR = new Color(255, 183, 15, 255);

    public static final String ROLE_WELCOMER = "Welcomer";
    public static final String CHANNEL_WELCOMER = "Welcome";

    public static final long PERM_NEUTRAL        = 0L;
    public static final long PERM_WELCOMER_ALLOW = 66560L;
    public static final long PERM_WELCOMER_DENY  = 535529191505L;

    private static final HashMap<Guild, GuildSettings> guildSettingsInstance = new HashMap<>();

    private boolean enableWelcomer;
    private boolean enableLeague;
    private boolean enableMusic;
    private boolean enableW2P;

    private final ArrayList<String> rulesList;

    private GuildSettings(final Guild guild) {
        rulesList = new ArrayList<>();

        enableWelcomer = false;
        enableLeague   = false;
        enableMusic    = false;
        enableW2P      = false;

        guildSettingsInstance.putIfAbsent(guild, this);
    }

    public static @NotNull GuildSettings getInstance(final Guild guild) {
        if (guildSettingsInstance.containsKey(guild)) {
            return guildSettingsInstance.get(guild);
        }
        return new GuildSettings(guild);
    }

    public final void addRule(final String rule) {
        rulesList.add(rule);
    }

    public final void clearRules() {
        rulesList.clear();
    }

    public final @NotNull String getRules() {
        final StringBuilder rulesBuilder = new StringBuilder();
        for (String rule : rulesList) {
            rulesBuilder.append(rule + "\n\n");
        }
        return rulesBuilder.toString();
    }

    public final boolean isRuleEmpty() {
        return rulesList.isEmpty();
    }

    public final boolean isWelcomerEnable() {
        return enableWelcomer;
    }

    public final boolean isLeagueEnable() {
        return enableLeague;
    }

    public final boolean isMusicEnable() {
        return enableMusic;
    }

    public final boolean isW2PEnable() {
        return enableW2P;
    }

    public void setEnableLeague(final boolean enableLeague) {
        this.enableLeague = enableLeague;
    }

    public void setEnableMusic(final boolean enableMusic) {
        this.enableMusic = enableMusic;
    }

    public void setEnableW2P(final boolean enableW2P) {
        this.enableW2P = enableW2P;
    }

    public void setEnableWelcomer(final boolean enableWelcomer) {
        this.enableWelcomer = enableWelcomer;
    }
}
