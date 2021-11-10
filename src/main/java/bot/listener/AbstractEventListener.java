package bot.listener;

import bot.Bot;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import util.Logger;

public abstract class AbstractEventListener extends ListenerAdapter {
    protected final Bot bot;

    protected static final Logger logger = Logger.getLogger();

    public AbstractEventListener(final Bot bot) {
        this.bot = bot;
    }
}
