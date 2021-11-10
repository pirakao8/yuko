package command.music;

import bot.Bot;
import command.CommandEnum;
import net.dv8tion.jda.api.interactions.Interaction;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;
import bot.EmojiEnum;

import java.util.List;

public class VolumeCommand extends AbstractMusicCommand {
    public VolumeCommand() {
        super(CommandEnum.VOLUME,
                new OptionData(OptionType.INTEGER, "volume", "Volume between 0 and 100", true)
                );
    }

    @Override
    public void execute(@NotNull final Interaction interaction, final @NotNull Bot bot, final @NotNull List<OptionMapping> options) {
        super.execute(interaction, bot, options);

        assert !options.isEmpty();

        if (!isPlayable(interaction, bot)) {
            return;
        }

        final int volume = (int) options.get(0).getAsLong();

        if (volume < 0 || volume > 100) {
            interaction.reply("Volume must be a value between 0 and 100").setEphemeral(true).queue();
            return;
        }

        guildMusicPlayer.setVolume(interaction.getGuild(), volume);
        interaction.reply(EmojiEnum.VOLUME.getTag() + " Volume is now at " + guildMusicPlayer.getVolume(interaction.getGuild())).queue();
    }
}
