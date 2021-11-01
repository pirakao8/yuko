package bot.listener;

import bot.Bot;
import bot.setting.EmojiList;
import bot.setting.GuildSettings;
import command.game.FlipCoinCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.interactions.components.Button;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;

public class ButtonClickListener extends AbstractListener {
    public static final String HEADS_BT_LABEL   = "Heads";
    public static final String TAILS_BT_LABEL   = "Tails";
    public static final String MUSIC_BT_LABEL   = "Music";
    public static final String JAIL_BT_LABEL    = "Yuko's jail";
    public static final String LEAGUE_BT_LABEL  = "League of Legends";
    public static final String W2P_BT_LABEL     = "W2P";
    public static final String PIN_BT_LABEL     = "Pin me";
    public static final String WELCOME_BT_LABEL = "Welcomer";
    public static final String AGREE_BT_LABEL   = "Agreed";

    private GuildSettings guildSettings;

    public ButtonClickListener(final Bot bot) {
        super(bot);
    }

    @Override
    public void onButtonClick(@NotNull final ButtonClickEvent event) {
        super.onButtonClick(event);

        assert event.getMember() != null;
        assert event.getGuild()  != null;
        assert event.getComponent() != null;

        guildSettings = bot.getGuildSetting(event.getGuild());

        logger.logDiscord(event.getGuild(), event.getMember(), event.getTextChannel(), "Button clicked:" + event.getComponent().getLabel());

        switch (event.getComponentId()) {
            case MUSIC_BT_LABEL:
                musicClicked(event);
                return;

            case JAIL_BT_LABEL:
                jailClicked(event);
                return;

            case LEAGUE_BT_LABEL:
                leagueClicked(event);
                return;

            case W2P_BT_LABEL:
                w2pClicked(event);
                return;

            case PIN_BT_LABEL:
                pinMeClicked(event);
                return;

            case HEADS_BT_LABEL:
                if (new FlipCoinCommand().getResult()) {
                    event.reply("Result: Heads, you won GG WP").setEphemeral(true).queue();
                } else {
                    event.reply("Result: Tails, you lost GL next").setEphemeral(true).queue();
                }
                return;

            case TAILS_BT_LABEL:
                if (new FlipCoinCommand().getResult()) {
                    event.reply("Result: Tails, you won GG WP").setEphemeral(true).queue();
                } else {
                    event.reply("Result: Heads, you lost GL next").setEphemeral(true).queue();
                }
                return;

            case WELCOME_BT_LABEL:
                welcomerClicked(event);
                return;

            case AGREE_BT_LABEL:
                agreeClicked(event);
                return;

            default:
        }
    }

    private void musicClicked(@NotNull final ButtonClickEvent event) {
        assert event.getMember() != null;
        assert event.getGuild() != null;
        assert guildSettings != null;

        if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            logger.logDiscordPermission(event.getGuild(), event.getMember(), event.getTextChannel(), Permission.ADMINISTRATOR);
            event.reply("You don't have permission to manage channels").setEphemeral(true).queue();
            return;
        }

        guildSettings.setEnableMusic(!guildSettings.isMusicEnable());

