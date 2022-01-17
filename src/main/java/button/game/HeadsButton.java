package button.game;

import button.Button;
import core.game.FlipACoin;
import dataBase.EmojiEnum;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.TimeUnit;

public class HeadsButton implements Button {
    @Contract(pure = true)
    @Override
    public final @NotNull String getLabel() {
        return "Heads";
    }

    @Contract(pure = true)
    @Override
    public final @NotNull String getId() {
        return "id_heads";
    }

    @Override
    public final @NotNull Emoji getEmoji() {
        return EmojiEnum.SWORD.getEmoji();
    }

    @Contract(pure = true)
    @Override
    public final @Nullable Permission getPermission() {
        return null;
    }

    @Override
    public final void execute(@NotNull final ButtonClickEvent event) {
        if (new FlipACoin().getResult()) {
            event.reply("Result: **Heads**, you won gg wp")
                    .queue(m -> m.deleteOriginal().submitAfter(2, TimeUnit.MINUTES));
        } else {
            event.reply("Result: **Tails**, you lost gl next")
                    .queue(m -> m.deleteOriginal().submitAfter(2, TimeUnit.MINUTES));
        }
    }
}
