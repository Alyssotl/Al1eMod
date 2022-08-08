package codes.alyssotl.al1e.utils;

import codes.alyssotl.al1e.commons.ChatColor;
import codes.alyssotl.al1e.commons.Settings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

/**
 * Class which simplifies the {@link net.minecraft.entity.player.EntityPlayer#addChatMessage(IChatComponent)} method and shortens it
 */
public class SimpleSender {

    // Mod prefix (for sending messages)
    private static final String PREFIX = ChatColor.format("&8«&5Al1e&8» ");

    /**
     * Sends a simple message to the client
     *
     * @param text Text to send, chat-formatted
     */
    public static void send(String text) {
        if (Minecraft.getMinecraft().thePlayer == null) return; // <- For safety
        StringBuilder messageBuilder = new StringBuilder();
        for (String word : text.split(" ")) {
            word = ChatColor.format(ChatColor.getLastColors(text) + word);
            messageBuilder.append(word).append(" ");
        }
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(PREFIX + ChatColor.format(messageBuilder.toString().trim())));
    }

    /**
     * Sends a simple message without the prefix to the client
     *
     * @param text Text to send, chat-formatted
     */
    public static void sendNoPrefix(String text) {
        if (Minecraft.getMinecraft().thePlayer == null) return; // <- For safety
        StringBuilder messageBuilder = new StringBuilder();
        for (String word : text.split(" ")) {
            word = ChatColor.format(ChatColor.getLastColors(text) + word);
            messageBuilder.append(word).append(" ");
        }
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(ChatColor.format(messageBuilder.toString().trim())));
    }

    /**
     * Sends a simple message without the prefix to the client with a sound effect
     *
     * @param text Text to send, chat-formatted
     */
    public static void sendWithSound(String text) {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        if (player == null) return; // <- For safety
        StringBuilder messageBuilder = new StringBuilder();
        for (String word : text.split(" ")) {
            word = ChatColor.format(ChatColor.getLastColors(text) + word);
            messageBuilder.append(word).append(" ");
        }

        String message = messageBuilder.toString().trim();
        if (message.length() == 0) return; // <- For safety
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(PREFIX + ChatColor.format(messageBuilder.toString().trim())));
        player.playSound(Settings.MESSAGE_SOUND.get(), 1.0F, 1.0F);
    }
}
