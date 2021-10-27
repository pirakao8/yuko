package command.music;

import bot.Bot;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.jetbrains.annotations.NotNull;
import bot.setting.EmojiList;
import util.music.GuildMusicPlayer;

public class SkipCommand extends AbstractMusicCommand {
    public SkipCommand(GuildMusicPlayer guildMusicPlayer) {
        super(guildMusicPlayer);
    }

    @Override
    public final void execute(@NotNull final SlashCommandEvent event, final Bot bot) {
        super.execute(event, bot);

        assert event.getGuild() != null;

        if (!isPlayable(event, bot) && !isMusicPlaying(event)) {
            return;
        }

        guildMusicPlayer.skipTrack(event.getGuild());
        event.reply(EmojiList.SKIP.getTag() + " Music skipped").queue();
    }
}
