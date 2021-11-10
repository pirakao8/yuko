package command.music;

import bot.Bot;
import button.ButtonEnum;
import command.CommandEnum;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.interactions.Interaction;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.components.Button;
import org.jetbrains.annotations.NotNull;
import bot.EmojiEnum;

import java.util.List;

public class PlayerCommand extends AbstractMusicCommand {
    public PlayerCommand() {
        super(CommandEnum.PLAYER);
    }

    @Override
    public void execute(@NotNull final Interaction interaction, final @NotNull Bot bot, final List<OptionMapping> options) {
        if (!isPlayable(interaction, bot)) {
            return;
        }

        interaction.reply("Music player").addActionRow(
                        Button.primary(ButtonEnum.BT_PAUSE_RESUME.getId(), ButtonEnum.BT_PAUSE_RESUME.getLabel()).withEmoji(Emoji.fromMarkdown(EmojiEnum.PLAY_PAUSE.getTag())),
                        Button.primary(ButtonEnum.BT_STOP.getId(), ButtonEnum.BT_STOP.getLabel()).withEmoji(Emoji.fromMarkdown(EmojiEnum.STOP.getTag())),
                        Button.primary(ButtonEnum.BT_SKIP.getId(), ButtonEnum.BT_SKIP.getLabel()).withEmoji(Emoji.fromMarkdown(EmojiEnum.SKIP.getTag())),
                        Button.primary(ButtonEnum.BT_SHUFFLE.getId(), ButtonEnum.BT_SHUFFLE.getLabel()).withEmoji(Emoji.fromMarkdown(EmojiEnum.SHUFFLE.getTag())),
                        Button.primary(ButtonEnum.BT_CLEAR.getId(), ButtonEnum.BT_CLEAR.getLabel()).withEmoji(Emoji.fromMarkdown(EmojiEnum.CLEAR.getTag())))
                .queue();
    }
}
