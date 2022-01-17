package command.music;

import dataBase.EmojiEnum;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ShuffleCommand extends AbstractMusicCommand {
    @Contract(pure = true)
    @Override
    public final @NotNull String getName() {
        return "shuffle";
    }

    @Contract(pure = true)
    @Override
    public final @NotNull String getDescription() {
        return "Shuffle the current queue";
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
            event.reply("No tracks in queue").setEphemeral(true).queue();
            return;
        }

        guildMusicPlayer.shuffleTracks(guild);
        event.reply(EmojiEnum.SHUFFLE.getTag() + " Queue shuffled").queue();
    }
}
