package command.league;

import bot.GuildSettings;
import command.Command;
import command.CommandCategoryEnum;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import util.Logger;

public abstract class AbstractLeagueCommand implements Command {
    protected static final Logger logger = Logger.getLogger();

    protected EmbedBuilder embedBuilder;

    @Override
    public final CommandCategoryEnum getCategory() {
        return CommandCategoryEnum.LEAGUE;
    }

    @Contract(pure = true)
    @Override
    public final @Nullable Permission getPermission() {
        return null;
    }

    @Override
    public void execute(@NotNull SlashCommandEvent event) {
        embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(GuildSettings.DEFAULT_COLOR);
    }
}
