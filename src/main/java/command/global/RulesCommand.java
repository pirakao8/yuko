package command.global;

import bot.Bot;
import bot.GuildSettings;
import command.CommandEnum;
import command.AbstractSlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.Interaction;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RulesCommand extends AbstractSlashCommand {
    public RulesCommand() {
        super(CommandEnum.RULES,
                new OptionData(OptionType.STRING, "rule", "Rule you want to add to this server", false)
        );
    }

    @Override
    public void execute(@NotNull final Interaction interaction, final @NotNull Bot bot, final @NotNull List<OptionMapping> options) {
        assert interaction.getMember() != null;
        assert interaction.getGuild() != null;

        final GuildSettings guildSettings = bot.getGuildSetting(interaction.getGuild());

        if (options.isEmpty()) {
            if (guildSettings.isRuleEmpty()) {
                interaction.reply("No rules for this guild").setEphemeral(true).queue();
                return;
            }

            final EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(GuildSettings.DEFAULT_COLOR);
            embedBuilder.setTitle(interaction.getGuild().getName() + "'s rules:");
            embedBuilder.appendDescription(guildSettings.getRules());

            interaction.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();
            embedBuilder.clear();
        }

        if (options.size() == 1) {
            if (!interaction.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                logger.logDiscordMemberPermission(interaction, Permission.ADMINISTRATOR);
                interaction.reply("You don't have permission to add rules").setEphemeral(true).queue();
                return;
            }

            final String rule = options.get(0).getAsString();
            guildSettings.addRule("- " + rule);
            interaction.reply("Rule: '**" + rule + "**' added").setEphemeral(true).queue();
        }
    }
}
