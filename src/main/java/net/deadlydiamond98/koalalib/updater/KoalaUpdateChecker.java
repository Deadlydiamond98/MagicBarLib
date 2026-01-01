package net.deadlydiamond98.koalalib.updater;

import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import com.google.gson.*;
import net.deadlydiamond98.koalalib.KoalaLib;
import net.deadlydiamond98.koalalib.config.KoalaLibConfigs;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModOrigin;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Used to check for Mod Updates via Modrinth
 */
public class KoalaUpdateChecker {

    public static final List<UpdatableMod> MOD_UPDATE_LIST = new ArrayList<>();
    private static final String MODRINTH_URL = "https://api.modrinth.com/v2/version_file/";

    // Version and Loader have a variable so that when I inevitably port the mod, things will be easier
    private static final String VERSION = "1.20.1";
    private static final String LOADER = "fabric";

    public record UpdatableMod(String name, String url) {}

    /**
     * This method can be called to add a checker for a mod. When the mod is loaded, this get the jar file corresponding
     * to the modid, and check Modrinth for the latest version.
     * @param modid Mod ID for the mod that will be checked
     */
    public static void addModUpdateChecker(String modid) {
        if (KoalaLibConfigs.Main.checkForUpdates) {
            Optional<ModContainer> optional = FabricLoader.getInstance().getModContainer(modid);

            if (optional.isPresent()) {
                ModContainer container = optional.get();
                String name = container.getMetadata().getName();

                try {
                    URL url = new URL(MODRINTH_URL + getSHA512Hash(name, container) + "/update?algorithm=sha512");
                    JsonObject jsonObject = JsonParser.parseString(requestJSONString(url)).getAsJsonObject();
                    JsonObject primaryRelease = getPrimaryRelease(jsonObject).getAsJsonObject();

                    String modFilepath = getJarLocation(FabricLoader.getInstance().getModContainer(modid).orElseThrow()).toString();
                    String modrinthName = primaryRelease.get("filename").getAsString();

                    if (!modFilepath.contains(modrinthName)) {
                        String modUrl = primaryRelease.get("url").getAsString();

                        MOD_UPDATE_LIST.add(new UpdatableMod(name, modUrl));

                        KoalaLib.LOGGER.info("-------------------------------\n");
                        KoalaLib.LOGGER.info("An update for {} has been detected!", name);
                        KoalaLib.LOGGER.info("You can download the latest version of {} here: \n{}", name, modUrl);
                        KoalaLib.LOGGER.info("\n-------------------------------\n");
                    }

                } catch (Exception ignored) {
                    KoalaLib.LOGGER.info("Failed to find {} on Modrinth!", name);
                }
            }
        }
    }

    /**
     * Gets a json from Modrinth that contains information about the latest release of a mod
     * @param url Url for the mod
     * @return Returns a string that contains the entire Json file
     */
    private static String requestJSONString(URL url) throws Exception {
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        http.setRequestProperty("Content-Type", "application/json");
        http.setRequestProperty("User-Agent", getContact());
        String data = "{\"loaders\":[\"" + LOADER + "\"],\"game_versions\":[\"" + VERSION + "\"]}";
        byte[] out = data.getBytes(StandardCharsets.UTF_8);
        OutputStream stream = http.getOutputStream();
        stream.write(out);

        String str = new BufferedReader(new InputStreamReader(http.getInputStream())).readLine();
        http.disconnect();
        return str;
    }

    /**
     * Filters the mod "files" that are uploaded for the modrinth version for the Primary release, and returns the primary
     * release
     * @param jsonObject json object
     * @return Returns the primary release
     */
    private static JsonElement getPrimaryRelease(JsonObject jsonObject) {
        return jsonObject.getAsJsonArray("files").asList().stream().filter(
                jsonElement -> jsonElement.getAsJsonObject().get("primary").getAsBoolean()
        ).findFirst().orElseThrow();
    }

    /**
     * Returns the location of your mod Jar
     * @param container mod container, basically mod information
     * @return Returns the location of your mod Jar
     */
    private static Path getJarLocation(ModContainer container) {
        return container.getOrigin().getPaths().stream().filter(
                path -> path.toString().toLowerCase(Locale.ROOT).endsWith(".jar")
        ).findFirst().orElseThrow();
    }

    /**
     * Returns a SHA-512 Hash that corresponds to a mod, which Modrinth needs for retrieving the latest version of a mod.<br>
     * (In all honestly, I have no idea what a Hash exactly is, I just Googled how to convert a file to a SHA-512 and went from there)
     * @param name Name of the mod
     * @param container mod container, basically mod information
     * @return returns a SHA-512 Hash corresponding to a mod
     */
    private static String getSHA512Hash(String name, ModContainer container) {
        try {
            if (container.getOrigin().getKind() == ModOrigin.Kind.PATH) {
                File file = getJarLocation(container).toFile();
                if (file.isFile()) {
                    return Files.asByteSource(file).hash(Hashing.sha512()).toString();
                }
            }
        } catch (Exception ignored) {
            KoalaLib.LOGGER.info("Unable to get the SHA-512 Hash for [{}]", name);
        }

        return null;
    }

    /**
     * Gets Contact to send to Modrinth User Agent so that Modrinth doesn't send a Hitman after me and so they can contact
     * me if too many requests are sent
     * @return returns contact info
     */
    private static String getContact() {
        return "Deadlydiamond98/KoalaLib/" + KoalaLib.getVersion() + " (diamonderrick1@gmail.com)";
    }
}
