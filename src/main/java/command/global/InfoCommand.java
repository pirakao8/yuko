package command.global;

import bot.Bot;
import bot.GuildSettings;
import command.CommandEnum;
import command.AbstractSlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.Interaction;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class InfoCommand extends AbstractSlashCommand {
    public InfoCommand() {
        super(CommandEnum.INFO);
    }

    @Override
    public final void execute(@NotNull final Interaction interaction, final @NotNull Bot bot, final List<OptionMapping> options) {
        final EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(GuildSettings.DEFAULT_COLOR);
        embedBuilder.setTitle("Yuko's info", "https://github.com/pirakao8/yuko");

        embedBuilder.addField("Version", bot.getVersion(), true);
        embedBuilder.addField("Contact", "pirakao8#1272", true);

        embedBuilder.addField("GitHub", "https://github.com/pirakao8/yuko", false);
        embedBuilder.addField("Contributors", "Flo#2478, Chronoxios#5235, Sioohz#3741, Neilor#8309, pirakao8#1272", false);
        embedBuilder.addField("Thank you", "Thank you for using Yuko, hope you enjoy it, if you have any questions our trouble, please contact us", false);

        interaction.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();
        embedBuilder.clear();
    }
}
