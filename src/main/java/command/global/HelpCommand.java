package command.global;

import bot.Bot;
import bot.GuildSettings;
import command.CommandEnum;
import command.AbstractSlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.Interaction;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;
import bot.EmojiEnum;

import java.util.Collection;
import java.util.List;

public class HelpCommand extends AbstractSlashCommand {
    public HelpCommand() {
        super(CommandEnum.HELP);
    }

    @Override
    public final void execute(@NotNull final Interaction interaction, final @NotNull Bot bot, final List<OptionMapping> options) {
        final Collection<AbstractSlashCommand> slashCommands = bot.getSlashCommandMap().values();
        final StringBuilder gameCommandsBuilder   = new StringBuilder();
        final StringBuilder globalCommandsBuilder = new StringBuilder();
        final StringBuilder musicCommandsBuilder  = new StringBuilder();
        final StringBuilder leagueCommandsBuilder = new StringBuilder();

        final EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(GuildSettings.DEFAULT_COLOR);
        embedBuilder.setTitle("All commands available");

        for (AbstractSlashCommand slashCommand : slashCommands) {
            final StringBuilder commandBuilder = new StringBuilder();
            commandBuilder.append("/" + slashCommand.getName());
            if (!slashCommand.getOptions().isEmpty()) {
                for (OptionData optionData : slashCommand.getOptions()) {
                    commandBuilder.append(" *<" + optionData.getDescription() + ">*");
                }
            }
            commandBuilder.append(" : " + slashCommand.getDescription() + "\n");

            if (slashCommand.getCommandGroup().equals(CommandEnum.FLIP_A_COIN.getGroup())) {
                gameCommandsBuilder.append(commandBuilder);
            }

            if (slashCommand.getCommandGroup().equals(CommandEnum.SETTINGS.getGroup())) {
                globalCommandsBuilder.append(commandBuilder);
            }

            if (slashCommand.getCommandGroup().equals(CommandEnum.PLAY.getGroup())) {
                musicCommandsBuilder.append(commandBuilder);
            }

            if (slashCommand.getCommandGroup().equals(CommandEnum.SUMMONER.getGroup())) {
                leagueCommandsBuilder.append(commandBuilder);
            }
        }

        embedBuilder.addField("Global commands", globalCommandsBuilder.toString(), false);
        embedBuilder.addField("Game commands " + EmojiEnum.CONTROLLER.getTag(), gameCommandsBuilder.toString(), false);
        embedBuilder.addField("Music commands " + EmojiEnum.MUSIC.getTag(), musicCommandsBuilder.toString(), false);

        if (bot.isApiLeagueEnable()) {
            embedBuilder.addField("League of Legends commands " + EmojiEnum.WITCHER.getTag(), leagueCommandsBuilder.toString(), false);
        }

        interaction.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();
        embedBuilder.clear();
    }
}
