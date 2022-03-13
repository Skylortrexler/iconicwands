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
                        1,
                        25,
                        5,
                        50,
                        0
                ),
                new Parts.Core(
                        "minecraft:end_rod",
                        5,
                        0,
                        50,
                        10,
                        100,
                        175
                ),
                new Parts.Core(
                        "minecraft:bone",
                        5,
                        10,
                        15,
                        30,
                        10,
                        75
                ),
                new Parts.Core(
                        "minecraft:copper_ingot",
                        5,
                        5,
                        20,
                        10,
                        35,
                        25
                ),
                new Parts.Core(
                        "minecraft:amethyst_shard",
                        1,
                        5,
                        75,
                        5,
                        50,
                        50
                ),
                new Parts.Core(
                        "minecraft:bamboo",
                        2,
                        3,
                        30,
                        10,
                        25,
                        255
                ),
                new Parts.Core(
                        "minecraft:carrot",
                        5,
                        5,
                        5,
                        20,
                        10,
                        0
                )
        );
        parts.addTips(
                new Parts.Tip(
                        "minecraft:glow_berries",
                        0.25f,
                        5,
                        50,
                        0.5f,
                        0.04f,
                        150
                ),
                new Parts.Tip(
                        "minecraft:ghast_tear",
                        0.4f,
                        75,
                        25,
                        2,
                        0.07f,
                        26
                ),
                new Parts.Tip(
                        "minecraft:shulker_shell",
                        0.7f,
                        25,
                        75,
                        0.5f,
                        0.03f,
                        100
                ),
                new Parts.Tip(
                        "minecraft:quartz",
                        1,
                        5,
                        10,
                        1f,
                        0.06f,
                        200
                ),
                new Parts.Tip(
                        "minecraft:emerald",
                        0.5f,
                        100,
                        50,
                        1.5f,
                        0.01f,
                        50
                ),
                new Parts.Tip(
                        "minecraft:lapis_lazuli",
                        0.25f,
                        25,
                        100,
                        1.5f,
                        0.1f,
                        0
                ),
                new Parts.Tip(
                        "minecraft:redstone_block",
                        2,
                        15,
                        100,
                        1.5f,
                        0.03f,
                        255
                ),
                new Parts.Tip(
                        "minecraft:potato",
                        0.1f,
                        5,
                        5,
                        5,
                        0f,
                        165
                )
        );
        parts.addHandles(
                new Parts.Handle(
                        "minecraft:gold_ingot",
                        4,
                        5,
                        15,
                        0.01f,
                        75
                ),
                new Parts.Handle(
                        "minecraft:iron_ingot",
                        3,
                        10,
                        25,
                        0.03f,
                        100
                ),
                new Parts.Handle(
                        "minecraft:shulker_shell",
                        3,
                        10,
                        50,
                        0.05f,
                        200
                ),
                new Parts.Handle(
                        "minecraft:leather",
                        3,
                        10,
                        10,
                        0.02f,
                        69
                ),
                new Parts.Handle(
                        "minecraft:kelp",
                        2,
                        5,
                        20,
                        0.03f,
                        50
                )
        );
    }
    @Override
    public void onInitialize() {
        Registrar.regBlock("timed_light_",Declarar.TIMED_LIGHT,MODID);
        Registrar.regItem("iconicwand_",Declarar.ICONIC_WAND,MODID);
        Registrar.regBlock("wand_bench_",Declarar.WAND_BENCH,MODID);
        Registrar.regItem("wand_bench_",Declarar.WAND_BENCH_ITEM,MODID);
        Registrar.regBlock("wand_pedestal_",Declarar.WAND_PEDESTAL,MODID);
        Registrar.regItem("wand_pedestal_",Declarar.WAND_PEDESTAL_ITEM,MODID);

        Registrar.regItem("wand_tester_",Declarar.WAND_TESTER,MODID);

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
    // presets in book
    // update stats in book
    // presets effects
}
