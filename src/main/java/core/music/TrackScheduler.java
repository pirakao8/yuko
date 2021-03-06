package core.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import dataBase.EmojiEnum;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler extends AudioEventAdapter {
    private final AudioPlayer audioPlayer;
    private final BlockingQueue<AudioTrack> queue;
    private final AudioManager audioManager;

    private TextChannel textChannel;
    private Message messageCurrentMusic;

    public TrackScheduler(AudioPlayer audioPlayer, AudioManager audioManager) {
        this.audioPlayer  = audioPlayer;
        this.audioManager = audioManager;
        queue = new LinkedBlockingQueue<>();
        messageCurrentMusic = null;
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, @NotNull AudioTrackEndReason endReason) {
        if (endReason.mayStartNext) {
            if (messageCurrentMusic != null) {
                messageCurrentMusic.delete().submit();
                messageCurrentMusic = null;
            }
            nextTrack();
        }
    }

    @Override
    public final void onTrackStart(final AudioPlayer player, final AudioTrack track) {
        super.onTrackStart(player, track);
        if (textChannel != null) {
            textChannel.sendMessage(EmojiEnum.PLAY.getTag() + " Music playing: " + track.getInfo().title).queue(m -> messageCurrentMusic = m);
        }
    }

    public final void queue(AudioTrack track) {
        if (!audioPlayer.startTrack(track, true)) {
            queue.offer(track);
        }
    }

    public final void nextTrack() {
        if (queue.isEmpty()) {
            if (audioManager.isConnected()) {
                audioManager.closeAudioConnection();
            }
            return;
        }
        audioPlayer.startTrack(queue.poll(), false);
    }

    public final void shuffleTracks() {
        queue.clear();
        List<AudioTrack> audioTracksTmp = new ArrayList<>(queue);
        Collections.shuffle(audioTracksTmp);
        queue.addAll(audioTracksTmp);
    }

    public final void clearTracks() {
        queue.clear();
    }

    public final void stopTracks() {
        clearTracks();
        audioPlayer.stopTrack();
        audioManager.closeAudioConnection();
        if (messageCurrentMusic != null) {
            messageCurrentMusic.delete().submit();
            messageCurrentMusic = null;
        }
    }

    public final BlockingQueue<AudioTrack> getTracks() {
        return queue;
    }

    public final void setTextChannel(TextChannel textChannel) {
        this.textChannel = textChannel;
    }
}
