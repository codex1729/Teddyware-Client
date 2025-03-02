package com.teddyware.client.module.client;

import com.teddyware.api.event.Event;
import com.teddyware.api.event.events.EventNotifier;
import com.teddyware.api.event.events.EventRenderGUI;
import com.teddyware.api.util.color.JColor;
import com.teddyware.api.util.font.FontUtil;
import com.teddyware.client.Teddyware;
import com.teddyware.client.module.Category;
import com.teddyware.client.module.Module;
import com.teddyware.client.module.ModuleManager;
import com.teddyware.client.setting.Setting;
import com.teddyware.client.setting.settings.BooleanSetting;
import com.teddyware.client.setting.settings.ColorSetting;
import com.teddyware.client.setting.settings.KeybindSetting;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.List;

@Module.Data(name = "TabGUI", description = "A GUI similar to ClickGUI for quick use.", key = Keyboard.KEY_NONE, category = Category.Client)
public class TabGUI extends Module {

    public static int currentTab;
    public boolean expanded;

    public ColorSetting activeColor = new ColorSetting("ActiveColor", this, new JColor(120, 63, 4));
    public ColorSetting backgroundColor = new ColorSetting("BackgroundColor", this, new JColor(0, 0, 0, 100));
    public ColorSetting fontColor = new ColorSetting("FontColor", this, new JColor(255, 255, 255));

    public TabGUI() {
        this.addSetting(activeColor, backgroundColor, fontColor);
    }

    @EventHandler
    private final Listener<EventRenderGUI> eventRenderGUIListener = new Listener<>(event -> {
        // Main tab
        FontRenderer fr = mc.fontRenderer;
        int count = 0;

        // background
        Gui.drawRect(5, 37, 75, 36 + Category.values().length * 16 + 2, backgroundColor.getValue().getRGB());
        // outline
        Gui.drawRect(7, 40 + currentTab * 16, 7 + 61, 40 + currentTab * 16 + 12, activeColor.getValue().getRGB());

        for (Category c : Category.values()) {
            FontUtil.drawStringWithShadow(c.name, 11, 42 + count * 16, fontColor.getValue());
            count++;
        }
        if (expanded) {
            // Multiple tabs
            Category category = Category.values()[currentTab];
            List<Module> modules = ModuleManager.getModulesByCategory(category);
            Module moduleOnSetting = modules.get(category.moduleIndex);

            if (modules.size() == 0)
                return;
            //background
            Gui.drawRect(75, (int) 37.5, 78 + 68, 36 + modules.size() * 16 + 2, backgroundColor.getValue().getRGB());
            //outline
            Gui.drawRect(75, 40 + category.moduleIndex * 16, 10 + 60 + 68, 40 + category.moduleIndex * 16 + 12, activeColor.getValue().getRGB());

            count = 0;
            for (Module m : modules) {

                if (count == category.moduleIndex && m.settingExpanded) {

                    if (!m.settings.isEmpty()) {
                        //background
                        Gui.drawRect(146, (int) 37.5, 217, 36 + m.settings.size() * 16 + 2, backgroundColor.getValue().getRGB());
                        //outline
                        Gui.drawRect(147, 40 + m.settingIndex * 16, 208, 40 + m.settingIndex * 16 + 12, activeColor.getValue().getRGB());
                    }

                    int settingIndex = 0;
                    for (Setting setting : m.settings) {
                        if (setting instanceof BooleanSetting) {
                            BooleanSetting bool = (BooleanSetting) setting;
                            fr.drawStringWithShadow(setting.name + ": " + (bool.isEnabled() ? "On" : "Off"), 83, 42 + settingIndex * 16, fontColor.getValue().getRGB());
                        }
                        if (setting instanceof KeybindSetting) {
                            KeybindSetting keyBind = (KeybindSetting) setting;
                            fr.drawStringWithShadow(setting.name + ": " + Keyboard.getKeyName(keyBind.code), 83, 42 + settingIndex * 16, fontColor.getValue().getRGB());

                        }

                        settingIndex++;
                    }

                }
                // names
                FontUtil.drawStringWithShadow(m.name, 79, 42 + count * 16, fontColor.getValue());
                count++;
            }
        }
    });

    @SubscribeEvent
    public void onKey(InputEvent.KeyInputEvent e) {
        if (Keyboard.getEventKeyState()) {
            int code = Keyboard.getEventKey();

            Category category = Category.values()[currentTab];
            List<Module> modules = ModuleManager.getModulesByCategory(category);
            Module module = modules.get(category.moduleIndex);

            if (code == Keyboard.KEY_UP) {
                if (expanded) {
                    if (category.moduleIndex <= 0) {
                        category.moduleIndex = modules.size() - 1;
                    } else {
                        category.moduleIndex--;
                    }

                    if (module.settingIndex <= 0) {
                        module.settingIndex = module.settings.size() - 1;
                    } else {
                        module.settingIndex--;
                    }
                } else {
                    if (currentTab <= 0) {
                        currentTab = Category.values().length - 1;
                    } else {
                        currentTab--;
                    }
                }
            }

            if (code == Keyboard.KEY_DOWN) {
                if (expanded) {
                    if (category.moduleIndex >= modules.size() - 1) {
                        category.moduleIndex = 0;
                    } else {
                        category.moduleIndex++;
                    }

                    if (module.settingIndex >= module.settings.size() - 1) {
                        module.settingIndex = 0;
                    } else {
                        module.settingIndex++;
                    }
                } else {
                    if (currentTab >= Category.values().length - 1) {
                        currentTab = 0;
                    } else {
                        currentTab++;
                    }
                }
            }
            if (code == Keyboard.KEY_RIGHT) {
                if (expanded && modules.size() > 0) {
                    Setting setting = module.settings.get(module.settingIndex);
                    if (setting instanceof BooleanSetting) {
                        if (module.settings.get(module.settingIndex).focused) {
                            ((BooleanSetting) setting).toggle();
                        }
                    }
                } else if (modules.size() == 0) {
                    expanded = false;
                } else if (!expanded) {
                    expanded = true;
                } else if (module.settings.size() == 0) {
                    module.settingExpanded = false;
                } else if (!module.settingExpanded) {
                    module.settingExpanded = true;
                }
            }
            if (code == Keyboard.KEY_LEFT) {
                if (module.settingExpanded) {
                    module.settingExpanded = false;
                }
                expanded = false;
            }

            if (code == Keyboard.KEY_RETURN) {
                if (expanded && modules.size() > 0) {
                    if (!module.name.equals("TabGUI")) {
                        module.toggle();
                    }
                }
            }
        }
    }

}
