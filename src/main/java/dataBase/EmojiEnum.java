package dataBase;

import net.dv8tion.jda.api.entities.Emoji;

public enum EmojiEnum {
    WITCHER("\uD83E\uDDD9\u200D♂️"),
    CONTROLLER("\uD83C\uDFAE"),
    SWORD("⚔️"),
    WELCOME("👋"),
    SHIELD("\uD83D\uDEE1"),
    CHECK("✅"),
    ARROW_DOWN("⬇️"),
    FIRST("\uD83E\uDD47"),
    SECOND("\uD83E\uDD48"),
    THIRD("\uD83E\uDD49"),
    THUMBS_UP("\uD83D\uDC4D"),
    THUMBS_DOWN("\uD83D\uDC4E"),
    MAYBE("\uD83E\uDD37\u200D♀️"),

    HP("\uD83D\uDC9A"),
    ARMOR("\uD83D\uDD36"),
    RM("\uD83D\uDD35"),
    AD("\uD83E\uDE93"),
    MS("\uD83C\uDFC3"),
    ATTACK_RANGE("\uD83C\uDFF9"),
    MANA("\uD83D\uDCA7"),
    AS("⚡"),

    MUSIC("\uD83C\uDFB5"),
    PLAY("▶️"),
    PAUSE("⏸️"),
    SKIP("⏭️"),
    STOP("⏹️"),
    SHUFFLE("\uD83D\uDD00"),
    CLEAR("\uD83C\uDFF3"),
    VOLUME("\uD83D\uDD0A");

    private final Emoji emojiEntity;
    private final String tag;

    EmojiEnum(String tag) {
        this.tag = tag;
        emojiEntity = Emoji.fromMarkdown(tag);
    }

    public String getTag() {
        return tag;
    }

    public final Emoji getEmoji() {
        return emojiEntity;
    }
}