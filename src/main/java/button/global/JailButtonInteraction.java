package button.global;

import bot.Bot;
import bot.GuildSettings;
import button.AbstractButtonInteraction;
import command.CommandEnum;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.interactions.Interaction;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class JailButtonInteraction extends AbstractButtonInteraction {

    @Override
    public final void execute(@NotNull final Interaction interaction, final @NotNull Bot bot) {
        assert interaction.getMember() != null;
        assert interaction.getGuild() != null;

        final Guild guild = interaction.getGuild();
        final GuildSettings guildSettings = bot.getGuildSetting(guild);

        if (!interaction.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            logger.logDiscordMemberPermission(interaction, Permission.ADMINISTRATOR);
            interaction.reply("You don't have enough permission to enable */" + CommandEnum.TIME_OUT.getName() + "* command").setEphemeral(true).queue();
            return;
        }

        guildSettings.setEnableJail(!guildSettings.isJailEnable());

        if (guildSettings.isJailEnable()) {
            if (guild.getRolesByName(GuildSettings.ROLE_JAIL, true).isEmpty()) {
                try {
                    guild.createRole().setName(GuildSettings.ROLE_JAIL).setColor(Color.BLACK).setPermissions(GuildSettings.PERM_NEUTRAL).complete();

                    final long idRole = guild.getRolesByName(GuildSettings.ROLE_JAIL, true).get(0).getIdLong();

                    for (TextChannel textChannel : guild.getTextChannels()) {
                        textChannel.getManager().putRolePermissionOverride(
                                        idRole, GuildSettings.PERM_NEUTRAL, Permission.VIEW_CHANNEL.getRawValue())
                                .queue();
                    }

                    for (Category category : guild.getCategories()) {
                        category.getManager().putRolePermissionOverride(
                                        idRole, GuildSettings.PERM_NEUTRAL, Permission.VIEW_CHANNEL.getRawValue())
                                .queue();
                    }

                    for (VoiceChannel voiceChannel : guild.getVoiceChannels()) {
                        voiceChannel.getManager().putRolePermissionOverride(
                                        idRole, GuildSettings.PERM_NEUTRAL, Permission.VIEW_CHANNEL.getRawValue())
                                .queue();
                    }
                    interaction.reply("*/" + CommandEnum.TIME_OUT.getName() + "* command is now enabled").setEphemeral(true).queue();
                } catch (InsufficientPermissionException e) {
                    logger.logDiscordBotPermission(guild, e);
                    interaction.reply("I don't have enough permission to manage roles, */" + CommandEnum.TIME_OUT.getName() + "* command won't work correctly").setEphemeral(true).queue();
                }
            }
        } else {
            try {
                if (guild.getRolesByName(GuildSettings.ROLE_JAIL, true).size() > 0) {
                    for (Role role : guild.getRolesByName(GuildSettings.ROLE_JAIL, true)) {
                        role.delete().queue();
                    }
                }
                interaction.reply("*/" + CommandEnum.TIME_OUT.getName() + "*command is now disabled").setEphemeral(true).queue();
            } catch (InsufficientPermissionException e) {
                logger.logDiscordBotPermission(guild, e);
                interaction.reply("I don't have enough permission to manage roles, '" + GuildSettings.ROLE_JAIL + "' role has not been deleted").setEphemeral(true).queue();
            } catch (HierarchyException e) {
                logger.logDiscordHierarchyPermission(guild, e);
                interaction.reply("My role is lower than '" + GuildSettings.ROLE_JAIL + "', I can't delete this role").setEphemeral(true).queue();
            }
        }
    }
}
