package net.deadlydiamond98.koalalib.config;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.deadlydiamond98.koalalib.KoalaLib;
import net.deadlydiamond98.koalalib.util.KoalaLibConfigs;
import net.fabricmc.loader.api.FabricLoader;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class KoalaConfigCreator {

    // TODO: Prevent resetting of entire config file if new values are added or removed!

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final HashMap<String, Class<?>> MOD_CONFIGS = new HashMap<>();

    public static final HashMap<String, HashMap<String, Class<?>>> MOD_TEST = new HashMap<>();

    /**
     * Call this method to add a config for your mod!!
     * @param modID The ModID of your mod!
     * @param configClass The Class where all of your config values will be contained!
     */
    public static void addModConfig(String modID, Class<?> configClass) {
        MOD_CONFIGS.put(modID, configClass);

//        for (int i = 0; i < 25; i++) {
//            MOD_CONFIGS.put(modID + i, configClass);
//        }

        readValuesFromConfig(getConfigFile(modID), configClass);
    }
    
    public static void addModConfigCategory(String modID, String category, Class<?> configClass) {
        
    }

    /**
     * This method reads values from their config files, and applies them in-game!
     * <br><br> A new config file will be generated if:
     *  <ul>
     *   <li><b>A</b> - A name of a value in the config file doesn't match up with a name found in configClass</li>
     *   <li><b>B</b> - The config file and config class don't have a matching amount of values</li>
     *   <li><b>C</b> - A Config File cannot be read or doesn't exist</li>
     * </ul>
     * @param path The path to the config file
     * @param configClass The class containing all the config files
     */
    private static void readValuesFromConfig(Path path, Class<?> configClass) {
        try (FileReader reader = new FileReader(path.toFile())) {
            Type type = new TypeToken<HashMap<String, Object>>(){}.getType();
            HashMap<String, Object> readConfigData = GSON.fromJson(reader, type);

            if (configClass.getFields().length == readConfigData.size()) {
                readConfigData.forEach((name, value) -> {
                    try {
                        Field field = configClass.getField(name);
                        field.set(configClass, matchValueToFieldType(value, field));
                    } catch (Exception e) {
                        createConfigFile(path, configClass, 0); // A
                    }
                });
            } else {
                createConfigFile(path, configClass, 1); // B
            }
        } catch (Exception ignored) {
            createConfigFile(path, configClass, 2); // C
        }
    }

    /**
     * This method takes in a value from the config, and converts it to the needed format
     * <br>(this is because GSON loves to read integers as doubles for whatever reason)
     * @param value The value from the json
     * @param field The field that the value will be assigned to
     * @return returns the value as the proper type
     */
    private static Object matchValueToFieldType(Object value, Field field) {
        Class<?> type = field.getType();

        if (value instanceof Double) {
            double numericalValue = Double.parseDouble(value.toString());

            if (type.equals(int.class)) {
                return (int) numericalValue;
            } else if (type.equals(float.class)) {
                return (float) numericalValue;
            }
        }

        return value;
    }

    /**
     * This method generates a new config file if one doesn't exist
     * @param path The path to the config file
     * @param configClass The class containing all the config files
     * @param errorID The ID of a message to explain the reason for file creation
     */
    private static void createConfigFile(Path path, Class<?> configClass, int errorID) {
        printError(errorID, path);
        try {
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
            writeToConfigFile(path, configClass);
        } catch (Exception ignored) {}
    }

    /**
     * This method populates a config file with values
     * @param path The path to the config file
     * @param configClass The class containing all the config files
     */
    public static void writeToConfigFile(Path path, Class<?> configClass) {
        try (FileWriter writer = new FileWriter(path.toFile())) {
            HashMap<String, Object> configData = new HashMap<>();
            for (Field field : configClass.getFields()) {
                configData.put(field.getName(), field.get(field.getName()));
            }
            GSON.toJson(configData, writer);
        } catch (Exception ignored) {}
    }


    /**
     * Used to update all the config files when they're changed in-game
     */
    public static void updateAllConfigFiles() {
        MOD_CONFIGS.forEach((name, configScreen) -> writeToConfigFile(getConfigFile(name), configScreen));
    }

    /**
     * This method gets the path to the config file
     * @param name The name of the config file
     * @return returns the path to the config file
     */
    private static Path getConfigFile(String name) {
        return FabricLoader.getInstance().getConfigDir().resolve(name + ".json");
    }

    /**
     * Logger method for various errors and stuff, mainly used for debugging
     * @param id the number for the error
     */
    private static void printError(int id, Path path) {
        String errorString = switch (id) {
            case 0 -> "An invalid value was found in " + path + "\nGenerating a new file!";
            case 1 -> "The number of values found in " + path +
                    "\ndoesn't match the number it should be.\nGenerating a new file!";
            case 2 -> "Could not find a config file at " + path + "\nGenerating a new file!";
            default -> "This message shouldn't be able to print! If you're seeing this, something is extremely wrong!!";
        };

        KoalaLib.LOGGER.info(errorString);
    }


    private static void debugPrint() {
        logger("\n");
        logger("------------------------------------------------------------------------------");
        try {
            for (Field field : KoalaLibConfigs.class.getFields()) {
                logger(field.getName() + " | " + field.get(field.getName()));
            }
        } catch (Exception ignored) {}
        logger("------------------------------------------------------------------------------\n");
    }

    private static void logger(String str) {
        KoalaLib.LOGGER.info(str);
    }
}