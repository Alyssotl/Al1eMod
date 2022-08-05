package codes.alyssotl.al1e.updater;

import codes.alyssotl.al1e.Al1eMod;
import codes.alyssotl.al1e.commands.Al1eCommand;
import codes.alyssotl.al1e.commons.Settings;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import codes.alyssotl.al1e.utils.Reference;
import codes.alyssotl.al1e.utils.SimpleSender;

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

    @SubscribeEvent
    public void onFMLNetworkClientConnectedToServer(ClientConnectedToServerEvent event) {
        if (!sent && Settings.SEND_UPDATES.get()) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    if (Al1eMod.INSTANCE.getChecker().isUpdateAvailable()) {
                        SimpleSender.send("&eAn update is available for &b%s&e! To update, do &a/%s update&e.", Reference.NAME, Al1eCommand.COMMAND_NAME);
                        sent = true;
                    }
                }
            }, 2000);
        }
    }
}
