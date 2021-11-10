package button;

import bot.Bot;
import net.dv8tion.jda.api.interactions.Interaction;
import org.jetbrains.annotations.NotNull;
import util.Logger;

public abstract class AbstractButtonInteraction {
    protected final static Logger logger = Logger.getLogger();

    public abstract void execute(@NotNull final Interaction interaction, final @NotNull Bot bot);
}
