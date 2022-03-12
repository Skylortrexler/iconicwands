package website.skylorbeck.minecraft.iconicwands.blocks;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import website.skylorbeck.minecraft.iconicwands.Declarar;
import website.skylorbeck.minecraft.iconicwands.entity.WandPedestalEntity;
import website.skylorbeck.minecraft.iconicwands.screen.WandBenchScreenHandler;

import java.util.stream.Stream;

public class WandPedestal extends BlockWithEntity {
    public WandPedestal(Settings settings) {
        super(settings);
    }
    public static final DirectionProperty FACING = Properties.FACING;

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }
        WandPedestalEntity entity = (WandPedestalEntity) world.getBlockEntity(pos);
        if (entity != null) {
            if (!entity.getStack(0).isEmpty()) {
                player.getInventory().offerOrDrop(entity.getStack(0));
            } else if (player.getStackInHand(hand).isOf(Declarar.ICONIC_WAND)) {
                entity.setStack(0,player.getStackInHand(hand));
                player.setStackInHand(hand, ItemStack.EMPTY);
            }
            Packet<ClientPlayPacketListener> updatePacket = entity.toUpdatePacket();
            if (updatePacket != null && !player.world.isClient) {
                ((ServerPlayerEntity)player).networkHandler.sendPacket(updatePacket);
            }
            entity.markDirty();
        }
        return ActionResult.CONSUME;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return WandBench.rotateShape(Direction.SOUTH,state.get(FACING),SHAPE);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing());
    }
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    private static final VoxelShape SHAPE = Stream.of(//
            Block.createCuboidShape(3, 0, 3, 13, 2, 13),
            Block.createCuboidShape(4, 2,4, 12, 16, 12)
            ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return Declarar.WAND_PEDESTAL_ENTITY.instantiate(pos,state);
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient) {
            WandPedestalEntity entity = (WandPedestalEntity) world.getBlockEntity(pos);
            if (!entity.getStack(0).isEmpty()) {
                player.getInventory().offerOrDrop(entity.getStack(0));
            }
        }
        super.onBreak(world, pos, state, player);
    }
}
