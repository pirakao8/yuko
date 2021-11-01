package command.music;

import bot.Bot;
import bot.setting.EmojiList;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.jetbrains.annotations.NotNull;
import util.music.GuildMusicPlayer;

public class QueueCommand extends AbstractMusicCommand {
    public QueueCommand(final GuildMusicPlayer guildMusicPlayer) {
        super(guildMusicPlayer);
    }

    @Override
    public void execute(@NotNull final SlashCommandEvent event, final Bot bot) {
        super.execute(event, bot);

        assert event.getGuild() != null;

        if (isPlayable(event, bot)) {
            return;
        }

        if (isMusicPlaying(event)) {
            return;
        }

        if (!guildMusicPlayer.getTracks(event.getGuild()).isEmpty()) {
            final StringBuilder queueBuilder = new StringBuilder();
            queueBuilder.append(EmojiList.MUSIC.getTag() + " " + guildMusicPlayer.getTracks(event.getGuild()).size() + " tracks in queue :");

            for (AudioTrack audioTrack :  guildMusicPlayer.getTracks(event.getGuild())) {
                queueBuilder.append("\n- '" + audioTrack.getInfo().title + "'");
            }
            event.reply(queueBuilder.toString()).queue();
        } else {
            event.reply("Queue empty").setEphemeral(true).queue();
        }
    }
}
