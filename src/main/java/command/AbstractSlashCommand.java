package command;

import bot.Bot;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.jetbrains.annotations.NotNull;
import util.Logger;

public abstract class AbstractSlashCommand {
    protected static Logger logger = Logger.getLogger();

    public void execute(@NotNull final SlashCommandEvent event) {
        event.getTextChannel().sendTyping().queue();
    }

    public void execute(@NotNull final SlashCommandEvent event, final Bot bot) {
        event.getTextChannel().sendTyping().queue();
    }
}
