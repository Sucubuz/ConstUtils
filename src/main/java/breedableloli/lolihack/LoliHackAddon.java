package breedableloli.lolihack;

import breedableloli.lolihack.modules.*;
//import com.example.addon.hud.HudExample;
//import com.example.addon.modules.ModuleExample;
import com.mojang.logging.LogUtils;
import meteordevelopment.meteorclient.addons.GithubRepo;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.commands.Commands;
import meteordevelopment.meteorclient.systems.hud.Hud;
import meteordevelopment.meteorclient.systems.hud.HudGroup;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import org.slf4j.Logger;

public class LoliHackAddon extends MeteorAddon {
    public static final Logger LOG = LogUtils.getLogger();
    public static final Category CATEGORY = new Category("ConstUtils");
    public static final HudGroup HUD_GROUP = new HudGroup("ConstUtils");

    @Override
    public void onInitialize() {
        Modules modules = Modules.get();
        // Modules
        modules.add(new ElytraFlyPlus());
        modules.add(new InfiniteWeapons());
        modules.add(new LagNotifier());
        modules.add(new AutoOmen());
        modules.add(new ConstAntiQueue());
        // Commands
        // Commands.add(new CommandExample());

        // HUD
        // Hud.get().register(HudExample.INFO);
    }

    @Override
    public void onRegisterCategories() {
        Modules.registerCategory(CATEGORY);
    }

    @Override
    public String getPackage() {
        return "breedableloli.lolihack";
    }

}
