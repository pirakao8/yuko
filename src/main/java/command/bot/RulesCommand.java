package command.bot;

import bot.Bot;
import bot.setting.GuildSettings;
import command.AbstractSlashCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.jetbrains.annotations.NotNull;

public class RulesCommand extends AbstractSlashCommand {
    @Override
    public void execute(@NotNull final SlashCommandEvent event, final Bot bot) {
        super.execute(event, bot);

        final GuildSettings guildSettings = bot.getGuildSetting(event.getGuild());

        if (event.getOptions().size() == 1) {
            final String rule = event.getOptions().get(0).getAsString();
            guildSettings.addRule("- " + rule);

            event.reply("Rule: '" + rule + "' added").setEphemeral(true).queue();
        } else {
            if (!guildSettings.isRuleEmpty()) {
                event.reply(guildSettings.getRules()).setEphemeral(true).queue();
            } else {
                event.reply("No rules for this server").setEphemeral(true).queue();
            }
        }
    }
}
