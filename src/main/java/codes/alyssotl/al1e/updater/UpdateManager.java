package codes.alyssotl.al1e.updater;

import net.minecraft.client.Minecraft;
import codes.alyssotl.al1e.Al1eMod;
import codes.alyssotl.al1e.utils.Reference;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Class which handles updating the mod
 */
public class UpdateManager {

    // Whether the new version is a snapshot or not
    private final boolean snapshot;

    /**
     * Initiates a new update manager
     *
     * @param snapshot Whether the new version is a snapshot or not
     */
    public UpdateManager(boolean snapshot) {
        this.snapshot = snapshot;
    }

    /**
     * Updates the mod file
     *
     * @return {@code true} if it was successful, {@code false} if otherwise.
     */
    public boolean updateMod() {
        boolean success = true;
        try {
            File modsDirectory = new File(Minecraft.getMinecraft().mcDataDir, "mods");
            File modFile = new File(modsDirectory, Reference.JAR_NAME);
            if (modFile.exists() && modFile.delete()) {
                if (modFile.createNewFile()) {
                    URL updateURL = new URL(getDownloadLink());
                    FileUtils.copyURLToFile(updateURL, modFile);
                }
            }
        } catch (IOException e) {
            success = false;
        }
        return success;
    }

    private String getDownloadLink() {
        String version = Al1eMod.INSTANCE.getChecker().getLatestVersion() + (snapshot ? "-SNAPSHOT" : "");
        return "https://github.com/Alyssotl/" +
                Reference.REPOSITORY_NAME +
                "/" +
                "releases" +
                "/" +
                "download" +
                "/" +
                version +
                "/" +
                Reference.JAR_NAME +
                "-" +
                version +
                ".jar";
    }
}