        if (guildSettings.isMusicEnable()) {
            event.reply("Music is now enabled").setEphemeral(true).queue();
        } else {
            event.reply("Music is now disabled").setEphemeral(true).queue();

            if (event.getGuild().getAudioManager().isConnected()) {
                event.getGuild().getAudioManager().closeAudioConnection();
            }
        }
    }

    private void jailClicked(@NotNull final ButtonClickEvent event) {
        assert event.getMember() != null;
        assert event.getGuild() != null;
        assert guildSettings != null;

        final Guild guild = event.getGuild();

        if(!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            logger.logDiscordPermission(event.getGuild(), event.getMember(), event.getTextChannel(), Permission.ADMINISTRATOR);
            event.reply("You don't have permission to manage roles").setEphemeral(true).queue();
            return;
        }

        guildSettings.setEnableJail(!guildSettings.isJailEnable());

        if(guildSettings.isJailEnable()) {
            event.reply("Time Out command is now enabled").setEphemeral(true).queue();

            if (guild.getRolesByName(GuildSettings.ROLE_JAIL, true).isEmpty()) {
                try {
                    guild.createRole().setName(GuildSettings.ROLE_JAIL)
                            .setColor(Color.BLACK).setPermissions(GuildSettings.PERM_NEUTRAL).complete();

                    final long idRole = guild.getRolesByName(GuildSettings.ROLE_JAIL, true).get(0).getIdLong();

                    for(TextChannel textChannel : guild.getTextChannels()) {
                        textChannel.getManager().putRolePermissionOverride(
                                idRole, GuildSettings.PERM_NEUTRAL, Permission.VIEW_CHANNEL.getRawValue()).queue();
                    }

                    for(Category category : guild.getCategories()) {
                        category.getManager().putRolePermissionOverride(
                                idRole, GuildSettings.PERM_NEUTRAL, Permission.VIEW_CHANNEL.getRawValue()).queue();
                    }

                    for(VoiceChannel voiceChannel : guild.getVoiceChannels()) {
                        voiceChannel.getManager().putRolePermissionOverride(
                                idRole, GuildSettings.PERM_NEUTRAL, Permission.VIEW_CHANNEL.getRawValue()).queue();
                    }

                } catch (HierarchyException | InsufficientPermissionException e) {
                    logger.logDiscordPermission(guild, event.getMember(), event.getTextChannel(), e.getPermission());
                }
            }
        } else {
            event.reply("Time Out command is now disabled").setEphemeral(true).queue();

            if (guild.getRolesByName(GuildSettings.ROLE_JAIL, true).size() > 0) {
                try {
                    for(Role role : guild.getRolesByName(GuildSettings.ROLE_JAIL, true)) {
                        role.delete().queue();
                    }
                } catch (HierarchyException | InsufficientPermissionException e) {
                    logger.logDiscordPermission(guild, event.getMember(), event.getTextChannel(), e.getPermission());
                }
            }
        }
    }

    private void leagueClicked(@NotNull final ButtonClickEvent event) {
        assert event.getMember() != null;
        assert event.getGuild() != null;
        assert guildSettings != null;

        if(!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            logger.logDiscordPermission(event.getGuild(), event.getMember(), event.getTextChannel(), Permission.ADMINISTRATOR);
            event.reply("You don't have permission to enable league commands").setEphemeral(true).queue();
            return;
        }

        guildSettings.setEnableLeague(!guildSettings.isLeagueEnable());

        if(guildSettings.isLeagueEnable()) {
            event.reply("League commands are now enabled").setEphemeral(true).queue();
        } else {
            event.reply("League commands are now disabled").setEphemeral(true).queue();
        }
    }

    private void w2pClicked(@NotNull final ButtonClickEvent event) {
        assert event.getMember() != null;
        assert event.getGuild() != null;
        assert guildSettings != null;

        if(!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            logger.logDiscordPermission(event.getGuild(), event.getMember(), event.getTextChannel(), Permission.ADMINISTRATOR);
            event.reply("You don't have permission to enable w2p command").setEphemeral(true).queue();
            return;
        }

        guildSettings.setEnableW2P(!guildSettings.isW2PEnable());

        if(guildSettings.isW2PEnable()) {
            event.reply("W2P command is now enabled").setEphemeral(true).queue();
        } else {
            event.reply("W2P command is now disabled").setEphemeral(true).queue();
        }
    }

    private void pinMeClicked(@NotNull final ButtonClickEvent event) {
        assert event.getMember() != null;
        assert event.getGuild() != null;

        if(!event.getMember().hasPermission(Permission.MESSAGE_MANAGE)) {
            logger.logDiscordPermission(event.getGuild(), event.getMember(), event.getTextChannel(), Permission.MESSAGE_MANAGE);
            event.reply("You don't have permission to manage messages").setEphemeral(true).queue();
            return;
        }

        if (event.getMessage().isPinned()) {
            event.getMessage().unpin().queue();
            event.reply("Message unpinned").setEphemeral(true).queue();
        } else {
            event.getMessage().pin().queue();
            event.reply("Message pinned").setEphemeral(true).queue();
        }
    }

    private void welcomerClicked(@NotNull final ButtonClickEvent event) {
        assert event.getMember() != null;
        assert event.getGuild() != null;
        assert guildSettings != null;

        final Guild guild = event.getGuild();

        if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            logger.logDiscordPermission(event.getGuild(), event.getMember(), event.getTextChannel(), Permission.ADMINISTRATOR);
            event.reply("You don't have permission to enable this").setEphemeral(true).queue();
            return;
        }

        if (guildSettings.isRuleEmpty()) {
            event.reply("To enable '" + GuildSettings.ROLE_WELCOMER + "', add some rules first with '/rules' command").setEphemeral(true).queue();
            return;
        }

        guildSettings.setEnableWelcomer(!guildSettings.isWelcomerEnable());

        if (guildSettings.isWelcomerEnable()) {

            event.reply(GuildSettings.ROLE_WELCOMER + " is now enabled").setEphemeral(true).queue();

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
                            Button.primary(AGREE_BT_LABEL, AGREE_BT_LABEL).withEmoji(Emoji.fromMarkdown(EmojiList.CHECK.getTag())))
                    .queue();
            embedBuilder.clear();

        } else {
            event.reply(GuildSettings.ROLE_WELCOMER + " is now disabled").setEphemeral(true).queue();

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
            } catch (HierarchyException | InsufficientPermissionException e) {
                logger.logDiscordPermission(guild, event.getMember(), event.getTextChannel(), e.getPermission());
            }
        }
    }

    private void agreeClicked(@NotNull final ButtonClickEvent event) {
        assert event.getMember() != null;
        assert event.getGuild() != null;
        event.reply("Rules checked").setEphemeral(true).queue();

        event.getGuild().removeRoleFromMember(event.getMember(), event.getGuild().getRolesByName(GuildSettings.ROLE_WELCOMER, true).get(0)).queue();
    }
}
