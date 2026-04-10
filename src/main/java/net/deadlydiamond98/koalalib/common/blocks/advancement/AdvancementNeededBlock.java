package net.deadlydiamond98.koalalib.common.blocks.advancement;

import net.deadlydiamond98.koalalib.init.KoalaLibBlockProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

/**
 * This block is only breakable if the block is placed by a player in survival or the player breaking it has the given advancement
 */
public class AdvancementNeededBlock extends Block implements IAdvancementNeeded {
    public static final BooleanProperty PLAYERMADE = KoalaLibBlockProperties.PLAYER_MADE_PROPERTY;
    private final String advancementID;

    public AdvancementNeededBlock(Settings settings, Identifier advancementID) {
        this(settings, advancementID.toString());
    }

    public AdvancementNeededBlock(Settings settings, String advancementID) {
        super(settings);
        this.advancementID = advancementID;
        setDefaultState(this.getDefaultState().with(PLAYERMADE, false));
    }

    @Nullable @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx).with(PLAYERMADE, isPlayerPlaced(ctx));
    }

    @Override
    public float calcBlockBreakingDelta(BlockState state, PlayerEntity player, BlockView world, BlockPos pos) {
        return hasAdvancment(player, this.advancementID) || state.get(PLAYERMADE) ? super.calcBlockBreakingDelta(state, player, world, pos) : -1;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(PLAYERMADE);
    }
}
