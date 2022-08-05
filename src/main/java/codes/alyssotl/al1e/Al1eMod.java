package codes.alyssotl.al1e;

import codes.alyssotl.al1e.commons.Settings;
import codes.alyssotl.al1e.events.ChatEvents;
import codes.alyssotl.al1e.proxy.IProxy;
import codes.alyssotl.al1e.updater.UpdateManager;
import codes.alyssotl.al1e.updater.VersionChecker;
import codes.alyssotl.al1e.utils.Reference;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.reflxction.simplejson.configuration.select.SelectableConfiguration;
import net.reflxction.simplejson.json.JsonFile;

import java.io.File;

/**
 * ExampleMod: The mod template used by all of my mods
 */
@Mod(
        modid = Reference.MOD_ID,
        name = Reference.NAME,
        version = Reference.VERSION,
        acceptedMinecraftVersions = Reference.ACCEPTED_VERSIONS
)
public class Al1eMod {

    public static final Al1eMod INSTANCE = new Al1eMod();

    /**
     * Config for saving data
     */
    private static final SelectableConfiguration CONFIGURATION = SelectableConfiguration.of(
            JsonFile.of(Minecraft.getMinecraft().mcDataDir + File.separator + "config" + File.separator + "al1e.cfg"));

    // Assign proxies of the mod
    @SidedProxy(

            // Client side proxy
            clientSide = Reference.CLIENT_PROXY,

            // Server side proxy
            serverSide = Reference.SERVER_PROXY
    )
    private static IProxy PROXY;

    /**
     * The update manager
     */
    private final UpdateManager updateManager = new UpdateManager(false);

    /**
     * The version checker
     */
    private final VersionChecker checker = new VersionChecker();

    /**
     * Called before the mod is fully initialized
     * <p>
     * Registries: Initiate variables and client command registries
     *
     * @param event Forge's pre-init event
     */
    @EventHandler
    public void onFMLPreInitialization(FMLPreInitializationEvent event) {
        CONFIGURATION.register(Settings.class);
        CONFIGURATION.associate();
        Runtime.getRuntime().addShutdownHook(new Thread(CONFIGURATION::save));
        PROXY.preInit(event);
    }

    /**
     * Called when the mod has been fully initialized
     * <p>
     * Registries: Events and client-server command registries
     *
     * @param event Forge's init event
     */
    @EventHandler
    public void onFMLInitialization(FMLInitializationEvent event) {
        PROXY.init(event);
        MinecraftForge.EVENT_BUS.register(new ChatEvents());
    }

    /**
     * Called after the mod has been successfully initialized
     * <p>
     * Registries: Nothing
     *
     * @param event Forge's post init event
     */
    @EventHandler
    public void onFMLPostInitialization(FMLPostInitializationEvent event) {
        PROXY.postInit(event);
    }

    /**
     * Called after {@link FMLServerAboutToStartEvent} and before {@link FMLServerStartedEvent}.
     *
     * @param event Forge's server-starting lifecycle event
     */
    @EventHandler
    public void onFMLServerStarting(FMLServerStartingEvent event) {
        PROXY.serverStarting(event);
    }

    /**
     * The mod update manager
     *
     * @return An instance of the mod update manager
     */
    public UpdateManager getUpdateManager() {
        return updateManager;
    }

    /**
     * Returns the mod version checker
     *
     * @return The mod's version checker
     */
    public VersionChecker getChecker() {
        return checker;
    }
}
