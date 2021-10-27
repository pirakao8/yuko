package command.music;

import bot.Bot;
import bot.setting.EmojiList;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.jetbrains.annotations.NotNull;
import util.music.GuildMusicPlayer;

public class ShuffleCommand extends AbstractMusicCommand {
    public ShuffleCommand(GuildMusicPlayer guildMusicPlayer) {
        super(guildMusicPlayer);
    }

    @Override
    public void execute(@NotNull final SlashCommandEvent event, final Bot bot) {
        super.execute(event, bot);

        assert event.getGuild() != null;

        if (!isPlayable(event, bot) && !isMusicPlaying(event)) {
            return;
        }

        guildMusicPlayer.shuffleTracks(event.getGuild());
        event.reply(EmojiList.SHUFFLE.getTag() + " Queue shuffled").queue();
    }
}
