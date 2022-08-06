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
package codes.alyssotl.al1e.updater;

import codes.alyssotl.al1e.utils.Reference;
import net.reflxction.simplejson.json.JsonURLReader;

import java.io.IOException;

import static java.lang.Double.parseDouble;

public class VersionChecker {
    /**
     * The latest version of the mod
     */
    public double latestVersion = parseDouble(Reference.VERSION);

    /**
     * Whether an update is available or not
     */
    private boolean updateAvailable = false;

    /**
     * The current mod version, not necessarily the latest
     */
    private final double version = parseDouble(Reference.VERSION);

    /**
     * The JSON file to get the latest version
     */
    private final String checkerURL;

    /**
     * Initiates a new version checker
     */
    public VersionChecker() {
        checkerURL = "https://raw.githubusercontent.com/Alyssotl/" + Reference.REPOSITORY_NAME + "/master/version.json";
    }

    /**
     * Gets the latest version from the JSON file.
     * This method also updates the state of {@link VersionChecker#updateAvailable}
     *
     * @return The latest version of the mod
     */
    double getLatestVersion() {
        try {
            JsonURLReader reader = new JsonURLReader(checkerURL);
            return reader.getContentAsObject().get("version").getAsDouble();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parseDouble(Reference.VERSION);
    }

    /**
     * Whether an update is available or not
     *
     * @return Whether an update for the mod is available or not
     */
    public boolean isUpdateAvailable() {
        return updateAvailable;
    }

    /**
     * Updates the state of {@link #updateAvailable}
     */
    public void updateState() {
        latestVersion = getLatestVersion();
        updateAvailable = latestVersion > version;
    }
}
