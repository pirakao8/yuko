package button.global;

import bot.Bot;
import bot.GuildSettings;
import button.AbstractButtonInteraction;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.interactions.Interaction;
import org.jetbrains.annotations.NotNull;

public class AgreedButtonInteraction extends AbstractButtonInteraction {
    @Override
    public final void execute(@NotNull final Interaction interaction, final @NotNull Bot bot) {
        assert interaction.getMember() != null;
        assert interaction.getGuild() != null;

        try {
            interaction.getGuild().removeRoleFromMember(interaction.getMember(), interaction.getGuild().getRolesByName(GuildSettings.ROLE_WELCOMER, true).get(0)).queue();
            interaction.reply("Rules checked").setEphemeral(true).queue();
        } catch (InsufficientPermissionException e) {
            logger.logDiscordBotPermission(interaction.getGuild(), e);
            interaction.reply("I don't have enough permission to remove '" + GuildSettings.ROLE_WELCOMER + "' role, please, contact an admin").setEphemeral(true).queue();
        } catch (HierarchyException e) {
            logger.logDiscordHierarchyPermission(interaction.getGuild(), e);
            interaction.reply("My role is lower than '" + GuildSettings.ROLE_WELCOMER + "', I can't remove yours, please, contact an admin").setEphemeral(true).queue();
        }
    }
}
