package util.music;

import bot.setting.EmojiList;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
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

    public TrackScheduler(AudioPlayer audioPlayer, AudioManager audioManager) {
        this.audioPlayer  = audioPlayer;
        this.audioManager = audioManager;
        queue = new LinkedBlockingQueue<>();
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, @NotNull AudioTrackEndReason endReason) {
        if (endReason.mayStartNext) {
            nextTrack();
        }
    }

    @Override
    public final void onTrackStart(final AudioPlayer player, final AudioTrack track) {
        super.onTrackStart(player, track);
        if(textChannel != null) {
            textChannel.sendMessage(EmojiList.PLAY.getTag() + " Music playing: " + track.getInfo().title).queue();
        }
    }

    public final void queue(AudioTrack track) {
        if (!audioPlayer.startTrack(track, true)) {
            queue.offer(track);
        }
    }

    public final void nextTrack() {
        if(queue.isEmpty()) {
            if(audioManager.isConnected()) {
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
    }

    public final BlockingQueue<AudioTrack> getTracks() {
        return queue;
    }

    public final void setTextChannel(TextChannel textChannel) {
        this.textChannel = textChannel;
    }
}
