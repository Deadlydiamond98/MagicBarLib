package net.deadlydiamond98.koalalib.updater;

import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import com.google.gson.*;
import net.deadlydiamond98.koalalib.KoalaLib;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModOrigin;
import oshi.util.tuples.Pair;

import java.io.*;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Locale;

public class KoalaUpdateChecker {

    public static HashMap<String, UpdatableMod> MOD_UPDATE_LIST = new HashMap<>();
    private static final String MODRINTH_URL = "https://api.modrinth.com/v2/version_file/";

    public record UpdatableMod(String name, String fileName, String modrinthName, String url) {}

//    String url = MODRINTH_URL + "044d33a1f4b24094c21d94e5168155d6cc76fccd9d49962ec3b25e20e79aff0fb93e70803fd1c132ba44b65112e5b96dc985577c671c73c68bd0586bedb5f184";
//    String url = MODRINTH_URL + "98dca2d8a9b371bf4c84a4300aaa0c87e6cac3d5f598600ea78f4e74dbe6d73dff2b1f1f53d8fb251d71ecb23097376d857012ef5eb1e3122bea2dee4363f652";
//    String url = MODRINTH_URL + "cdd4f52dc6930d89f01765e95f481d3520ec5f85c85112e738cf0b716418c543aae90d343bf381512613c8ed67944af93d1732cf84eb7a6b85b80617ba10879b";
//
    public static void addModUpdateChecker(String modid) {
        String url = MODRINTH_URL + getSHA512Hash(modid);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(url).openStream()))) {
            JsonObject jsonObject = JsonParser.parseString(reader.readLine()).getAsJsonObject();
            JsonObject primaryRelease = getPrimaryRelease(jsonObject).getAsJsonObject();

            String modrinthName = primaryRelease.get("filename").getAsString();
            String modUrl = primaryRelease.get("url").getAsString();
            KoalaLib.LOGGER.info("-------------------------------");
            KoalaLib.LOGGER.info("Name: {}", FabricLoader.getInstance().getModContainer(modid).orElseThrow().getMetadata().getName());
            String path = getJarLocation(modid, FabricLoader.getInstance().getModContainer(modid).orElseThrow()).toString();
//            KoalaLib.LOGGER.info("Parent Mod ID: {}", path);
            KoalaLib.LOGGER.info("Modrinth File: {}", modrinthName);
            KoalaLib.LOGGER.info("URL: {}", modUrl);
            KoalaLib.LOGGER.info("Is Up to date?: {}", path.contains(modrinthName));
            KoalaLib.LOGGER.info("-------------------------------\n");

        } catch (Exception ignored) {
//            KoalaLib.LOGGER.info("Failed to find [{}] on Modrinth!", modid);
        }
    }

    private static JsonElement getPrimaryRelease(JsonObject jsonObject) {
        return jsonObject.getAsJsonArray("files").asList().stream().filter(
                jsonElement -> jsonElement.getAsJsonObject().get("primary").getAsBoolean()
        ).findFirst().orElseThrow();
    }

    private static Path getJarLocation(String modid, ModContainer container) {
        return container.getOrigin().getPaths().stream().filter(
                path -> path.toString().toLowerCase(Locale.ROOT).endsWith(".jar")
        ).findFirst().orElseThrow();
    }

    private static String getSHA512Hash(String modid) {
        ModContainer container = FabricLoader.getInstance().getModContainer(modid).orElseThrow();

        try {
            if (container.getOrigin().getKind() == ModOrigin.Kind.PATH) {
                File file = getJarLocation(modid, container).toFile();
                if (file.isFile()) {
                    return Files.asByteSource(file).hash(Hashing.sha512()).toString();
                }
            }
        } catch (Exception ignored) {
            KoalaLib.LOGGER.info("Unable to get the SHA-512 Hash for [{}]", modid);
        }

        return null;
    }
}
