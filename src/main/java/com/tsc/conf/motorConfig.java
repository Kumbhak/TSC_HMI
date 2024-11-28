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

public class motorConfig {
    private List<Motor> motors;

    public List<Motor> getMotors() {
        return motors;
    }

    public static motorConfig loadConfig(String filePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File configFile = new File(filePath);
        if (!configFile.exists()) {
            // File not found, generate default config
            motorConfig defaultConfig = generateDefaultConfig(filePath);
            System.out.println("Generated default config at: " + configFile.getAbsolutePath());
            return defaultConfig;
        }
        try (InputStream inputStream = Files.newInputStream(configFile.toPath())) {
            return mapper.readValue(inputStream, motorConfig.class);
        }
    }

    private static motorConfig generateDefaultConfig(String filePath) throws IOException {
        motorConfig defaultConfig = new motorConfig();
        defaultConfig.motors = Arrays.asList(
                new Motor("Motor1", 1),
                new Motor("Motor2", 2)
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