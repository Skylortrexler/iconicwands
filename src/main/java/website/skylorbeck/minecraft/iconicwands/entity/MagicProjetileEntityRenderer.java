package website.skylorbeck.minecraft.iconicwands.entity;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.DragonFireballEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;
import website.skylorbeck.minecraft.iconicwands.Iconicwands;

public class MagicProjetileEntityRenderer
        extends EntityRenderer<MagicProjectileEntity> {

    public MagicProjetileEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    public Identifier getTexture(MagicProjectileEntity arrowEntity) {
        return Iconicwands.getId("textures/entity/magic.png");
    }

    @Override
    public void render(MagicProjectileEntity persistentProjectileEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.scale(0.5f, 0.5f,0.5f);
        matrixStack.multiply(this.dispatcher.getRotation());
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0f));
        MatrixStack.Entry entry = matrixStack.peek();
        Matrix4f matrix4f = entry.getPositionMatrix();
        Matrix3f matrix3f = entry.getNormalMatrix();
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutoutNoCull(getTexture(persistentProjectileEntity)));
        produceVertex(vertexConsumer, matrix4f, matrix3f, i, 0f, 0f, 0, 0.2f);
        produceVertex(vertexConsumer, matrix4f, matrix3f, i, 1f, 0f, 0.2f, 0.2f);
        produceVertex(vertexConsumer, matrix4f, matrix3f, i, 1f, 1f, 0.2f, 0);
        produceVertex(vertexConsumer, matrix4f, matrix3f, i, 0f, 1f, 0, 0);
        matrixStack.pop();
        super.render(persistentProjectileEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }
    private static void produceVertex(VertexConsumer vertexConsumer, Matrix4f positionMatrix, Matrix3f normalMatrix, int light, float x, float y, float textureU, float textureV) {
        vertexConsumer.vertex(positionMatrix, x-0.4f , y-0.3f, 0.0f).color(255, 255, 255, 255).texture(textureU, textureV).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normalMatrix, 0.0f, 1.0f, 0.0f).next();
    }
}