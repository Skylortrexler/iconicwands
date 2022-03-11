package website.skylorbeck.minecraft.iconicwands.entity;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.property.Properties;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;

public class WandBenchEntityRenderer<T extends BlockEntity> implements BlockEntityRenderer<WandBenchEntity> {

    public WandBenchEntityRenderer(BlockEntityRendererFactory.Context context) {
    }

    @Override
    public void render(WandBenchEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        Direction direction = entity.getWorld().getBlockState(entity.getPos()).get(Properties.FACING);
        matrices.push();
        matrices.multiply(Vec3f.NEGATIVE_Y.getDegreesQuaternion(direction.asRotation()));
        switch (direction){
            case NORTH -> matrices.translate(-1,0,-1);
            case SOUTH -> matrices.translate(0,0,0);
            case WEST -> matrices.translate(0,0,-1);
            case EAST -> matrices.translate(-1,0,0);
        }
        DefaultedList<ItemStack> items = entity.getInventory();
        ItemStack itemStack = items.get(1);
        ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
        matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90));
        matrices.scale(0.25f,0.25f,0.25f);
        matrices.translate(3.1,0.75,-3.3);
        itemRenderer.renderItem(itemStack, ModelTransformation.Mode.GROUND,light,overlay,matrices,vertexConsumers,0);
        matrices.translate(-1.1,0,0);
        itemStack = items.get(2);
        itemRenderer.renderItem(itemStack, ModelTransformation.Mode.GROUND,light,overlay,matrices,vertexConsumers,0);
        matrices.translate(-1.1,0,0);
        itemStack = items.get(3);
        itemRenderer.renderItem(itemStack, ModelTransformation.Mode.GROUND,light,overlay,matrices,vertexConsumers,0);
        itemStack = items.get(0);
        if (!itemStack.isOf(Items.BARRIER)) {
            double time = entity.getWorld().getTime();
            matrices.translate(0.7f,1.5f,-0.65f+(0.25*Math.sin(0.05*time)));
            matrices.scale(4,4,4);
            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180));
            matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(45));
            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion((float) (10*Math.sin(0.05*time))));
            matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion((float) (10*Math.cos(0.05*time))));
            itemRenderer.renderItem(itemStack, ModelTransformation.Mode.GROUND, light, overlay, matrices, vertexConsumers, 0);
        }
        matrices.pop();
    }
}

