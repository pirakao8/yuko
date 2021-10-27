package command.music;

import bot.Bot;
import bot.setting.EmojiList;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.jetbrains.annotations.NotNull;
import util.music.GuildMusicPlayer;

public class PauseCommand extends AbstractMusicCommand {
    public PauseCommand(GuildMusicPlayer guildMusicPlayer) {
        super(guildMusicPlayer);
    }

    @Override
    public final void execute(@NotNull final SlashCommandEvent event, final Bot bot) {
        super.execute(event, bot);

        assert event.getGuild() != null;

        if (!isPlayable(event, bot) && !isMusicPlaying(event)) {
            return;
        }

        if(guildMusicPlayer.pauseTrack(event.getGuild())) {
            event.reply(EmojiList.PAUSE.getTag() + " Music paused").queue();
        } else {
            event.reply("Music already paused").setEphemeral(true).queue();
        }
    }
}
