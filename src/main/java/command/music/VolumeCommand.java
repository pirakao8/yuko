package command.music;

import bot.Bot;
import bot.setting.EmojiList;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.jetbrains.annotations.NotNull;
import util.music.GuildMusicPlayer;

public class VolumeCommand extends AbstractMusicCommand {
    public VolumeCommand(final GuildMusicPlayer guildMusicPlayer) {
        super(guildMusicPlayer);
    }

    @Override
    public void execute(@NotNull final SlashCommandEvent event, final Bot bot) {
        super.execute(event, bot);

        assert event.getGuild() != null;

        final int volume = (int) event.getOptions().get(0).getAsLong();

        if (volume < 0 || volume > 100) {
            event.reply("Volume must be a value between 0 and 100").setEphemeral(true).queue();
            return;
        }

        guildMusicPlayer.setVolume(event.getGuild(), volume);
        event.reply(EmojiList.VOLUME.getTag() + " Volume is now at " + guildMusicPlayer.getVolume(event.getGuild())).queue();
    }
}
