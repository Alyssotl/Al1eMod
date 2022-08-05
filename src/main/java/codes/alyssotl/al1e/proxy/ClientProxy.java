package codes.alyssotl.al1e.proxy;

import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.*;
import codes.alyssotl.al1e.Al1eMod;
import codes.alyssotl.al1e.commands.Al1eCommand;
import codes.alyssotl.al1e.commons.Multithreading;
import codes.alyssotl.al1e.commons.Settings;
import codes.alyssotl.al1e.updater.NotificationSender;

public class ClientProxy implements IProxy {

    /**
     * Called before the mod is fully initialized
     * <p>
     * Registries: Initiate variables and client command registries
     *
     * @param event Forge's pre-init event
     */
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        if (Settings.SEND_UPDATES.get()) {
            Multithreading.runAsync(() -> Al1eMod.INSTANCE.getChecker().updateState());
        }
        ClientCommandHandler.instance.registerCommand(new Al1eCommand());
    }

    /**
     * Called when the mod has been fully initialized
     * <p>
     * Registries: Events and client-server command registries
     *
     * @param event Forge's init event
     */
    @Override
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new NotificationSender());
    }

    /**
     * Called after the mod has been successfully initialized
     * <p>
     * Registries: Nothing
     *
     * @param event Forge's post init event
     */
    @Override
    public void postInit(FMLPostInitializationEvent event) {
    }

    /**
     * Called after {@link FMLServerAboutToStartEvent} and before {@link FMLServerStartedEvent}.
     * <p>
     * Registries: Server commands
     *
     * @param event Forge's server starting event
     */
    @Override
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new Al1eCommand());
    }

}
