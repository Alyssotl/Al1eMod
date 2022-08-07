package codes.alyssotl.al1e.events;

import codes.alyssotl.al1e.commons.Settings;
import codes.alyssotl.al1e.utils.SimpleSender;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.util.TimerTask;

public class ChatEvents {
    /**
     * Data about the current game
     */
    public GameSession session = new GameSession();

    /**
     * Callback when the client disconnects from the server
     *
     * @param event The event which contains the message
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onFMLNetworkClientDisconnectedToServer(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        session.pushToSettings();
    }

    /**
     * Callback when a chat message is sent
     *
     * @param event The event which contains the message
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onClientChatReceivedEvent(ClientChatReceivedEvent event) {
        // Check if enabled
        if (!Settings.ENABLED.get()) {
            return;
        }

        // Check only for chat messages
        if (event.type != 0) {
            return;
        }

        // Do not check for player messages
        if (event.message.getUnformattedText().contains(":")) {
            return;
        }

        // Get the formatted message
        String formattedText = event.message.getFormattedText();

        // Print the message
        String currentPlayer = Settings.CURRENT_PLAYER.get();
        if (session.currentGame == GameType.SKYWARS) {
            // Fetch only yellow (system) messages
            String[] data = formattedText.split("§e", 2);

            if (data.length != 1) {
                if (data[0].contains(currentPlayer)) {
                    // Player has died if a yellow message starts with their name
                    SimpleSender.sendWithSound(Settings.DEATH_MESSAGE.get());
                    session.eliminateTeammate();
                } else if (session.teammates.contains(event.message.getUnformattedText().split("\\s+")[0])) {
                    // Teammate has been eliminated if a yellow message starts with their name
                    session.eliminateTeammate();
                } else if (data[1].endsWith(currentPlayer + "§r§e.§r")) {
                    // Player has killed someone if a yellow message ends with their name
                    SimpleSender.sendWithSound(Settings.KILL_MESSAGE.get());
                } else if ((data[1].contains("Winner") || data[1].contains("Winners")) && data[1].contains(currentPlayer)) {
                    // Player has won if a message contains both winner(s) and their name
                    session.reset();
                    new java.util.Timer().schedule(
                            new TimerTask() {
                                @Override
                                public void run() {
                                    SimpleSender.sendWithSound(Settings.WIN_MESSAGE.get());
                                }
                            }, 1000);
                }
            }
        } else if (session.currentGame == GameType.BEDWARS) {
            // Fetch only gray (system) messages
            String[] data = formattedText.split("§7", 2);

            if (data.length != 1) {
                boolean isFinalKill = data[1].contains("FINAL KILL!");
                if (isFinalKill) {
                    data[1] = data[1].replace(" §r§b§lFINAL KILL!§r", "");
                } else {
                    data[1] = data[1].substring(0, data[1].length() - 2);
                }

                if (data[0].contains(currentPlayer) && !data[1].contains("reconnected")) {
                    // Player has died if a gray message starts with their name and does not contain "reconnected"
                    SimpleSender.sendWithSound(Settings.DEATH_MESSAGE.get());
                    if (isFinalKill) session.eliminateTeammate();
                } else if (isFinalKill && data[0].contains(session.teamColor)) {
                    // Teammate has been eliminated if a gray message contains their name and FINAL KILL!
                    session.eliminateTeammate();
                } else if (data[1].endsWith(currentPlayer + "§r§7.")) {
                    // Player has killed someone if a gray message ends with their name
                    SimpleSender.sendWithSound(Settings.KILL_MESSAGE.get());
                } else if (data[1].contains("-") && data[1].contains(currentPlayer) && !(data[1].contains("Killer"))) {
                    // Player has won if a message contains both - and their name and does not contain "Killer"
                    session.reset();
                    new java.util.Timer().schedule(
                            new TimerTask() {
                                @Override
                                public void run() {
                                    SimpleSender.sendWithSound(Settings.WIN_MESSAGE.get());
                                }
                            }, 1000);
                }
            }
        } else {
            if (formattedText.endsWith("§r§f§lSkyWars§r")) {
                session.currentGame = GameType.SKYWARS;

                new java.util.Timer().schedule(
                        new TimerTask() {
                            @Override
                            public void run() {
                                session.teamColor = "§a";
                                session.getTeammatesLeft();
                                SimpleSender.send("&fPlaying Sky Wars " + session.getTeammateText());
                            }
                        }, 2000);
            } else if (formattedText.endsWith("§r§f§lBed Wars§r")) {
                session.currentGame = GameType.BEDWARS;

                new java.util.Timer().schedule(
                        new TimerTask() {
                            @Override
                            public void run() {
                                session.getTeamColor();
                                session.getTeammatesLeft();
                                SimpleSender.send("&fPlaying Bed Wars as team " + session.getTeamColorAsWord() + "&r&f " + session.getTeammateText());
                            }
                        }, 2000);
            } else if (formattedText.contains(currentPlayer) && formattedText.contains("reconnected")) {
                session.popFromSettings();
            }
        }
    }
}
