package command.bot;

import command.AbstractSlashCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.jetbrains.annotations.NotNull;

public class TopicCommand extends AbstractSlashCommand {
    @Override
    public final void execute(@NotNull final SlashCommandEvent event) {
        super.execute(event);

        if(event.getTextChannel().getTopic() != null) {
            event.reply(event.getTextChannel().getTopic()).setEphemeral(true).queue();
        } else {
            event.reply("No topic for this channel").setEphemeral(true).queue();
        }
    }
}
