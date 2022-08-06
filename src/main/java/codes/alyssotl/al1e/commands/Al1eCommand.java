package codes.alyssotl.al1e.commands;

import codes.alyssotl.al1e.commons.Settings;
import codes.alyssotl.al1e.utils.Reference;
import codes.alyssotl.al1e.utils.SimpleSender;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Class which handles command input for "/al1e"
 */
public class Al1eCommand implements ICommand {

    /**
     * The command name, follows the slash
     */
    public static final String COMMAND_NAME = "al1e";

    /**
     * The usage of this command
     */
    private static final String COMMAND_USAGE = "/al1e <toggle/check/event/reset/info/player/sound>";

    /**
     * The aliases of the command
     */
    private static final List<String> ALIASES = ImmutableList.of("ex");

    /**
     * The content which appears when the user TABs the command arguments
     */
    private static final List<String> TABS = Arrays.asList("toggle", "check", "event", "reset", "info", "player", "sound");

    /**
     * Returns "&cNone" if the String is empty, else returns the String
     *
     * @param string The String to check
     */
    private String ifEmptyNone(String string) {
        if (Objects.equals(string, "")) {
            return "&cNone";
        } else {
            return string;
        }
    }

    /**
     * Returns a String concatenation of all arguments in an array of Strings after, and including, the 2nd index
     *
     * @param args The array of Strings to concatenate
     */
    private String fromArgsAfterIndex(String[] args) {
        StringBuilder builder = new StringBuilder();
        for (int i = 2; i < args.length; i++) {
            builder.append(args[i]);
            builder.append(" ");
        }

        return builder.toString().trim();
    }

