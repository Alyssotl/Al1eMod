package codes.alyssotl.al1e.commons;

import codes.alyssotl.al1e.events.GameType;
import codes.alyssotl.al1e.utils.Pair;
import codes.alyssotl.al1e.utils.SimpleSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A session of a specific Hypixel game mode
 */
public class GameSession {
    /**
     * The current game type the user is in
     */
    public GameType currentGame = GameType.NONE;

    /**
     * The player's team color
     */
    public String teamColor = "";

    /**
     * The amount of teammates left alive
     */
    public int playersLeft;

    /**
     * If the current game session is synced with the real time events
     */
    public boolean synced = true;

    /**
     * List of teammates left alive
     */
    public Collection<PlayerData> teammates = new ArrayList<>();

    /**
     * Reset state
     */
    public void reset() {
        currentGame = GameType.NONE;
        teamColor = "";
        playersLeft = 0;
        teammates = new ArrayList<>();
        synced = true;
    }

    /**
     * Handle kill for teammate
     */
    public void killTeammate(PlayerData teammate) {
        teammate.respawn();
    }

    /**
     * Handle elimination for teammate
     */
    public void eliminateTeammate(PlayerData teammate) {
        teammate.eliminated = true;
        eliminate();
    }

    /**
     * Handle reconnection for teammate
     */
    public void reconnectTeammate(PlayerData teammate) {
        teammate.eliminated = false;
        playersLeft++;
    }
    
    /**
     * Handle final kill for self
     */
    public void eliminateSelf() {
        eliminate();
    }

