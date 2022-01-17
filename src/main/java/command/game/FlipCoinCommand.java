package command.game;

import bot.GuildSettings;
import button.game.HeadsButton;
import button.game.TailsButton;
import command.Command;
import command.CommandCategoryEnum;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FlipCoinCommand implements Command {
    @Contract(pure = true)
    @Override
    public final @NotNull String getName() {
        return "flipacoin";
    }

    @Contract(pure = true)
    @Override
    public final @NotNull String getDescription() {
        return "Flip a coin. Basic. Simple";
    }

    @Override
    public final CommandCategoryEnum getCategory() {
        return CommandCategoryEnum.GAMES;
    }

    @Contract(pure = true)
    @Override
    public final OptionData @Nullable [] getOptions() {
        return null;
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
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(GuildSettings.DEFAULT_COLOR);
        embedBuilder.setTitle("Heads or tails?");

        event.replyEmbeds(embedBuilder.build()).addActionRow(
                        new HeadsButton().getComponent(),
                        new TailsButton().getComponent())
                .queue();

        embedBuilder.clear();
    }
}