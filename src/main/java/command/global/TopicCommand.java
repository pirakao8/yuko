package command.global;

import bot.GuildSettings;
import command.Command;
import command.CommandCategoryEnum;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.TimeUnit;

public class TopicCommand implements Command {
    @Contract(pure = true)
    @Override
    public final @NotNull String getName() {
        return "topic";
    }

    @Contract(pure = true)
    @Override
    public final @NotNull String getDescription() {
        return "Get the topic for the current channel";
    }

    @Override
    public final CommandCategoryEnum getCategory() {
        return CommandCategoryEnum.GLOBAL;
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
        if (event.getTextChannel().getTopic() == null) {
            event.reply("No topic for this channel").setEphemeral(true).queue();
            return;
        }

        final EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(GuildSettings.DEFAULT_COLOR);
        embedBuilder.setTitle("#" + event.getTextChannel().getName() + "'s topic");
        embedBuilder.setDescription(event.getTextChannel().getTopic());

        event.replyEmbeds(embedBuilder.build()).queue(m -> m.deleteOriginal().submitAfter(5, TimeUnit.MINUTES));
        embedBuilder.clear();
    }
}
