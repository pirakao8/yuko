package command;

public enum CommandCategoryEnum {
    LEAGUE("League Of Legends"),
    MUSIC("Music"),
    ADMIN("Admin"),
    GAMES("Game"),
    GLOBAL("Global");

    private final String name;

    CommandCategoryEnum(String name) {
        this.name = name;
    }

    public final String getName() {
        return name;
    }
}
