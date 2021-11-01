package bot.listener;

import bot.Bot;
import bot.setting.GuildSettings;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.channel.category.CategoryCreateEvent;
import net.dv8tion.jda.api.events.channel.text.TextChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.voice.VoiceChannelCreateEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ChannelCreateListener extends AbstractListener {
    public ChannelCreateListener(Bot bot) {
        super(bot);
    }

    @Override
    public void onTextChannelCreate(@NotNull final TextChannelCreateEvent event) {
        super.onTextChannelCreate(event);
        onChannelEvent(event);
    }

    @Override
    public void onCategoryCreate(@NotNull final CategoryCreateEvent event) {
        super.onCategoryCreate(event);
        onChannelEvent(event);
    }

    @Override
    public void onVoiceChannelCreate(@NotNull final VoiceChannelCreateEvent event) {
        super.onVoiceChannelCreate(event);
        onChannelEvent(event);
    }

    private void onChannelEvent(@NotNull GenericEvent event) {
        final GuildChannel guildChannel;
        final Guild guild;
        if (event instanceof VoiceChannelCreateEvent) {
            guildChannel = ((VoiceChannelCreateEvent) event).getChannel();
            guild = ((VoiceChannelCreateEvent) event).getGuild();
        } else if (event instanceof CategoryCreateEvent) {
            guildChannel = ((CategoryCreateEvent) event).getCategory();
            guild = ((CategoryCreateEvent) event).getGuild();
        } else if (event instanceof TextChannelCreateEvent) {
            guildChannel = ((TextChannelCreateEvent) event).getChannel();
            guild = ((TextChannelCreateEvent) event).getGuild();
        } else {
            return;
        }

        if(guildChannel.getName().equalsIgnoreCase(GuildSettings.CHANNEL_WELCOMER)) {
            return;
        }

        if (bot.getGuildSetting(guild).isWelcomerEnable()) {
            final List<Role> roleList = guild.getRolesByName(GuildSettings.ROLE_WELCOMER, true);
            if(!roleList.isEmpty()) {
                guildChannel.getManager()
                        .putRolePermissionOverride(roleList.get(0).getIdLong(), GuildSettings.PERM_NEUTRAL, Permission.VIEW_CHANNEL.getRawValue())
                        .putRolePermissionOverride(guild.getPublicRole().getIdLong(), GuildSettings.PERM_NEUTRAL, GuildSettings.PERM_NEUTRAL)
                        .queue();
                logger.logDiscord(guild, "New channel created, role " + GuildSettings.ROLE_WELCOMER + " added");
            }
        }

        if (bot.getGuildSetting(guild).isJailEnable()) {
            final List<Role> roleList = guild.getRolesByName(GuildSettings.ROLE_JAIL, true);
            if(!roleList.isEmpty()) {
                guildChannel.getManager()
                        .putRolePermissionOverride(roleList.get(0).getIdLong(), GuildSettings.PERM_NEUTRAL, Permission.VIEW_CHANNEL.getRawValue())
                        .putRolePermissionOverride(guild.getPublicRole().getIdLong(), GuildSettings.PERM_NEUTRAL, GuildSettings.PERM_NEUTRAL)
                        .queue();
                logger.logDiscord(guild, "New channel created, role " + GuildSettings.ROLE_JAIL + " added");
            }
        }
    }
}
