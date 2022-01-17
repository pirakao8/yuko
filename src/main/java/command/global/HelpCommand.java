package command.global;

import dataBase.EmojiEnum;
import bot.GuildSettings;
import command.Command;
import command.CommandCategoryEnum;
import dataBase.CommandDataBase;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class HelpCommand implements Command {
    @Contract(pure = true)
    @Override
    public final @NotNull String getName() {
        return "help";
    }

    @Contract(pure = true)
    @Override
    public final @NotNull String getDescription() {
        return "Check all the commands available";
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
        final ArrayList<Command> commandArrayList = CommandDataBase.getCommands();
        final StringBuilder adminCommandsBuilder  = new StringBuilder();
        final StringBuilder gameCommandsBuilder   = new StringBuilder();
        final StringBuilder globalCommandsBuilder = new StringBuilder();
        final StringBuilder musicCommandsBuilder  = new StringBuilder();
        final StringBuilder leagueCommandsBuilder = new StringBuilder();

        final EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(GuildSettings.DEFAULT_COLOR);

        embedBuilder.setTitle("All commands available");

        for (Command command : commandArrayList) {
            if (command.isEnable()) {
                final StringBuilder commandBuilder = new StringBuilder();
                commandBuilder.append("/" + command.getName());
                if (command.getOptions() != null) {
                    for (OptionData optionData : command.getOptions()) {
                        commandBuilder.append(" *<" + optionData.getDescription() + ">*");
                    }
                }
                commandBuilder.append(" : " + command.getDescription() + "\n");
                switch (command.getCategory()) {
                    case ADMIN:
                        adminCommandsBuilder.append(commandBuilder);
                        break;

                    case GAMES:
                        gameCommandsBuilder.append(commandBuilder);
                        break;

                    case GLOBAL:
                        globalCommandsBuilder.append(commandBuilder);
                        break;

                    case LEAGUE:
                        leagueCommandsBuilder.append(commandBuilder);
                        break;

                    case MUSIC:
                        musicCommandsBuilder.append(commandBuilder);
                        break;

                    default:
                        break;
                }
            }
        }

        embedBuilder.addField(CommandCategoryEnum.ADMIN.getName(), adminCommandsBuilder.toString(), false);
        embedBuilder.addField(CommandCategoryEnum.GLOBAL.getName(), globalCommandsBuilder.toString(), false);
        embedBuilder.addField(CommandCategoryEnum.GAMES.getName() + " " + EmojiEnum.CONTROLLER.getTag(), gameCommandsBuilder.toString(), false);
        embedBuilder.addField(CommandCategoryEnum.MUSIC.getName() + " " + EmojiEnum.MUSIC.getTag(), musicCommandsBuilder.toString(), false);

        if (!leagueCommandsBuilder.toString().isEmpty()) {
            embedBuilder.addField(CommandCategoryEnum.LEAGUE.getName() + " " + EmojiEnum.WITCHER.getTag(), leagueCommandsBuilder.toString(), false);
        }

        event.replyEmbeds(embedBuilder.build()).queue(m -> m.deleteOriginal().submitAfter(5, TimeUnit.MINUTES));
        embedBuilder.clear();
    }
}
