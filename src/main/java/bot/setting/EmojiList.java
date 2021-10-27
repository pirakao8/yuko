package bot.setting;

public enum EmojiList {
    JAIL("\uD83E\uDDD1\u200D⚖️"),
    WITCHER("\uD83E\uDDD9\u200D♂️"),
    CONTROLLER("\uD83C\uDFAE"),
    PIN("\uD83D\uDCCC"),
    SWORD("⚔️"),
    SHIELD("\uD83D\uDEE1️"),

    MUSIC("\uD83C\uDFB5"),
    PLAY("▶️"),
    PAUSE("⏸️"),
    SKIP("⏭️"),
    STOP("⏹️"),
    SHUFFLE("\uD83D\uDD00"),
    CLEAR("\uD83C\uDFF3️"),
    VOLUME("\uD83D\uDD0A");

    private final String tag;

    EmojiList(String tag) {
        this.tag = tag;
    }

    public final String getTag() {
        return tag;
    }
}
