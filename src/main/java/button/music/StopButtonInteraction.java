package button.music;

import bot.Bot;
import button.AbstractButtonInteraction;
import command.CommandEnum;
import net.dv8tion.jda.api.interactions.Interaction;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class StopButtonInteraction extends AbstractButtonInteraction {
    @Override
    public final void execute(@NotNull final Interaction interaction, final @NotNull Bot bot) {
        bot.getCommandByName(CommandEnum.STOP.getName()).execute(interaction, bot, new ArrayList<>());
    }
}
