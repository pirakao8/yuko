package bot.listener;

import bot.Bot;
import net.dv8tion.jda.api.events.ReadyEvent;
import org.jetbrains.annotations.NotNull;
import util.Logger;

public class ReadyListener extends AbstractListener {
    public ReadyListener(final Bot bot) {
        super(bot);
    }

    @Override
    public final void onReady(@NotNull final ReadyEvent event) {
        super.onReady(event);
        logger.log(Logger.Level.SYSTEM, "Discord API ready");
    }
}