    /**
     * Gets the name of the command
     */
    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }

    /**
     * Gets the usage string for the command.
     *
     * @param sender The command sender that executed the command
     */
    @Override
    public String getCommandUsage(ICommandSender sender) {
        return COMMAND_USAGE;
    }

    /**
     * Returns the list of command aliases which behave the same as {@link #getCommandName()}
     *
     * @return The command aliases
     */
    @Override
    public List<String> getCommandAliases() {
        return ALIASES;
    }

    /**
     * Callback when the command is invoked
     *
     * @param sender The command sender that executed the command
     * @param args   The arguments that were passed
     */
    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        switch (args.length) {
            case 0:
                SimpleSender.send("&cIncorrect command usage. Try " + getCommandUsage(sender));
                break;
            case 1:
                switch (args[0]) {
                    case "toggle":
                        Settings.ENABLED.set(!Settings.ENABLED.get());
                        SimpleSender.send(Settings.ENABLED.get() ? "&a" + Reference.NAME + " has been enabled" : "&c" + Reference.NAME + " has been disabled");
                        break;
                    case "check":
                        Settings.SEND_UPDATES.set(!Settings.SEND_UPDATES.get());
                        SimpleSender.send(Settings.SEND_UPDATES.get() ? "&aYou will be notified on updates" : "&cYou will no longer be notified on updates");
                        break;
                    case "event":
                        SimpleSender.send("&cPlease provide an event name <kill/death/win/loss>");
                        break;
                    case "reset":
                        SimpleSender.send("&aReset to default values");
                        Settings.KILL_MESSAGE.set(Settings.DEFAULT_KILL_MESSAGE);
                        Settings.DEATH_MESSAGE.set(Settings.DEFAULT_DEATH_MESSAGE);
                        Settings.WIN_MESSAGE.set(Settings.DEFAULT_WIN_MESSAGE);
                        Settings.LOSS_MESSAGE.set(Settings.DEFAULT_LOSS_MESSAGE);
                        break;
                    case "info":
                        SimpleSender.send("&aCurrent messages per event:");
                        SimpleSender.sendNoPrefix("&r &8- &aKill: &r" + ifEmptyNone(Settings.KILL_MESSAGE.get()));
                        SimpleSender.sendNoPrefix("&r &8- &aDeath: &r" + ifEmptyNone(Settings.DEATH_MESSAGE.get()));
                        SimpleSender.sendNoPrefix("&r &8- &aWin: &r" + ifEmptyNone(Settings.WIN_MESSAGE.get()));
                        SimpleSender.sendNoPrefix("&r &8- &aLoss: &r" + ifEmptyNone(Settings.LOSS_MESSAGE.get()));
                        break;
                    case "player":
                        Settings.CURRENT_PLAYER.set(Minecraft.getMinecraft().thePlayer.getName());
                        SimpleSender.send("&aUpdated current player to " + Settings.CURRENT_PLAYER.get());
                        break;
                    case "sound":
                        SimpleSender.send("&aCurrent sound: " + Settings.MESSAGE_SOUND.get());
                        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
                        if (player == null) return; // <- For safety
                        player.playSound(Settings.MESSAGE_SOUND.get(), 1, 1);
                        break;
                    default:
                        SimpleSender.send("&cIncorrect command usage. Try &8" + getCommandUsage(sender));
                        break;
                }
                break;
            case 2:
                switch (args[0]) {
                    case "event":
                        switch (args[1]) {
                            case "kill":
                                SimpleSender.send("&aCurrent kill message: " + ifEmptyNone(Settings.KILL_MESSAGE.get()));
                                break;
                            case "death":
                                SimpleSender.send("&aCurrent death message: " + ifEmptyNone(Settings.DEATH_MESSAGE.get()));
                                break;
                            case "win":
                                SimpleSender.send("&aCurrent win message: " + ifEmptyNone(Settings.WIN_MESSAGE.get()));
                                break;
                            case "loss":
                                SimpleSender.send("&aCurrent loss message: " + ifEmptyNone(Settings.LOSS_MESSAGE.get()));
                                break;
                            default:
                                SimpleSender.send("&cUnknown event: &8" + args[1]);
                                break;
                        }
                        break;
                    case "reset":
                        switch (args[1]) {
                            case "kill":
                                Settings.KILL_MESSAGE.set(Settings.DEFAULT_KILL_MESSAGE);
                                SimpleSender.send("&aReset kill message to default");
                                break;
                            case "death":
                                Settings.DEATH_MESSAGE.set(Settings.DEFAULT_DEATH_MESSAGE);
                                SimpleSender.send("&aReset death message to default");
                                break;
                            case "win":
                                Settings.WIN_MESSAGE.set(Settings.DEFAULT_WIN_MESSAGE);
                                SimpleSender.send("&aReset win message to default");
                                break;
                            case "loss":
                                Settings.LOSS_MESSAGE.set(Settings.DEFAULT_LOSS_MESSAGE);
                                SimpleSender.send("&aReset loss message to default");
                                break;
                            case "sound":
                                Settings.MESSAGE_SOUND.set(Settings.DEFAULT_MESSAGE_SOUND);
                                SimpleSender.send("&aReset sound to default");
                                break;
                            default:
                                SimpleSender.send("&cUnknown field or event: &8" + args[1]);
                                break;
                        }
                        break;
                    case "sound":
                        Settings.MESSAGE_SOUND.set(args[1]);
                        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
                        if (player == null) return; // <- For safety
                        try {
                            player.playSound(Settings.MESSAGE_SOUND.get(), 1, 1);
                        } catch (Exception e) {
                            SimpleSender.send("&cInvalid sound: &8" + args[1]);
                        }

                        SimpleSender.send("&aUpdated current sound to " + Settings.MESSAGE_SOUND.get());
                        break;
                    default:
                        SimpleSender.send("&cIncorrect command usage. Try &8" + getCommandUsage(sender));
                        break;
                }
                break;
            default:
                if ("event".equals(args[0])) {
                    String message = fromArgsAfterIndex(args);
                    switch (args[1]) {
                        case "kill":
                            SimpleSender.send("&aShow every &8kill&a event: &r" + message);
                            Settings.KILL_MESSAGE.set(message);
                            break;
                        case "death":
                            SimpleSender.send("&aShow every &8death&a event: &r" + message);
                            Settings.DEATH_MESSAGE.set(message);
                            break;
                        case "win":
                            SimpleSender.send("&aShow every &8win&a event: &r" + message);
                            Settings.WIN_MESSAGE.set(message);
                            break;
                        case "loss":
                            SimpleSender.send("&aShow every &8loss&a event: &r" + message);
                            Settings.LOSS_MESSAGE.set(message);
                            break;
                        default:
                            SimpleSender.send("&cUnknown event: &8" + args[1]);
                            break;
                    }
                } else {
                    SimpleSender.send("&cIncorrect command usage. Try &8" + getCommandUsage(sender));
                }
                break;
        }
    }

    /**
     * Returns true if the given command sender is allowed to use this command.
     *
     * @param sender The command sender that executed the command
     */
    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return TABS;
    }

    /**
     * Return whether the specified command parameter index is a username parameter.
     *
     * @param args  The arguments that were passed
     * @param index Argument index to check
     */
    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Override
    public int compareTo(@Nonnull ICommand o) {
        return 0;
    }
}
