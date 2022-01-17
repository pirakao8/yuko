package listener;

import command.Command;
import dataBase.CommandDataBase;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.jetbrains.annotations.NotNull;
import util.Logger;

public class CommandEventListener extends AbstractEventListener {
    @Override
    public final void onSlashCommand(@NotNull final SlashCommandEvent event) {
        if (!event.isFromGuild()) {
            event.reply("You must be on a guild to use my commands").queue();
            return;
        }

        assert event.getMember() != null;
        assert event.getGuild()  != null;

        if (event.getUser().isBot() || event.getUser().isSystem()) {
            return;
        }

        final Command command = CommandDataBase.getCommandByName(event.getName());

        if (command == null) {
            event.reply("Interaction failed, command unknown").setEphemeral(true).queue();
            logger.log(Logger.Level.WARNING, "Command:" + event.getName() + " unknown, commands may have not been correctly initialized");
            return;
        }

        if (command.getPermission() != null && !event.getMember().hasPermission(command.getPermission())) {
            logger.logDiscordMemberPermission(event, command.getPermission());
            event.reply("You don't have the permission to use this command").setEphemeral(true).queue();
            return;
        }

        if (!command.isEnable()) {
            event.reply("Sorry, this command is disabled").setEphemeral(true).queue();
            return;
        }

        command.execute(event);
        logger.logDiscord(event.getGuild(), event.getUser(), event.getTextChannel(), "Command:" + command.getName() + "|SUCCESS");
    }
}
