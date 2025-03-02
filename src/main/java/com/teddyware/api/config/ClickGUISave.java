package com.teddyware.api.config;

import com.teddyware.client.Teddyware;
import com.teddyware.client.gui.clickgui.ClickGUIConfig;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ClickGUISave {


    public ClickGUISave() {
        try {
            clickGUISave();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static final String fileName = "teddyware/";
    String mainName = "ClickGUI/";

    public void clickGUISave() throws IOException {
        if (!Files.exists(Paths.get(fileName))) {
            Files.createDirectories(Paths.get(fileName));
        }
        if (!Files.exists(Paths.get(fileName + mainName))) {
            Files.createDirectories(Paths.get(fileName + mainName));
        }
    }

    public void registerFiles(String location, String name) throws IOException {
        if (!Files.exists(Paths.get(fileName + location + name + ".json"))) {
            Files.createFile(Paths.get(fileName + location + name + ".json"));
        } else {
            File file = new File(fileName + location + name + ".json");
            file.delete();
            Files.createFile(Paths.get(fileName + location +name + ".json"));
        }
    }

    public void saveClickGUIPositions() throws IOException {
        registerFiles(mainName, "ClickGUI");
        Teddyware.instance.clickGUIScreen.gui.saveConfig(new ClickGUIConfig(fileName + mainName));
    }

}
