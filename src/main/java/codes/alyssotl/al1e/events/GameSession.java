package codes.alyssotl.al1e.events;

import codes.alyssotl.al1e.commons.Settings;
import codes.alyssotl.al1e.utils.SimpleSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A session of a specific Hypixel game mode
 */
public class GameSession {
    /**
     * The current game type the user is in
     */
    public GameType currentGame;

    /**
     * The player's team color
     */
    public String teamColor;

    /**
     * The amount of teammates left alive
     */
    public int playersLeft;

    /**
     * List of teammates left alive
     */
    public Collection<String> teammates = new ArrayList<>();

    /**
     * Reset state
     */
    public void reset() {
        currentGame = null;
        teamColor = null;
        playersLeft = 0;
        teammates = new ArrayList<>();
    }

    /**
     * Handle final kill
     */
    public void eliminateTeammate() {
        playersLeft--;
        if (playersLeft <= 0) {
            SimpleSender.sendWithSound(Settings.LOSS_MESSAGE.get());
            reset();
        }
    }

    /**
     * Team color to word converter
     */
    public String getTeamColorAsWord() {
        switch (teamColor) {
            case "§a":
                return "&a&lGreen";
            case "§b":
                return "&b&lAqua";
            case "§c":
                return "&c&lRed";
            case "§d":
                return "&d&lLight Purple";
            case "§e":
                return "&e&lYellow";
            case "§0":
                return "&0&lBlack";
            case "§1":
                return "&1&lDark Blue";
            case "§2":
                return "&2&lDark Green";
            case "§3":
                return "&3&lDark Aqua";
            case "§4":
                return "&4&lDark Red";
            case "§5":
                return "&5&lDark Purple";
            case "§6":
                return "&6&lGold";
            case "§7":
                return "&7&lGray";
            case "§8":
                return "&8&lDark Gray";
            case "§9":
                return "&9&lBlue";
            default:
                return "&f&lWhite";
        }
    }

    /**
     * Get teammate text
     */
    public String getTeammateText() {
        if (playersLeft - 1 == 0) {
            return "alone";
        } else if (playersLeft - 1 == 1) {
            return "with 1 teammate";
        } else {
            return "with " + (playersLeft - 1) + " teammates";
        }
    }

    /**
     * Gets the player's current team color
     */
    public void getTeamColor() {
        Minecraft minecraft = Minecraft.getMinecraft();
        if (minecraft.thePlayer == null) return; // <- For safety
        String name = minecraft.thePlayer.getName();
        Collection<NetworkPlayerInfo> players = minecraft.getNetHandler().getPlayerInfoMap();
        for (NetworkPlayerInfo playerInfo : players) {
            try {
                if (playerInfo.getGameProfile().getName().contains(name)) {
                    // replace name with "", trim, get the last two characters, and replace § with &
                    String color = minecraft.ingameGUI.getTabList().getPlayerName(playerInfo).replace(name, "").trim();
                    teamColor = color.substring(color.length() - 2);
                }
            } catch (Exception e) {
                System.out.println("[DEBUG] Exception: " + e.getMessage());
            }
        }
    }

    /**
     * Gets your current teammates left
     */
    public void getTeammatesLeft() {
        Minecraft minecraft = Minecraft.getMinecraft();
        if (minecraft.thePlayer == null) return; // <- For safety
        Collection<NetworkPlayerInfo> players = minecraft.getNetHandler().getPlayerInfoMap();
        teammates = new ArrayList<>();
        for (NetworkPlayerInfo playerInfo : players) {
            try {
                String name = minecraft.ingameGUI.getTabList().getPlayerName(playerInfo);
                if (name.contains(teamColor)) {
                    playersLeft++;
                    if (!name.contains(minecraft.thePlayer.getName())) {
                        teammates.add(name.split("\\s+")[1].replaceAll(teamColor, ""));
                    }
                }
            } catch (Exception e) {
                System.out.println("[DEBUG] Exception: " + e.getMessage());
            }
        }
    }

    /**
     * Moves the values from the session to the settings
     */
    public void pushToSettings() {
        Settings.SESSION_GAME_TYPE.set(currentGame);
        Settings.SESSION_TEAM_COLOR.set(teamColor);
        Settings.SESSION_PLAYERS_LEFT.set(playersLeft);
        Settings.SESSION_TEAMMATES.set(teammates);
        currentGame = null;
        teamColor = null;
        playersLeft = 0;
        teammates = null;
        System.out.println("[DEBUG] Pushed:\n - Game type: " + Settings.SESSION_GAME_TYPE.get() + "\n - Team color: " + Settings.SESSION_TEAM_COLOR.get() + "\n - Players left: " + Settings.SESSION_PLAYERS_LEFT.get() + "\n - Teammates: " + Settings.SESSION_TEAMMATES.get());
    }

    /**
     * Moves the values from the settings to the session
     */
    public void popFromSettings() {
        currentGame = Settings.SESSION_GAME_TYPE.get();
        teamColor = Settings.SESSION_TEAM_COLOR.get();
        playersLeft = Settings.SESSION_PLAYERS_LEFT.get();
        teammates = Settings.SESSION_TEAMMATES.get();
        Settings.SESSION_GAME_TYPE.set(null);
        Settings.SESSION_TEAM_COLOR.set(null);
        Settings.SESSION_PLAYERS_LEFT.set(null);
        Settings.SESSION_TEAMMATES.set(null);
        System.out.println("[DEBUG] Popped:\n - Game type: " + currentGame + "\n - Team color: " + teamColor + "\n - Players left: " + playersLeft + "\n - Teammates: " + teammates);
    }
}
