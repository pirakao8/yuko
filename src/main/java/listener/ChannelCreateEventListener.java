package listener;

import bot.GuildSettings;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.channel.category.CategoryCreateEvent;
import net.dv8tion.jda.api.events.channel.text.TextChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.voice.VoiceChannelCreateEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ChannelCreateEventListener extends AbstractEventListener {
    @Override
    public final void onTextChannelCreate(@NotNull final TextChannelCreateEvent event) {
        onChannelEvent(event);
    }

    @Override
    public final void onCategoryCreate(@NotNull final CategoryCreateEvent event) {
        onChannelEvent(event);
    }

    @Override
    public final void onVoiceChannelCreate(@NotNull final VoiceChannelCreateEvent event) {
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

        if (guildChannel.getName().equalsIgnoreCase(GuildSettings.CHANNEL_WELCOMER)) {
            return;
        }

        if (GuildSettings.getInstance(guild).isWelcomerEnable()) {
            final List<Role> roleList = guild.getRolesByName(GuildSettings.ROLE_WELCOMER, true);
            if (!roleList.isEmpty()) {
                guildChannel.getManager()
                        .putRolePermissionOverride(roleList.get(0).getIdLong(), GuildSettings.PERM_NEUTRAL, Permission.VIEW_CHANNEL.getRawValue())
                        .putRolePermissionOverride(guild.getPublicRole().getIdLong(), GuildSettings.PERM_NEUTRAL, GuildSettings.PERM_NEUTRAL)
                        .queue();
                logger.logDiscord(guild, event.getJDA().getSelfUser(), guildChannel, "Channel created, role " + GuildSettings.ROLE_WELCOMER + " added");
            }
        }
    }
}
