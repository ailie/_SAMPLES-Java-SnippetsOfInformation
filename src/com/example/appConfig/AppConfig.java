package com.example.appConfig;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class AppConfig {

    private static final String     CONFIG_FILE_USR = "usr.conf";

    private static final Properties DEFAULT_CONFIG  = load("def.conf", new Properties());
    public static final Properties  CURRENT_CONFIG  = load(CONFIG_FILE_USR, new Properties(DEFAULT_CONFIG));

    private static Properties load(String propertiesFile, Properties intoProperties) {
        try (InputStream src = new FileInputStream(propertiesFile)) {
            intoProperties.load(src);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return intoProperties;
    }

    private static void save(Properties properties, String intoPropertiesFile) {
        try (OutputStream dst = new FileOutputStream(intoPropertiesFile)) {
            properties.store(dst, "---No Comment---");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onAppExit() { // our process is dying, time to save our configs
        save(CURRENT_CONFIG, CONFIG_FILE_USR);
    }
}
