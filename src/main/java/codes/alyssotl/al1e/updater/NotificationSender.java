package codes.alyssotl.al1e.updater;

import codes.alyssotl.al1e.Al1eMod;
import codes.alyssotl.al1e.commons.Settings;
import codes.alyssotl.al1e.utils.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Listener which sends the player a notification update
 */
public class NotificationSender {

    /**
     * Whether the notification was already sent or not
     */
    private boolean sent;

    /**
     * Callback when the client connects to the server
     *
     * @param event The event which contains the message
     */
    @SubscribeEvent
    public void onFMLNetworkClientConnectedToServer(ClientConnectedToServerEvent event) {
        if (!sent && Settings.SEND_UPDATES.get()) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    if (Al1eMod.INSTANCE.getChecker().isUpdateAvailable()) {
                        ChatComponentText testChat = new ChatComponentText("«!» An update is available for " + Reference.REPOSITORY_NAME + "! Click to update!");
                        testChat.getChatStyle().setChatClickEvent(new ClickEvent(
                                ClickEvent.Action.OPEN_URL, "https://github.com/Alyssotl/" + Reference.REPOSITORY_NAME + "/" + "releases" + "/" + Al1eMod.INSTANCE.getChecker().latestVersion
                        ));
                        testChat.getChatStyle().setColor(EnumChatFormatting.DARK_PURPLE);
                        testChat.getChatStyle().setBold(true);

                        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
                        if (player == null) return; // <- For safety
                        player.addChatMessage(testChat);
                        player.playSound("random.orb", 1, 1);
                        sent = true;
                    }
                }
            }, 2000);
        }
    }
}
