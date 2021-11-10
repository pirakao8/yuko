package bot.listener;

import bot.Bot;
import command.AbstractSlashCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.jetbrains.annotations.NotNull;
import util.Logger;

public class SlashCommandEventListener extends AbstractEventListener {
    public SlashCommandEventListener(final Bot bot) {
        super(bot);
    }

    @Override
    public final void onSlashCommand(@NotNull final SlashCommandEvent event) {
        super.onSlashCommand(event);

        assert event.getMember() != null;
        assert event.getGuild()  != null;

        if (event.getMember().getUser().isBot() || event.getMember().getUser().isSystem()) {
            return;
        }

        final AbstractSlashCommand slashCommand = bot.getCommandByName(event.getName());
        if (slashCommand == null) {
            event.reply("Interaction failed, command unknown").setEphemeral(true).queue();
            logger.log(Logger.Level.WARNING, "Command:" + event.getName() + " unknown, commands may have not been correctly initialized");
            return;
        }

        slashCommand.execute(event, bot, event.getOptions());

        logger.logDiscordCommandSuccess(event, slashCommand);
    }
}
