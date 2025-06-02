package breedableloli.lolihack.modules;

import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import breedableloli.lolihack.LoliHackAddon;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.mixininterface.IPlayerInteractEntityC2SPacket;
import meteordevelopment.meteorclient.utils.network.MeteorExecutor;
import meteordevelopment.meteorclient.settings.*;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;

import meteordevelopment.orbit.EventHandler;

public class InfiniteWeapons extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final Setting<Integer> Delay = sgGeneral.add(new IntSetting.Builder()
            .name("delay")
            .description("Delay, too long can break killaura")
            .defaultValue(0)
            .sliderRange(0, 1000)
            .build());

    public InfiniteWeapons() {
        super(LoliHackAddon.CATEGORY, "Infinite-Weapons", "Artibruite Swap");
    }

    private void swap(int slot) {
        try {
            Thread.sleep(Delay.get());
        } catch (Exception e) {
        }
        InvUtils.swap(slot, false);
    }

    @EventHandler
    private void onSendPacket(PacketEvent.Send event) {
        if (event.packet instanceof IPlayerInteractEntityC2SPacket packet
                && packet.meteor$getType() == PlayerInteractEntityC2SPacket.InteractType.ATTACK) {
            int oldslot = mc.player.getInventory().getSelectedSlot();
            ItemStack stack = mc.player.getMainHandStack();
            if (stack.isIn(ItemTags.SWORDS) || stack.isIn(ItemTags.AXES)) {
                for (int i = 0; i < 9; i++) {
                    ItemStack find = mc.player.getInventory().getStack(i);
                    if (!find.isDamageable()) {
                        InvUtils.swap(i, false);
                        MeteorExecutor.execute(() -> swap(oldslot));
                        break;
                    }
                }
            }
        }
    }
}
