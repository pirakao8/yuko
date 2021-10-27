package util.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

public class GuildMusicManager {
    private final AudioPlayer audioPlayer;
    private final TrackScheduler trackScheduler;

    public GuildMusicManager(@NotNull final AudioPlayerManager audioPlayerManager, final AudioManager audioManager) {
        audioPlayer = audioPlayerManager.createPlayer();
        trackScheduler = new TrackScheduler(audioPlayer, audioManager);
        audioPlayer.addListener(trackScheduler);
    }

    public AudioPlayerSendHandler getSendHandler() {
        return new AudioPlayerSendHandler(audioPlayer);
    }

    public final AudioPlayer getAudioPlayer() {
        return audioPlayer;
    }

    public final TrackScheduler getScheduler() {
        return trackScheduler;
    }
}
