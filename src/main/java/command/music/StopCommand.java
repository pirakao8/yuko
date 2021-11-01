package command.music;

import bot.Bot;
import bot.setting.EmojiList;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.jetbrains.annotations.NotNull;
import util.music.GuildMusicPlayer;

public class StopCommand extends AbstractMusicCommand {
    public StopCommand(final GuildMusicPlayer guildMusicPlayer) {
        super(guildMusicPlayer);
    }

    @Override
    public final void execute(@NotNull final SlashCommandEvent event, final Bot bot) {
        super.execute(event, bot);

        assert event.getGuild() != null;

        if (isPlayable(event, bot)) {
            return;
        }

        if (isMusicPlaying(event)) {
            return;
        }

        guildMusicPlayer.stopTracks(event.getGuild());
        event.reply(EmojiList.STOP.getTag() + " Music stopped").queue();
    }
}
