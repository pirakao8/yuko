package command.bot;

import bot.Bot;
import bot.listener.OnButtonClickListener;
import bot.setting.EmojiList;
import bot.setting.GuildSettings;
import command.AbstractSlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.components.Button;

import java.util.ArrayList;

public class SettingsCommand extends AbstractSlashCommand {
    @Override
    public final void execute(final SlashCommandEvent event, final Bot bot) {
        super.execute(event, bot);

        assert event.getGuild() != null;

        final ArrayList<Button> buttons = new ArrayList<>();
        if(bot.isApiLeagueEnable()) {
            buttons.add(Button.primary(OnButtonClickListener.LEAGUE_BT_LABEL, OnButtonClickListener.LEAGUE_BT_LABEL).withEmoji(Emoji.fromMarkdown(EmojiList.WITCHER.getTag())));
        }
        buttons.add(Button.primary(OnButtonClickListener.MUSIC_BT_LABEL, OnButtonClickListener.MUSIC_BT_LABEL).withEmoji(Emoji.fromMarkdown(EmojiList.MUSIC.getTag())));
        buttons.add(Button.primary(OnButtonClickListener.JAIL_BT_LABEL, OnButtonClickListener.JAIL_BT_LABEL).withEmoji(Emoji.fromMarkdown(EmojiList.JAIL.getTag())));
        buttons.add(Button.primary(OnButtonClickListener.W2P_BT_LABEL, OnButtonClickListener.W2P_BT_LABEL).withEmoji(Emoji.fromMarkdown(EmojiList.CONTROLLER.getTag())));

        final EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(GuildSettings.DEFAULT_COLOR);
        embedBuilder.setTitle("Yuko's configuration");
        embedBuilder.appendDescription("You can enable/disable some of my settings by clicking on the buttons just above this");

        event.replyEmbeds(embedBuilder.build()).addActionRow(buttons).queue();

        embedBuilder.clear();
    }
}
