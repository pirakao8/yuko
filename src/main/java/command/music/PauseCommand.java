package command.music;

import bot.Bot;
import command.CommandEnum;
import net.dv8tion.jda.api.interactions.Interaction;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;
import bot.EmojiEnum;

import java.util.List;

public class PauseCommand extends AbstractMusicCommand {
    public PauseCommand() {
        super(CommandEnum.PAUSE);
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

        if (!guildMusicPlayer.isPaused(interaction.getGuild())) {
            guildMusicPlayer.pauseTrack(interaction.getGuild());
            interaction.reply(EmojiEnum.PAUSE.getTag() + " Music paused").queue();
        } else {
            guildMusicPlayer.resumeTrack(interaction.getGuild());
            interaction.reply(EmojiEnum.PLAY.getTag() + " Music resumed").queue();
        }
    }
}
