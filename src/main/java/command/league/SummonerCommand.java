package command.league;

import bot.Bot;
import com.merakianalytics.orianna.datapipeline.riotapi.exceptions.ForbiddenException;
import com.merakianalytics.orianna.types.core.championmastery.ChampionMasteries;
import com.merakianalytics.orianna.types.core.championmastery.ChampionMastery;
import com.merakianalytics.orianna.types.core.league.LeagueEntry;
import com.merakianalytics.orianna.types.core.league.LeaguePositions;
import com.merakianalytics.orianna.types.core.match.Match;
import com.merakianalytics.orianna.types.core.match.MatchHistory;
import com.merakianalytics.orianna.types.core.match.Participant;
import com.merakianalytics.orianna.types.core.match.ParticipantStats;
import com.merakianalytics.orianna.types.core.spectator.CurrentMatch;
import com.merakianalytics.orianna.types.core.summoner.Summoner;
import command.AbstractSlashCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.jetbrains.annotations.NotNull;
import util.Logger;

import java.util.Date;
import java.util.List;

public class SummonerCommand extends AbstractSlashCommand {
    @Override
    public final void execute(@NotNull final SlashCommandEvent event, @NotNull final Bot bot) {
        super.execute(event);

        assert event.getOptions().get(0) != null;

        if(!bot.getGuildSetting(event.getGuild()).isLeagueEnable()) {
            event.reply("League commands are disabled").setEphemeral(true).queue();
            return;
        }

        try {
            final Summoner summoner = Summoner.named(event.getOptions().get(0).getAsString()).get();

            if (!summoner.exists()) {
                event.reply("Summoner not found").queue();
                return;
            }

            event.reply(getInfoLeagueSummoner(summoner) + "\n"
                    + getInfoChampionMasteries(summoner) + getInfoCurrentGame(summoner) + "\n\n"
                    + getInfoLastGame(summoner)).queue();
        } catch (ForbiddenException e) {
            logger.log(Logger.Level.ERROR, "Riot API returned FORBIDDEN EXCEPTION about SUMMONER, check key permission or api status page");
            event.reply("Summoner info unavailable due to an error with Riot API").setEphemeral(true).queue();
        }
    }

    private @NotNull String getInfoLeagueSummoner(@NotNull final Summoner summoner) {
        final StringBuilder infoLeagueSummoner = new StringBuilder();
        try {
            infoLeagueSummoner.append("Summoner : " + summoner.getName() + ", lvl " + summoner.getLevel() + "\n\n");

            final LeaguePositions leaguePositions = summoner.getLeaguePositions();
            if (leaguePositions.exists()) {
                for (LeagueEntry currentLeague : leaguePositions) {
                    String queueName = currentLeague.getQueue().name();
                    if (queueName.equals("RANKED_SOLO")) queueName = "SoloQ ";
                    else if (queueName.equals("RANKED_FLEX")) queueName = "Flex ";

                    infoLeagueSummoner.append(queueName + " : " + currentLeague.getTier().name().toLowerCase() + " ");
                    infoLeagueSummoner.append(currentLeague.getDivision() + " " + currentLeague.getLeaguePoints() + " LP\n");
                }
            } else infoLeagueSummoner.append("Unranked");
        } catch (ForbiddenException e) {
            logger.log(Logger.Level.ERROR, "Riot API returned FORBIDDEN EXCEPTION about SUMMONER, check key permission or api status page");
            infoLeagueSummoner.append("Summoner info unavailable due to an error with Riot API");
        }
        return infoLeagueSummoner.toString();
    }

