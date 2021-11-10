package bot.listener;

import bot.Bot;
import bot.GuildSettings;
import button.ButtonEnum;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.interactions.components.Button;
import org.jetbrains.annotations.NotNull;
import bot.EmojiEnum;

public class GuildJoinEventListener extends AbstractEventListener {
    public GuildJoinEventListener(final Bot bot) {
        super(bot);
    }

    @Override
    public final void onGuildMemberJoin(@NotNull final GuildMemberJoinEvent event) {
        super.onGuildMemberJoin(event);

        if (event.getUser().isBot()) {
            return;
        }

        try {
            if (bot.getGuildSetting(event.getGuild()).isWelcomerEnable()) {
                event.getGuild().addRoleToMember(
                                event.getMember().getId(),
                                event.getGuild().getRolesByName(GuildSettings.ROLE_WELCOMER, true).get(0))
                        .queue();
            }
        } catch (InsufficientPermissionException e) {
            logger.logDiscordBotPermission(event.getGuild(), e);
        } catch (HierarchyException e) {
            logger.logDiscordHierarchyPermission(event.getGuild(), e);
        }
    }

    @Override
    public final void onGuildJoin(@NotNull final GuildJoinEvent event) {
        super.onGuildJoin(event);

        assert event.getGuild().getBotRole() != null;

        final TextChannel textChannel;

        bot.addSetting(new GuildSettings(event.getGuild()));

        try {
            event.getGuild().getBotRole().getManager().setColor(GuildSettings.DEFAULT_COLOR).queue();
        } catch (InsufficientPermissionException e) {
            logger.logDiscordBotPermission(event.getGuild(), e);
        } catch (HierarchyException e) {
            logger.logDiscordHierarchyPermission(event.getGuild(), e);
        }


        if (event.getGuild().getTextChannels().isEmpty()) {
            return;
        }

        if (event.getGuild().getDefaultChannel() != null) {
            textChannel = event.getGuild().getDefaultChannel();
        } else {
            textChannel = event.getGuild().getTextChannels().get(0);
        }

        final EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(GuildSettings.DEFAULT_COLOR);
        embedBuilder.setTitle("Hello everyone!");
        embedBuilder.appendDescription("I'm Yuko the bot and I'm here to help you! How? With multiple features, type '/help' if you want to know them." +
                "But before anything else, you can enable/disable some of my settings by clicking on the buttons " + EmojiEnum.ARROW_DOWN.getTag() + ". " +
                "To enable '" + GuildSettings.ROLE_WELCOMER + "', add some rules first with '/rules' command. Enjoy!");

        try {
            textChannel.sendTyping().queue();
            textChannel.sendMessageEmbeds(embedBuilder.build()).setActionRow(
                            Button.primary(ButtonEnum.BT_LEAGUE.getId(), ButtonEnum.BT_LEAGUE.getLabel())
                                    .withEmoji(Emoji.fromMarkdown(EmojiEnum.WITCHER.getTag())).withDisabled(!bot.isApiLeagueEnable()),
                            Button.primary(ButtonEnum.BT_MUSIC.getId(), ButtonEnum.BT_MUSIC.getLabel()).withEmoji(Emoji.fromMarkdown(EmojiEnum.MUSIC.getTag())),
                            Button.primary(ButtonEnum.BT_JAIL.getId(), ButtonEnum.BT_JAIL.getLabel()).withEmoji(Emoji.fromMarkdown(EmojiEnum.JAIL.getTag())),
                            Button.primary(ButtonEnum.BT_W2P.getId(), ButtonEnum.BT_W2P.getLabel()).withEmoji(Emoji.fromMarkdown(EmojiEnum.CONTROLLER.getTag())))
                    .queue();
            logger.logDiscord(event.getGuild(), "Guild joined, welcome message sent");
        } catch (InsufficientPermissionException e) {
            logger.logDiscordBotPermission(event.getGuild(), e);
        }
        embedBuilder.clear();
    }
}
