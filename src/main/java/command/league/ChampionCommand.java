package command.league;

import bot.GuildSettings;
import dataBase.EmojiEnum;
import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.datapipeline.riotapi.exceptions.ForbiddenException;
import com.merakianalytics.orianna.types.core.staticdata.*;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import util.Logger;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ChampionCommand extends AbstractLeagueCommand {
    @Contract(pure = true)
    @Override
    public final @NotNull String getName() {
        return "champ";
    }

    @Contract(pure = true)
    @Override
    public final @NotNull String getDescription() {
        return "Get tips on a league champion";
    }

    @Contract(" -> new")
    @Override
    public final OptionData @NotNull [] getOptions() {
        return new OptionData[] {
                new OptionData(OptionType.STRING, "champion", "Champion's name", true)
        };
    }

    @Override
    public final boolean isEnable() {
        return true;
    }

    @Override
    public final void execute(@NotNull final SlashCommandEvent event) {
        if (!GuildSettings.getInstance(event.getGuild()).isLeagueEnable()) {
            event.reply("League commands are disabled").setEphemeral(true).queue();
            return;
        }

        super.execute(event);

        try {
            final String championName = event.getOptions().get(0).getAsString();
            final Champion champion = Orianna.championNamed(championName).get();

            if (!champion.exists()) {
                event.reply("This champion doesn't exist, or you misspelled it").setEphemeral(true).queue();
                return;
            }

            final List<Skin> skinList = champion.getSkins();

            embedBuilder.setTitle(champion.getName() + ", " + champion.getTitle(),
                    "https://universe.leagueoflegends.com/en_us/champion/" + champion.getName().toLowerCase() + "/");
            embedBuilder.setDescription(champion.getLore());
            embedBuilder.setImage(skinList.get(skinList.size() - 1).getSplashImageURL());
            embedBuilder.setThumbnail(champion.getImage().getURL());

            final ChampionStats championStats = champion.getStats();
            embedBuilder.addField(EmojiEnum.HP.getTag(), (int) championStats.getHealth() + " +" + (int) championStats.getHealthPerLevel(), true);
            embedBuilder.addField(EmojiEnum.ARMOR.getTag(), (int) championStats.getArmor() + " +" + (int) championStats.getArmorPerLevel(), true);
            embedBuilder.addField(EmojiEnum.RM.getTag(), (int) championStats.getMagicResist() + " +" + (int) championStats.getMagicResistPerLevel(), true);

            embedBuilder.addField(EmojiEnum.AD.getTag(), (int) championStats.getAttackDamage() + " +" + (int) championStats.getAttackDamagePerLevel(), true);
            embedBuilder.addField(EmojiEnum.MS.getTag(), String.valueOf((int) championStats.getMovespeed()), true);
            embedBuilder.addField(EmojiEnum.ATTACK_RANGE.getTag(), String.valueOf((int) championStats.getAttackRange()), true);

            if (champion.getResource().equalsIgnoreCase("mana")) {
                embedBuilder.addField(EmojiEnum.MANA.getTag(), (int) championStats.getMana() + " +" + (int) championStats.getManaPerLevel(), true);
            } else {
                embedBuilder.addField(EmojiEnum.MANA.getTag(), "N/A", true);
            }
            embedBuilder.addField(EmojiEnum.AS.getTag(), "+" + championStats.getAttackSpeedPerLevel() + "%", true);
            embedBuilder.addBlankField(true);

            final Passive passive = champion.getPassive();
            embedBuilder.addField("Passive: " + passive.getName(), passive.description(), false);

            final List<ChampionSpell> championSpells = champion.getSpells();
            final char[] keySpells = "QWER".toCharArray();
            for (int i = 0; i < championSpells.size(); i++) {
                embedBuilder.addField(keySpells[i] + ": " + championSpells.get(i).getName(), championSpells.get(i).getDescription().replace("<br>", "\n"), false);
            }

            embedBuilder.addField("Ally tips", champion.getAllyTips().toString().replace("[", "").replace("]", ""), false);
            embedBuilder.addField("Enemy tips", champion.getEnemyTips().toString().replace("[", "").replace("]", ""), false);

            event.replyEmbeds(embedBuilder.build()).queue(m -> m.deleteOriginal().submitAfter(5, TimeUnit.MINUTES));
            embedBuilder.clear();
        } catch (ForbiddenException e) {
            logger.log(Logger.Level.ERROR, "Riot API returned FORBIDDEN EXCEPTION, check key permission or api status page");
            event.reply("Champion unavailable due to an error with Riot API").setEphemeral(true).queue();
        }
    }
}
