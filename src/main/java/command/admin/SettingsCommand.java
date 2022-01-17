package command.admin;

import bot.Bot;
import bot.GuildSettings;
import button.admin.LeagueButton;
import button.admin.MusicButton;
import button.admin.W2PButton;
import button.admin.WelcomerButton;
import command.Command;
import command.CommandCategoryEnum;
import dataBase.EmojiEnum;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.TimeUnit;

public class SettingsCommand implements Command {
    @Contract(pure = true)
    @Override
    public final @NotNull String getName() {
        return "settings";
    }

    @Contract(pure = true)
    @Override
    public final @NotNull String getDescription() {
        return "Enable/Disable what I can do";
    }

    @Override
    public final CommandCategoryEnum getCategory() {
        return CommandCategoryEnum.ADMIN;
    }

    @Contract(pure = true)
    @Override
    public final OptionData @Nullable [] getOptions() {
        return null;
    }

    @Override
    public final Permission getPermission() {
        return Permission.ADMINISTRATOR;
    }

    @Override
    public final boolean isEnable() {
        return true;
    }

    @Override
    public final void execute(@NotNull final SlashCommandEvent event) {
        assert event.getMember() != null;

        final GuildSettings guildSettings = GuildSettings.getInstance(event.getGuild());

        final EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(GuildSettings.DEFAULT_COLOR);
        embedBuilder.setTitle("Settings");
        embedBuilder.appendDescription("You can enable/disable some of my settings by clicking on the buttons " + EmojiEnum.ARROW_DOWN.getTag() +
                "\nTo enable **" + GuildSettings.ROLE_WELCOMER + "**, add some rules first with **/rules** command.");

        event.replyEmbeds(embedBuilder.build()).addActionRow(
                new LeagueButton().getComponent(!Bot.isApiLeagueEnable()),
                new W2PButton().getComponent(),
                new WelcomerButton().getComponent(guildSettings.isRuleEmpty()),
                new MusicButton().getComponent())
                .queue(m -> m.deleteOriginal().submitAfter(5, TimeUnit.MINUTES));

        embedBuilder.clear();
    }
}
