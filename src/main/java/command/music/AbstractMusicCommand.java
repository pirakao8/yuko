package command.music;

import bot.Bot;
import command.AbstractSlashCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.jetbrains.annotations.NotNull;
import util.music.GuildMusicPlayer;

public abstract class AbstractMusicCommand extends AbstractSlashCommand {
    protected final GuildMusicPlayer guildMusicPlayer;
    public AbstractMusicCommand(final GuildMusicPlayer guildMusicPlayer) {
        this.guildMusicPlayer = guildMusicPlayer;
    }

    protected final boolean isPlayable(@NotNull final SlashCommandEvent event, @NotNull final Bot bot) {
        assert event.getGuild() != null;
        assert event.getMember() != null;
        assert event.getMember().getVoiceState() != null;

        if (!bot.getGuildSetting(event.getGuild()).isMusicEnable()) {
            event.reply("Music commands are disabled").setEphemeral(true).queue();
            return true;
        }

        if (!event.getMember().getVoiceState().inVoiceChannel()) {
            event.reply("You have to be in an audio channel").setEphemeral(true).queue();
            return true;
        }
        return false;
    }

    protected final boolean isMusicPlaying(@NotNull final SlashCommandEvent event) {
        assert event.getGuild() != null;

        if(event.getGuild().getAudioManager().isConnected()) {
            return false;
        }

        event.reply("Music not playing").setEphemeral(true).queue();
        return true;
    }
}
