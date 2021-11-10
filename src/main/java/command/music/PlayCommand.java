package command.music;

import bot.Bot;
import command.CommandEnum;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.interactions.Interaction;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PlayCommand extends AbstractMusicCommand {
    public PlayCommand() {
        super(CommandEnum.PLAY,
                new OptionData(OptionType.STRING, "title", "YouTube link or title of the YouTube audio", true)
        );
    }

    @Override
    public final void execute(@NotNull final Interaction interaction, final @NotNull Bot bot, final @NotNull List<OptionMapping> options) {
        super.execute(interaction, bot, options);

        assert interaction.getMember() != null;
        assert interaction.getGuild()  != null;
        assert interaction.getMember().getVoiceState() != null;
        assert !options.isEmpty();

        if (!isPlayable(interaction, bot)) {
            return;
        }

        String source = options.get(0).getAsString();
        if (!source.startsWith("http")) {
            source = "ytsearch: " + source;
        }

        try {
            if (!interaction.getGuild().getAudioManager().isConnected()) {
                interaction.getGuild().getAudioManager().openAudioConnection(interaction.getMember().getVoiceState().getChannel());
            }

            guildMusicPlayer.loadAndPlay(interaction, source);
        } catch (InsufficientPermissionException e) {
            logger.logDiscordBotPermission(interaction.getGuild(), e);
            interaction.reply("I don't have permission to connect to your voice channel and speak").setEphemeral(true).queue();
        }
    }
}
