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
    public static final String DEFAULT_OWN_BED_BROKEN_MESSAGE = "";
    public static final String DEFAULT_OTHER_BED_BROKEN_MESSAGE = "";

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
     * Message for the ownBedBroken event
     */
    @SelectKey("ownBedBrokenMessage")
    public static final SelectionHolder<String> OWN_BED_BROKEN_MESSAGE = new SelectionHolder<>(DEFAULT_OWN_BED_BROKEN_MESSAGE);

    /**
     * Message for the otherBedBroken event
     */
    @SelectKey("otherBedBrokenMessage")
    public static final SelectionHolder<String> OTHER_BED_BROKEN_MESSAGE = new SelectionHolder<>(DEFAULT_OTHER_BED_BROKEN_MESSAGE);

    /**
     * Whether the mod should send updates or check for them
     */
    @SelectKey("sendUpdates")
    public static final SelectionHolder<Boolean> SEND_UPDATES = new SelectionHolder<>(true);
}
