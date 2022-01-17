package command.music;

import bot.GuildSettings;
import command.Command;
import command.CommandCategoryEnum;
import core.music.GuildMusicPlayer;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractMusicCommand implements Command {
    protected final String msgMusicNotPlaying    = "Music not playing";
    protected final String msgMemberNotConnected = "You have to be in an audio channel";
    protected final String msgMusicDisabled      = "Music commands are disabled";

    protected GuildMusicPlayer guildMusicPlayer;

    @Override
    public final CommandCategoryEnum getCategory() {
        return CommandCategoryEnum.MUSIC;
    }

    @Contract(pure = true)
    @Override
    public final @Nullable Permission getPermission() {
        return null;
    }

    @Override
    public void execute(@NotNull final SlashCommandEvent event) {
        guildMusicPlayer = GuildMusicPlayer.getInstance();
    }

    protected final boolean isMemberConnected(@NotNull final Member member) {
        assert member.getVoiceState() != null;

        return member.getVoiceState().inVoiceChannel();
    }

    protected final boolean isBotPlayingMusic(@NotNull final Guild guild) {
        return guild.getAudioManager().isConnected();
    }

    protected final boolean isMusicEnable(@NotNull final Guild guild) {
        return GuildSettings.getInstance(guild).isMusicEnable();
    }

    protected final boolean isExecutable(@NotNull final SlashCommandEvent event) {
        assert event.getMember() != null;
        assert event.getGuild()  != null;

        if (!isMusicEnable(event.getGuild())) {
            event.reply(msgMusicDisabled).setEphemeral(true).queue();
            return false;
        }

        if (!isMemberConnected(event.getMember())) {
            event.reply(msgMemberNotConnected).setEphemeral(true).queue();
            return false;
        }

        if (!isBotPlayingMusic(event.getGuild())) {
            event.reply(msgMusicNotPlaying).setEphemeral(true).queue();
            return false;
        }

        return true;
    }
}
