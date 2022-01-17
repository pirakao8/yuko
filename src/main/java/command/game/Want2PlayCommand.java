package command.game;

import bot.GuildSettings;
import command.Command;
import command.CommandCategoryEnum;
import dataBase.EmojiEnum;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class Want2PlayCommand implements Command {
    private final String[] tabMsgMention = {
            "Let's play my friend!",
            "Wanna play?",
            "We need you on the field!",
    };

    @Contract(pure = true)
    @Override
    public final @NotNull String getName() {
        return "w2p";
    }

    @Contract(pure = true)
    @Override
    public final @NotNull String getDescription() {
        return "I ask to everybody connected if they want to play with you";
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
        if (!GuildSettings.getInstance(event.getGuild()).isW2PEnable()) {
            event.reply("W2P is disabled").setEphemeral(true).queue();
            return;
        }

        final ArrayList<Member> memberList = new ArrayList<>();
        for (Member member : event.getTextChannel().getMembers()) {
            if (member.getOnlineStatus() == OnlineStatus.ONLINE && !member.getUser().isBot()) {
                memberList.add(member);

            }
        }
        memberList.remove(event.getMember());

        if (memberList.isEmpty()) {
            event.reply("Sorry, nobody available to play with you").setEphemeral(true).queue();
            return;
        }

        assert event.getMember() != null;

        final StringBuilder w2pBuilder = new StringBuilder();
        w2pBuilder.append("Want2Play? from " + event.getMember().getEffectiveName() + "\n\n");

        for (Member member : memberList) {
            w2pBuilder.append("<@" + member.getId() + "> " + tabMsgMention[(int) (Math.random() * tabMsgMention.length)] + "\n");
        }

        event.reply("w2p sent").setEphemeral(true).queue();
        event.getTextChannel().sendMessage(w2pBuilder.toString()).queue(m -> {
            m.addReaction(EmojiEnum.THUMBS_UP.getTag()).queue();
            m.addReaction(EmojiEnum.MAYBE.getTag()).queue();
            m.addReaction(EmojiEnum.THUMBS_DOWN.getTag()).queue();
        });
    }
}
