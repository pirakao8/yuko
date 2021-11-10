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

public class TopicCommand extends AbstractSlashCommand {
    public TopicCommand() {
        super(CommandEnum.TOPIC);
    }

    @Override
    public final void execute(@NotNull final Interaction interaction, final Bot bot, final List<OptionMapping> options) {
        if (interaction.getTextChannel().getTopic() != null) {
            final EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(GuildSettings.DEFAULT_COLOR);
            embedBuilder.setTitle("#" + interaction.getTextChannel().getName() + "'s topic");
            embedBuilder.setDescription(interaction.getTextChannel().getTopic());

            interaction.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();
            embedBuilder.clear();
        } else {
            interaction.reply("No topic for this channel").setEphemeral(true).queue();
        }
    }
}
