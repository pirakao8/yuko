package bot.listener;

import bot.Bot;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import util.Logger;

public abstract class AbstractListener extends ListenerAdapter {
    protected final Bot bot;

    protected static final Logger logger = Logger.getLogger();

    public AbstractListener(final Bot bot) {
        this.bot = bot;
    }
}
