package button;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.interactions.components.Component;
import org.jetbrains.annotations.NotNull;

public interface Button {
    String getLabel();

    String getId();

    Emoji getEmoji();

    Permission getPermission();

    default Component getComponent(final boolean isDisabled) {
        return net.dv8tion.jda.api.interactions.components.Button.primary(getId(), getLabel()).withEmoji(getEmoji()).withDisabled(isDisabled);
    }

    default Component getComponent() {
        return getComponent(false);
    }

    void execute(@NotNull final ButtonClickEvent event);
}