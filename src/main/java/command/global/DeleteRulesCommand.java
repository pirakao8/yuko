package command.global;

import bot.Bot;
import bot.GuildSettings;
import command.CommandEnum;
import command.AbstractSlashCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.Interaction;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DeleteRulesCommand extends AbstractSlashCommand {
    public DeleteRulesCommand() {
        super(CommandEnum.DELETE_RULES);
    }

    @Override
    public void execute(@NotNull final Interaction interaction, final Bot bot, final List<OptionMapping> options) {
        assert interaction.getMember() != null;

        if (!interaction.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            logger.logDiscordMemberPermission(interaction, Permission.ADMINISTRATOR);
            interaction.reply("You don't have permission to delete rules").setEphemeral(true).queue();
            return;
        }

        final GuildSettings guildSettings = bot.getGuildSetting(interaction.getGuild());

        if (guildSettings.isRuleEmpty()) {
            interaction.reply("No rules for this guild").setEphemeral(true).queue();
            return;
        }

        guildSettings.clearRules();
        interaction.reply("All rules deleted").setEphemeral(true).queue();
    }
}