    /**
     * Elimination logic
     */
    private void eliminate() {
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
            case "§f":
                return "&f&lWhite";
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
                return "&f&lUnknown";
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

    /*
     * Get teammate names
     */
    public Pair<Boolean, PlayerData> isTeammate(String name) {
        for (PlayerData teammate : teammates) {
            if (teammate.name.equals(name)) {
                return new Pair<>(true, teammate);
            }
        }
        return new Pair<>(false, null);
    }

    /**
     * Gets the player's current team color
     */
    public void getTeamColor() {
        Minecraft minecraft = Minecraft.getMinecraft();
        Collection<NetworkPlayerInfo> players = minecraft.getNetHandler().getPlayerInfoMap();
        String name = Settings.CURRENT_PLAYER.get();
        for (NetworkPlayerInfo playerInfo : players) {
            if (playerInfo.getGameProfile().getName().contains(name)) {
                // replace name with "", trim, get the last two characters, and replace § with &
                String color = minecraft.ingameGUI.getTabList().getPlayerName(playerInfo).replace(name, "").trim();
                teamColor = color.substring(color.length() - 2);
            }
        }
    }

    /**
     * Gets your current teammates left
     */
    public void getTeammates() {
        Minecraft minecraft = Minecraft.getMinecraft();
        if (minecraft.thePlayer == null) return; // <- For safety
        Collection<NetworkPlayerInfo> players = minecraft.getNetHandler().getPlayerInfoMap();
        teammates = new ArrayList<>();
        for (NetworkPlayerInfo playerInfo : players) {
            String name = minecraft.ingameGUI.getTabList().getPlayerName(playerInfo);
            System.out.println("[AL1E] Name: " + name);
            System.out.println("[AL1E] Team color: " + teamColor);
            if (name.contains(teamColor)) {
                playersLeft++;
                System.out.println("[AL1E] Player found: " + name + " now " + playersLeft + " players left");
                if (!name.contains(minecraft.thePlayer.getName())) {
                    System.out.println("[AL1E] Adding teammate: " + name.split("\\s+")[1].replaceAll(teamColor, "").replaceAll("§r", ""));
                    teammates.add(new PlayerData(name.split("\\s+")[1].replaceAll(teamColor, "").replaceAll("§r", "")));
                }
            }
        }

        for (PlayerData teammate : teammates) {
            System.out.println("[AL1E] Teammates: " + teammate.name);
        }
    }

    /*
     * Archive the current game session
     */
    public void archive() {
        pushToSettings();
    }

    /*
     * Has the player been respawned while syncing
     */
    public boolean playerFound = false;

    /*
     * Get found players from an array of names
     */
    public Collection<String> getFoundPlayers(Minecraft minecraft) {
        Collection<NetworkPlayerInfo> players = minecraft.getNetHandler().getPlayerInfoMap();
        Collection<String> foundPlayers = new ArrayList<>();
        for (NetworkPlayerInfo playerInfo : players) {
            String name = minecraft.ingameGUI.getTabList().getPlayerName(playerInfo);
            if (name.contains(teamColor)) {
                String[] split = name.split("\\s+");
                if (split.length <= 1) {
                    // Found a spectator
                    continue;
                }

                System.out.println("[AL1E] Player found: " + split[1].replaceAll(teamColor, "").replaceAll("§r", ""));
                if (!name.contains(minecraft.thePlayer.getName())) {
                    foundPlayers.add(split[1].replaceAll(teamColor, "").replaceAll("§r", ""));
                } else {
                    System.out.println("[AL1E] Found self");
                    playerFound = true;
                }
            }
        }
        return foundPlayers;
    }

    private static Collection<String> foundPlayers;

    /*
     * Fetch the current game session
     */
    public void fetch() {
        synced = false;
        popFromSettings();
        if (currentGame == GameType.NONE) {
            synced = true;
            return;
        }

        SimpleSender.send("Restoring game session");
        System.out.println("Players left: " + playersLeft);
        System.out.println("[AL1E] Loaded teammates: ");
        for (PlayerData teammate : teammates) {
            System.out.println("[AL1E] - " + teammate.name);
        }

        // Loop over all teammates and check if they are alive
        final Minecraft minecraft = Minecraft.getMinecraft();
        foundPlayers = getFoundPlayers(minecraft);
        System.out.println("[AL1E] Found players [1/2]: " + foundPlayers);

        // Do the same thing over 5 seconds
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (currentGame == GameType.NONE) return;
                foundPlayers = getFoundPlayers(minecraft);
                System.out.println("[AL1E] Found players [2/2]: " + foundPlayers);

                // Eliminate all teammates which are not in the found list
                for (PlayerData teammate : teammates) {
                    if (!foundPlayers.contains(teammate.name)) {
                        teammate.eliminated = true;
                        playersLeft--;
                        System.out.println("[AL1E] Eliminating teammate: " + teammate.name + " now " + playersLeft + " players left");
                    }
                }

                // If the player is not found, eliminate self
                if (!playerFound) {
                    playersLeft--;
                    System.out.println("[AL1E] Eliminating self: now " + playersLeft + " players left");
                }

                // Print all eliminated teammates while they were gone to the player
                System.out.println("[AL1E] Game type: " + currentGame);
                if (currentGame != GameType.NONE) {
                    if (foundPlayers.size() != teammates.size()) {
                        if (foundPlayers.size() == 0) {
                            SimpleSender.send("&c&lNo teammates found");
                        } else {
                            if (playerFound) {
                                foundPlayers.add(minecraft.thePlayer.getName());
                            }

                            SimpleSender.send("&e&lActive teammates: &r&5" + String.join("&f, &5", foundPlayers));
                        }
                    } else {
                        SimpleSender.send("&a&lAll teammates are here");
                    }
                }

                // Check if the game has been lost
                if (playersLeft <= 0) {
                    SimpleSender.sendWithSound(Settings.LOSS_MESSAGE.get());
                    reset();
                } else {
                    // Synced
                    synced = true;
                }
            }
        }, 5000);
    }

    /**
     * Moves the values from the session to the settings
     */
    private void pushToSettings() {
        // Add to settings
        Settings.SESSION_GAME_TYPE.set(currentGame);
        Settings.SESSION_TEAM_COLOR.set(teamColor);
        Settings.SESSION_PLAYERS_LEFT.set(playersLeft);
        Collection<String> teammateNames = new ArrayList<>();
        for (PlayerData teammate : teammates) {
            if (!teammate.eliminated) teammateNames.add(teammate.name);
        }
        Settings.SESSION_TEAMMATES.set(teammateNames);

        // Remove from session
        reset();
        System.out.println("[AL1E] Pushed:\n - Game type: " + Settings.SESSION_GAME_TYPE.get() + "\n - Team color: " + Settings.SESSION_TEAM_COLOR.get() + "\n - Players left: " + Settings.SESSION_PLAYERS_LEFT.get() + "\n - Teammates: " + Settings.SESSION_TEAMMATES.get());
    }

    /**
     * Moves the values from the settings to the session
     */
    private void popFromSettings() {
        // Add to session
        currentGame = Settings.SESSION_GAME_TYPE.get();
        teamColor = Settings.SESSION_TEAM_COLOR.get();
        playersLeft = Settings.SESSION_PLAYERS_LEFT.get();
        Collection<PlayerData> teammateData = new ArrayList<>();
        for (String teammateName : Settings.SESSION_TEAMMATES.get()) {
            teammateData.add(new PlayerData(teammateName));
        }
        teammates = teammateData;

        // Remove from settings
        Settings.SESSION_GAME_TYPE.set(null);
        Settings.SESSION_TEAM_COLOR.set(null);
        Settings.SESSION_PLAYERS_LEFT.set(null);
        Settings.SESSION_TEAMMATES.set(null);
        System.out.println("[AL1E] Popped:\n - Game type: " + currentGame + "\n - Team color: " + teamColor + "\n - Players left: " + playersLeft + "\n - Teammates: " + teammates);
    }
}
