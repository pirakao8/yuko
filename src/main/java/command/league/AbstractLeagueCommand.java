package command.league;

import bot.setting.GuildSettings;
import command.AbstractSlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;

public class AbstractLeagueCommand extends AbstractSlashCommand {
    protected final EmbedBuilder embedBuilder;

    public AbstractLeagueCommand() {
        embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(GuildSettings.DEFAULT_COLOR);
    }
}
