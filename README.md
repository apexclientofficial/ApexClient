# ApexClient

> **Important Notice:** This repository is the **only** official page to download the authentic ApexClient Jar File, excluding our official community Discord server. Do not download this client from any other third-party websites or unverified sources.

---

## Join the Community

Get access to real-time announcements, changelogs, config sharing, and direct support from the development team.

* **Official Discord Server:** [Join our Discord](https://discord.gg/T2FeVbYw4n)

------------------------------------------------------------------------------------------------------------------------------------------------------------

## What's New in v4.0

### User Interface Overhaul
* **New Config Menu System:** Replaced the old cyclic toggle system with a fully interactive dropdown menu workflow. You can now select a configuration profile, enter an editing state to tweak individual module behaviors, and save or export your setups as new configuration files.
* **RGB Color Picker Integration:** Added a full 0-255 RGB selection interface for all visual modules. Changes made in the color picker now update dynamically in real-time across both the ESP loops and primary HUD elements.
* **Category Visual Cleanups:** Updated the main ClickGUI window headers with integrated Unicode identifiers mapping cleanly to each module group.

### Combat & Gameplay Enhancements
* **Advanced Target Filtering:** Upgraded the tracking capabilities for KillAura, Triggerbot, and AimAssist. You can now sort and prioritize targets based on lowest health or closest distance.
* **AntiTeam Rewrite:** Overhauled the team-verification mechanics. The system now cross-references native Scoreboard team assignments alongside player name color formatting strings for much more accurate teammate filtering.
* **Scaffold Mode Presets:** Added distinct "Legit" and "Blatant" operational modes. Legit mode tracks your exact position relative to block edges to automate precise sneaking and block placement, while Blatant mode focuses on maximum speed.
* **AutoClicker Mode Settings:** Added options for stable CPS execution alongside a randomized frequency mode that utilizes custom minimum and maximum CPS thresholds.
* **Custom HandView / Cosmetics:** Added a localized item rendering offset module. You can now independently adjust X, Y, and Z axes, customize sword rotation angles, and tweak animation speed modifiers to change the visual arm-swing cycle.

### Network & Security Optimization
* **Packet Manipulation Tools:** Added a dedicated networking module operating via outbound Netty channel handlers to safely monitor, delay, or modify specific traffic fields before they are sent to the server.
* **Transaction Interception:** Implemented custom packet buffering loops to hold outbound confirm and keep-alive actions, letting you simulate artificial latency to balance out server-side movement prediction.
* **Silent Spoofing Overrides:** Added active field reflection overrides to modify ground-state indicators during specific fall boundaries and maintain silent vector rotations during active combat loops.

---

## Included Modules

ApexClient comes fully equipped with a highly optimized suite of modules built for performance and seamless execution.

| Combat | Movement & Physics | Visuals & Render | Player, World & Network |
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
| **AntiTeam** | | **Spotify** | **Packet Manipulation** |
| | | | **Transaction Interception** |
| | | | **Silent Spoofing** |

---

## Installation & Requirements

* **File Type:** Executable `.jar`
* **Requirement:** Forge is required to run this client. That is all you need to load the file.