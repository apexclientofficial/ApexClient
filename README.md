# ApexClient

> **Important Notice:** This repository is the **only** official page to download the authentic ApexClient `.jar` file, outside of our official Discord server. Do not download this client from third-party websites or unverified sources.

---

## Join the Community

Get access to announcements, changelogs, config sharing, and direct support.

* **Official Discord Server:** [Join our Discord](https://discord.gg/T2FeVbYw4n)

---

## What's New in v5.0

### New Modules
* **AutoTool (Player):** Automatically swaps to the best weapon when attacking, or the best tool when breaking blocks, then switches back to your previous hotbar slot.
* **KillEffects (Render):** Triggers custom lightning bolts, blood particle bursts, and hit sound effects whenever you eliminate a target.
* **ParticleTrails (Render):** Renders customizable glowing particle trails behind your player movement.
* **PotionStatus (Misc):** A moveable HUD card displaying your active potion effects, amplifiers, and live countdown timers.
* **FovRing (Render):** Draws a crosshair FOV indicator directly matched to your active Aimbot or AimAssist settings.
* **Notifications (Misc):** Master toggle to enable or disable toast popups and sound cues.

### Module Overhauls & Fixes
* **TargetHUD:** Now only triggers when actively attacking an enemy instead of showing nearby players. Fully draggable anywhere on screen.
* **Starfall Skybox:** Added a new Nebula mode that renders drifting cosmic lines across the custom sky.
* **Spinbot Fixes:** Rewrote server-side rotation handling. Upgrades C04 movement packets to C06 position/look packets for proper third-person spinning on the server, paired with smooth third-person (F5) rendering locally.
* **AutoClicker & TriggerBot:** Added a Method selector (OS-level Robot clicks vs. native Packet clicks) along with an optional swing animation toggle.

### ClickGUI & System Updates
* **Unified "ALL" Category:** Added an "ALL" tab to view every module in a single list inside the GUI.
* **Live Search:** Filter modules instantly by typing in the new ClickGUI search bar.
* **Scissor & Scroll Fix:** Fixed bottom card clipping issues when searching inside the ClickGUI.
* **Clean Category Headers:** Replaced emoji headers with clean text category labels across all interface panels.
* **Toast Notification Engine:** Added slide-in screen alerts accompanied by audio cues when saving/loading configs or toggling features.

---

## What's New in v4.0

### User Interface Overhaul
* **New Config Menu System:** Replaced the old cyclic toggle system with a fully interactive dropdown menu workflow. You can select a configuration profile, enter an editing state to tweak individual module behaviors, and save or export setups as new configuration files.
* **RGB Color Picker Integration:** Added a full 0-255 RGB selection interface for all visual modules. Changes made in the color picker update dynamically in real-time across both ESP loops and primary HUD elements.
* **Category Visual Cleanups:** Updated the main ClickGUI window headers with integrated identifiers mapping cleanly to each module group.

### Combat & Gameplay Enhancements
* **Advanced Target Filtering:** Upgraded tracking capabilities for KillAura, Triggerbot, and AimAssist. Sort and prioritize targets based on lowest health or closest distance.
* **AntiTeam Rewrite:** Overhauled team-verification mechanics. Cross-references native Scoreboard team assignments alongside player name color formatting strings for accurate teammate filtering.
* **Scaffold Mode Presets:** Added distinct "Legit" and "Blatant" operational modes. Legit mode tracks your exact position relative to block edges to automate precise sneaking and block placement, while Blatant mode focuses on maximum speed.
* **AutoClicker Mode Settings:** Added options for stable CPS execution alongside a randomized frequency mode that utilizes custom minimum and maximum CPS thresholds.
* **Custom HandView / Cosmetics:** Added a localized item rendering offset module. Adjust X, Y, and Z axes independently, customize sword rotation angles, and tweak animation speed modifiers to change the visual arm-swing cycle.

### Network & Performance Optimizations
* **Packet Manipulation Tools:** Added a dedicated networking module operating via outbound Netty channel handlers to safely monitor, delay, or modify specific traffic fields before they are sent to the server.
* **Transaction Interception:** Implemented custom packet buffering loops to hold outbound confirm and keep-alive actions, letting you simulate artificial latency to balance out server-side movement prediction.
* **Silent Spoofing Overrides:** Added active field reflection overrides to modify ground-state indicators during specific fall boundaries and maintain silent vector rotations during active combat loops.

---

## Included Modules

ApexClient comes equipped with a suite of modules built for performance and customization.

| Combat | Movement & Physics | Visuals & Render | Player, World & Misc |
| :--- | :--- | :--- | :--- |
| **KillAura** | **Sprint** | **ESP** | **ChestStealer** |
| **Triggerbot** | **Speed** | **Tracers** | **AutoArmor** |
| **Velocity** | **Fly** | **Nametags** | **AutoTotem** |
| **Reach** | **NoFall** | **ItemESP** | **AutoSoup** |
| **AutoClicker** | **Jesus** | **Chams** | **AutoRespawn** |
| **Criticals** | **Step** | **Xray** | **FastPlace** |
| **HitBox** | **Phase** | **FullBright** | **FastBreak** |
| **AimAssist** | **InventoryWalk** | **Freecam** | **Timer** |
| **Backtrack** | **Sneak** | **ClickGUI** | **Regen** |
| **AntiBot** | **Scaffold** | **HUD** | **HandView** |
| **AntiTeam** | | **KillEffects** | **AutoTool** |
| | | **ParticleTrails** | **PotionStatus** |
| | | **FovRing** | **Notifications** |
| | | **Starfall** | **Packet Manipulation** |

---

## Installation & Requirements

* **File Type:** Executable `.jar`
* **Requirement:** Minecraft Forge 1.8.9
