package command.music;

import bot.Bot;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.jetbrains.annotations.NotNull;
import util.music.GuildMusicPlayer;

public class PlayCommand extends AbstractMusicCommand {
    public PlayCommand(final GuildMusicPlayer guildMusicPlayer) {
        super(guildMusicPlayer);
    }

    @Override
    public final void execute(@NotNull final SlashCommandEvent event, final Bot bot) {
        super.execute(event, bot);

        assert event.getMember() != null;
        assert event.getGuild() != null;
        assert event.getMember().getVoiceState() != null;

        if (isPlayable(event, bot)) {
            return;
        }

        String source = event.getOptions().get(0).getAsString();
        if(!source.startsWith("http")) {
            source = "ytsearch: " + source;
        }

        if (!event.getGuild().getAudioManager().isConnected()) {
            event.getGuild().getAudioManager().openAudioConnection(event.getMember().getVoiceState().getChannel());
        }

        guildMusicPlayer.loadAndPlay(event, source);
    }
}
