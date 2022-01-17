package button.admin;

import bot.GuildSettings;
import button.Button;
import dataBase.EmojiEnum;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import util.Logger;

public class AgreedButton implements Button {
    private static final Logger logger = Logger.getLogger();

    @Contract(pure = true)
    @Override
    public final @NotNull String getLabel() {
        return "I agree";
    }

    @Contract(pure = true)
    @Override
    public final @NotNull String getId() {
        return "id_agree";
    }

    @Override
    public final @NotNull Emoji getEmoji() {
        return EmojiEnum.CHECK.getEmoji();
    }

    @Contract(pure = true)
    @Override
    public final @Nullable Permission getPermission() {
        return null;
    }

    @Override
    public final void execute(@NotNull final ButtonClickEvent event) {
        assert event.getMember() != null;
        assert event.getGuild()  != null;

        final Guild guild = event.getGuild();

        try {
            guild.removeRoleFromMember(event.getMember(), guild.getRolesByName(GuildSettings.ROLE_WELCOMER, true).get(0)).queue();
            event.reply("Rules checked").setEphemeral(true).queue();
        } catch (InsufficientPermissionException e) {
            logger.logDiscordBotPermission(guild, e);
            event.reply("I don't have enough permission to remove your **" + GuildSettings.ROLE_WELCOMER + "** role, please, contact an admin").queue();
        } catch (HierarchyException e) {
            logger.logDiscordHierarchyPermission(guild, e);
            event.reply("My role is lower than **" + GuildSettings.ROLE_WELCOMER + "**, I can't remove yours, please, contact an admin").queue();
        }
    }
}
