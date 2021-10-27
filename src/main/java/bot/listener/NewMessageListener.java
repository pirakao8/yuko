package bot.listener;

import bot.Bot;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

public class NewMessageListener extends AbstractListener {
    public NewMessageListener(final Bot bot) {
        super(bot);
    }

    @Override
    public final void onGuildMessageReceived(@NotNull final GuildMessageReceivedEvent event) {
        super.onGuildMessageReceived(event);
        if(event.getAuthor().isBot()) return;

        assert event.getMember() != null;

        if (event.getMessage().getContentDisplay().startsWith("/") || event.getMessage().getContentDisplay().startsWith("!")) {
            logger.logDiscord(event.getGuild(), event.getMember(), event.getMessage().getTextChannel(), "Attempt command:" + event.getMessage().getContentDisplay());

            event.getMessage().reply("That's not how you supposed to use me, try '/help'").queue();
        }
    }
}
