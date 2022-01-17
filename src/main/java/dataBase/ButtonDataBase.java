package dataBase;

import button.Button;
import button.admin.*;
import button.admin.MusicButton;
import button.game.HeadsButton;
import button.game.TailsButton;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class ButtonDataBase {
    public static @Nullable Button getCommandById(@NotNull final String id) {
        for (Button button : getButtons()) {
            if (button.getId().equalsIgnoreCase(id)) {
                return button;
            }
        }
        return null;
    }

    public static @NotNull ArrayList<Button> getButtons() {
        final ArrayList<Button> buttonArrayList = new ArrayList<>();

        buttonArrayList.add(new WelcomerButton());
        buttonArrayList.add(new MusicButton());
        buttonArrayList.add(new W2PButton());
        buttonArrayList.add(new LeagueButton());
        buttonArrayList.add(new AgreedButton());

        buttonArrayList.add(new HeadsButton());
        buttonArrayList.add(new TailsButton());

        return buttonArrayList;
    }
}
