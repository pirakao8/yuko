package command;

public enum CommandEnum {
    FLIP_A_COIN("flipacoin", "Flip a coin. Basic. Simple", "GAME"),
    W2P("w2p", "I ask to everybody connected if they want to play with you", "GAME"),

    DELETE_RULES("deleterules", "Delete all rules of this guild", "GLOBAL"),
    RULES("rules", "Add or read rules for this server", "GLOBAL"),
    HELP("help", "Check all the commands available", "GLOBAL"),
    INFO("info", "Get some info about Yuko's life", "GLOBAL"),
    PING("ping", "Check my response time", "GLOBAL"),
    SETTINGS("settings", "Enable/Disable settings", "GLOBAL"),
    TIME_OUT("to", "Time out someone", "GLOBAL"),
    TOPIC("topic", "Get the topic for the current channel", "GLOBAL"),
    UP_TIME("uptime", "Get the time since I'm up", "GLOBAL"),

    BUILD("build", "Get the recommended build for a league champion", "LEAGUE_OF_LEGENDS"),
    CHAMP("champ", "Get tips on a league champion",  "LEAGUE_OF_LEGENDS"),
    PATCH("patch", "Get the last patch note",  "LEAGUE_OF_LEGENDS"),
    SUMMONER("summoner", "Get some info about a summoner",  "LEAGUE_OF_LEGENDS"),

    CLEAR("clear", "Clear the current queue", "MUSIC"),
    PAUSE("pause", "Pause/Resume the track", "MUSIC"),
    PLAY("play", "Play a YouTube audio", "MUSIC"),
    PLAYER("player", "Get a player, like a radio", "MUSIC"),
    QUEUE("queue", "Display the current queue", "MUSIC"),
    SHUFFLE("shuffle", "Shuffle the current queue", "MUSIC"),
    SKIP("skip", "Skip the track currently playing", "MUSIC"),
    STOP("stop", "Stop the track and clear the queue", "MUSIC"),
    VOLUME("volume", "Set up the volume", "MUSIC");

    private final String name;
    private final String description;
    private final String group;

    CommandEnum(final String name, final String description, final String group) {
        this.name         = name;
        this.description  = description;
        this.group        = group;
    }

    public final String getName() {
        return name;
    }

    public final String getDescription() {
        return description;
    }

    public final String getGroup() {
        return group;
    }
}

