package command.music;

import bot.Bot;
import bot.GuildSettings;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import command.CommandEnum;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.Interaction;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;
import bot.EmojiEnum;

import java.util.List;

public class QueueCommand extends AbstractMusicCommand {
    public QueueCommand() {
        super(CommandEnum.QUEUE);
    }

    @Override
    public void execute(@NotNull final Interaction interaction, final @NotNull Bot bot, final List<OptionMapping> options) {
        super.execute(interaction, bot, options);

        if (!isPlayable(interaction, bot)) {
            return;
        }

        if (!isMusicPlaying(interaction)) {
            return;
        }

        if (guildMusicPlayer.getTracks(interaction.getGuild()).isEmpty()) {
            interaction.reply("Queue empty").setEphemeral(true).queue();
            return;
        }

        final EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(GuildSettings.DEFAULT_COLOR);
        embedBuilder.setTitle("Current queue " + EmojiEnum.MUSIC.getTag());
        embedBuilder.setDescription(guildMusicPlayer.getTracks(interaction.getGuild()).size() + " tracks in queue");

        for (AudioTrack audioTrack :  guildMusicPlayer.getTracks(interaction.getGuild())) {
            embedBuilder.addField("- '" + audioTrack.getInfo().title + "'", null, false);
        }

        interaction.replyEmbeds(embedBuilder.build()).queue();
        embedBuilder.clear();
    }
}
