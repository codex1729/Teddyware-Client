package com.teddyware.client.module.client;

import com.teddyware.client.module.Category;
import com.teddyware.client.module.Module;
import org.lwjgl.input.Keyboard;

@Module.Data(name = "Hud", description = "Shows the client GUI.", key = Keyboard.KEY_NONE, category = Category.Client)
public class Hud extends Module {

    public Hud() {

    }

    @Override
    public void onEnable() { }

    @Override
    public void onDisable() { }
}
