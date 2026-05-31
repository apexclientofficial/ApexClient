package com.apex.client.module.misc;

import com.apex.client.Module;
import com.apex.client.setting.BooleanSetting;
import com.apex.client.setting.NumberSetting;

public class HUD extends Module {

    // What to show
    private final BooleanSetting showTopBar = new BooleanSetting("TopBar", true);
    private final BooleanSetting showArrayList = new BooleanSetting("ArrayList", true);
    private final BooleanSetting showKeystrokes = new BooleanSetting("Keystrokes", true);
    private final BooleanSetting showCPS = new BooleanSetting("CPS", true);
    private final BooleanSetting showFPS = new BooleanSetting("FPS", true);
    private final BooleanSetting showSpotify = new BooleanSetting("Spotify", true);

    // Position offsets for keystrokes
    private final NumberSetting keysX = new NumberSetting("KeysX", 10, 0, 500, 5);
    private final NumberSetting keysY = new NumberSetting("KeysY", 50, 0, 500, 5);

    // Position offsets for spotify
    private final NumberSetting spotX = new NumberSetting("SpotifyX", 10, 0, 1000, 5);
    private final NumberSetting spotY = new NumberSetting("SpotifyY", 100, 0, 1000, 5);

    public HUD() {
        super("HUD", "Toggle and configure HUD elements", Category.MISC);
        addSetting(showTopBar);
        addSetting(showArrayList);
        addSetting(showKeystrokes);
        addSetting(showCPS);
        addSetting(showFPS);
        addSetting(showSpotify);
        addSetting(keysX);
        addSetting(keysY);
        addSetting(spotX);
        addSetting(spotY);
        setEnabled(true); // On by default
    }

    public boolean isTopBarEnabled()   { return showTopBar.isEnabled(); }
    public boolean isArrayListEnabled() { return showArrayList.isEnabled(); }
    public boolean isKeystrokesEnabled() { return showKeystrokes.isEnabled(); }
    public boolean isCPSEnabled()      { return showCPS.isEnabled(); }
    public boolean isFPSEnabled()      { return showFPS.isEnabled(); }
    public boolean isSpotifyEnabled()  { return showSpotify.isEnabled(); }
    public int getKeysXOffset()        { return keysX.getIntValue(); }
    public int getKeysYOffset()        { return keysY.getIntValue(); }
    public int getSpotXOffset()        { return spotX.getIntValue(); }
    public int getSpotYOffset()        { return spotY.getIntValue(); }
}
