package com.apex.client.module.misc;

import com.apex.client.Module;
import com.apex.client.setting.BooleanSetting;
import com.apex.client.setting.NumberSetting;

public class HUD extends Module {

    // What to show
    private final BooleanSetting showTopBar = new BooleanSetting("TopBar", true);
    private final BooleanSetting showArrayList = new BooleanSetting("ArrayList", true);
    private final BooleanSetting showCoords = new BooleanSetting("Coords", true);
    private final BooleanSetting showYaw = new BooleanSetting("Yaw", true);
    private final BooleanSetting showPitch = new BooleanSetting("Pitch", true);
    private final BooleanSetting showFPS = new BooleanSetting("FPS", true);
    private final BooleanSetting showWorld = new BooleanSetting("World", false);

    // Position offsets
    private final NumberSetting coordsX = new NumberSetting("CoordsX", 10, 0, 500, 5);
    private final NumberSetting coordsY = new NumberSetting("CoordsY", 0, 0, 500, 5);

    public HUD() {
        super("HUD", "Toggle and configure HUD elements", Category.MISC);
        addSetting(showTopBar);
        addSetting(showArrayList);
        addSetting(showCoords);
        addSetting(showYaw);
        addSetting(showPitch);
        addSetting(showFPS);
        addSetting(showWorld);
        addSetting(coordsX);
        addSetting(coordsY);
        setEnabled(true); // On by default
    }

    public boolean isTopBarEnabled()   { return showTopBar.isEnabled(); }
    public boolean isArrayListEnabled() { return showArrayList.isEnabled(); }
    public boolean isCoordsEnabled()   { return showCoords.isEnabled(); }
    public boolean isYawEnabled()      { return showYaw.isEnabled(); }
    public boolean isPitchEnabled()    { return showPitch.isEnabled(); }
    public boolean isFPSEnabled()      { return showFPS.isEnabled(); }
    public boolean isWorldEnabled()    { return showWorld.isEnabled(); }
    public int getCoordsXOffset()      { return coordsX.getIntValue(); }
    public int getCoordsYOffset()      { return coordsY.getIntValue(); }
}
