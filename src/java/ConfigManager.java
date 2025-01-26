package org.example.mr_yinkina.yinkinapro.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.example.mr_yinkina.yinkinapro.YinKinaPro;
import org.example.mr_yinkina.yinkinapro.module.Module;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final Path configDir;
    private final Path modulesFile;

    public ConfigManager() {
        this.configDir = Paths.get("config", "yinkinapro");
        this.modulesFile = configDir.resolve("modules.json");
        createDirectories();
    }

    private void createDirectories() {
        try {
            Files.createDirectories(configDir);
        } catch (IOException e) {
            YinKinaPro.LOGGER.error("Failed to create config directory", e);
        }
    }

    public void saveConfig() {
        try {
            JsonObject modulesObject = new JsonObject();
            
            for (Module module : YinKinaPro.INSTANCE.moduleManager.getModules()) {
                JsonObject moduleObject = new JsonObject();
                moduleObject.addProperty("enabled", module.isEnabled());
                moduleObject.addProperty("key", module.getKey());
                modulesObject.add(module.getName(), moduleObject);
            }

            try (Writer writer = new FileWriter(modulesFile.toFile())) {
                GSON.toJson(modulesObject, writer);
            }
        } catch (IOException e) {
            YinKinaPro.LOGGER.error("Failed to save config", e);
        }
    }

    public void loadConfig() {
        try {
            if (!Files.exists(modulesFile)) {
                return;
            }

            JsonObject modulesObject = GSON.fromJson(new FileReader(modulesFile.toFile()), JsonObject.class);
            
            for (Module module : YinKinaPro.INSTANCE.moduleManager.getModules()) {
                if (modulesObject.has(module.getName())) {
                    JsonObject moduleObject = modulesObject.getAsJsonObject(module.getName());
                    if (moduleObject.has("enabled")) {
                        module.setEnabled(moduleObject.get("enabled").getAsBoolean());
                    }
                    if (moduleObject.has("key")) {
                        module.setKey(moduleObject.get("key").getAsInt());
                    }
                }
            }
        } catch (IOException e) {
            YinKinaPro.LOGGER.error("Failed to load config", e);
        }
    }
} 