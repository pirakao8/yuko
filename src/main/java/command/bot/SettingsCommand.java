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

import java.util.ArrayList;

public class SettingsCommand extends AbstractSlashCommand {
    @Override
    public final void execute(@NotNull final SlashCommandEvent event, final Bot bot) {
        super.execute(event, bot);

        assert event.getGuild() != null;

        final ArrayList<Button> buttons = new ArrayList<>();
        if (bot.isApiLeagueEnable()) {
            buttons.add(Button.primary(ButtonClickListener.LEAGUE_BT_LABEL, ButtonClickListener.LEAGUE_BT_LABEL).withEmoji(Emoji.fromMarkdown(EmojiList.WITCHER.getTag())));
        }
        buttons.add(Button.primary(ButtonClickListener.MUSIC_BT_LABEL, ButtonClickListener.MUSIC_BT_LABEL).withEmoji(Emoji.fromMarkdown(EmojiList.MUSIC.getTag())));
        buttons.add(Button.primary(ButtonClickListener.JAIL_BT_LABEL, ButtonClickListener.JAIL_BT_LABEL).withEmoji(Emoji.fromMarkdown(EmojiList.JAIL.getTag())));
        buttons.add(Button.primary(ButtonClickListener.W2P_BT_LABEL, ButtonClickListener.W2P_BT_LABEL).withEmoji(Emoji.fromMarkdown(EmojiList.CONTROLLER.getTag())));
        buttons.add(Button.primary(ButtonClickListener.WELCOME_BT_LABEL, ButtonClickListener.WELCOME_BT_LABEL).withEmoji(Emoji.fromMarkdown(EmojiList.WELCOME.getTag())));

        final EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(GuildSettings.DEFAULT_COLOR);
        embedBuilder.setTitle("Yuko's configuration");
        embedBuilder.appendDescription("You can enable/disable some of my settings by clicking on the buttons " + EmojiList.ARROW_DOWN.getTag() +
                "\nTo enable '" + GuildSettings.ROLE_WELCOMER + "', add some rules first with '/rules' command.");

        event.replyEmbeds(embedBuilder.build()).addActionRow(buttons).queue();

        embedBuilder.clear();
    }
}
