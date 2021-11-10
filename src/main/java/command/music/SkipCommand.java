package command.music;

import bot.Bot;
import command.CommandEnum;
import net.dv8tion.jda.api.interactions.Interaction;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;
import bot.EmojiEnum;

import java.util.List;

public class SkipCommand extends AbstractMusicCommand {
    public SkipCommand() {
        super(CommandEnum.SKIP);
    }

    @Override
    public final void execute(@NotNull final Interaction interaction, final @NotNull Bot bot, final List<OptionMapping> options) {
        super.execute(interaction, bot, options);

        if (!isPlayable(interaction, bot)) {
            return;
        }

        if (!isMusicPlaying(interaction)) {
            return;
        }

        guildMusicPlayer.skipTrack(interaction.getGuild());
        interaction.reply(EmojiEnum.SKIP.getTag() + " Music skipped").queue();
    }
}
