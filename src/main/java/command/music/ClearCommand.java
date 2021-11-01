package command.music;

import bot.Bot;
import bot.setting.EmojiList;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.jetbrains.annotations.NotNull;
import util.music.GuildMusicPlayer;

public class ClearCommand extends AbstractMusicCommand {
    public ClearCommand(final GuildMusicPlayer guildMusicPlayer) {
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

        if(!guildMusicPlayer.getTracks(event.getGuild()).isEmpty()) {
            guildMusicPlayer.clearTracks(event.getGuild());
            event.reply(EmojiList.CLEAR.getTag() + " Queue cleared").queue();
        } else {
            event.reply("No tracks in queue").setEphemeral(true).queue();
        }
    }
}
