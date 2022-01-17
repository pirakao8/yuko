package listener;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import util.Logger;

public abstract class AbstractEventListener extends ListenerAdapter {
    protected static final Logger logger = Logger.getLogger();
}
