package command.music;

import dataBase.EmojiEnum;
import bot.GuildSettings;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class QueueCommand extends AbstractMusicCommand {
    @Contract(pure = true)
    @Override
    public final @NotNull String getName() {
        return "queue";
    }

    @Contract(pure = true)
    @Override
    public final @NotNull String getDescription() {
        return "Display the current queue";
    }

    @Contract(pure = true)
    @Override
    public final OptionData @Nullable [] getOptions() {
        return null;
    }

    @Override
    public final boolean isEnable() {
        return true;
    }

    @Override
    public final void execute(@NotNull final SlashCommandEvent event) {
        super.execute(event);

        if (!isExecutable(event)) {
            return;
        }

        final Guild guild = event.getGuild();

        if (guildMusicPlayer.getTracks(guild).isEmpty()) {
            event.reply("Queue empty").setEphemeral(true).queue();
            return;
        }

        final EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(GuildSettings.DEFAULT_COLOR);
        embedBuilder.setTitle("File " + EmojiEnum.MUSIC.getTag());

        if (guildMusicPlayer.getTracks(guild).size() > 1) {
            embedBuilder.setDescription(guildMusicPlayer.getTracks(guild).size() + " tracks in queue");
        } else {
            embedBuilder.setDescription("Only 1 track in queue");
        }

        int trackPos = 0;
        for (AudioTrack audioTrack : guildMusicPlayer.getTracks(guild)) {
            trackPos++;
            embedBuilder.addField(String.valueOf(trackPos), "- '" + audioTrack.getInfo().title + "'", false);
        }

        event.replyEmbeds(embedBuilder.build()).queue();
        embedBuilder.clear();
    }
}
