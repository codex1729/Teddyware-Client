package com.teddyware.api.event;

import com.teddyware.api.event.events.EventRender;
import com.teddyware.client.Teddyware;
import com.teddyware.client.module.ModuleManager;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class EventProcessor {

    private Minecraft mc = Minecraft.getMinecraft();

    public EventProcessor() {
        Teddyware.EVENT_BUS.subscribe(this);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onWorldRender(final RenderWorldLastEvent event) {
        ModuleManager.onWorldRender(event);
        Teddyware.EVENT_BUS.post(new EventRender(event.getPartialTicks()));
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (mc.player != null) {
            ModuleManager.onUpdate();
        }
    }

}
