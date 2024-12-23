package com.tsc.conf;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class HMIConfig {
    private int motorBlocksPerPage;

    public int getMotorBlocksPerPage() {
        return motorBlocksPerPage;
    }

    public void setMotorBlocksPerPage(int motorBlocksPerPage) {
        this.motorBlocksPerPage = motorBlocksPerPage;
    }

    public static HMIConfig loadConfig(String filePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File(filePath), HMIConfig.class);
    }

    public static void generateDefaultConfig(String filePath) throws IOException {
        HMIConfig defaultConfig = new HMIConfig();
        defaultConfig.setMotorBlocksPerPage(2); // Set default value

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File(filePath), defaultConfig);
    }
}