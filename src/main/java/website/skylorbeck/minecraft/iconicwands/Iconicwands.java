package website.skylorbeck.minecraft.iconicwands;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.util.Identifier;
import website.skylorbeck.minecraft.iconicwands.config.Parts;
import website.skylorbeck.minecraft.iconicwands.screen.WandBenchScreenHandler;
import website.skylorbeck.minecraft.skylorlib.ConfigFileHandler;
import website.skylorbeck.minecraft.skylorlib.Registrar;

import java.io.IOException;

public class Iconicwands implements ModInitializer {
    public static Parts parts = new Parts();
    static {
        parts.addCores(
                new Parts.Core(
                        "minecraft:stick",
                        1,
                        10,
                        5,
                        10,
                        10,
                        150
                ),
                new Parts.Core(
                        "minecraft:blaze_rod",
                        1,
                        5,
                        10,
                        20,
                        10,
                        0
                ),
                new Parts.Core(
                        "minecraft:end_rod",
                        5,
                        5,
                        50,
                        10,
                        10,
                        50
                ),
                new Parts.Core(
                        "minecraft:bone",
                        5,
                        5,
                        50,
                        10,
                        10,
                        50
                ),
                new Parts.Core(
                        "minecraft:copper_ingot",
                        5,
                        5,
                        50,
                        10,
                        10,
                        50
                ),
                new Parts.Core(
                        "minecraft:amethyst_shard",
                        5,
                        5,
                        50,
                        10,
                        10,
                        50
                )
        );
        parts.addTips(
                new Parts.Tip(
                        "minecraft:glow_berries",
                        1,
                        5,
                        50,
                        1,
                        0.04f,
                        150
                ),
                new Parts.Tip(
                        "minecraft:ghast_tear",
                        1,
                        75,
                        25,
                        2,
                        0.07f,
                        26
                ),
                new Parts.Tip(
                        "minecraft:shulker_shell",
                        2,
                        25,
                        75,
                        1.5f,
                        0.03f,
                        100
                ),
                new Parts.Tip(
                        "minecraft:quartz",
                        2,
                        25,
                        75,
                        1.5f,
                        0.03f,
                        100
                ),
                new Parts.Tip(
                        "minecraft:emerald",
                        2,
                        25,
                        75,
                        1.5f,
                        0.03f,
                        100
                ),
                new Parts.Tip(
                        "minecraft:lapis_lazuli",
                        2,
                        25,
                        75,
                        1.5f,
                        0.03f,
                        100
                )
        );
        parts.addHandles(
                new Parts.Handle(
                        "minecraft:gold_ingot",
                        1,
                        10,
                        50,
                        0.01f,
                        150
                ),
                new Parts.Handle(
                        "minecraft:iron_ingot",
                        1,
                        5,
                        25,
                        0.05f,
                        100
                ),
                new Parts.Handle(
                        "minecraft:shulker_shell",
                        2,
                        10,
                        50,
                        0.03f,
                        200
                ),
                new Parts.Handle(
                        "minecraft:leather",
                        2,
                        10,
                        50,
                        0.03f,
                        200
                )
        );
    }
    @Override
    public void onInitialize() {
        Registrar.regBlock("timed_light_",Declarar.TIMED_LIGHT,MODID);
        Registrar.regItem("iconicwand_",Declarar.ICONIC_WAND,MODID);
        Registrar.regBlock("wand_bench_",Declarar.WAND_BENCH,MODID);
        Registrar.regItem("wand_bench_",Declarar.WAND_BENCH_ITEM,MODID);
        Declarar.WANDING = ScreenHandlerRegistry.registerSimple(Iconicwands.getId("wand_crafting"),((syncId, inventory) -> new WandBenchScreenHandler(syncId,inventory,new SimpleInventory(4))));
        try {
            parts = ConfigFileHandler.initConfigFile("iconic_wands.json",parts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String MODID = "iconicwands";

    public static Identifier getId(String name) {
        return new Identifier(MODID, name);
    }

    //todo
    // balance
    // more parts
}
