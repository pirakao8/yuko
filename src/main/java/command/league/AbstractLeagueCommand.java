package command.league;

import bot.GuildSettings;
import command.CommandEnum;
import command.AbstractSlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractLeagueCommand extends AbstractSlashCommand {
    protected final EmbedBuilder embedBuilder;

    protected AbstractLeagueCommand(@NotNull final CommandEnum commandEnum, final OptionData... optionData) {
        super(commandEnum, optionData);

        embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(GuildSettings.DEFAULT_COLOR);
    }
}
