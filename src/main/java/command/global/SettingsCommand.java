package command.global;

import bot.Bot;
import bot.GuildSettings;
import button.ButtonEnum;
import command.CommandEnum;
import command.AbstractSlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.interactions.Interaction;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.components.Button;
import org.jetbrains.annotations.NotNull;
import bot.EmojiEnum;

import java.util.List;

public class SettingsCommand extends AbstractSlashCommand {
    public SettingsCommand() {
        super(CommandEnum.SETTINGS);
    }

    @Override
    public final void execute(@NotNull final Interaction interaction, final Bot bot, final List<OptionMapping> options) {
        assert interaction.getMember() != null;

        if (!interaction.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            logger.logDiscordMemberPermission(interaction, Permission.ADMINISTRATOR);
            interaction.reply("You don't have permission to manage settings").setEphemeral(true).queue();
            return;
        }

        final EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(GuildSettings.DEFAULT_COLOR);
        embedBuilder.setTitle("Yuko's configuration");
        embedBuilder.appendDescription("You can enable/disable some of my settings by clicking on the buttons " + EmojiEnum.ARROW_DOWN.getTag() +
                "\nTo enable '" + GuildSettings.ROLE_WELCOMER + "', add some rules first with '/rules' command.");

        interaction.replyEmbeds(embedBuilder.build()).addActionRow(
                        Button.primary(ButtonEnum.BT_LEAGUE.getId(), ButtonEnum.BT_LEAGUE.getLabel())
                                .withEmoji(Emoji.fromMarkdown(EmojiEnum.WITCHER.getTag())).withDisabled(!bot.isApiLeagueEnable()),
                        Button.primary(ButtonEnum.BT_MUSIC.getId(), ButtonEnum.BT_MUSIC.getLabel()).withEmoji(Emoji.fromMarkdown(EmojiEnum.MUSIC.getTag())),
                        Button.primary(ButtonEnum.BT_JAIL.getId(), ButtonEnum.BT_JAIL.getLabel()).withEmoji(Emoji.fromMarkdown(EmojiEnum.JAIL.getTag())),
                        Button.primary(ButtonEnum.BT_W2P.getId(), ButtonEnum.BT_W2P.getLabel()).withEmoji(Emoji.fromMarkdown(EmojiEnum.CONTROLLER.getTag())),
                        Button.primary(ButtonEnum.BT_WELCOME.getId(), ButtonEnum.BT_WELCOME.getLabel())
                                .withEmoji(Emoji.fromMarkdown(EmojiEnum.WELCOME.getTag())).withDisabled(bot.getGuildSetting(interaction.getGuild()).isRuleEmpty()))
                .queue();
        embedBuilder.clear();
    }
}
