package command.game;

import bot.Bot;
import bot.GuildSettings;
import command.CommandEnum;
import command.AbstractSlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.Interaction;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Want2PlayCommand extends AbstractSlashCommand {
    private final String[] tabMsgMention = {
            "Let's play my friend!",
            "Wanna play?",
            "We need you on the field!",
    };

    public Want2PlayCommand() {
        super(CommandEnum.W2P);
    }

    @Override
    public final void execute(@NotNull final Interaction interaction, final @NotNull Bot bot, final List<OptionMapping> options) {
        if (!bot.getGuildSetting(interaction.getGuild()).isW2PEnable()) {
            interaction.reply("W2P is disabled").setEphemeral(true).queue();
            return;
        }

        final ArrayList<Member> memberList = new ArrayList<>();
        for (Member member : interaction.getTextChannel().getMembers()) {
            if (member.getOnlineStatus() == OnlineStatus.ONLINE && !member.getUser().isBot()) {
               memberList.add(member);
            }
        }
        memberList.remove(interaction.getMember());

        if (memberList.isEmpty()) {
            interaction.reply("Sorry, nobody available to play with you").setEphemeral(true).queue();
            return;
        }

        final EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(GuildSettings.DEFAULT_COLOR);
        embedBuilder.setTitle("W2P?");
        embedBuilder.setDescription(tabMsgMention[(int) (Math.random() * tabMsgMention.length)]);

        interaction.replyEmbeds(embedBuilder.build()).mention(memberList).queue();
        embedBuilder.clear();
    }
}
