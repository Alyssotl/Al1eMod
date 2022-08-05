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

    public static final String DEFAULT_KILL_MESSAGE = "";
    public static final String DEFAULT_DEATH_MESSAGE = "";
    public static final String DEFAULT_WIN_MESSAGE = "";
    public static final String DEFAULT_LOSE_MESSAGE = "";

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
     * Message for the lose event
     */
    @SelectKey("loseMessage")
    public static final SelectionHolder<String> LOSE_MESSAGE = new SelectionHolder<>(DEFAULT_LOSE_MESSAGE);

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
