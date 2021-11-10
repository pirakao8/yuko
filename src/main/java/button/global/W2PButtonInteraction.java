package button.global;

import bot.Bot;
import bot.GuildSettings;
import button.AbstractButtonInteraction;
import command.CommandEnum;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.Interaction;
import org.jetbrains.annotations.NotNull;

public class W2PButtonInteraction extends AbstractButtonInteraction {
    @Override
    public final void execute(@NotNull final Interaction interaction, final @NotNull Bot bot) {
        assert interaction.getMember() != null;
        assert interaction.getGuild() != null;

        final GuildSettings guildSettings = bot.getGuildSetting(interaction.getGuild());

        if (!interaction.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            interaction.reply("You don't have permission to enable */" + CommandEnum.W2P.getName() + "* command").setEphemeral(true).queue();
            logger.logDiscordMemberPermission(interaction, Permission.ADMINISTRATOR);
            return;
        }

        guildSettings.setEnableW2P(!guildSettings.isW2PEnable());

        if (guildSettings.isW2PEnable()) {
            interaction.reply("*/" + CommandEnum.W2P.getName() + "* is now enabled").setEphemeral(true).queue();
        } else {
            interaction.reply("*/" + CommandEnum.W2P.getName() + "* is now disabled").setEphemeral(true).queue();
        }
    }
}
