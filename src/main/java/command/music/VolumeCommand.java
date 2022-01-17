package command.music;

import dataBase.EmojiEnum;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class VolumeCommand extends AbstractMusicCommand {
    @Contract(pure = true)
    @Override
    public final @NotNull String getName() {
        return "volume";
    }

    @Contract(pure = true)
    @Override
    public final @NotNull String getDescription() {
        return "Set up the volume";
    }

    @Override
    public OptionData[] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.INTEGER, "value", "Value between 0 and 100", true)
        };
    }

    @Override
    public final boolean isEnable() {
        return true;
    }

    @Override
    public final void execute(@NotNull final SlashCommandEvent event) {
        super.execute(event);

        final Guild guild = event.getGuild();

        try {
            final int volume  = (int) event.getOptions().get(0).getAsLong();

            if (volume < 0 || volume > 100) {
                throw new IllegalStateException();
            }

            guildMusicPlayer.setVolume(guild, volume);
            event.reply(EmojiEnum.VOLUME.getTag() + " Volume is now at **" + guildMusicPlayer.getVolume(guild) + "**").queue();

        } catch (IllegalStateException | NumberFormatException e) {
            event.reply("Volume must be a value between 0 and 100").setEphemeral(true).queue();
        }
    }
}
