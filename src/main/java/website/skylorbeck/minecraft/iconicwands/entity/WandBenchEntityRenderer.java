package website.skylorbeck.minecraft.iconicwands.entity;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Vec3f;

public class WandBenchEntityRenderer<T extends BlockEntity> implements BlockEntityRenderer<WandBenchEntity> {

    public WandBenchEntityRenderer(BlockEntityRendererFactory.Context context) {
    }

    @Override
    public void render(WandBenchEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        DefaultedList<ItemStack> items = entity.getInventory();
        ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
        matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90));
        matrices.scale(0.25f,0.25f,0.25f);
        matrices.translate(3.15,0.75,-3.3);
        itemRenderer.renderItem(items.get(1), ModelTransformation.Mode.GROUND,light,overlay,matrices,vertexConsumers,0);
        matrices.translate(-1.15,0,0);
        itemRenderer.renderItem(items.get(2), ModelTransformation.Mode.GROUND,light,overlay,matrices,vertexConsumers,0);
        matrices.translate(-1.1,0,0);
        itemRenderer.renderItem(items.get(3), ModelTransformation.Mode.GROUND,light,overlay,matrices,vertexConsumers,0);
        matrices.translate(0.7f,1.5f,-0.3f);
        matrices.scale(4,4,4);

        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180));
        matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(45));
        itemRenderer.renderItem(items.get(0), ModelTransformation.Mode.GROUND,light,overlay,matrices,vertexConsumers,0);
        matrices.pop();
    }
}

