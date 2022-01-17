package button.admin;

import bot.GuildSettings;
import button.Button;
import dataBase.EmojiEnum;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class MusicButton implements Button {
    @Contract(pure = true)
    @Override
    public final @NotNull String getLabel() {
        return "Music";
    }

    @Contract(pure = true)
    @Override
    public final @NotNull String getId() {
        return "id_music";
    }

    @Override
    public final @NotNull Emoji getEmoji() {
        return EmojiEnum.MUSIC.getEmoji();
    }

    @Contract(pure = true)
    @Override
    public final @NotNull Permission getPermission() {
        return Permission.ADMINISTRATOR;
    }

    @Override
    public final void execute(@NotNull final ButtonClickEvent event) {
        assert event.getGuild()  != null;

        final Guild guild = event.getGuild();
        final GuildSettings guildSettings = GuildSettings.getInstance(guild);

        guildSettings.setEnableMusic(!guildSettings.isMusicEnable());

        if (guildSettings.isMusicEnable()) {
            event.reply("Music commands are now enabled").queue();
        } else {
            if (guild.getAudioManager().isConnected()) {
                guild.getAudioManager().closeAudioConnection();
            }
            event.reply("Music commands are now disabled").queue();
        }
    }
}
