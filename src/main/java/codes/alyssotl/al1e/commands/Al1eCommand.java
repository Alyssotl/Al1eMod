/*
 * * Copyright 2019 github.com/ReflxctionDev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package codes.alyssotl.al1e.commands;

import codes.alyssotl.al1e.Al1eMod;
import codes.alyssotl.al1e.commons.Multithreading;
import codes.alyssotl.al1e.commons.Settings;
import codes.alyssotl.al1e.utils.Reference;
import codes.alyssotl.al1e.utils.SimpleSender;
import com.google.common.collect.ImmutableList;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Class which handles command input for "/example"
 */
public class Al1eCommand implements ICommand {

    /**
     * The command name, follows the slash
     */
    public static final String COMMAND_NAME = "al1e";

    /**
     * The usage of this command
     */
    private static final String COMMAND_USAGE = "/al1e <toggle/check/update/event/reset/info>";

    /**
     * The aliases of the command
     */
    private static final List<String> ALIASES = ImmutableList.of("ex");

    /**
     * The content which appears when the user TABs the command arguments
     */
    private static final List<String> TABS = Arrays.asList("toggle", "check", "update", "event", "reset", "info");

    private String ifEmptyNone(String string) {
        if (Objects.equals(string, "")) {
            return "&cNone";
        } else {
            return string;
        }
    }

    private String fromArgsAfterIndex(String[] args, int index) {
        StringBuilder builder = new StringBuilder();
        for (int i = index; i < args.length; i++) {
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
                        SimpleSender.send(Settings.ENABLED.get() ? "&a%s has been enabled" : "&c%s has been disabled", Reference.NAME);
                        break;
                    case "update":
                        if (Al1eMod.INSTANCE.getChecker().isUpdateAvailable()) {
                            Multithreading.runAsync(() -> {
                                if (Al1eMod.INSTANCE.getUpdateManager().updateMod()) {
                                    SimpleSender.send("&aSuccessfully updated the mod! Restart your game to see changes.");
                                } else {
                                    SimpleSender.send("&cFailed to update mod! To avoid any issues, delete the mod jar and install it manually again.");
                                }
                            });
                        } else {
                            SimpleSender.send("&cNo updates found. You're up to date!");
                        }
                        break;
                    case "check":
                        Settings.SEND_UPDATES.set(!Settings.SEND_UPDATES.get());
                        SimpleSender.send(Settings.SEND_UPDATES.get() ? "&aYou will be notified on updates" : "&cYou will no longer be notified on updates");
                        break;
                    case "event":
                        SimpleSender.send("&cPlease provide an event name");
                        break;
                    case "reset":
                        SimpleSender.send("&aReset to default values");
                        Settings.KILL_MESSAGE.set(Settings.DEFAULT_KILL_MESSAGE);
                        Settings.DEATH_MESSAGE.set(Settings.DEFAULT_DEATH_MESSAGE);
                        Settings.WIN_MESSAGE.set(Settings.DEFAULT_WIN_MESSAGE);
                        Settings.LOSE_MESSAGE.set(Settings.DEFAULT_LOSE_MESSAGE);
                        Settings.OWN_BED_BROKEN_MESSAGE.set(Settings.DEFAULT_OWN_BED_BROKEN_MESSAGE);
                        Settings.OTHER_BED_BROKEN_MESSAGE.set(Settings.DEFAULT_OTHER_BED_BROKEN_MESSAGE);
                        break;
                    case "info":
                        SimpleSender.send("&aCurrent messages per event:");
                        SimpleSender.sendNoPrefix("&r &8- &aKill: &r%s", ifEmptyNone(Settings.KILL_MESSAGE.get()));
                        SimpleSender.sendNoPrefix("&r &8- &aDeath: &r%s", ifEmptyNone(Settings.DEATH_MESSAGE.get()));
                        SimpleSender.sendNoPrefix("&r &8- &aWin: &r%s", ifEmptyNone(Settings.WIN_MESSAGE.get()));
                        SimpleSender.sendNoPrefix("&r &8- &aLose: &r%s", ifEmptyNone(Settings.LOSE_MESSAGE.get()));
                        SimpleSender.sendNoPrefix("&r &8- &aOwn bed broken: &r%s", ifEmptyNone(Settings.OWN_BED_BROKEN_MESSAGE.get()));
                        SimpleSender.sendNoPrefix("&r &8- &aOther bed broken: &r%s", ifEmptyNone(Settings.OTHER_BED_BROKEN_MESSAGE.get()));
                        break;
                    default:
                        SimpleSender.send("&cIncorrect command usage. Try &8" + getCommandUsage(sender));
                        break;
                }
                break;
            case 2:
                if ("event".equals(args[0])) {
                    SimpleSender.send("&cPlease provide an event message");
                } else {
                    SimpleSender.send("&cIncorrect command usage. Try &8" + getCommandUsage(sender));
                }
                break;
            default:
                if ("event".equals(args[0])) {
                    String message = fromArgsAfterIndex(args, 2);
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
                        case "lose":
                            SimpleSender.send("&aShow every &8lose&a event: &r" + message);
                            Settings.LOSE_MESSAGE.set(message);
                            break;
                        case "ownbedbroken":
                            SimpleSender.send("&aShow every &8own bed broken&a event: &r" + message);
                            Settings.OWN_BED_BROKEN_MESSAGE.set(message);
                            break;
                        case "otherbedbroken":
                            SimpleSender.send("& every &8other bed broken&a event: &r" + message);
                            Settings.OTHER_BED_BROKEN_MESSAGE.set(message);
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
    public int compareTo(ICommand o) {
        return 0;
    }
}
