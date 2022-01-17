package command.admin;

import bot.Bot;
import bot.GuildSettings;
import command.Command;
import command.CommandCategoryEnum;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import util.Logger;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class RulesCommand implements Command {
    private static final Logger logger = Logger.getLogger();

    @Contract(pure = true)
    @Override
    public final @NotNull String getName() {
        return "rules";
    }

    @Contract(pure = true)
    @Override
    public final @NotNull String getDescription() {
        return "Add or read rules for this server";
    }

    @Override
    public final CommandCategoryEnum getCategory() {
        return CommandCategoryEnum.ADMIN;
    }

    @Contract(pure = true)
    @Override
    public final OptionData @Nullable [] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.STRING, "rule", "Rule you want to add to this guild", false)
        };
    }

    @Contract(pure = true)
    @Override
    public final @Nullable Permission getPermission() {
        return null;
    }

    @Override
    public final boolean isEnable() {
        return true;
    }

    @Override
    public final void execute(@NotNull final SlashCommandEvent event) {
        assert event.getMember() != null;
        assert event.getGuild() != null;

        final GuildSettings guildSettings = GuildSettings.getInstance(event.getGuild());
        final List<OptionMapping> options = event.getOptions();

        if (options.isEmpty()) {
            if (guildSettings.isRuleEmpty()) {
                event.reply("No rules for this guild").setEphemeral(true).queue();
                return;
            }

            final EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(GuildSettings.DEFAULT_COLOR);
            embedBuilder.setTitle(event.getGuild().getName() + "'s rules");
            embedBuilder.appendDescription(GuildSettings.getInstance(event.getGuild()).getRules());

            event.replyEmbeds(embedBuilder.build())
                    .queue(m -> m.deleteOriginal().submitAfter(5, TimeUnit.MINUTES));
            embedBuilder.clear();
            return;
        }

        if (options.size() == 1) {
            if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                logger.logDiscordMemberPermission(event, Permission.ADMINISTRATOR);
                event.reply("You don't have the permission to use this command").setEphemeral(true).queue();
                return;
            }

            final String rule = options.get(0).getAsString();
            guildSettings.addRule("- " + rule);
            event.reply("Rule: '**" + rule + "**' added").setEphemeral(true).queue();
        }
    }
}
