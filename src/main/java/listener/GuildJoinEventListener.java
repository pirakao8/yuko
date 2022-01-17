package listener;

import bot.Bot;
import bot.GuildSettings;
import button.admin.LeagueButton;
import button.admin.MusicButton;
import button.admin.W2PButton;
import button.admin.WelcomerButton;
import dataBase.EmojiEnum;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import org.jetbrains.annotations.NotNull;

public class GuildJoinEventListener extends AbstractEventListener {
    @Override
    public final void onGuildMemberJoin(@NotNull final GuildMemberJoinEvent event) {
        assert event.getGuild().getDefaultChannel() != null;

        if (event.getUser().isBot()) {
            return;
        }

        if (GuildSettings.getInstance(event.getGuild()).isWelcomerEnable()) {
            event.getGuild().addRoleToMember(
                            event.getMember().getId(),
                            event.getGuild().getRolesByName(GuildSettings.ROLE_WELCOMER, true).get(0))
                    .queue();
        }

        logger.logDiscord(event.getGuild(), event.getUser(), "User joined the guild");
    }
    @Override
    public final void onGuildJoin(@NotNull final GuildJoinEvent event) {
        assert event.getGuild().getBotRole() != null;

        final TextChannel textChannel;

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
                "To enable **" + GuildSettings.ROLE_WELCOMER + "**, add some rules first with **/rules** command. Enjoy!");

        try {
            textChannel.sendMessageEmbeds(embedBuilder.build()).setActionRow(
                            new LeagueButton().getComponent(!Bot.isApiLeagueEnable()),
                            new W2PButton().getComponent(),
                            new WelcomerButton().getComponent(GuildSettings.getInstance(event.getGuild()).isRuleEmpty()),
                            new MusicButton().getComponent())
                    .queue();
            logger.logDiscord(event.getGuild(), event.getJDA().getSelfUser(), "Guild joined");
        } catch (InsufficientPermissionException e) {
            logger.logDiscordBotPermission(event.getGuild(), e);
        }
        embedBuilder.clear();
    }
}