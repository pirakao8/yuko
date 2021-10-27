package util.music;

import bot.setting.EmojiList;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class GuildMusicPlayer {
    private static GuildMusicPlayer guildMusicPlayer;

    private final AudioPlayerManager audioPlayerManager;
    private final Map<Long, GuildMusicManager> musicManagers;

    private GuildMusicPlayer() {
        musicManagers = new HashMap<>();
        audioPlayerManager = new DefaultAudioPlayerManager();

        AudioSourceManagers.registerRemoteSources(audioPlayerManager);
        AudioSourceManagers.registerLocalSource(audioPlayerManager);
    }

    public static GuildMusicPlayer getInstance() {
        if(guildMusicPlayer == null) {
            guildMusicPlayer = new GuildMusicPlayer();
        }
        return guildMusicPlayer;
    }

    private synchronized @NotNull GuildMusicManager getGuildAudioPlayer(@NotNull Guild guild) {
        long guildId = Long.parseLong(guild.getId());

        GuildMusicManager musicManager = musicManagers.get(guildId);

        if (musicManager == null) {
            musicManager = new GuildMusicManager(audioPlayerManager, guild.getAudioManager());
            musicManagers.put(guildId, musicManager);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

        return musicManager;
    }

    public final void loadAndPlay(final @NotNull SlashCommandEvent event, final String source) {
        assert event.getGuild() != null;

        final GuildMusicManager musicManager = getGuildAudioPlayer(event.getGuild());

        musicManager.getScheduler().setTextChannel(event.getTextChannel());

        audioPlayerManager.loadItemOrdered(musicManager, source, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(@NotNull AudioTrack track) {
                event.reply(EmojiList.MUSIC.getTag() + " Track " + track.getInfo().title + " added in the queue").queue();
                musicManager.getScheduler().queue(track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                trackLoaded(playlist.getTracks().get(0));
            }

            @Override
            public void noMatches() {
                event.reply("Nothing found by '" + source + "'").setEphemeral(true).queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                event.reply("Can't load this type of track").setEphemeral(true).queue();
            }
        });
    }

    public final void skipTrack(final Guild guild) {
        getGuildAudioPlayer(guild).getScheduler().nextTrack();
    }

    public final void clearTracks(final Guild guild) {
        getGuildAudioPlayer(guild).getScheduler().clearTracks();
    }

    public final void shuffleTracks(final Guild guild) {
        getGuildAudioPlayer(guild).getScheduler().shuffleTracks();
    }

    public final boolean pauseTrack(final Guild guild) {
        final AudioPlayer audioPlayer = getGuildAudioPlayer(guild).getAudioPlayer();
        if(audioPlayer.isPaused()) {
            return false;
        } else {
            audioPlayer.setPaused(true);
            return true;
        }
    }

    public final boolean resumeTrack(final Guild guild) {
        final AudioPlayer audioPlayer = getGuildAudioPlayer(guild).getAudioPlayer();
        if(audioPlayer.isPaused()) {
            audioPlayer.setPaused(false);
            return true;
        } else {
            return false;
        }
    }

    public final void stopTracks(final Guild guild) {
        getGuildAudioPlayer(guild).getScheduler().stopTracks();
    }

    public final void setVolume(final Guild guild, int volume) {
        getGuildAudioPlayer(guild).getAudioPlayer().setVolume(volume);
    }

    public final int getVolume(final Guild guild) {
        return getGuildAudioPlayer(guild).getAudioPlayer().getVolume();
    }

    public final BlockingQueue<AudioTrack> getTracks(final Guild guild) {
        return getGuildAudioPlayer(guild).getScheduler().getTracks();
    }

    public final void updateVoiceStatus(@NotNull final GuildVoiceLeaveEvent event) {
        if (event.getGuild().getAudioManager().isConnected()) {
            if (event.getChannelLeft().getMembers().size() == 1) {
                stopTracks(event.getGuild());
            }
        }
    }
}