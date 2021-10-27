package bot.listener;

import bot.Bot;
import bot.setting.GuildSettings;
import command.game.FlipCoinCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class OnButtonClickListener extends AbstractListener {
    public static final String HEADS_BT_LABEL = "headsBt";
    public static final String TAILS_BT_LABEL = "tailsBt";
    public static final String MUSIC_BT_LABEL  = "Music";
    public static final String JAIL_BT_LABEL   = "Yuko's jail";
    public static final String LEAGUE_BT_LABEL = "League of Legends";
    public static final String W2P_BT_LABEL    = "W2P";
    public static final String PIN_BT_LABEL    = "Pin me";

    private GuildSettings guildSettings;

    public OnButtonClickListener(final Bot bot) {
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
                if(new FlipCoinCommand().getResult()) {
                    event.reply("Result: Tails, you won GG WP").setEphemeral(true).queue();
                } else {
                    event.reply("Result: Heads, you lost GL next").setEphemeral(true).queue();
                }
                return;

            default:
                break;
        }
    }

    private void musicClicked(@NotNull final ButtonClickEvent event) {
        assert event.getMember() != null;
        assert event.getGuild() != null;
        assert guildSettings != null;

        if (event.getMember().hasPermission(Permission.MANAGE_CHANNEL)) {
            guildSettings.setEnableMusic(!guildSettings.isMusicEnable());

            if (guildSettings.isMusicEnable()) {
                event.reply("Music is now enabled").setEphemeral(true).queue();

                if (event.getGuild().getTextChannelsByName(GuildSettings.CHANNEL_MUSIC, true).isEmpty()) {
                    try {
                        event.getGuild().createTextChannel(GuildSettings.CHANNEL_MUSIC).queue();
                        event.getChannel().sendMessage("Channel '" + GuildSettings.CHANNEL_MUSIC + "' created").queue();
                    } catch (InsufficientPermissionException e) {
                        logger.logDiscordPermission(event.getGuild(), event.getMember(), event.getTextChannel(), e.getPermission());
                        event.getChannel().sendMessage("Can't create '" + GuildSettings.CHANNEL_MUSIC + "', music is enable in all channels").queue();
                    }
                }
            } else {
                event.reply("Music is now disabled").setEphemeral(true).queue();

                if(event.getGuild().getAudioManager().isConnected()) {
                    event.getGuild().getAudioManager().closeAudioConnection();
                }

                if (event.getGuild().getTextChannelsByName(GuildSettings.CHANNEL_MUSIC, true).size() > 0) {
                    for (TextChannel textChannel : event.getGuild().getTextChannelsByName(GuildSettings.CHANNEL_MUSIC, true)) {
                        try {
                            textChannel.delete().queue();
                            event.getChannel().sendMessage("Channel '" + GuildSettings.CHANNEL_MUSIC + "' deleted").queue();
                        } catch (InsufficientPermissionException e) {
                            logger.logDiscordPermission(event.getGuild(), event.getMember(), event.getTextChannel(), e.getPermission());
                            event.getChannel().sendMessage("Can't delete '" + GuildSettings.CHANNEL_MUSIC + "'").queue();
                        }
                    }
                }
            }
        } else {
            event.reply("You don't have permission to manage channels").setEphemeral(true).queue();
        }
    }

    private void jailClicked(@NotNull final ButtonClickEvent event) {
        assert event.getMember() != null;
        assert event.getGuild() != null;
        assert guildSettings != null;

        if(event.getMember().hasPermission(Permission.MANAGE_ROLES)) {
            guildSettings.setEnableJail(!guildSettings.isJailEnable());

            if(guildSettings.isJailEnable()) {
                event.reply("Time Out command is now enabled").setEphemeral(true).queue();

                if (event.getGuild().getRolesByName(GuildSettings.ROLE_JAIL, true).isEmpty()) {
                    try {
                        event.getGuild().createRole().setName(GuildSettings.ROLE_JAIL)
                                .setColor(Color.BLACK).setPermissions(GuildSettings.ROLE_JAIL_PERM).complete();

                        for(TextChannel textChannel : event.getGuild().getTextChannels()) {
                            textChannel.getManager().putRolePermissionOverride(
                                    event.getGuild().getRolesByName(GuildSettings.ROLE_JAIL, true).get(0).getIdLong(),
                                    GuildSettings.ROLE_JAIL_PERM_TEXT_ALLOWED, GuildSettings.ROLE_JAIL_PERM_TEXT_DENY).queue();
                        }

                        for(VoiceChannel voiceChannel : event.getGuild().getVoiceChannels()) {
                            voiceChannel.getManager().putRolePermissionOverride(
                                    event.getGuild().getRolesByName(GuildSettings.ROLE_JAIL, true).get(0).getIdLong(),
                                    GuildSettings.ROLE_JAIL_PERM_VOICE_ALLOWED, GuildSettings.ROLE_JAIL_PERM_VOICE_DENY).queue();
                        }
                    } catch (HierarchyException | InsufficientPermissionException e) {
                        logger.logDiscordPermission(event.getGuild(), event.getMember(), event.getTextChannel(), e.getPermission());
                    }
                }
            } else {
                event.reply("Time Out command is now disabled").setEphemeral(true).queue();


                if (event.getGuild().getRolesByName(GuildSettings.ROLE_JAIL, true).size() > 0) {
                    try {
                        for(Role role : event.getGuild().getRolesByName(GuildSettings.ROLE_JAIL, true)) {
                            role.delete().queue();
                        }
                    } catch (HierarchyException | InsufficientPermissionException e) {
                        logger.logDiscordPermission(event.getGuild(), event.getMember(), event.getTextChannel(), e.getPermission());
                    }
                }
            }
        } else {
            event.reply("You don't have permission to manage roles").setEphemeral(true).queue();
        }
    }

    private void leagueClicked(@NotNull final ButtonClickEvent event) {
        assert event.getMember() != null;
        assert event.getGuild() != null;
        assert guildSettings != null;

        if(event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            guildSettings.setEnableLeague(!guildSettings.isLeagueEnable());

            if(guildSettings.isLeagueEnable()) {
                event.reply("League commands are now enabled").setEphemeral(true).queue();
            } else {
                event.reply("League commands are now disabled").setEphemeral(true).queue();
            }
        } else {
            logger.logDiscordPermission(event.getGuild(), event.getMember(), event.getTextChannel(), Permission.ADMINISTRATOR);

            event.reply("You don't have permission to enable league commands").setEphemeral(true).queue();
        }
    }

    private void w2pClicked(@NotNull final ButtonClickEvent event) {
        assert event.getMember() != null;
        assert event.getGuild() != null;
        assert guildSettings != null;

        if(event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            guildSettings.setEnableW2P(!guildSettings.isW2PEnable());

            if(guildSettings.isW2PEnable()) {
                event.reply("W2P command is now enabled").setEphemeral(true).queue();
            } else {
                event.reply("W2P command is now disabled").setEphemeral(true).queue();
            }
        } else {
            logger.logDiscordPermission(event.getGuild(), event.getMember(), event.getTextChannel(), Permission.ADMINISTRATOR);

            event.reply("You don't have permission to enable w2p command").setEphemeral(true).queue();
        }
    }

    private void pinMeClicked(@NotNull final ButtonClickEvent event) {
        assert event.getMember() != null;
        assert event.getGuild() != null;

        if(event.getMember().hasPermission(Permission.MESSAGE_MANAGE)) {
            if (event.getMessage().isPinned()) {
                event.getMessage().unpin().queue();
                event.reply("Message unpinned").setEphemeral(true).queue();
            } else {
                event.getMessage().pin().queue();
                event.reply("Message pinned").setEphemeral(true).queue();
            }
        } else {
            logger.logDiscordPermission(event.getGuild(), event.getMember(), event.getTextChannel(), Permission.MESSAGE_MANAGE);

            event.reply("You don't have permission to manage messages").setEphemeral(true).queue();
        }
    }
}
