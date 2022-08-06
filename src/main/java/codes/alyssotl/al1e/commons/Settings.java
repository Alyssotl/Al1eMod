package codes.alyssotl.al1e.commons;

import net.reflxction.simplejson.configuration.select.SelectKey;
import net.reflxction.simplejson.configuration.select.SelectionHolder;

/**
 * A class with all commons as constants
 */
public class Settings {

    /**
     * Whether the mod is enabled or not
     */
    @SelectKey("enabled")
    public static final SelectionHolder<Boolean> ENABLED = new SelectionHolder<>(true);

    /**
     * The default message for the kill event
     */
    public static final String DEFAULT_KILL_MESSAGE = "";

    /**
     * The default message for the death event
     */
    public static final String DEFAULT_DEATH_MESSAGE = "";

    /**
     * The default message for the win event
     */
    public static final String DEFAULT_WIN_MESSAGE = "";

    /**
     * The default message for the loss event
     */
    public static final String DEFAULT_LOSS_MESSAGE = "";

    /**
     * The default message sound
     */
    public static final String DEFAULT_MESSAGE_SOUND = "mob.cat.meow";

    /**
     * Message for the kill event
     */
    @SelectKey("killMessage")
    public static final SelectionHolder<String> KILL_MESSAGE = new SelectionHolder<>(DEFAULT_KILL_MESSAGE);

    /**
     * Message for the death event
     */
    @SelectKey("deathMessage")
    public static final SelectionHolder<String> DEATH_MESSAGE = new SelectionHolder<>(DEFAULT_DEATH_MESSAGE);

    /**
     * Message for the win event
     */
    @SelectKey("winMessage")
    public static final SelectionHolder<String> WIN_MESSAGE = new SelectionHolder<>(DEFAULT_WIN_MESSAGE);

    /**
     * Message for the loss event
     */
    @SelectKey("lossMessage")
    public static final SelectionHolder<String> LOSS_MESSAGE = new SelectionHolder<>(DEFAULT_LOSS_MESSAGE);

    /**
     * Whether the mod should send updates or check for them
     */
    @SelectKey("sendUpdates")
    public static final SelectionHolder<Boolean> SEND_UPDATES = new SelectionHolder<>(true);

    /**
     * Name of the current player
     */
    @SelectKey("currentPlayer")
    public static final SelectionHolder<String> CURRENT_PLAYER = new SelectionHolder<>("Al1e");

    /**
     * Current message sound
     */
    @SelectKey("currentSound")
    public static final SelectionHolder<String> MESSAGE_SOUND = new SelectionHolder<>("mob.cat.meow");
}
