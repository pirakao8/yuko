package bot.setting;

import net.dv8tion.jda.api.entities.Guild;

import java.awt.*;

public class GuildSettings {
    public static final String ROLE_JAIL = "Yuko's Jail";
    public static final long JAIL_DEFAULT_TIME = 2;

    public static final String CHANNEL_MUSIC = "music";

    public static final Color  DEFAULT_COLOR   = new Color(87,108,236);

    public static final long ROLE_JAIL_PERM = 556232934464L;
    public static final long ROLE_JAIL_PERM_TEXT_DENY = 535529191441L;
    public static final long ROLE_JAIL_PERM_TEXT_ALLOWED = 66624L;
    public static final long ROLE_JAIL_PERM_VOICE_DENY = 299893521L;
    public static final long ROLE_JAIL_PERM_VOICE_ALLOWED = 34604032L;

    private boolean enableLeague;
    private boolean enableMusic;
    private boolean enableJail;
    private boolean enableW2P;

    private final Guild guild;

    public GuildSettings(final Guild guild) {
        this.guild     = guild;

        enableLeague = false;
        enableMusic  = false;
        enableJail   = false;
        enableW2P    = false;
    }

    public final Guild getGuild() {
        return guild;
    }

    public final boolean isLeagueEnable() {
        return enableLeague;
    }

    public final boolean isMusicEnable() {
        return enableMusic;
    }

    public final boolean isJailEnable() {
        return enableJail;
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

    public void setEnableJail(final boolean enableJail) {
        this.enableJail = enableJail;
    }

    public void setEnableW2P(final boolean enableW2P) {
        this.enableW2P = enableW2P;
    }
}
