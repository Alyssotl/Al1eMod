package codes.alyssotl.al1e.events;

import codes.alyssotl.al1e.commons.PlayerData;
import codes.alyssotl.al1e.commons.Settings;
import codes.alyssotl.al1e.utils.Pair;
import codes.alyssotl.al1e.utils.SimpleSender;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.util.TimerTask;

import static codes.alyssotl.al1e.Al1eMod.SESSION;

public class ChatEvents {
    /**
     * Callback when the client disconnects from the server
     *
     * @param event The event which contains the message
     */
    @SubscribeEvent
    public void onFMLNetworkClientDisconnectedToServer(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        SESSION.archive();
    }

    /**
     * Callback when a chat message is sent
     *
     * @param event The event which contains the message
     */
    @SubscribeEvent
    public boolean onClientChatReceivedEvent(ClientChatReceivedEvent event) {
        // 1. Check if enabled
        // 2. Check only for chat messages
        // 3. Do not check for player messages
        if (!Settings.ENABLED.get() || event.type != 0 || event.message.getUnformattedText().contains(":")) {
            return true;
        }

        // Get the formatted message, current player, and data
        String formattedText = event.message.getFormattedText();
        String currentPlayer = Settings.CURRENT_PLAYER.get();
        String[] data;

        switch (SESSION.currentGame) {
            case SKYWARS:
                // Fetch only yellow (system) messages
                data = formattedText.split("§e", 2);

                if (data.length >= 2) {
                    if (data[0].contains(currentPlayer)) {
                        // Player has died if a yellow message starts with their name
                        SimpleSender.sendWithSound(Settings.DEATH_MESSAGE.get());
                        SESSION.eliminateSelf();
                        return true;
                    }

                    Pair<Boolean, PlayerData> isTeammate = SESSION.isTeammate(event.message.getUnformattedText().split("\\s+")[0]);
                    if (isTeammate.first()) {
                        // Teammate has been eliminated if a yellow message starts with their name
                        SESSION.eliminateTeammate(isTeammate.second());
                    } else if (data[1].endsWith(currentPlayer + "§r§e.§r")) {
                        // Player has killed someone if a yellow message ends with their name
                        SimpleSender.sendWithSound(Settings.KILL_MESSAGE.get());
                    } else if ((data[1].contains("Winner") || data[1].contains("Winners")) && data[1].contains(currentPlayer)) {
                        // Player has won if a message contains both winner(s) and their name
                        SESSION.reset();
                        new java.util.Timer().schedule(
                                new WinTask(), 1000);
                    }
                }
                break;
            case BEDWARS:
                // Fetch only gray (system) messages
                data = formattedText.split("§7", 2);

                if (data.length >= 2) {
                    boolean isFinalKill = data[1].contains("FINAL KILL!");
                    if (isFinalKill) {
                        data[1] = data[1].replace(" §r§b§lFINAL KILL!§r", "");
                    }

                    if (data[0].contains(currentPlayer) && !data[1].contains("reconnected")) {
                        // Player has died if a gray message starts with their name and does not contain "reconnected"
                        SimpleSender.sendWithSound(Settings.DEATH_MESSAGE.get());
                        if (isFinalKill) SESSION.eliminateSelf();
                        return isFinalKill;
                    }

                    Pair<Boolean, PlayerData> isTeammate = SESSION.isTeammate(event.message.getUnformattedText().split("\\s+")[0]);
                    if (isTeammate.first()) {
                        if (data[1].contains("reconnected")) {
                            SESSION.reconnectTeammate(isTeammate.second());
                        } else if (data[1].contains("disconnected")) {
                            SESSION.eliminateTeammate(isTeammate.second());
                        } else if (isFinalKill) {
                            // Teammate has been eliminated if a gray message contains their name and FINAL KILL!
                            SESSION.eliminateTeammate(isTeammate.second());
                        } else {
                            // Teammate has been killed if a gray message contains their name and does not contain FINAL KILL!
                            SESSION.killTeammate(isTeammate.second());
                        }
                    } else if (data[1].endsWith(currentPlayer + "§r§7.") || data[1].endsWith(currentPlayer + "§r§7.§r")) {
                        // Player has killed someone if a gray message ends with their name
                        SimpleSender.sendWithSound(Settings.KILL_MESSAGE.get());
                    } else if (data[1].contains("-") && data[1].contains(currentPlayer) && !(data[1].contains("Killer"))) {
                        // Player has won if a message contains both - and their name and does not contain "Killer"
                        SESSION.reset();
                        new java.util.Timer().schedule(
                                new WinTask(), 2000);
                    }
                }
                break;
        }

        String unformattedText = event.message.getUnformattedText().trim();
        if (unformattedText.equals("Gather resources and equipment on your")) {
            SESSION.reset();
            SESSION.currentGame = GameType.SKYWARS;

            new java.util.Timer().schedule(
                    new SetupSkywarsTask(), 2000);
        } else if (unformattedText.equals("Protect your bed and destroy the enemy beds.")) {
            SESSION.reset();
            SESSION.currentGame = GameType.BEDWARS;

            new java.util.Timer().schedule(
                    new SetupBedwarsTask(), 2000);
        } else if ((unformattedText.contains("reconnected") && unformattedText.contains(currentPlayer)) || (unformattedText.equals("Your bed was destroyed so you are a spectator!")) && SESSION.currentGame == GameType.NONE) {
            SESSION.fetch();
        }

        return true;
    }

    private static class WinTask extends TimerTask {
        @Override
        public void run() {
            SimpleSender.sendWithSound(Settings.WIN_MESSAGE.get());
        }
    }

    private static class SetupSkywarsTask extends TimerTask {
        @Override
        public void run() {
            SESSION.teamColor = "§a";
            SESSION.getTeammates();
            SimpleSender.send("&fPlaying Sky Wars " + SESSION.getTeammateText());
        }
    }

    private static class SetupBedwarsTask extends TimerTask {
        @Override
        public void run() {
            SESSION.getTeamColor();
            SESSION.getTeammates();
            SimpleSender.send("&fPlaying Bed Wars as team " + SESSION.getTeamColorAsWord() + "&r&f " + SESSION.getTeammateText());
        }
    }
}
