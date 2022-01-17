package listener;

import button.Button;
import dataBase.ButtonDataBase;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import org.jetbrains.annotations.NotNull;
import util.Logger;

public class ButtonEventListener extends AbstractEventListener {
    @Override
    public final void onButtonClick(@NotNull final ButtonClickEvent event) {
        assert event.getButton() != null;
        assert event.getButton().getId() != null;

        final Button button = ButtonDataBase.getCommandById(event.getButton().getId());
        if (button == null) {
            assert event.getComponent() != null;

            event.getMessage().delete().submit();
            event.reply("I don't recognize this button, I deleted it").setEphemeral(true).queue();
            logger.log(Logger.Level.WARNING, "Button:" + event.getComponent().getId() + " unknown, buttons may have not been correctly initialized");
            return;
        }

        if (event.isFromGuild()) {
            assert event.getMember() != null;

            if (button.getPermission() != null && !event.getMember().hasPermission(button.getPermission())) {
                logger.logDiscordMemberPermission(event, button.getPermission());
                event.reply("You don't have the permission to use this button").setEphemeral(true).queue();
                return;
            }
        }

        button.execute(event);

        if (event.isFromGuild()) {
            assert event.getGuild()  != null;

            logger.logDiscord(event.getGuild(), event.getUser(), event.getTextChannel(), "Button:" + button.getId() + "|SUCCESS");
        } else {
            logger.logDiscord(event.getUser(), "Button:" + button.getId() + "|SUCCESS");
        }
    }
}
