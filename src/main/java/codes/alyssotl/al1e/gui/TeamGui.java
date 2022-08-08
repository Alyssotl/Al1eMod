package codes.alyssotl.al1e.gui;

import codes.alyssotl.al1e.commons.PlayerData;
import codes.alyssotl.al1e.events.GameType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import static codes.alyssotl.al1e.Al1eMod.SESSION;

public class TeamGui extends Gui {
    /*
     * Instance of minecraft
     */
    private final Minecraft minecraft = Minecraft.getMinecraft();

    /**
     * Callback when the overlays are rendered
     *
     * @param event The event which contains the message
     */
    @SubscribeEvent
    public void renderOverlay(RenderGameOverlayEvent event) {
        if (event.type != RenderGameOverlayEvent.ElementType.ALL) {
            return;
        }

        GL11.glPushMatrix();
        try {
            int offset = 0;
            if (SESSION.synced && SESSION.currentGame == GameType.BEDWARS) {
                for (PlayerData teammate : SESSION.teammates) {
                    if (teammate.dead && !teammate.eliminated) {
                        // minecraft.fontRendererObj.drawStringWithShadow(teammate.name + " has died [" + (5 - teammate.deadFor) + "]", 1, 1 + offset, 0xFFFFFF);
                        drawString(minecraft.fontRendererObj, teammate.name + " has died [" + (5 - teammate.deadFor) + "]", 1, 1 + offset, 0xFFFFFF);
                        offset += 10;
                    }
                }
            }
        } catch (Exception ignored) {
        } finally {
            GL11.glPopMatrix();
        }
    }
}
