package button.global;

import bot.Bot;
import bot.GuildSettings;
import button.ButtonEnum;
import button.AbstractButtonInteraction;
import command.CommandEnum;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.interactions.Interaction;
import net.dv8tion.jda.api.interactions.components.Button;
import org.jetbrains.annotations.NotNull;
import bot.EmojiEnum;

import java.util.List;

public class WelcomerButtonInteraction extends AbstractButtonInteraction {
    @Override
    public final void execute(@NotNull final Interaction interaction, final @NotNull Bot bot) {
        assert interaction.getMember() != null;
        assert interaction.getGuild() != null;

        final Guild guild = interaction.getGuild();
        final GuildSettings guildSettings = bot.getGuildSetting(guild);

        if (!interaction.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            logger.logDiscordMemberPermission(interaction, Permission.ADMINISTRATOR);
            interaction.reply("You don't have permission to enable this").setEphemeral(true).queue();
            return;
        }

        if (guildSettings.isRuleEmpty()) {
            interaction.reply("To enable '" + GuildSettings.ROLE_WELCOMER + "', add some rules first with *" + CommandEnum.RULES.getName() + "* command").setEphemeral(true).queue();
            return;
        }

        guildSettings.setEnableWelcomer(!guildSettings.isWelcomerEnable());

        if (guildSettings.isWelcomerEnable()) {
            try {
                guild.createRole().setName(GuildSettings.ROLE_WELCOMER).setColor(GuildSettings.DEFAULT_COLOR).setPermissions(Permission.EMPTY_PERMISSIONS).complete();

                final long idRole = guild.getRolesByName(GuildSettings.ROLE_WELCOMER, true).get(0).getIdLong();

                for (Category category : guild.getCategories()) {
                    category.getManager().putRolePermissionOverride(idRole, GuildSettings.PERM_NEUTRAL, Permission.VIEW_CHANNEL.getRawValue()).queue();
                }

                for (TextChannel textChannel : guild.getTextChannels()) {
                    textChannel.getManager().putRolePermissionOverride(idRole, GuildSettings.PERM_NEUTRAL, Permission.VIEW_CHANNEL.getRawValue()).queue();
                }

                for (VoiceChannel voiceChannel : guild.getVoiceChannels()) {
                    voiceChannel.getManager().putRolePermissionOverride(idRole, GuildSettings.PERM_NEUTRAL, Permission.VIEW_CHANNEL.getRawValue()).queue();
                }

                guild.createTextChannel(GuildSettings.CHANNEL_WELCOMER)
                        .addRolePermissionOverride(idRole, GuildSettings.PERM_WELCOMER_ALLOW, GuildSettings.PERM_WELCOMER_DENY)
                        .addRolePermissionOverride(guild.getPublicRole().getIdLong(), GuildSettings.PERM_NEUTRAL, Permission.VIEW_CHANNEL.getRawValue())
                        .complete();

                final EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setColor(GuildSettings.DEFAULT_COLOR);
                embedBuilder.setTitle(guild.getName() + "'s rules:");
                embedBuilder.appendDescription(guildSettings.getRules());

                guild.getTextChannelsByName(GuildSettings.CHANNEL_WELCOMER, true).get(0)
                        .sendMessageEmbeds(embedBuilder.build()).setActionRow(
                                Button.primary(ButtonEnum.BT_AGREE.getId(), ButtonEnum.BT_AGREE.getLabel()).withEmoji(Emoji.fromMarkdown(EmojiEnum.CHECK.getTag())))
                        .queue();
                embedBuilder.clear();

                interaction.reply(GuildSettings.ROLE_WELCOMER + " is now enabled").setEphemeral(true).queue();
            } catch (InsufficientPermissionException e) {
                logger.logDiscordBotPermission(guild, e);
                interaction.reply("I don't have enough permission to manage roles, '" + GuildSettings.ROLE_WELCOMER + "' won't work correctly").setEphemeral(true).queue();
            }
        } else {
            interaction.reply(GuildSettings.ROLE_WELCOMER + " is now disabled").setEphemeral(true).queue();

            final List<Role> roleList = guild.getRolesByName(GuildSettings.ROLE_WELCOMER, true);
            final List<TextChannel> textChannelList = guild.getTextChannelsByName(GuildSettings.CHANNEL_WELCOMER, true);
            try {
                if (!roleList.isEmpty()) {
                    for(Role role : roleList) {
                        role.delete().queue();
                    }
                }
                if (!textChannelList.isEmpty()) {
                    for (TextChannel textChannel : textChannelList) {
                        textChannel.delete().queue();
                    }
                }
            } catch (InsufficientPermissionException e) {
                logger.logDiscordBotPermission(guild, e);
                interaction.reply("I don't have enough permission to manage roles, '" + GuildSettings.ROLE_WELCOMER + "' has not been deleted").setEphemeral(true).queue();
            } catch (HierarchyException e) {
                logger.logDiscordHierarchyPermission(guild, e);
                interaction.reply("My role is lower than '" + GuildSettings.ROLE_WELCOMER + "', I can't delete this role").setEphemeral(true).queue();
            }
        }
    }
}
