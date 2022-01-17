package command;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

public interface Command {
    String getName();

    String getDescription();

    CommandCategoryEnum getCategory();

    OptionData[] getOptions();

    Permission getPermission();

    boolean isEnable();

    void execute(@NotNull final SlashCommandEvent event);
}
