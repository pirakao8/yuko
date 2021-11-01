package command.game;

import bot.Bot;
import command.AbstractSlashCommand;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Want2PlayCommand extends AbstractSlashCommand {
    //TODO add msg
    private final String[] tabMsgMention = {
            "Let's play my friend",
            "Wanna play ?",
    };

    @Override
    public final void execute(@NotNull final SlashCommandEvent event, @NotNull final Bot bot) {
        super.execute(event);

        assert event.getMember() != null;

        if (!bot.getGuildSetting(event.getGuild()).isW2PEnable()) {
            event.reply("W2P is disabled").setEphemeral(true).queue();
            return;
        }

        final ArrayList<Member> memberList = new ArrayList<>(event.getTextChannel().getMembers());
        final StringBuilder mentionMsg = new StringBuilder();

        memberList.remove(event.getMember());

        if (!memberList.isEmpty()) {
            for (Member member : memberList) {
                if (member.getOnlineStatus() != OnlineStatus.OFFLINE && !member.getUser().isBot()) {
                    mentionMsg.append("<@" + member.getId() + "> " + tabMsgMention[(int) (Math.random() * tabMsgMention.length)] + "\n");
                }
            }
        }

        if(!mentionMsg.toString().isEmpty()) {
            if(mentionMsg.toString().length() < Message.MAX_CONTENT_LENGTH) {
                event.reply(mentionMsg.toString()).queue();
            } else {
                event.reply("There is too much people in this channel, I can't mention them all").setEphemeral(true).queue();
            }
        } else {
            event.reply("Sorry, nobody available to play with you...").setEphemeral(true).queue();
        }
    }
}
