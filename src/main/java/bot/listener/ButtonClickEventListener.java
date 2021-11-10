package bot.listener;

import bot.Bot;
import button.ButtonEnum;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import org.jetbrains.annotations.NotNull;
import util.Logger;

public class ButtonClickEventListener extends AbstractEventListener {
    public ButtonClickEventListener(final Bot bot) {
        super(bot);
    }

    @Override
    public void onButtonClick(@NotNull final ButtonClickEvent event) {
        super.onButtonClick(event);

        assert event.getGuild() != null;

        final ButtonEnum buttonLabelEnum = ButtonEnum.getComponent(event.getComponentId());
        if (buttonLabelEnum == null) {
            logger.log(Logger.Level.WARNING, "Button" + event.getComponentId() + " unknown, buttons may not have been correctly initialized");
            event.reply("Interaction failed, command unknown").setEphemeral(true).queue();
            return;
        }

        buttonLabelEnum.getButtonInteraction().execute(event, bot);

        logger.logDiscordButtonSuccess(event, buttonLabelEnum);
    }
}
