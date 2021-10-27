package command.bot;

import command.AbstractSlashCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class PingCommand extends AbstractSlashCommand {
    @Override
    public final void execute(final SlashCommandEvent event) {
        super.execute(event);

        event.reply("Pong !").setEphemeral(true)
                .flatMap(v -> event.getHook().editOriginalFormat("Pong : %d ms", System.currentTimeMillis() - System.currentTimeMillis())).queue();
    }
}
