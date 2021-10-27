package command.bot;

import bot.Bot;
import bot.setting.GuildSettings;
import command.AbstractSlashCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TimeOutCommand extends AbstractSlashCommand {
    @Override
    public final void execute(final SlashCommandEvent event, @NotNull final Bot bot) {
        super.execute(event);

        assert event.getGuild()  != null;
        assert event.getMember() != null;
        assert event.getOptions().get(0) != null;

        final Member kickedMember = event.getOptions().get(0).getAsMember();

        assert kickedMember != null;

        if(!bot.getGuildSetting(event.getGuild()).isJailEnable()) {
            event.reply("Jail is disabled").setEphemeral(true).queue();
            return;
        }

        if(event.getGuild().getRolesByName(GuildSettings.ROLE_JAIL, true).isEmpty()) {
            event.reply("Role '" + GuildSettings.ROLE_JAIL +
                    "' must be created. If you want me to create it, type '/settings' to enable/disable TimeOut command").setEphemeral(true).queue();
            return;
        }

        if(kickedMember.getUser().isBot()) {
            event.reply("You can't time out a bot").setEphemeral(true).queue();
            return;
        }

        if(event.getMember().equals(kickedMember)) {
            event.reply("You can't time out yourself").setEphemeral(true).queue();
            return;
        }

        if(!event.getMember().hasPermission(event.getTextChannel(), Permission.MANAGE_ROLES) &&
                !kickedMember.hasPermission(event.getTextChannel(), Permission.ADMINISTRATOR)) {
            event.reply("You don't have permission to time out " +
                    kickedMember.getEffectiveName()).setEphemeral(true).queue();
            return;
        }

        long time = GuildSettings.JAIL_DEFAULT_TIME;
        try {
            if (event.getOptions().size() == 2) {
                time = event.getOptions().get(1).getAsLong();

                if(time > 120 || time < 2) {
                    throw new NumberFormatException();
                }
            }
        } catch (IllegalStateException | NumberFormatException e){
            event.reply("Time must be a number between 1 and 120").setEphemeral(true).queue();
            return;
        }

        try {
            assert kickedMember.getVoiceState() != null;

            final List<Role> listRole = kickedMember.getRoles();

            if(listRole.contains(event.getGuild().getRolesByName(GuildSettings.ROLE_JAIL, true).get(0))) {
                event.reply(kickedMember.getEffectiveName() + " is already kicked temporarily").setEphemeral(true).queue();
                return;
            }

            if(!listRole.isEmpty()) {
                for (Role role : listRole) {
                    event.getGuild().removeRoleFromMember(kickedMember, role).queue();
                }
            }

            if(kickedMember.getVoiceState().inVoiceChannel()) {
                event.getGuild().mute(kickedMember, true).queue();
            }

            event.getGuild().addRoleToMember(kickedMember, event.getGuild().getRolesByName(GuildSettings.ROLE_JAIL, true).get(0)).queue();

            event.reply("User " + kickedMember.getEffectiveName() + " kicked in jail for " + time + " minutes!").queue();

            final TimerTask unKickTask = new TimerTask() {
                @Override
                public void run() {
                    event.getGuild().removeRoleFromMember(kickedMember, event.getGuild().getRolesByName(GuildSettings.ROLE_JAIL, true).get(0)).queue();

                    if(!listRole.isEmpty()) {
                        for (Role role : listRole) {
                            event.getGuild().addRoleToMember(kickedMember, role).queue();
                        }
                    }

                    if(kickedMember.getVoiceState().inVoiceChannel()) {
                        event.getGuild().mute(kickedMember, false).queue();
                    }
                    event.getTextChannel().sendMessage("Welcome back " + kickedMember.getEffectiveName()).queue();
                }
            };
            final Timer timer = new Timer();
            timer.schedule(unKickTask, time * 60000);
        } catch (HierarchyException | InsufficientPermissionException e) {
            logger.logDiscordPermission(event.getGuild(), event.getMember(), event.getTextChannel(), e.getPermission());

            event.reply("I don't have enough permission to manage roles and kick " + kickedMember.getEffectiveName()).setEphemeral(true).queue();
        }
    }
}
