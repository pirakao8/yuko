package command.admin;

import bot.GuildSettings;
import command.Command;
import command.CommandCategoryEnum;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import util.Logger;

import java.util.concurrent.TimeUnit;

public class DeleteRulesCommand implements Command {
    private static final Logger logger = Logger.getLogger();

    @Contract(pure = true)
    @Override
    public final @NotNull String getName() {
        return "deleterules";
    }

    @Contract(pure = true)
    @Override
    public final @NotNull String getDescription() {
        return "Delete all rules of this guild";
    }

    @Override
    public final CommandCategoryEnum getCategory() {
        return CommandCategoryEnum.ADMIN;
    }

    @Contract(pure = true)
    @Override
    public final OptionData @Nullable [] getOptions() {
        return null;
    }

    @Override
    public final Permission getPermission() {
        return Permission.ADMINISTRATOR;
    }

    @Override
    public final boolean isEnable() {
        return true;
    }

    @Override
    public final void execute(@NotNull final SlashCommandEvent event) {
        assert event.getMember() != null;
        assert event.getGuild()  != null;

        final GuildSettings guildSettings = GuildSettings.getInstance(event.getGuild());
        final Guild guild = event.getGuild();

        if (guildSettings.isRuleEmpty()) {
            event.reply("No rules for this guild").setEphemeral(true).queue();
            return;
        }

        guildSettings.clearRules();

        try {
            for (Role role : guild.getRolesByName(GuildSettings.ROLE_WELCOMER, true)) {
                role.delete().queue();
            }

            for (TextChannel textChannel : guild.getTextChannelsByName(GuildSettings.CHANNEL_WELCOMER, true)) {
                textChannel.delete().queue();
            }
            event.reply("All rules deleted").queue(m -> m.deleteOriginal().submitAfter(5, TimeUnit.MINUTES));
        } catch (InsufficientPermissionException e) {
            logger.logDiscordBotPermission(guild, e);
            event.reply("I don't have enough permission to manage roles, **" + GuildSettings.ROLE_WELCOMER + "** has not been deleted").queue();
        } catch (HierarchyException e) {
            logger.logDiscordHierarchyPermission(guild, e);
            event.reply("My role is lower than **" + GuildSettings.ROLE_WELCOMER + "**, I can't delete this role").queue();
        }
    }
}
