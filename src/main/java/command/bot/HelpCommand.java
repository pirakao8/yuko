package command.bot;

import bot.Bot;
import bot.setting.Commands;
import command.AbstractSlashCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class HelpCommand extends AbstractSlashCommand {
    @Override
    public final void execute(final SlashCommandEvent event, final Bot bot) {
        super.execute(event, bot);

        final StringBuilder helpBuilder = new StringBuilder();

        helpBuilder.append("All commands available : \n \n");
        for (CommandData commandData : Commands.getCommands(bot.isApiLeagueEnable())) {
            helpBuilder.append("/" + commandData.getName());

            if (!commandData.getOptions().isEmpty()) {
                for(OptionData optionData : commandData.getOptions()) {
                    helpBuilder.append(" <" + optionData.getDescription() + ">");
                }
            }
            helpBuilder.append(" : " + commandData.getDescription() + "\n");
        }
        event.reply(helpBuilder.toString()).queue();
    }
}
