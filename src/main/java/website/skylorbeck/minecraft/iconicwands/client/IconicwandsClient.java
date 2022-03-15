package website.skylorbeck.minecraft.iconicwands.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.impl.blockrenderlayer.BlockRenderLayerMapImpl;
import net.fabricmc.fabric.impl.client.rendering.BlockEntityRendererRegistryImpl;
import net.fabricmc.fabric.impl.client.rendering.EntityRendererRegistryImpl;
import net.minecraft.client.render.RenderLayer;
import website.skylorbeck.minecraft.iconicwands.Declarar;
import website.skylorbeck.minecraft.iconicwands.entity.MagicProjetileEntityRenderer;
import website.skylorbeck.minecraft.iconicwands.entity.WandBenchEntityRenderer;
import website.skylorbeck.minecraft.iconicwands.entity.WandPedestalEntityRenderer;
import website.skylorbeck.minecraft.iconicwands.items.WandTooltipComponent;
import website.skylorbeck.minecraft.iconicwands.items.WandTooltipData;
import website.skylorbeck.minecraft.iconicwands.screen.WandBenchScreen;

@Environment(EnvType.CLIENT)
public class IconicwandsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistryImpl.register(Declarar.MAGIC_PROJECTILE, MagicProjetileEntityRenderer::new);
        TooltipComponentCallback.EVENT.register(data -> new WandTooltipComponent((WandTooltipData) data));
        ScreenRegistry.register(Declarar.WANDING, WandBenchScreen::new);
        BlockRenderLayerMapImpl.INSTANCE.putBlock(Declarar.WAND_BENCH, RenderLayer.getTranslucent());
        BlockRenderLayerMapImpl.INSTANCE.putBlock(Declarar.WAND_PEDESTAL, RenderLayer.getTranslucent());
        BlockEntityRendererRegistryImpl.register(Declarar.WAND_BENCH_ENTITY, WandBenchEntityRenderer::new);
        BlockEntityRendererRegistryImpl.register(Declarar.WAND_PEDESTAL_ENTITY, WandPedestalEntityRenderer::new);
    }
}
