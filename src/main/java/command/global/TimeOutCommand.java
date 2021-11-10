package command.global;

import bot.Bot;
import bot.GuildSettings;
import command.CommandEnum;
import command.AbstractSlashCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.interactions.Interaction;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TimeOutCommand extends AbstractSlashCommand {
    public TimeOutCommand() {
        super(CommandEnum.TIME_OUT,
                new OptionData(OptionType.USER, "member", "Member you want to kick temporarily", true),
                new OptionData(OptionType.INTEGER, "time", "Time in minutes", false)
        );
    }

    @Override
    public final void execute(@NotNull final Interaction interaction, final @NotNull Bot bot, final @NotNull List<OptionMapping> options) {
        assert interaction.getGuild()  != null;
        assert interaction.getMember() != null;
        assert !options.isEmpty();

        final Member kickedMember = options.get(0).getAsMember();
        final Guild guild = interaction.getGuild();

        assert kickedMember != null;

        if (!bot.getGuildSetting(guild).isJailEnable()) {
            interaction.reply("Jail is disabled").setEphemeral(true).queue();
            return;
        }

        if (guild.getRolesByName(GuildSettings.ROLE_JAIL, true).isEmpty()) {
            interaction.reply("Role '" + GuildSettings.ROLE_JAIL +
                    "' must be created. If you want me to create it, type '/settings' to enable/disable"
                    + CommandEnum.TIME_OUT.getName() + " command").setEphemeral(true).queue();
            return;
        }

        if (kickedMember.getUser().isBot()) {
            interaction.reply("You can't kick a bot").setEphemeral(true).queue();
            return;
        }

        if (interaction.getMember().equals(kickedMember)) {
            interaction.reply("You can't kick yourself").setEphemeral(true).queue();
            return;
        }

        if (kickedMember.hasPermission(Permission.ADMINISTRATOR)) {
            interaction.reply("You can't kick an admin").setEphemeral(true).queue();
            return;
        }

        if (!interaction.getMember().hasPermission(interaction.getTextChannel(), Permission.MANAGE_ROLES)) {
            logger.logDiscordMemberPermission(interaction, Permission.MANAGE_ROLES);
            interaction.reply("You don't have permission to time out " + kickedMember.getEffectiveName()).setEphemeral(true).queue();
            return;
        }

        assert kickedMember.getVoiceState() != null;

        final List<Role> listRole = kickedMember.getRoles();

        if (listRole.contains(guild.getRolesByName(GuildSettings.ROLE_JAIL, true).get(0))) {
            interaction.reply(kickedMember.getEffectiveName() + " is already kicked").setEphemeral(true).queue();
            return;
        }

        long time = GuildSettings.JAIL_DEFAULT_TIME;
        try {
            if (options.size() == 2) {
                time = options.get(1).getAsLong();

                if(time > 120 || time < 2) {
                    throw new NumberFormatException();
                }
            }
        } catch (IllegalStateException | NumberFormatException e) {
            interaction.reply("Time must be a number between 1 and 120").setEphemeral(true).queue();
            return;
        }

        try {
            if (kickedMember.getVoiceState().inVoiceChannel()) {
                guild.kickVoiceMember(kickedMember).queue();
            }

            if (!listRole.isEmpty()) {
                for (Role role : listRole) {
                    guild.removeRoleFromMember(kickedMember, role).queue();
                }
            }

            guild.addRoleToMember(kickedMember, guild.getRolesByName(GuildSettings.ROLE_JAIL, true).get(0)).queue();

            interaction.reply("User " + kickedMember.getEffectiveName() + " kicked in jail for " + time + " minutes!").queue();

            final TimerTask unKickTask = new TimerTask() {
                @Override
                public void run() {
                    guild.removeRoleFromMember(kickedMember, guild.getRolesByName(GuildSettings.ROLE_JAIL, true).get(0)).queue();

                    if (!listRole.isEmpty()) {
                        for (Role role : listRole) {
                            guild.addRoleToMember(kickedMember, role).queue();
                        }
                    }

                    interaction.getTextChannel().sendMessage("Welcome back " + kickedMember.getEffectiveName()).queue();
                }
            };
            final Timer timer = new Timer();
            timer.schedule(unKickTask, time * 60000);
        } catch (InsufficientPermissionException e) {
            logger.logDiscordBotPermission(guild, e);
            if (e.getPermission() != Permission.MESSAGE_WRITE) {
                interaction.reply("I don't have permission to manage roles, I can't kick properly").setEphemeral(true).queue();
            }
            interaction.reply("I don't have permission to send messages, you won't see when '" + kickedMember.getEffectiveName() + "' will be unkick").setEphemeral(true).queue();
        } catch (HierarchyException e) {
            logger.logDiscordHierarchyPermission(guild, e);
            interaction.reply("My role is lower than one of the target, I can't remove it").setEphemeral(true).queue();
        }
    }
}
