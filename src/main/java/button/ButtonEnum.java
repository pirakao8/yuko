package button;

import button.global.*;
import button.music.*;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum ButtonEnum {
    BT_HEADS("id_heads", "Heads", new HeadsButtonInteraction()),
    BT_TAILS("id_tails", "Tails", new TailsButtonInteraction()),

    BT_MUSIC("id_music", "Music", new MusicButtonInteraction()),

    BT_JAIL("id_jail", "Yuko's jail", new JailButtonInteraction()),

    BT_LEAGUE("id_league", "League of Legends", new LeagueButtonInteraction()),

    BT_W2P("id_w2p", "W2P", new W2PButtonInteraction()),

    BT_WELCOME("id_welcome", "Welcomer", new WelcomerButtonInteraction()),
    BT_AGREE("id_agree", "Agreed", new AgreedButtonInteraction()),

    BT_STOP("id_stop", "Stop", new StopButtonInteraction()),
    BT_CLEAR("id_clear", "Clear", new ClearButtonInteraction()),
    BT_SKIP("id_skip", "Skip", new SkipButtonInteraction()),
    BT_PAUSE_RESUME("id_pause_resume", "Pause/Resume", new PauseResumeButtonInteraction()),
    BT_SHUFFLE("id_shuffle", "Shuffle", new ShuffleButtonInteraction());

    private final String id;
    private final String label;
    private final AbstractButtonInteraction buttonInteraction;

    private static final Map<String, ButtonEnum> lookUp = new HashMap<>();

    ButtonEnum(final String id, final String label, final AbstractButtonInteraction buttonInteraction) {
        this.id = id;
        this.label = label;
        this.buttonInteraction = buttonInteraction;
    }

    public static ButtonEnum getComponent(String id) {
        return lookUp.get(id);
    }

    static {
        for (ButtonEnum buttonLabelEnum : EnumSet.allOf(ButtonEnum.class)) {
            lookUp.put(buttonLabelEnum.id, buttonLabelEnum);
        }
    }

    public final String getId() {
        return id;
    }

    public final String getLabel() {
        return label;
    }

    public AbstractButtonInteraction getButtonInteraction() {
        return buttonInteraction;
    }
}
