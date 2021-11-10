package button.global;

import bot.Bot;
import bot.GuildSettings;
import button.AbstractButtonInteraction;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.Interaction;
import org.jetbrains.annotations.NotNull;

public class LeagueButtonInteraction extends AbstractButtonInteraction {
    @Override
    public final void execute(@NotNull final Interaction interaction, final @NotNull Bot bot) {
        assert interaction.getMember() != null;
        assert interaction.getGuild() != null;

        final GuildSettings guildSettings = bot.getGuildSetting(interaction.getGuild());

        if (!interaction.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            logger.logDiscordMemberPermission(interaction, Permission.ADMINISTRATOR);
            interaction.reply("You don't have permission to enable league commands").setEphemeral(true).queue();
            return;
        }

        if (!bot.isApiLeagueEnable()) {
            interaction.reply("League of Legends commands are unavailable due to an error with Riot API").setEphemeral(true).queue();
            return;
        }

        guildSettings.setEnableLeague(!guildSettings.isLeagueEnable());

        if (guildSettings.isLeagueEnable()) {
            interaction.reply("League of Legends commands are now enabled").setEphemeral(true).queue();
        } else {
            interaction.reply("League of Legends commands are now disabled").setEphemeral(true).queue();
        }
    }
}
