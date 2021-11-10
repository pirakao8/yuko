package command;

import bot.Bot;
import net.dv8tion.jda.api.interactions.Interaction;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;
import util.Logger;

import java.util.List;

public abstract class AbstractSlashCommand extends CommandData {
    protected static Logger logger = Logger.getLogger();

    private final String commandGroup;

    protected AbstractSlashCommand(@NotNull final CommandEnum commandEnum, @NotNull final OptionData @NotNull ... optionData) {
        super(commandEnum.getName(), commandEnum.getDescription());
        this.commandGroup = commandEnum.getGroup();

        if (optionData.length != 0) {
            addOptions(optionData);
        }
    }

    public abstract void execute(@NotNull final Interaction interaction, final Bot bot, List<OptionMapping> options);

    public String getCommandGroup() {
        return commandGroup;
    }
}
