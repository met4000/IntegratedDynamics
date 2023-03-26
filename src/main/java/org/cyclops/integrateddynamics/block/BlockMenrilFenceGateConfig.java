package org.cyclops.integrateddynamics.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.integrateddynamics.IntegratedDynamics;

/**
 * Config for the Menril Fence Gate
 * @author rubensworks
 *
 */
public class BlockMenrilFenceGateConfig extends BlockConfig {

    public BlockMenrilFenceGateConfig() {
        super(
                IntegratedDynamics._instance,
                "menril_fence_gate",
                eConfig -> new FenceGateBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_CYAN)
                        .strength(2.0F, 3.0F)
                        .sound(SoundType.WOOD), WoodType.OAK) {
                    @Override
                    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                        return 5;
                    }

                    @Override
                    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                        return 20;
                    }
                },
                getDefaultItemConstructor(IntegratedDynamics._instance)
        );
    }
}
