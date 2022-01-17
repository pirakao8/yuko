package listener;

import core.music.GuildMusicPlayer;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import org.jetbrains.annotations.NotNull;

public class VoiceLeaveEventListener extends AbstractEventListener {
    @Override
    public final void onGuildVoiceLeave(@NotNull final GuildVoiceLeaveEvent event) {
        GuildMusicPlayer.getInstance().onUpdateVoiceStatus(event.getGuild(), event.getChannelLeft());
    }
}