    private @NotNull String getInfoChampionMasteries(@NotNull final Summoner summoner) {
        final StringBuilder infoChampionMasteries = new StringBuilder();
        try {
            final ChampionMasteries masteries = summoner.getChampionMasteries();
            final List<ChampionMastery> m6 = masteries.filter((ChampionMastery mastery) -> {
                assert mastery != null;
                return mastery.getLevel() == 6;
            });
            if (!m6.isEmpty()) {
                infoChampionMasteries.append("M6 : ");
                for (ChampionMastery championMastery : m6) {
                    infoChampionMasteries.append(championMastery.getChampion().getName() + ", ");
                }
                infoChampionMasteries.append("\n");
            } else infoChampionMasteries.append("No champ M6 \n");

            final List<ChampionMastery> m7 = masteries.filter((ChampionMastery mastery) -> {
                assert mastery != null;
                return mastery.getLevel() == 7;
            });
            if (!m7.isEmpty()) {
                infoChampionMasteries.append("M7 : " + m7.get(0).getChampion().getName() + " with ");
                infoChampionMasteries.append(m7.get(0).getPoints() + " points");
                m7.remove(0);
                for (ChampionMastery championMastery : m7) {
                    infoChampionMasteries.append(", " + championMastery.getChampion().getName());
                }
                infoChampionMasteries.append("\n");
            } else infoChampionMasteries.append("No champ M7");
        } catch (ForbiddenException e) {
            logger.log(Logger.Level.ERROR, "Riot API returned FORBIDDEN EXCEPTION about CHAMPION MASTERIES, check key permission or api status page");
            infoChampionMasteries.append("Champion masteries unavailable due to an error with Riot API");
        }
        return infoChampionMasteries.toString();
    }

    private @NotNull String getInfoCurrentGame(@NotNull final Summoner summoner) {
        final StringBuilder infoCurrentGame = new StringBuilder();
        try {
            if (summoner.isInGame()) {
                final CurrentMatch currentMatch = summoner.getCurrentMatch();
                final long gameDuration = (new Date().getTime() - currentMatch.getCreationTime().getMillis()) / (1000 * 60);

                infoCurrentGame.append(summoner.getName() + " currently in game since " + gameDuration + " minutes ");
                if (currentMatch.getMode().name().equalsIgnoreCase("classic")) infoCurrentGame.append("on the rift");
                else infoCurrentGame.append("in " + currentMatch.getMode().name().toLowerCase());
            }
        } catch (ForbiddenException e) {
            logger.log(Logger.Level.ERROR, "Riot API returned FORBIDDEN EXCEPTION about CURRENT GAME, check key permission or api status page");
            infoCurrentGame.append("Current game info unavailable due to an error with Riot API");
        }
        return infoCurrentGame.toString();
    }

    private @NotNull String getInfoLastGame(@NotNull final Summoner summoner) {
        final StringBuilder infoLastGameBuilder = new StringBuilder();
        try {
            final MatchHistory matchHistory = summoner.matchHistory().get();
            if (matchHistory.exists()) {
                final Match lastGame = matchHistory.get(0);
                if (lastGame.exists()) {
                    final List<Participant> participants = lastGame.getParticipants();

                    for (Participant currentParticipant : participants) {
                        if (currentParticipant.getProfileIcon().equals(summoner.getProfileIcon())) {
                            final ParticipantStats stats = currentParticipant.getStats();
                            infoLastGameBuilder.append("Last game with " + currentParticipant.getChampion().getName() +
                                    " in " + stats.getKills() + "/" + stats.getDeaths() + "/" + stats.getAssists());

                            final String lastGameMode = lastGame.getMode().name();
                            if (lastGameMode.equals("classic")) infoLastGameBuilder.append(" at " + currentParticipant.getLane().name().toLowerCase() + "\n");
                            else infoLastGameBuilder.append(" in " + lastGameMode);
                        }
                    }
                }
            }
        } catch (ForbiddenException e) {
            logger.log(Logger.Level.ERROR, "Riot API returned FORBIDDEN EXCEPTION about GAME HISTORY, check key permission or api status page");
            infoLastGameBuilder.append("Last game unavailable due to an error with Riot API");
        }
        return infoLastGameBuilder.toString();
    }
}
