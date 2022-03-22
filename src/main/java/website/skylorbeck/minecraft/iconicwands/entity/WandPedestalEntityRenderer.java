package website.skylorbeck.minecraft.iconicwands.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;
import website.skylorbeck.minecraft.iconicwands.Declarar;

public class WandPedestalEntityRenderer<T extends BlockEntity> implements BlockEntityRenderer<WandPedestalEntity> {

    public WandPedestalEntityRenderer(BlockEntityRendererFactory.Context context) {
    }

    @Override
    public void render(WandPedestalEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        BlockState state = entity.getWorld().getBlockState(entity.getPos());
        if (state.isOf(Declarar.WAND_PEDESTAL)||state.isOf(Declarar.WAND_PEDESTAL_DISPLAY)) {
            Direction direction = state.get(Properties.FACING);
            ItemStack itemStack = entity.getInventory().get(0);
            ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
            matrices.push();
            if (entity.isHide()){
                itemRenderer.renderItem(itemStack, ModelTransformation.Mode.GROUND, light, overlay, matrices, vertexConsumers, 0);
            } else {
                matrices.multiply(Vec3f.NEGATIVE_Y.getDegreesQuaternion(direction.asRotation()));
                switch (direction) {
                    case NORTH -> matrices.translate(-1, 0, -1);
                    case SOUTH -> matrices.translate(0, 0, 0);
                    case WEST -> matrices.translate(0, 0, -1);
                    case EAST -> matrices.translate(-1, 0, 0);
                }
                double time = entity.getWorld().getTime();
                matrices.translate(0.4, 0.5 + (0.1 * Math.sin(0.05 * time)), 0.5);
                matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion((float) (10 * Math.sin(0.05 * time))));
                matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion((float) (10 * Math.cos(0.05 * time))));
                matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(-45));
                itemRenderer.renderItem(itemStack, ModelTransformation.Mode.GROUND, light, overlay, matrices, vertexConsumers, 0);
            }
            matrices.pop();
        }
    }
}
