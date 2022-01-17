package command.music;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class PlayCommand extends AbstractMusicCommand {
    @Contract(pure = true)
    @Override
    public final @NotNull String getName() {
        return "play";
    }

    @Contract(pure = true)
    @Override
    public final @NotNull String getDescription() {
        return "Play a YouTube audio";
    }

    @Contract(" -> new")
    @Override
    public final OptionData @NotNull [] getOptions() {
        return new OptionData[] {new OptionData(OptionType.STRING, "title", "Title, or Youtube link", true)};
    }

    @Override
    public final boolean isEnable() {
        return true;
    }

    @Override
    public final void execute(@NotNull final SlashCommandEvent event) {
        super.execute(event);

        assert event.getGuild()  != null;
        assert event.getMember() != null;
        assert event.getMember().getVoiceState() != null;

        if (!isMusicEnable(event.getGuild())) {
            event.reply(msgMusicDisabled).setEphemeral(true).queue();
            return;
        }

        if (!isMemberConnected(event.getMember())) {
            event.reply(msgMemberNotConnected).setEphemeral(true).queue();
            return;
        }

        String source = event.getOptions().get(0).getAsString();
        if (!source.startsWith("http")) {
            source = "ytsearch: " + source;
        }

        guildMusicPlayer.loadAndPlay(event, source);
    }
}
