package command.music;

import dataBase.EmojiEnum;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StopCommand extends AbstractMusicCommand {
    @Contract(pure = true)
    @Override
    public final @NotNull String getName() {
        return "stop";
    }

    @Contract(pure = true)
    @Override
    public final @NotNull String getDescription() {
        return "Stop the track and clear the queue";
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

        guildMusicPlayer.stopTracks(event.getGuild());
        event.reply(EmojiEnum.STOP.getTag() + " Music stopped").queue();
    }
}
