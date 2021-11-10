package command.music;

import bot.Bot;
import command.CommandEnum;
import net.dv8tion.jda.api.interactions.Interaction;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;
import bot.EmojiEnum;

import java.util.List;

public class ClearCommand extends AbstractMusicCommand {
    public ClearCommand() {
        super(CommandEnum.CLEAR);
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
            interaction.reply("No tracks in queue").setEphemeral(true).queue();
            return;
        }

        guildMusicPlayer.clearTracks(interaction.getGuild());
        interaction.reply(EmojiEnum.CLEAR.getTag() + " Queue cleared").queue();
    }
}
