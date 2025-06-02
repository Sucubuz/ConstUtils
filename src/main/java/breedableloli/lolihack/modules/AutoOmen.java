package breedableloli.lolihack.modules;

import net.minecraft.registry.entry.RegistryEntry;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.utils.Utils;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import java.util.Map;

import breedableloli.lolihack.LoliHackAddon;
import meteordevelopment.meteorclient.systems.modules.Module;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import meteordevelopment.orbit.EventHandler;

public class AutoOmen extends Module {
    private boolean eat = false;

    public AutoOmen() {
        super(LoliHackAddon.CATEGORY, "AutoOmen", "dirnk omen bottle");
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        Map<RegistryEntry<StatusEffect>, StatusEffectInstance> effects = mc.player.getActiveStatusEffects();

        if (!effects.containsKey(StatusEffects.BAD_OMEN) && !effects.containsKey(StatusEffects.RAID_OMEN)) {
            for (int i = 0; i < 9; i++) {
                ItemStack stack = mc.player.getInventory().getStack(i);
                if (stack.isEmpty())
                    continue;
                if (stack.getItem() == Items.OMINOUS_BOTTLE) {
                    mc.options.useKey.setPressed(true);
                    this.eat = true;
                    InvUtils.swap(i, false);
                    if (!mc.player.isUsingItem())
                        Utils.rightClick();
                    return;
                }
            }
        } else {
            if (this.eat) {
                mc.options.useKey.setPressed(false);
                this.eat = false;
            }
        }
    }

}
