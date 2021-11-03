package bot.listener;

import bot.Bot;
import bot.setting.EmojiList;
import bot.setting.GuildSettings;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.interactions.components.Button;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class GuildJoinListener extends AbstractListener {
    public GuildJoinListener(final Bot bot) {
        super(bot);
    }

    @Override
    public final void onGuildMemberJoin(@NotNull final GuildMemberJoinEvent event) {
        super.onGuildMemberJoin(event);

        assert event.getGuild().getDefaultChannel() != null;

        if (event.getUser().isBot()) return;

        if (bot.getGuildSetting(event.getGuild()).isWelcomerEnable()) {
            event.getGuild().addRoleToMember(
                            event.getMember().getId(),
                            event.getGuild().getRolesByName(GuildSettings.ROLE_WELCOMER, true).get(0))
                    .queue();
        }
    }

    @Override
    public final void onGuildJoin(@NotNull final GuildJoinEvent event) {
        super.onGuildJoin(event);

        assert event.getGuild().getBotRole() != null;

        final TextChannel textChannel;

        bot.addSetting(new GuildSettings(event.getGuild()));

        logger.logDiscord(event.getGuild(), "Guild joined");

        event.getGuild().getBotRole().getManager().setColor(GuildSettings.DEFAULT_COLOR).queue();

        if (event.getGuild().getDefaultChannel() == null) {
            if(event.getGuild().getRulesChannel() == null) {
                if (event.getGuild().getTextChannels().isEmpty()) {
                    return;
                } else {
                    textChannel = event.getGuild().getTextChannels().get(0);
                }
            } else {
                textChannel = event.getGuild().getRulesChannel();
            }
        } else {
            textChannel = event.getGuild().getDefaultChannel();
        }

        final ArrayList<Button> buttons = new ArrayList<>();
            buttons.add(Button.primary(ButtonClickListener.BT_LABEL_LEAGUE, ButtonClickListener.BT_LABEL_LEAGUE)
                    .withEmoji(Emoji.fromMarkdown(EmojiList.WITCHER.getTag())).withDisabled(!bot.isApiLeagueEnable()));
        buttons.add(Button.primary(ButtonClickListener.BT_LABEL_MUSIC, ButtonClickListener.BT_LABEL_MUSIC).withEmoji(Emoji.fromMarkdown(EmojiList.MUSIC.getTag())));
        buttons.add(Button.primary(ButtonClickListener.BT_LABEL_JAIL, ButtonClickListener.BT_LABEL_JAIL).withEmoji(Emoji.fromMarkdown(EmojiList.JAIL.getTag())));
        buttons.add(Button.primary(ButtonClickListener.BT_LABEL_W2P, ButtonClickListener.BT_LABEL_W2P).withEmoji(Emoji.fromMarkdown(EmojiList.CONTROLLER.getTag())));
        buttons.add(Button.primary(ButtonClickListener.BT_LABEL_PIN, ButtonClickListener.BT_LABEL_PIN).withEmoji(Emoji.fromMarkdown(EmojiList.PIN.getTag())));

        final EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(GuildSettings.DEFAULT_COLOR);
        embedBuilder.setTitle("Hello everyone!");
        embedBuilder.appendDescription("I'm Yuko the bot and I'm here to help you! How? With multiple features, type '/help' if you want to know them." +
                "But before anything else, you can enable/disable some of my settings by clicking on the buttons " + EmojiList.ARROW_DOWN.getTag() + ". " +
                "To enable '" + GuildSettings.ROLE_WELCOMER + "', add some rules first with '/rules' command. Enjoy!");

        textChannel.sendTyping().queue();
        textChannel.sendMessageEmbeds(embedBuilder.build()).setActionRow(buttons).queue();
        embedBuilder.clear();
    }
}
