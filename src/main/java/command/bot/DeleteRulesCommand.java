package command.bot;

import bot.Bot;
import bot.setting.GuildSettings;
import command.AbstractSlashCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.jetbrains.annotations.NotNull;

public class DeleteRulesCommand extends AbstractSlashCommand {
    @Override
    public void execute(@NotNull SlashCommandEvent event, Bot bot) {
        super.execute(event, bot);

        final GuildSettings guildSettings = bot.getGuildSetting(event.getGuild());

        if (guildSettings.isRuleEmpty()) {
            event.reply("No rules for this guild").setEphemeral(true).queue();
            return;
        }

        guildSettings.clearRules();
        event.reply("All rules deleted").setEphemeral(true).queue();
    }
}
