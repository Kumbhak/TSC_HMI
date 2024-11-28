package com.tsc.conf;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class PLCConfigManager {
    public static List<PLCConfig> loadConfig(String filePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File configFile = new File(filePath);
        if (!configFile.exists()) {
            // File not found, generate default config
            List<PLCConfig> defaultConfig = generateDefaultConfig(filePath);
            System.out.println("Generated default config at: " + configFile.getAbsolutePath());
            return defaultConfig;
        }
        try (InputStream inputStream = Files.newInputStream(configFile.toPath())) {
            return mapper.readValue(inputStream, mapper.getTypeFactory().constructCollectionType(List.class, PLCConfig.class));
        }
    }

    private static List<PLCConfig> generateDefaultConfig(String filePath) throws IOException {
        List<PLCConfig> defaultConfig = Arrays.asList(
                new PLCConfig("192.168.0.1", 0, 1),
                new PLCConfig("192.168.0.2", 0, 1)
        );

        ObjectMapper mapper = new ObjectMapper();
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            mapper.writeValue(fos, defaultConfig);
        }

        return defaultConfig;
    }

    public static void createConfigDirectory() throws IOException {
        Files.createDirectories(Paths.get("config"));
    }
}