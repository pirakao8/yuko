package command.global;

import bot.Bot;
import command.CommandEnum;
import command.AbstractSlashCommand;
import net.dv8tion.jda.api.interactions.Interaction;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PingCommand extends AbstractSlashCommand {
    public PingCommand() {
        super(CommandEnum.PING);
    }

    @Override
    public final void execute(@NotNull final Interaction interaction, final Bot bot, final List<OptionMapping> options) {
        interaction.reply("Pong!").setEphemeral(true).flatMap(
                v -> interaction.getHook().editOriginalFormat("Pong: %d ms", System.currentTimeMillis() - System.currentTimeMillis())).queue();
    }
}
