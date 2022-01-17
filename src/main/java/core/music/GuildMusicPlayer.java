package core.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dataBase.EmojiEnum;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.interactions.Interaction;
import net.dv8tion.jda.api.managers.AudioManager;
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
        if (guildMusicPlayer == null) {
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

    public final void loadAndPlay(final @NotNull Interaction interaction, final String source) {
        assert interaction.getGuild()  != null;
        assert interaction.getMember() != null;
        assert interaction.getMember().getVoiceState() != null;

        final GuildMusicManager musicManager = getGuildAudioPlayer(interaction.getGuild());

        audioPlayerManager.loadItemOrdered(musicManager, source, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(@NotNull AudioTrack track) {
                musicManager.getScheduler().queue(track);
                musicManager.getScheduler().setTextChannel(interaction.getTextChannel());

                final AudioManager audioManager = interaction.getGuild().getAudioManager();
                if (!audioManager.isConnected()) {
                    audioManager.openAudioConnection(interaction.getMember().getVoiceState().getChannel());
                }

                interaction.reply(EmojiEnum.MUSIC.getTag() + " Music " + track.getInfo().title + " added in the queue").queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                trackLoaded(playlist.getTracks().get(0));
            }

            @Override
            public void noMatches() {
                interaction.reply("Nothing found by **" + source + "**").setEphemeral(true).queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                interaction.reply("Can't load this type of track").setEphemeral(true).queue();
            }
        });
    }

    public final void skipTrack(final Guild guild) {
        if (getTracks(guild).isEmpty()) {
            getGuildAudioPlayer(guild).getScheduler().stopTracks();
        } else {
            getGuildAudioPlayer(guild).getScheduler().nextTrack();
        }
    }

    public final void clearTracks(final Guild guild) {
        getGuildAudioPlayer(guild).getScheduler().clearTracks();
    }

    public final void shuffleTracks(final Guild guild) {
        getGuildAudioPlayer(guild).getScheduler().shuffleTracks();
    }

    public final void pauseTrack(final Guild guild, final boolean pause) {
        getGuildAudioPlayer(guild).getAudioPlayer().setPaused(pause);
    }

    public final void stopTracks(final Guild guild) {
        getGuildAudioPlayer(guild).getScheduler().stopTracks();
    }

    public final boolean isPaused(final Guild guild) {
        return getGuildAudioPlayer(guild).getAudioPlayer().isPaused();
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

    public final void onUpdateVoiceStatus(@NotNull final Guild guild, final VoiceChannel voiceChannelLeft) {
        if (guild.getAudioManager().isConnected()) {
            if (voiceChannelLeft.getMembers().size() == 1) {
                getGuildAudioPlayer(guild).getScheduler().stopTracks();
            }
        }
    }
}