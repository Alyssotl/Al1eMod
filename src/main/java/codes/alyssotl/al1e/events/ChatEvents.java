package codes.alyssotl.al1e.events;

import codes.alyssotl.al1e.commons.Settings;
import codes.alyssotl.al1e.utils.SimpleSender;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChatEvents {
    /**
     * The current game type the user is in
     */
    public GameType currentGame;

    /**
     * Callback when a chat message is sent
     *
     * @param event The event which contains the message
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void ClientChatReceivedEvent(net.minecraftforge.client.event.ClientChatReceivedEvent event) {
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
        if (currentGame == GameType.SKYWARS) {
            // Fetch only yellow (system) messages
            String[] data = formattedText.split("§e", 2);

            if (data.length != 1) {
                if (data[0].contains(currentPlayer)) {
                    // Player has died if a yellow message starts with their name
                    SimpleSender.sendWithSound(Settings.DEATH_MESSAGE.get());
                    Minecraft.getMinecraft().thePlayer.playSound("random.anvil_break", 1.0F, 1.0F);

                    // ! Make this work for non-solo games
                    SimpleSender.sendWithSound(Settings.LOSS_MESSAGE.get());
                } else if (data[1].endsWith(currentPlayer + "§r§e.§r")) {
                    // Player has killed someone if a yellow message ends with their name
                    SimpleSender.sendWithSound(Settings.KILL_MESSAGE.get());
                } else if ((data[1].contains("Winner") || data[1].contains("Winners")) && data[1].contains(currentPlayer)) {
                    // Player has won if a message contains both winner(s) and their name
                    SimpleSender.sendWithSound(Settings.WIN_MESSAGE.get());
                }

                return;
            }
        } else if (currentGame == GameType.BEDWARS) {
            // Fetch only gray (system) messages
            String[] data = formattedText.split("§7", 2);

            if (data.length != 1) {
                boolean isFinalKill = data[1].contains("FINAL KILL!");
                data[1] = data[1].replace(" §r§b§lFINAL KILL!§r", "");

                if (data[0].contains(currentPlayer)) {
                    // Player has died if a gray message starts with their name
                    SimpleSender.sendWithSound(Settings.DEATH_MESSAGE.get());

                    // ! Make this work for non-solo games
                    if (isFinalKill) {
                        SimpleSender.sendWithSound(Settings.LOSS_MESSAGE.get());
                    }
                } else if (data[1].endsWith(currentPlayer + "§r§7.§r")) {
                    // Player has killed someone if a gray message ends with their name
                    SimpleSender.sendWithSound(Settings.KILL_MESSAGE.get());
                } else if (data[1].contains("§7-") && data[1].contains(currentPlayer) && !(data[1].contains("Killer"))) {
                    // Player has won if a message contains both §7- and their name and does not contain "Killer"
                    SimpleSender.sendWithSound(Settings.WIN_MESSAGE.get());
                }

                return;
            }
        }

        if (formattedText.endsWith("§r§f§lSkyWars§r")) {
            this.currentGame = GameType.SKYWARS;
            /* new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    SimpleSender.sendWithSound("Starting a new SkyWars session.");
                }
            }, 100); */
        } else if (formattedText.endsWith("§r§f§lBed Wars§r")) {
            this.currentGame = GameType.BEDWARS;
            /* new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    SimpleSender.sendWithSound("Starting a new Bed Wars session.");
                }
            }, 100); */
        }
    }
}
