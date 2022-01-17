package button.admin;

import bot.Bot;
import bot.GuildSettings;
import button.Button;
import dataBase.EmojiEnum;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class LeagueButton implements Button {
    @Contract(pure = true)
    @Override
    public final @NotNull String getLabel() {
        return "League Of Legends";
    }

    @Contract(pure = true)
    @Override
    public final @NotNull String getId() {
        return "id_league";
    }

    @Override
    public final @NotNull Emoji getEmoji() {
        return EmojiEnum.WITCHER.getEmoji();
    }

    @Contract(pure = true)
    @Override
    public final @NotNull Permission getPermission() {
        return Permission.ADMINISTRATOR;
    }

    @Override
    public final void execute(@NotNull final ButtonClickEvent event) {
        assert event.getGuild()  != null;

        if (!Bot.isApiLeagueEnable()) {
            event.reply("League of Legends commands are unavailable due to an error with Riot API").setEphemeral(true).queue();
            return;
        }

        final GuildSettings guildSettings = GuildSettings.getInstance(event.getGuild());

        guildSettings.setEnableLeague(!guildSettings.isLeagueEnable());

        if (guildSettings.isLeagueEnable()) {
            event.reply("League of Legends commands are now enabled").queue();
        } else {
            event.reply("League of Legends commands are now disabled").queue();
        }
    }
}
