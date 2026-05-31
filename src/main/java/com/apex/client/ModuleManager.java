package com.apex.client;

import com.apex.client.module.combat.*;
import com.apex.client.module.movement.*;
import com.apex.client.module.player.*;
import com.apex.client.module.render.*;
import com.apex.client.module.misc.*;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    private final List<Module> modules = new ArrayList<>();

    public void init() {
        // ── Combat ──────────────────────────────────────────
        add(new KillAura());
        add(new TriggerBot());
        add(new AimAssist());
        add(new Aimbot());
        add(new Velocity());
        add(new Reach());
        add(new AutoClicker());
        add(new Criticals());
        add(new AntiBot());
        add(new HitBox());
        add(new Backtrack());

        // ── Movement ────────────────────────────────────────
        add(new Sprint());
        add(new Speed());
        add(new Fly());
        add(new NoFall());
        add(new Scaffold());
        add(new InventoryMove());
        add(new Jesus());
        add(new Step());
        add(new Phase());
        add(new Sneak());
        add(new NoSlow());
        add(new Timer());
        add(new Freecam());

        // ── Player ──────────────────────────────────────────
        add(new ChestStealer());
        add(new FastPlace());
        add(new FastBreak());
        add(new AutoArmor());
        add(new AutoSoup());
        add(new AutoRespawn());
        add(new Regen());

        // ── Render ──────────────────────────────────────────
        add(new ESP());
        add(new Tracers());
        add(new Nametags());
        add(new ItemESP());
        add(new Chams());
        add(new Xray());
        add(new FullBright());

        // ── Misc ────────────────────────────────────────────
        add(new HUD());
        add(new ClickGUIModule());
        add(new Config());
    }

    private void add(Module m) {
        modules.add(m);
    }

    public List<Module> getModules() {
        return modules;
    }

    public List<Module> getModulesByCategory(Module.Category category) {
        List<Module> out = new ArrayList<>();
        for (Module m : modules) {
            if (m.getCategory() == category) out.add(m);
        }
        return out;
    }

    public Module getModuleByName(String name) {
        for (Module m : modules) {
            if (m.getName().equalsIgnoreCase(name)) return m;
        }
        return null;
    }
}
