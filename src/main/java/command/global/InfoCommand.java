package command.global;

import bot.Bot;
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

public class InfoCommand implements Command {
    @Contract(pure = true)
    @Override
    public final @NotNull String getName() {
        return "info";
    }

    @Contract(pure = true)
    @Override
    public final @NotNull String getDescription() {
        return "Get some info about Yuko's life";
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
        final EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(GuildSettings.DEFAULT_COLOR);

        embedBuilder.setTitle("Yuko's info", "https://github.com/pirakao8/yuko");

        embedBuilder.addField("Version", Bot.getVersion(), true);
        embedBuilder.addField("Contact", "pirakao8#1272", true);

        embedBuilder.addField("GitHub", "https://github.com/pirakao8/yuko", false);
        embedBuilder.addField("Contributors", "Flo#2478, Chronoxios#5235, Sioohz#3741, Neilor#8309, pirakao8#1272", false);
        embedBuilder.addField("Thank you", "Thank you for using Yuko, hope you enjoy it, if you have any questions our trouble, please contact us", false);

        event.replyEmbeds(embedBuilder.build()).queue(m -> m.deleteOriginal().submitAfter(5, TimeUnit.MINUTES));
        embedBuilder.clear();
    }
}
