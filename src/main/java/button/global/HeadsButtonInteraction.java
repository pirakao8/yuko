package button.global;

import bot.Bot;
import button.AbstractButtonInteraction;
import command.game.FlipCoinCommand;
import net.dv8tion.jda.api.interactions.Interaction;
import org.jetbrains.annotations.NotNull;

public class HeadsButtonInteraction extends AbstractButtonInteraction {
    @Override
    public final void execute(@NotNull final Interaction interaction, final @NotNull Bot bot) {
        if (new FlipCoinCommand().getResult()) {
            interaction.reply("Result: Heads, you won GG WP").setEphemeral(true).queue();
        } else {
            interaction.reply("Result: Tails, you lost GL next").setEphemeral(true).queue();
        }
    }
}
