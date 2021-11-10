package button.music;

import bot.Bot;
import bot.GuildSettings;
import button.AbstractButtonInteraction;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.Interaction;
import org.jetbrains.annotations.NotNull;

public class MusicButtonInteraction extends AbstractButtonInteraction {
    @Override
    public final void execute(@NotNull final Interaction interaction, final @NotNull Bot bot) {
        assert interaction.getMember() != null;
        assert interaction.getGuild() != null;

        final GuildSettings guildSettings = bot.getGuildSetting(interaction.getGuild());

        if (!interaction.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            logger.logDiscordMemberPermission(interaction, Permission.ADMINISTRATOR);
            interaction.reply("You don't have permission to manage channels").setEphemeral(true).queue();
            return;
        }

        guildSettings.setEnableMusic(!guildSettings.isMusicEnable());

        if (guildSettings.isMusicEnable()) {
            interaction.reply("Music is now enabled").setEphemeral(true).queue();
        } else {
            if (interaction.getGuild().getAudioManager().isConnected()) {
                interaction.getGuild().getAudioManager().closeAudioConnection();
            }
            interaction.reply("Music is now disabled").setEphemeral(true).queue();
        }
    }
}
