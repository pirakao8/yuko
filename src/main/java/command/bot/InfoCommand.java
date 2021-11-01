package command.bot;

import bot.Bot;
import command.AbstractSlashCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.jetbrains.annotations.NotNull;

public class InfoCommand extends AbstractSlashCommand {
    @Override
    public void execute(@NotNull final SlashCommandEvent event, final Bot bot) {
        super.execute(event, bot);

        event.reply("Yuko's info:\n\n" +

                    "Version: " + bot.getVersion() + "\n" +
                    "GitHub link: https://github.com/pirakao8/yuko\n" +
                    "Contributors: Flo#2478|Chronoxios#5235|Sioohz#3741|Neilor#8309|pirakao8#1272\n" +
                    "Contact: pirakao8#1272\n" +
                    "Thank you for using Yuko, hope you enjoy it, if you have any questions our trouble, please contact us")
                .setEphemeral(true).queue();
    }
}
