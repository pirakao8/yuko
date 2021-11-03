package command.bot;

import bot.Bot;
import bot.listener.ButtonClickListener;
import bot.setting.EmojiList;
import bot.setting.GuildSettings;
import command.AbstractSlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.components.Button;
import org.jetbrains.annotations.NotNull;

public class SettingsCommand extends AbstractSlashCommand {
    @Override
    public final void execute(@NotNull final SlashCommandEvent event, final Bot bot) {
        super.execute(event, bot);

        assert event.getGuild() != null;

        final EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(GuildSettings.DEFAULT_COLOR);
        embedBuilder.setTitle("Yuko's configuration");
        embedBuilder.appendDescription("You can enable/disable some of my settings by clicking on the buttons " + EmojiList.ARROW_DOWN.getTag() +
                "\nTo enable '" + GuildSettings.ROLE_WELCOMER + "', add some rules first with '/rules' command.");

        event.replyEmbeds(embedBuilder.build()).addActionRow(
                Button.primary(ButtonClickListener.BT_LABEL_LEAGUE, ButtonClickListener.BT_LABEL_LEAGUE)
                        .withEmoji(Emoji.fromMarkdown(EmojiList.WITCHER.getTag())).withDisabled(!bot.isApiLeagueEnable()),
                Button.primary(ButtonClickListener.BT_LABEL_MUSIC, ButtonClickListener.BT_LABEL_MUSIC)
                        .withEmoji(Emoji.fromMarkdown(EmojiList.MUSIC.getTag())),
                Button.primary(ButtonClickListener.BT_LABEL_JAIL, ButtonClickListener.BT_LABEL_JAIL)
                        .withEmoji(Emoji.fromMarkdown(EmojiList.JAIL.getTag())),
                Button.primary(ButtonClickListener.BT_LABEL_W2P, ButtonClickListener.BT_LABEL_W2P)
                        .withEmoji(Emoji.fromMarkdown(EmojiList.CONTROLLER.getTag())),
                Button.primary(ButtonClickListener.BT_LABEL_WELCOME, ButtonClickListener.BT_LABEL_WELCOME)
                        .withEmoji(Emoji.fromMarkdown(EmojiList.WELCOME.getTag())).withDisabled(bot.getGuildSetting(event.getGuild()).isRuleEmpty()))
                .queue();

        embedBuilder.clear();
    }
}
