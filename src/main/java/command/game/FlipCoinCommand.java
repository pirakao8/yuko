package command.game;

import bot.listener.ButtonClickListener;
import bot.setting.EmojiList;
import bot.setting.GuildSettings;
import command.AbstractSlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.components.Button;
import org.jetbrains.annotations.NotNull;

public class FlipCoinCommand extends AbstractSlashCommand {
    @Override
    public final void execute(@NotNull final SlashCommandEvent event) {
        super.execute(event);

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(GuildSettings.DEFAULT_COLOR);
        embedBuilder.setTitle("Heads or tails?");

        event.replyEmbeds(embedBuilder.build()).addActionRow(
                        Button.primary(ButtonClickListener.BT_LABEL_HEADS, ButtonClickListener.BT_LABEL_HEADS).withEmoji(Emoji.fromMarkdown(EmojiList.SWORD.getTag())),
                        Button.primary(ButtonClickListener.BT_LABEL_TAILS, ButtonClickListener.BT_LABEL_TAILS).withEmoji(Emoji.fromMarkdown(EmojiList.SHIELD.getTag())))
                .queue();

        embedBuilder.clear();
    }

    public final boolean getResult() {
        return (int) (Math.random() * 2) == 0;
    }
}
