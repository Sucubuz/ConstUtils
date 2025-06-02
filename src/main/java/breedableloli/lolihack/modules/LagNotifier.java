package breedableloli.lolihack.modules;

import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.render.Render2DEvent;
import meteordevelopment.meteorclient.utils.render.color.Color;

import java.text.DecimalFormat;

import breedableloli.lolihack.LoliHackAddon;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.renderer.text.TextRenderer;

import meteordevelopment.orbit.EventHandler;

public class LagNotifier extends Module {

    private long packetTimer = System.nanoTime();

    public LagNotifier() {
        super(LoliHackAddon.CATEGORY, "Lag-Notifier", "Show text when lag");
    }

    @EventHandler
    public void onPacketReceive(PacketEvent.Receive e) {
        if (mc.player == null || mc.world == null)
            return;

        if (e.packet instanceof WorldTimeUpdateS2CPacket)
            packetTimer = System.nanoTime();
    }

    @EventHandler
    private void onRender2D(Render2DEvent event) {
        TextRenderer text = TextRenderer.get();
        if ((System.nanoTime() - packetTimer) / 1000000L >= 3000) {

            text.render("The Server stopped responding! " +
                    new DecimalFormat().format(((System.nanoTime() - packetTimer) / 1000000L / 1000f)),
                    mc.getWindow().getWidth() / 2, 50, new Color(Color.RED));

        }

    }
}
