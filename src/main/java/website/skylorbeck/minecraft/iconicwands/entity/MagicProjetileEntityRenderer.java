package website.skylorbeck.minecraft.iconicwands.entity;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;

public class MagicProjetileEntityRenderer
        extends ProjectileEntityRenderer<MagicProjectileEntity> {
    public static final Identifier TEXTURE = new Identifier("textures/entity/projectiles/arrow.png");
    public static final Identifier TIPPED_TEXTURE = new Identifier("textures/entity/projectiles/tipped_arrow.png");

    public MagicProjetileEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    public Identifier getTexture(MagicProjectileEntity arrowEntity) {
        return arrowEntity.getColor() > 0 ? TIPPED_TEXTURE : TEXTURE;
    }
}