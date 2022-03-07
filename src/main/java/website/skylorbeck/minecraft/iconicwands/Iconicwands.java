package website.skylorbeck.minecraft.iconicwands;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import website.skylorbeck.minecraft.skylorlib.Registrar;
import website.skylorbeck.minecraft.skylorlib.SkylorLib;

public class Iconicwands implements ModInitializer {
    @Override
    public void onInitialize() {
        Registrar.regItem("iconicwand_",Declarar.ICONIC_WAND,MODID);
    }
    public static String MODID = "iconicwands";

    public static Identifier getId(String name) {
        return new Identifier(MODID, name);
    }
}
