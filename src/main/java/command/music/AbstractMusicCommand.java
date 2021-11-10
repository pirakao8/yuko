package command.music;

import bot.Bot;
import command.CommandEnum;
import command.AbstractSlashCommand;
import net.dv8tion.jda.api.interactions.Interaction;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;
import util.Logger;
import util.music.GuildMusicPlayer;

import java.util.List;

public abstract class AbstractMusicCommand extends AbstractSlashCommand {
    protected static Logger logger = Logger.getLogger();

    protected GuildMusicPlayer guildMusicPlayer;

    protected AbstractMusicCommand(@NotNull CommandEnum commandEnum, @NotNull OptionData @NotNull ... optionData) {
        super(commandEnum, optionData);
    }

    @Override
    public void execute(@NotNull Interaction interaction, @NotNull Bot bot, List<OptionMapping> options) {
            guildMusicPlayer = bot.getGuildMusicPlayer();
    }

    protected final boolean isPlayable(@NotNull final Interaction interaction, @NotNull final Bot bot) {
        assert interaction.getMember() != null;
        assert interaction.getMember().getVoiceState() != null;

        if (!bot.getGuildSetting(interaction.getGuild()).isMusicEnable()) {
            interaction.reply("Music commands are disabled").setEphemeral(true).queue();
            return false;
        }

        if (!interaction.getMember().getVoiceState().inVoiceChannel()) {
            interaction.reply("You have to be in an audio channel").setEphemeral(true).queue();
            return false;
        }
        return true;
    }

    protected final boolean isMusicPlaying(@NotNull final Interaction interaction) {
        assert interaction.getGuild() != null;

        if (!interaction.getGuild().getAudioManager().isConnected()) {
            interaction.reply("Music not playing").setEphemeral(true).queue();
            return false;
        }
        return true;
    }
}
