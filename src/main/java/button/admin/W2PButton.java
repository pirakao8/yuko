package button.admin;

import bot.GuildSettings;
import button.Button;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class W2PButton implements Button {
    @Contract(pure = true)
    @Override
    public final @NotNull String getLabel() {
        return "Want2Play?";
    }

    @Contract(pure = true)
    @Override
    public final @NotNull String getId() {
        return "id_w2p";
    }

    @Contract(pure = true)
    @Override
    public final @Nullable Emoji getEmoji() {
        return null;
    }

    @Contract(pure = true)
    @Override
    public final @NotNull Permission getPermission() {
        return Permission.ADMINISTRATOR;
    }

    @Override
    public final void execute(@NotNull final ButtonClickEvent event) {
        assert event.getGuild()  != null;

        final GuildSettings guildSettings = GuildSettings.getInstance(event.getGuild());

        guildSettings.setEnableW2P(!guildSettings.isW2PEnable());

        if (guildSettings.isW2PEnable()) {
            event.reply("**/w2p** is now enabled").queue();
        } else {
            event.reply("**/w2p** is now disabled").queue();
        }
    }
}
