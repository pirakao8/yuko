package bot.listener;

import bot.Bot;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import org.jetbrains.annotations.NotNull;
import util.music.GuildMusicPlayer;

public class VoiceLeaveListener extends AbstractListener {
    public VoiceLeaveListener(final Bot bot) {
        super(bot);
    }

    @Override
    public void onGuildVoiceLeave(@NotNull final GuildVoiceLeaveEvent event) {
        super.onGuildVoiceLeave(event);

        GuildMusicPlayer.getInstance().updateVoiceStatus(event);
    }
}
