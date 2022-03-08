package website.skylorbeck.minecraft.iconicwands.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.fabricmc.fabric.impl.client.rendering.EntityRendererRegistryImpl;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import website.skylorbeck.minecraft.iconicwands.Declarar;
import website.skylorbeck.minecraft.iconicwands.Iconicwands;
import website.skylorbeck.minecraft.iconicwands.entity.MagicProjetileEntityRenderer;
import website.skylorbeck.minecraft.iconicwands.items.WandTooltipComponent;
import website.skylorbeck.minecraft.iconicwands.items.WandTooltipData;
import website.skylorbeck.minecraft.iconicwands.screen.WandBenchScreen;
import website.skylorbeck.minecraft.iconicwands.screen.WandBenchScreenHandler;

@Environment(EnvType.CLIENT)
public class IconicwandsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistryImpl.register(Declarar.MAGIC_PROJECTILE, MagicProjetileEntityRenderer::new);
        TooltipComponentCallback.EVENT.register(data -> new WandTooltipComponent((WandTooltipData) data));
        ScreenRegistry.register(Declarar.WANDING, WandBenchScreen::new);
    }
}
