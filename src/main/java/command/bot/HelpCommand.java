package command.bot;

import bot.Bot;
import bot.setting.Commands;
import command.AbstractSlashCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

public class HelpCommand extends AbstractSlashCommand {
    @Override
    public final void execute(final @NotNull SlashCommandEvent event, final Bot bot) {
        super.execute(event, bot);

        final StringBuilder helpBuilder = new StringBuilder();
        final Commands commands = new Commands();

        helpBuilder.append("All commands available:\n\n");

        helpBuilder.append("**Global commands**:\n");
        for (CommandData commandData : commands.getGlobalCommands()) {
            helpBuilder.append("/" + commandData.getName());

            if (!commandData.getOptions().isEmpty()) {
                for(OptionData optionData : commandData.getOptions()) {
                    helpBuilder.append(" *<" + optionData.getDescription() + ">*");
                }
            }
            helpBuilder.append(" : " + commandData.getDescription() + "\n");
        }

        helpBuilder.append("\n**Music commands**:\n");
        for (CommandData commandData : commands.getMusicCommands()) {
            helpBuilder.append("/" + commandData.getName());

            if (!commandData.getOptions().isEmpty()) {
                for(OptionData optionData : commandData.getOptions()) {
                    helpBuilder.append(" *<" + optionData.getDescription() + ">*");
                }
            }
            helpBuilder.append(" : " + commandData.getDescription() + "\n");
        }

        if (bot.isApiLeagueEnable()) {
            helpBuilder.append("\n**League of Legends commands**:\n");
            for (CommandData commandData : commands.getLeagueCommands()) {
                helpBuilder.append("/" + commandData.getName());

                if (!commandData.getOptions().isEmpty()) {
                    for (OptionData optionData : commandData.getOptions()) {
                        helpBuilder.append(" *<" + optionData.getDescription() + ">*");
                    }
                }
                helpBuilder.append(" : " + commandData.getDescription() + "\n");
            }
        }
        event.reply(helpBuilder.toString()).setEphemeral(true).queue();
    }
}
