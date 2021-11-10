package command.game;

import bot.Bot;
import bot.GuildSettings;
import button.ButtonEnum;
import command.CommandEnum;
import command.AbstractSlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.interactions.Interaction;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.components.Button;
import org.jetbrains.annotations.NotNull;
import bot.EmojiEnum;

import java.util.List;

public class FlipCoinCommand extends AbstractSlashCommand {
    public FlipCoinCommand() {
        super(CommandEnum.FLIP_A_COIN);
    }

    @Override
    public final void execute(@NotNull final Interaction interaction, final Bot bot, final List<OptionMapping> options) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(GuildSettings.DEFAULT_COLOR);
        embedBuilder.setTitle("Heads or tails?");
        embedBuilder.setDescription("Your choice " + EmojiEnum.ARROW_DOWN.getTag());

        interaction.replyEmbeds(embedBuilder.build()).addActionRow(
                        Button.primary(ButtonEnum.BT_HEADS.getId(), ButtonEnum.BT_HEADS.getLabel()).withEmoji(Emoji.fromMarkdown(EmojiEnum.SWORD.getTag())),
                        Button.primary(ButtonEnum.BT_TAILS.getId(), ButtonEnum.BT_TAILS.getLabel()).withEmoji(Emoji.fromMarkdown(EmojiEnum.SHIELD.getTag())))
                .queue();

        embedBuilder.clear();
    }

    public final boolean getResult() {
        return (int) (Math.random() * 2) == 0;
    }
}
