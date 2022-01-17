package button.admin;

import bot.GuildSettings;
import button.Button;
import dataBase.EmojiEnum;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import util.Logger;

import java.util.concurrent.TimeUnit;

public class WelcomerButton implements Button {
    private static final Logger logger = Logger.getLogger();

    @Contract(pure = true)
    @Override
    public final @NotNull String getLabel() {
        return GuildSettings.ROLE_WELCOMER;
    }

    @Contract(pure = true)
    @Override
    public final @NotNull String getId() {
        return "id_welcome";
    }

    @Override
    public final @NotNull Emoji getEmoji() {
        return EmojiEnum.WELCOME.getEmoji();
    }

    @Contract(pure = true)
    @Override
    public final @NotNull Permission getPermission() {
        return Permission.ADMINISTRATOR;
    }

    @Override
    public final void execute(@NotNull final ButtonClickEvent event) {
        assert event.getGuild() != null;

        final Guild guild = event.getGuild();
        final GuildSettings guildSettings = GuildSettings.getInstance(guild);

        if (guildSettings.isRuleEmpty()) {
            event.reply("To enable **" + GuildSettings.ROLE_WELCOMER + "**, add some rules first with **/rules** command").setEphemeral(true).queue();
            return;
        }

        try {
            for (Role role : guild.getRolesByName(GuildSettings.ROLE_WELCOMER, true)) {
                role.delete().queue();
            }

            for (TextChannel textChannel : guild.getTextChannelsByName(GuildSettings.CHANNEL_WELCOMER, true)) {
                textChannel.delete().queue();
            }

            guildSettings.setEnableWelcomer(!guildSettings.isWelcomerEnable());

            if (guildSettings.isWelcomerEnable()) {
                guild.createRole().setName(GuildSettings.ROLE_WELCOMER).setColor(GuildSettings.DEFAULT_COLOR).setPermissions(Permission.EMPTY_PERMISSIONS)
                        .queue(welcomerRole -> {
                            addRoleToChannels(guild, welcomerRole);
                            sendWelcomeMessage(guild, welcomerRole, guildSettings);
                        });
                event.reply("**" + GuildSettings.ROLE_WELCOMER + "** is now enabled").queue();
            } else {
                event.reply("**" + GuildSettings.ROLE_WELCOMER + "** is now disabled").queue();
            }
        } catch (InsufficientPermissionException e) {
            logger.logDiscordBotPermission(guild, e);
            event.reply("I don't have enough permission to manage roles, **" + GuildSettings.ROLE_WELCOMER + "** has not been deleted").queue();
        } catch (HierarchyException e) {
            logger.logDiscordHierarchyPermission(guild, e);
            event.reply("My role is lower than **" + GuildSettings.ROLE_WELCOMER + "**, I can't delete this role").queue();
        }
    }

    private void addRoleToChannels(final @NotNull Guild guild, final Role role) {
        for (Category category : guild.getCategories()) {
            category.getManager().putRolePermissionOverride(role.getIdLong(), GuildSettings.PERM_NEUTRAL, Permission.VIEW_CHANNEL.getRawValue())
                    .queueAfter(1, TimeUnit.SECONDS);
        }

        for (TextChannel textChannel : guild.getTextChannels()) {
            textChannel.getManager().putRolePermissionOverride(role.getIdLong(), GuildSettings.PERM_NEUTRAL, Permission.VIEW_CHANNEL.getRawValue())
                    .queueAfter(1, TimeUnit.SECONDS);
        }

        for (VoiceChannel voiceChannel : guild.getVoiceChannels()) {
            voiceChannel.getManager().putRolePermissionOverride(role.getIdLong(), GuildSettings.PERM_NEUTRAL, Permission.VIEW_CHANNEL.getRawValue())
                    .queueAfter(1, TimeUnit.SECONDS);
        }
    }

    private void sendWelcomeMessage(final @NotNull Guild guild, final @NotNull Role role, final GuildSettings guildSettings) {
        guild.createTextChannel(GuildSettings.CHANNEL_WELCOMER)
                .addRolePermissionOverride(role.getIdLong(), GuildSettings.PERM_WELCOMER_ALLOW, GuildSettings.PERM_WELCOMER_DENY)
                .addRolePermissionOverride(guild.getPublicRole().getIdLong(), GuildSettings.PERM_NEUTRAL, Permission.VIEW_CHANNEL.getRawValue())
                .queue(m -> {
                    final EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder.setColor(GuildSettings.DEFAULT_COLOR);
                    embedBuilder.setTitle(guild.getName() + "'s rules:");
                    embedBuilder.appendDescription(guildSettings.getRules());

                    m.sendMessageEmbeds(embedBuilder.build())
                            .setActionRow(new AgreedButton().getComponent()).queue();
                    embedBuilder.clear();
                });
    }
}
