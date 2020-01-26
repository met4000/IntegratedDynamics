package org.cyclops.integrateddynamics.part.aspect;

import com.google.common.collect.Lists;
import com.google.common.math.DoubleMath;
import com.google.common.math.Stats;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.IParticleData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.state.properties.NoteBlockInstrument;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.cyclops.cyclopscore.datastructure.DimPos;
import org.cyclops.cyclopscore.helper.FluidHelpers;
import org.cyclops.cyclopscore.helper.LocationHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.helper.TileHelpers;
import org.cyclops.integrateddynamics.GeneralConfig;
import org.cyclops.integrateddynamics.IntegratedDynamics;
import org.cyclops.integrateddynamics.Reference;
import org.cyclops.integrateddynamics.api.evaluate.EvaluationException;
import org.cyclops.integrateddynamics.api.evaluate.IValueInterface;
import org.cyclops.integrateddynamics.api.evaluate.variable.IValue;
import org.cyclops.integrateddynamics.api.network.IEnergyConsumingNetworkElement;
import org.cyclops.integrateddynamics.api.part.PartPos;
import org.cyclops.integrateddynamics.api.part.PartTarget;
import org.cyclops.integrateddynamics.api.part.aspect.AspectUpdateType;
import org.cyclops.integrateddynamics.api.part.aspect.IAspectRead;
import org.cyclops.integrateddynamics.api.part.aspect.IAspectRegistry;
import org.cyclops.integrateddynamics.api.part.aspect.IAspectWrite;
import org.cyclops.integrateddynamics.api.part.aspect.property.IAspectProperties;
import org.cyclops.integrateddynamics.capability.network.EnergyNetworkConfig;
import org.cyclops.integrateddynamics.capability.valueinterface.ValueInterfaceConfig;
import org.cyclops.integrateddynamics.core.evaluate.operator.Operators;
import org.cyclops.integrateddynamics.core.evaluate.operator.PositionedOperator;
import org.cyclops.integrateddynamics.core.evaluate.operator.PositionedOperatorRecipeHandlerInputs;
import org.cyclops.integrateddynamics.core.evaluate.operator.PositionedOperatorRecipeHandlerOutput;
import org.cyclops.integrateddynamics.core.evaluate.operator.PositionedOperatorRecipeHandlerRecipeByInput;
import org.cyclops.integrateddynamics.core.evaluate.operator.PositionedOperatorRecipeHandlerRecipeByOutput;
import org.cyclops.integrateddynamics.core.evaluate.operator.PositionedOperatorRecipeHandlerRecipesByInput;
import org.cyclops.integrateddynamics.core.evaluate.operator.PositionedOperatorRecipeHandlerRecipesByOutput;
import org.cyclops.integrateddynamics.core.evaluate.variable.*;
import org.cyclops.integrateddynamics.core.helper.EnergyHelpers;
import org.cyclops.integrateddynamics.core.helper.Helpers;
import org.cyclops.integrateddynamics.core.part.aspect.build.AspectBuilder;
import org.cyclops.integrateddynamics.core.part.aspect.build.IAspectValuePropagator;
import org.cyclops.integrateddynamics.network.packet.SpeakTextPacket;
import org.cyclops.integrateddynamics.part.aspect.read.AspectReadBuilders;
import org.cyclops.integrateddynamics.part.aspect.write.AspectWriteBuilders;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

/**
 * Collection of all aspects.
 * @author rubensworks
 */
public class Aspects {

    public static final IAspectRegistry REGISTRY = IntegratedDynamics._instance.getRegistryManager().getRegistry(IAspectRegistry.class);

    public static void load() {}

    public static final class Read {

        public static final class Audio {

            public static final IAspectRead<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_HARP_NOTE =
                    AspectReadBuilders.Audio.forInstrument(NoteBlockInstrument.HARP)
                            .handle(AspectReadBuilders.PROP_GET_INTEGER).buildRead();
            public static final IAspectRead<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_BASEDRUM_NOTE =
                    AspectReadBuilders.Audio.forInstrument(NoteBlockInstrument.BASEDRUM)
                            .handle(AspectReadBuilders.PROP_GET_INTEGER).buildRead();
            public static final IAspectRead<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_SNARE_NOTE =
                    AspectReadBuilders.Audio.forInstrument(NoteBlockInstrument.SNARE)
                            .handle(AspectReadBuilders.PROP_GET_INTEGER).buildRead();
            public static final IAspectRead<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_HAT_NOTE =
                    AspectReadBuilders.Audio.forInstrument(NoteBlockInstrument.HAT)
                            .handle(AspectReadBuilders.PROP_GET_INTEGER).buildRead();
            public static final IAspectRead<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_BASS_NOTE =
                    AspectReadBuilders.Audio.forInstrument(NoteBlockInstrument.BASS)
                            .handle(AspectReadBuilders.PROP_GET_INTEGER).buildRead();
            public static final IAspectRead<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_FLUTE_NOTE =
                    AspectReadBuilders.Audio.forInstrument(NoteBlockInstrument.FLUTE)
                            .handle(AspectReadBuilders.PROP_GET_INTEGER).buildRead();
            public static final IAspectRead<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_BELL_NOTE =
                    AspectReadBuilders.Audio.forInstrument(NoteBlockInstrument.BELL)
                            .handle(AspectReadBuilders.PROP_GET_INTEGER).buildRead();
            public static final IAspectRead<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_GUITAR_NOTE =
                    AspectReadBuilders.Audio.forInstrument(NoteBlockInstrument.GUITAR)
                            .handle(AspectReadBuilders.PROP_GET_INTEGER).buildRead();
            public static final IAspectRead<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_CHIME_NOTE =
                    AspectReadBuilders.Audio.forInstrument(NoteBlockInstrument.CHIME)
                            .handle(AspectReadBuilders.PROP_GET_INTEGER).buildRead();
            public static final IAspectRead<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_XYLOPHONE_NOTE =
                    AspectReadBuilders.Audio.forInstrument(NoteBlockInstrument.XYLOPHONE)
                            .handle(AspectReadBuilders.PROP_GET_INTEGER).buildRead();
            public static final IAspectRead<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_IRON_XYLOPHONE_NOTE =
                    AspectReadBuilders.Audio.forInstrument(NoteBlockInstrument.IRON_XYLOPHONE)
                            .handle(AspectReadBuilders.PROP_GET_INTEGER).buildRead();
            public static final IAspectRead<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_COW_BELL_NOTE =
                    AspectReadBuilders.Audio.forInstrument(NoteBlockInstrument.COW_BELL)
                            .handle(AspectReadBuilders.PROP_GET_INTEGER).buildRead();
            public static final IAspectRead<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_DIDGERIDOO_NOTE =
                    AspectReadBuilders.Audio.forInstrument(NoteBlockInstrument.DIDGERIDOO)
                            .handle(AspectReadBuilders.PROP_GET_INTEGER).buildRead();
            public static final IAspectRead<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_BIT_NOTE =
                    AspectReadBuilders.Audio.forInstrument(NoteBlockInstrument.BIT)
                            .handle(AspectReadBuilders.PROP_GET_INTEGER).buildRead();
            public static final IAspectRead<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_BANJO_NOTE =
                    AspectReadBuilders.Audio.forInstrument(NoteBlockInstrument.BANJO)
                            .handle(AspectReadBuilders.PROP_GET_INTEGER).buildRead();
            public static final IAspectRead<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_PLING_NOTE =
                    AspectReadBuilders.Audio.forInstrument(NoteBlockInstrument.PLING)
                            .handle(AspectReadBuilders.PROP_GET_INTEGER).buildRead();

        }

        public static final class Block {
            public static final IAspectRead<ValueTypeBoolean.ValueBoolean, ValueTypeBoolean> BOOLEAN_BLOCK =
                    AspectReadBuilders.Block.BUILDER_BOOLEAN.handle(
                        dimPos -> dimPos.getWorld(true).getBlockState(dimPos.getBlockPos()).getBlock() != Blocks.AIR
                    ).withUpdateType(AspectUpdateType.BLOCK_UPDATE)
                            .handle(AspectReadBuilders.PROP_GET_BOOLEAN, "block").buildRead();
            public static final IAspectRead<ValueTypeString.ValueString, ValueTypeString> INTEGER_DIMENSION =
                    AspectReadBuilders.Block.BUILDER_STRING.handle(AspectReadBuilders.World.PROP_GET_WORLD).handle(
                            world -> world.getDimension().getType().getRegistryName().toString()
                    ).withUpdateType(AspectUpdateType.NEVER)
                            .handle(AspectReadBuilders.PROP_GET_STRING, "dimension").buildRead();
            public static final IAspectRead<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_POSX =
                    AspectReadBuilders.Block.BUILDER_INTEGER.handle(AspectReadBuilders.World.PROP_GET_POS).handle(
                        BlockPos::getX
                    ).withUpdateType(AspectUpdateType.NEVER)
                            .handle(AspectReadBuilders.PROP_GET_INTEGER, "posx").buildRead();
            public static final IAspectRead<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_POSY =
                    AspectReadBuilders.Block.BUILDER_INTEGER.handle(AspectReadBuilders.World.PROP_GET_POS).handle(
                        BlockPos::getY
                    ).withUpdateType(AspectUpdateType.NEVER)
                            .handle(AspectReadBuilders.PROP_GET_INTEGER, "posy").buildRead();
            public static final IAspectRead<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_POSZ =
                    AspectReadBuilders.Block.BUILDER_INTEGER.handle(AspectReadBuilders.World.PROP_GET_POS).handle(
                        BlockPos::getZ
                    ).withUpdateType(AspectUpdateType.NEVER)
                            .handle(AspectReadBuilders.PROP_GET_INTEGER, "posz").buildRead();
            public static final IAspectRead<ValueObjectTypeBlock.ValueBlock, ValueObjectTypeBlock> BLOCK =
                    AspectReadBuilders.Block.BUILDER_BLOCK
                            .handle(
                        dimPos -> dimPos.getWorld(true).getBlockState(dimPos.getBlockPos())
                    ).withUpdateType(AspectUpdateType.BLOCK_UPDATE)
                            .handle(AspectReadBuilders.PROP_GET_BLOCK).buildRead();
            public static final IAspectRead<ValueTypeNbt.ValueNbt, ValueTypeNbt> NBT =
                    AspectReadBuilders.Block.BUILDER_NBT.handle(dimPos -> {
                        TileEntity tile = dimPos.getWorld(true).getTileEntity(dimPos.getBlockPos());
                        try {
                            if (tile != null) {
                                return tile.write(new CompoundNBT());
                            }
                        } catch (Exception e) {
                            // Catch possible errors
                        }
                        return null;
                    }).handle(AspectReadBuilders.PROP_GET_NBT, "tile").buildRead();
            public static final IAspectRead<ValueTypeString.ValueString, ValueTypeString> STRING_BIOME =
                    AspectReadBuilders.Block.BUILDER_STRING
                            .handle(
                                    dimPos -> dimPos.getWorld(true).getBiome(dimPos.getBlockPos()).getRegistryName().toString()
                            ).withUpdateType(AspectUpdateType.BLOCK_UPDATE)
                            .handle(AspectReadBuilders.PROP_GET_STRING, "biome").buildRead();
            public static final IAspectRead<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_LIGHT =
                    AspectReadBuilders.Block.BUILDER_INTEGER.handle(dimPos -> dimPos.getWorld(true).getLight(dimPos.getBlockPos()))
                            .handle(AspectReadBuilders.PROP_GET_INTEGER, "light").buildRead();
        }

        public static final class Entity {
            public static final IAspectRead<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_ITEMFRAMEROTATION =
                    AspectReadBuilders.Entity.BUILDER_INTEGER_ALL
                            .handle(AspectReadBuilders.World.PROP_GET_ITEMFRAME)
                            .handle(itemFrame -> itemFrame != null ? itemFrame.getRotation() : 0)
                            .handle(AspectReadBuilders.PROP_GET_INTEGER, "itemframerotation").buildRead();
            public static final IAspectRead<ValueTypeList.ValueList, ValueTypeList> LIST_ENTITIES =
                    AspectReadBuilders.Entity.BUILDER_LIST.handle(dimPos -> {
                        List<net.minecraft.entity.Entity> entities = dimPos.getWorld(true).getEntitiesInAABBexcluding(null,
                                new AxisAlignedBB(dimPos.getBlockPos(), dimPos.getBlockPos().add(1, 1, 1)), EntityPredicates.NOT_SPECTATING);
                        return ValueTypeList.ValueList.ofList(ValueTypes.OBJECT_ENTITY, Lists.transform(entities,
                            ValueObjectTypeEntity.ValueEntity::of
                        ));
                    }).appendKind("entities").buildRead();
            public static final IAspectRead<ValueTypeList.ValueList, ValueTypeList> LIST_PLAYERS =
                    AspectReadBuilders.Entity.BUILDER_LIST.handle(dimPos -> {
                        List<net.minecraft.entity.Entity> entities = dimPos.getWorld(true).getEntitiesInAABBexcluding(null,
                                new AxisAlignedBB(dimPos.getBlockPos(), dimPos.getBlockPos().add(1, 1, 1)), Helpers.SELECTOR_IS_PLAYER);
                        return ValueTypeList.ValueList.ofList(ValueTypes.OBJECT_ENTITY, Lists.transform(entities,
                            ValueObjectTypeEntity.ValueEntity::of
                        ));
                    }).appendKind("players").buildRead();

            public static final IAspectRead<ValueObjectTypeEntity.ValueEntity, ValueObjectTypeEntity> ENTITY =
                    AspectReadBuilders.Entity.BUILDER_ENTITY.withProperties(AspectReadBuilders.LIST_PROPERTIES).handle(input -> {
                        int i = input.getRight().getValue(AspectReadBuilders.PROPERTY_LISTINDEX).getRawValue();
                        DimPos dimPos = input.getLeft().getTarget().getPos();
                        List<net.minecraft.entity.Entity> entities = dimPos.getWorld(true).getEntitiesInAABBexcluding(null,
                                new AxisAlignedBB(dimPos.getBlockPos(), dimPos.getBlockPos().add(1, 1, 1)), EntityPredicates.NOT_SPECTATING);
                        return ValueObjectTypeEntity.ValueEntity.of(i < entities.size() ? entities.get(i) : null);
                    }).buildRead();

            public static final IAspectRead<ValueObjectTypeItemStack.ValueItemStack, ValueObjectTypeItemStack> ITEMSTACK_ITEMFRAMECONTENTS =
                    AspectReadBuilders.Entity.BUILDER_ITEMSTACK
                            .handle(AspectReadBuilders.World.PROP_GET_ITEMFRAME)
                            .handle(itemFrame -> itemFrame != null ? itemFrame.getDisplayedItem() : ItemStack.EMPTY)
                            .handle(AspectReadBuilders.PROP_GET_ITEMSTACK, "itemframecontents").buildRead();
        }

        public static final class ExtraDimensional {

            private static final Random RANDOM = new Random();

            public static final IAspectRead<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_RANDOM =
                    AspectReadBuilders.ExtraDimensional.BUILDER_INTEGER.handle(
                        minecraft -> RANDOM.nextInt()
                    ).handle(AspectReadBuilders.PROP_GET_INTEGER, "random").buildRead();
            public static final IAspectRead<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_PLAYERCOUNT =
                    AspectReadBuilders.ExtraDimensional.BUILDER_INTEGER.handle(
                        MinecraftServer::getCurrentPlayerCount
                    ).handle(AspectReadBuilders.PROP_GET_INTEGER, "playercount").buildRead();
            public static final IAspectRead<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_TICKTIME =
                    AspectReadBuilders.ExtraDimensional.BUILDER_INTEGER.handle(
                        minecraft -> (int) DoubleMath.mean(minecraft.tickTimeArray)
                    ).handle(AspectReadBuilders.PROP_GET_INTEGER, "ticktime").buildRead();

            public static final IAspectRead<ValueTypeDouble.ValueDouble, ValueTypeDouble> DOUBLE_TPS =
                    AspectReadBuilders.ExtraDimensional.BUILDER_DOUBLE.handle(
                            minecraft -> Math.min(20, Stats.meanOf(minecraft.tickTimeArray) / 1000)
                    ).handle(AspectReadBuilders.PROP_GET_DOUBLE, "tps").buildRead();

            public static final IAspectRead<ValueTypeList.ValueList, ValueTypeList> LIST_PLAYERS =
                    AspectReadBuilders.ExtraDimensional.BUILDER_LIST.handle(
                        minecraft -> ValueTypeList.ValueList.ofList(ValueTypes.OBJECT_ENTITY, Lists.transform(minecraft.getPlayerList().getPlayers(),
                                ValueObjectTypeEntity.ValueEntity::of))
                    ).appendKind("players").buildRead();

        }

        public static final class Fluid {

            public static final IAspectRead<ValueTypeBoolean.ValueBoolean, ValueTypeBoolean> BOOLEAN_FULL =
                    AspectReadBuilders.Fluid.BUILDER_BOOLEAN.handle(tankInfo -> {
                        boolean allFull = true;
                        for (int i = 0; i < tankInfo.getTanks(); i++) {
                            if (tankInfo.getFluidInTank(i).isEmpty() && tankInfo.getTankCapacity(i) > 0
                                    || (!tankInfo.getFluidInTank(i).isEmpty() && tankInfo.getFluidInTank(i).getAmount() < tankInfo.getTankCapacity(i))) {
                                allFull = false;
                            }
                        }
                        return allFull;
                    }).handle(AspectReadBuilders.PROP_GET_BOOLEAN, "full").buildRead();
            public static final IAspectRead<ValueTypeBoolean.ValueBoolean, ValueTypeBoolean> BOOLEAN_EMPTY =
                    AspectReadBuilders.Fluid.BUILDER_BOOLEAN.handle(tankInfo -> {
                        for (int i = 0; i < tankInfo.getTanks(); i++) {
                            if (!tankInfo.getFluidInTank(i).isEmpty() && tankInfo.getTankCapacity(i) > 0
                                    || (!tankInfo.getFluidInTank(i).isEmpty() && tankInfo.getFluidInTank(i).getAmount() < tankInfo.getTankCapacity(i))) {
                                return false;
                            }
                        }
                        return true;
                    }).handle(AspectReadBuilders.PROP_GET_BOOLEAN, "empty").buildRead();
            public static final IAspectRead<ValueTypeBoolean.ValueBoolean, ValueTypeBoolean> BOOLEAN_NONEMPTY =
                    AspectReadBuilders.Fluid.BUILDER_BOOLEAN.handle(tankInfo -> {
                        boolean hasFluid = false;
                        for (int i = 0; i < tankInfo.getTanks(); i++) {
                            if (!tankInfo.getFluidInTank(i).isEmpty() && tankInfo.getFluidInTank(i).getAmount() > 0) {
                                hasFluid = true;
                            }
                        }
                        return hasFluid;
                    }).handle(AspectReadBuilders.PROP_GET_BOOLEAN, "nonempty").buildRead();
            public static final IAspectRead<ValueTypeBoolean.ValueBoolean, ValueTypeBoolean> BOOLEAN_APPLICABLE =
                    AspectReadBuilders.Fluid.BUILDER_BOOLEAN.handle(
                        tankInfo -> tankInfo.getTanks() > 0
                    ).handle(AspectReadBuilders.PROP_GET_BOOLEAN, "applicable").buildRead();

            public static final IAspectRead<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_AMOUNT =
                    AspectReadBuilders.Fluid.BUILDER_INTEGER_ACTIVATABLE.handle(AspectReadBuilders.Fluid.PROP_GET_FLUIDSTACK).handle(
                            FluidStack::getAmount
                    ).handle(AspectReadBuilders.PROP_GET_INTEGER, "amount").buildRead();
            public static final IAspectRead<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_AMOUNTTOTAL =
                    AspectReadBuilders.Fluid.BUILDER_INTEGER.handle(tankInfo -> {
                        int amount = 0;
                        for (int i = 0; i < tankInfo.getTanks(); i++) {
                            amount += tankInfo.getFluidInTank(i).getAmount();
                        }
                        return amount;
                    }).handle(AspectReadBuilders.PROP_GET_INTEGER, "totalamount").buildRead();
            public static final IAspectRead<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_CAPACITY =
                    AspectReadBuilders.Fluid.BUILDER_INTEGER_ACTIVATABLE.handle(
                        tankInfo -> tankInfo != null ? tankInfo.getLeft().getTankCapacity(tankInfo.getRight()) : 0
                    ).handle(AspectReadBuilders.PROP_GET_INTEGER, "capacity").buildRead();
            public static final IAspectRead<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_CAPACITYTOTAL =
                    AspectReadBuilders.Fluid.BUILDER_INTEGER.handle(tankInfo -> {
                        int capacity = 0;
                        for (int i = 0; i < tankInfo.getTanks(); i++) {
                            capacity += tankInfo.getTankCapacity(i);
                        }
                        return capacity;
                    }).handle(AspectReadBuilders.PROP_GET_INTEGER, "totalcapacity").buildRead();
            public static final IAspectRead<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_TANKS =
                    AspectReadBuilders.Fluid.BUILDER_INTEGER.handle(
                            IFluidHandler::getTanks
                    ).handle(AspectReadBuilders.PROP_GET_INTEGER, "tanks").buildRead();

            public static final IAspectRead<ValueTypeDouble.ValueDouble, ValueTypeDouble> DOUBLE_FILLRATIO =
                    AspectReadBuilders.Fluid.BUILDER_DOUBLE_ACTIVATABLE.handle(tankInfo -> {
                        if(tankInfo == null) {
                            return 0D;
                        }
                        double amount = tankInfo.getLeft().getFluidInTank(tankInfo.getRight()).getAmount();
                        return amount / (double) tankInfo.getLeft().getTankCapacity(tankInfo.getRight());
                    }).handle(AspectReadBuilders.PROP_GET_DOUBLE, "fillratio").buildRead();

            public static final IAspectRead<ValueTypeList.ValueList, ValueTypeList> LIST_TANKFLUIDS =
                    AspectReadBuilders.BUILDER_LIST.appendKind("fluid").handle(AspectReadBuilders.Fluid.PROP_GET_LIST_FLUIDSTACKS, "fluidstacks").buildRead();
            public static final IAspectRead<ValueTypeList.ValueList, ValueTypeList> LIST_TANKCAPACITIES =
                    AspectReadBuilders.BUILDER_LIST.appendKind("fluid").handle(AspectReadBuilders.Fluid.PROP_GET_LIST_CAPACITIES, "capacities").buildRead();

            public static final IAspectRead<ValueObjectTypeFluidStack.ValueFluidStack, ValueObjectTypeFluidStack> FLUIDSTACK =
                    AspectReadBuilders.BUILDER_OBJECT_FLUIDSTACK
                            .handle(AspectReadBuilders.Fluid.PROP_GET_ACTIVATABLE, "fluid").withProperties(AspectReadBuilders.Fluid.PROPERTIES)
                            .handle(AspectReadBuilders.Fluid.PROP_GET_FLUIDSTACK).handle(AspectReadBuilders.PROP_GET_FLUIDSTACK).buildRead();

            public static final IAspectRead<ValueObjectTypeFluidStack.ValueFluidStack, ValueObjectTypeFluidStack> BLOCK =
                    AspectReadBuilders.BUILDER_OBJECT_FLUIDSTACK
                            .handle(AspectReadBuilders.Block.PROP_GET, "block")
                            .handle(dimPos -> {
                                BlockState blockState = dimPos.getWorld(true).getBlockState(dimPos.getBlockPos());
                                net.minecraft.block.Block block = blockState.getBlock();
                                if (block instanceof FlowingFluidBlock) {
                                    return new FluidStack(((FlowingFluidBlock) block).getFluid(), FluidHelpers.BUCKET_VOLUME);
                                }
                                return FluidStack.EMPTY;
                            })
                            .handle(AspectReadBuilders.PROP_GET_FLUIDSTACK).buildRead();

        }

        public static final class Inventory {
            public static final IAspectRead<ValueTypeBoolean.ValueBoolean, ValueTypeBoolean> BOOLEAN_FULL =
                    AspectReadBuilders.Inventory.BUILDER_BOOLEAN.handle(inventory -> {
                        if(inventory != null) {
                            for (int i = 0; i < inventory.getSlots(); i++) {
                                ItemStack itemStack = inventory.getStackInSlot(i);
                                if (itemStack.isEmpty()) {
                                    return false;
                                }
                            }
                        }
                        return true;
                    }).handle(AspectReadBuilders.PROP_GET_BOOLEAN, "full").buildRead();
            public static final IAspectRead<ValueTypeBoolean.ValueBoolean, ValueTypeBoolean> BOOLEAN_EMPTY =
                    AspectReadBuilders.Inventory.BUILDER_BOOLEAN.handle(inventory -> {
                        if(inventory != null) {
                            for(int i = 0; i < inventory.getSlots(); i++) {
                                ItemStack itemStack = inventory.getStackInSlot(i);
                                if(!itemStack.isEmpty()) {
                                    return false;
                                }
                            }
                        }
                        return true;
                    }).handle(AspectReadBuilders.PROP_GET_BOOLEAN, "empty").buildRead();
            public static final IAspectRead<ValueTypeBoolean.ValueBoolean, ValueTypeBoolean> BOOLEAN_NONEMPTY =
                    AspectReadBuilders.Inventory.BUILDER_BOOLEAN.handle(inventory -> {
                        if(inventory != null) {
                            for(int i = 0; i < inventory.getSlots(); i++) {
                                ItemStack itemStack = inventory.getStackInSlot(i);
                                if(!itemStack.isEmpty()) {
                                    return true;
                                }
                            }
                        }
                        return false;
                    }).handle(AspectReadBuilders.PROP_GET_BOOLEAN, "nonempty").buildRead();
            public static final IAspectRead<ValueTypeBoolean.ValueBoolean, ValueTypeBoolean> BOOLEAN_APPLICABLE =
                    AspectReadBuilders.Inventory.BUILDER_BOOLEAN.handle(
                        Objects::nonNull
                    ).handle(AspectReadBuilders.PROP_GET_BOOLEAN, "applicable").buildRead();

            public static final IAspectRead<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_COUNT =
                    AspectReadBuilders.Inventory.BUILDER_INTEGER.handle(inventory -> {
                        int count = 0;
                        if(inventory != null) {
                            for (int i = 0; i < inventory.getSlots(); i++) {
                                ItemStack itemStack = inventory.getStackInSlot(i);
                                if (!itemStack.isEmpty()) {
                                    count += itemStack.getCount();
                                }
                            }
                        }
                        return count;
                    }).handle(AspectReadBuilders.PROP_GET_INTEGER, "count").buildRead();
            public static final IAspectRead<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_SLOTS =
                    AspectReadBuilders.Inventory.BUILDER_INTEGER.handle(
                        inventory -> inventory != null ? inventory.getSlots() : 0
                    ).handle(AspectReadBuilders.PROP_GET_INTEGER, "slots").buildRead();
            public static final IAspectRead<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_SLOTSFILLED =
                    AspectReadBuilders.Inventory.BUILDER_INTEGER.handle(inventory -> {
                        int count = 0;
                        if(inventory != null) {
                            for (int i = 0; i < inventory.getSlots(); i++) {
                                ItemStack itemStack = inventory.getStackInSlot(i);
                                if (!itemStack.isEmpty()) {
                                    count++;
                                }
                            }
                        }
                        return count;
                    }).handle(AspectReadBuilders.PROP_GET_INTEGER, "slotsfilled").buildRead();

            public static final IAspectRead<ValueTypeDouble.ValueDouble, ValueTypeDouble> DOUBLE_FILLRATIO =
                    AspectReadBuilders.Inventory.BUILDER_DOUBLE.handle(inventory -> {
                        int count = 0;
                        if(inventory != null) {
                            for (int i = 0; i < inventory.getSlots(); i++) {
                                ItemStack itemStack = inventory.getStackInSlot(i);
                                if (!itemStack.isEmpty()) {
                                    count++;
                                }
                            }
                        }
                        return ((double) count) / (double) (inventory != null ? inventory.getSlots() : 1);
                    }).handle(AspectReadBuilders.PROP_GET_DOUBLE, "fillratio").buildRead();

            public static final IAspectRead<ValueTypeList.ValueList, ValueTypeList> LIST_ITEMSTACKS =
                    AspectReadBuilders.BUILDER_LIST.appendKind("inventory")
                            .handle(AspectReadBuilders.Inventory.PROP_GET_LIST, "itemstacks").buildRead();

            public static final IAspectRead<ValueObjectTypeItemStack.ValueItemStack, ValueObjectTypeItemStack> OBJECT_ITEM_STACK_SLOT =
                    AspectReadBuilders.Inventory.BUILDER_ITEMSTACK.handle(AspectReadBuilders.PROP_GET_ITEMSTACK).buildRead();

        }

        public static final class Machine {

            public static final IAspectRead<ValueTypeBoolean.ValueBoolean, ValueTypeBoolean> BOOLEAN_ISWORKER =
                    AspectReadBuilders.Machine.BUILDER_WORKER_BOOLEAN.handle(
                        Objects::nonNull
                    ).handle(AspectReadBuilders.PROP_GET_BOOLEAN, "isworker").buildRead();
            public static final IAspectRead<ValueTypeBoolean.ValueBoolean, ValueTypeBoolean> BOOLEAN_HASWORK =
                    AspectReadBuilders.Machine.BUILDER_WORKER_BOOLEAN.handle(
                        worker -> worker != null && worker.hasWork()
                    ).handle(AspectReadBuilders.PROP_GET_BOOLEAN, "haswork").buildRead();
            public static final IAspectRead<ValueTypeBoolean.ValueBoolean, ValueTypeBoolean> BOOLEAN_CANWORK =
                    AspectReadBuilders.Machine.BUILDER_WORKER_BOOLEAN.handle(
                        worker -> worker != null && worker.canWork()
                    ).handle(AspectReadBuilders.PROP_GET_BOOLEAN, "canwork").buildRead();
            public static final IAspectRead<ValueTypeBoolean.ValueBoolean, ValueTypeBoolean> BOOLEAN_ISWORKING =
                    AspectReadBuilders.Machine.BUILDER_WORKER_BOOLEAN.handle(
                        worker -> worker != null && worker.canWork() && worker.hasWork()
                    ).handle(AspectReadBuilders.PROP_GET_BOOLEAN, "isworking").buildRead();

            public static final IAspectRead<ValueTypeBoolean.ValueBoolean, ValueTypeBoolean> BOOLEAN_ISTEMPERATURE =
                    AspectReadBuilders.Machine.BUILDER_TEMPERATURE_BOOLEAN.handle(
                        Objects::nonNull
                    ).handle(AspectReadBuilders.PROP_GET_BOOLEAN, "istemperature").buildRead();
            public static final IAspectRead<ValueTypeDouble.ValueDouble, ValueTypeDouble> DOUBLE_TEMPERATURE =
                    AspectReadBuilders.Machine.BUILDER_TEMPERATURE_DOUBLE.handle(
                        temperature -> temperature != null ? temperature.getTemperature() : 0
                    ).handle(AspectReadBuilders.PROP_GET_DOUBLE, "temperature").buildRead();
            public static final IAspectRead<ValueTypeDouble.ValueDouble, ValueTypeDouble> DOUBLE_MAXTEMPERATURE =
                    AspectReadBuilders.Machine.BUILDER_TEMPERATURE_DOUBLE.handle(
                        temperature -> temperature != null ? temperature.getMaximumTemperature() : 0
                    ).handle(AspectReadBuilders.PROP_GET_DOUBLE, "maxtemperature").buildRead();
            public static final IAspectRead<ValueTypeDouble.ValueDouble, ValueTypeDouble> DOUBLE_MINTEMPERATURE =
                    AspectReadBuilders.Machine.BUILDER_TEMPERATURE_DOUBLE.handle(
                        temperature -> temperature != null ? temperature.getMinimumTemperature() : 0
                    ).handle(AspectReadBuilders.PROP_GET_DOUBLE, "mintemperature").buildRead();
            public static final IAspectRead<ValueTypeDouble.ValueDouble, ValueTypeDouble> DOUBLE_DEFAULTTEMPERATURE =
                    AspectReadBuilders.Machine.BUILDER_TEMPERATURE_DOUBLE.handle(
                        temperature -> temperature != null ? temperature.getDefaultTemperature() : 0
                    ).handle(AspectReadBuilders.PROP_GET_DOUBLE, "defaulttemperature").buildRead();

            public static final IAspectRead<ValueTypeBoolean.ValueBoolean, ValueTypeBoolean> BOOLEAN_ISRECIPEHANDLER =
                    AspectReadBuilders.Machine.BUILDER_RECIPE_HANDLER_BOOLEAN
                            .handle(Objects::nonNull)
                            .handle(AspectReadBuilders.PROP_GET_BOOLEAN, "applicable").buildRead();
            public static final IAspectRead<ValueTypeList.ValueList, ValueTypeList> LIST_GETRECIPES =
                    AspectReadBuilders.Machine.BUILDER_RECIPE_HANDLER_LIST.handle(
                            input -> ValueTypeList.ValueList.ofFactory(new ValueTypeListProxyPositionedRecipes(
                                    input.getLeft().getTarget().getPos(), input.getLeft().getTarget().getSide()))).appendKind("recipes").buildRead();
            public static final IAspectRead<ValueTypeOperator.ValueOperator, ValueTypeOperator> OPERATOR_GETRECIPEOUTPUT =
                    AspectReadBuilders.Machine.BUILDER_RECIPE_HANDLER_OPERATOR.handle(
                            input -> ValueTypeOperator.ValueOperator.of(new PositionedOperatorRecipeHandlerOutput<>(
                                    input.getLeft().getTarget().getPos(), input.getLeft().getTarget().getSide()
                            ))).appendKind("recipeoutputbyinput").buildRead();
            static {
                Operators.REGISTRY.registerSerializer(new PositionedOperator.Serializer(
                        PositionedOperatorRecipeHandlerOutput.class, new ResourceLocation(Reference.MOD_ID, "positioned_recipe_handler_output")));
            }
            public static final IAspectRead<ValueTypeOperator.ValueOperator, ValueTypeOperator> OPERATOR_GETRECIPEINPUTS =
                    AspectReadBuilders.Machine.BUILDER_RECIPE_HANDLER_OPERATOR.handle(input ->
                            ValueTypeOperator.ValueOperator.of(new PositionedOperatorRecipeHandlerInputs<>(
                                    input.getLeft().getTarget().getPos(), input.getLeft().getTarget().getSide()
                            ))).appendKind("recipeinputsbyoutput").buildRead();
            static {
                Operators.REGISTRY.registerSerializer(new PositionedOperator.Serializer(
                        PositionedOperatorRecipeHandlerInputs.class, new ResourceLocation(Reference.MOD_ID, "positioned_recipe_handler_inputs")));
            }
            public static final IAspectRead<ValueTypeOperator.ValueOperator, ValueTypeOperator> OPERATOR_GETRECIPESBYINPUT =
                    AspectReadBuilders.Machine.BUILDER_RECIPE_HANDLER_OPERATOR.handle(
                            input -> ValueTypeOperator.ValueOperator.of(new PositionedOperatorRecipeHandlerRecipesByInput<>(
                                    input.getLeft().getTarget().getPos(), input.getLeft().getTarget().getSide()
                            ))).appendKind("recipesbyinput").buildRead();
            static {
                Operators.REGISTRY.registerSerializer(new PositionedOperator.Serializer(
                        PositionedOperatorRecipeHandlerRecipesByInput.class, new ResourceLocation(Reference.MOD_ID, "positioned_recipe_handler_recipes_by_input")));
            }
            public static final IAspectRead<ValueTypeOperator.ValueOperator, ValueTypeOperator> OPERATOR_GETRECIPESBYOUTPUT =
                    AspectReadBuilders.Machine.BUILDER_RECIPE_HANDLER_OPERATOR.handle(
                            input -> ValueTypeOperator.ValueOperator.of(new PositionedOperatorRecipeHandlerRecipesByOutput<>(
                                    input.getLeft().getTarget().getPos(), input.getLeft().getTarget().getSide()
                            ))).appendKind("recipesbyoutput").buildRead();
            static {
                Operators.REGISTRY.registerSerializer(new PositionedOperator.Serializer(
                        PositionedOperatorRecipeHandlerRecipesByOutput.class, new ResourceLocation(Reference.MOD_ID, "positioned_recipe_handler_recipes_by_output")));
            }
            public static final IAspectRead<ValueTypeOperator.ValueOperator, ValueTypeOperator> OPERATOR_GETRECIPEBYINPUT =
                    AspectReadBuilders.Machine.BUILDER_RECIPE_HANDLER_OPERATOR.handle(
                            input -> ValueTypeOperator.ValueOperator.of(new PositionedOperatorRecipeHandlerRecipeByInput<>(
                                    input.getLeft().getTarget().getPos(), input.getLeft().getTarget().getSide()
                            ))).appendKind("recipebyinput").buildRead();
            static {
                Operators.REGISTRY.registerSerializer(new PositionedOperator.Serializer(
                        PositionedOperatorRecipeHandlerRecipeByInput.class, new ResourceLocation(Reference.MOD_ID, "positioned_recipe_handler_recipe_by_input")));
            }
            public static final IAspectRead<ValueTypeOperator.ValueOperator, ValueTypeOperator> OPERATOR_GETRECIPEBYOUTPUT =
                    AspectReadBuilders.Machine.BUILDER_RECIPE_HANDLER_OPERATOR.handle(
                            input -> ValueTypeOperator.ValueOperator.of(new PositionedOperatorRecipeHandlerRecipeByOutput<>(
                                    input.getLeft().getTarget().getPos(), input.getLeft().getTarget().getSide()
                            ))).appendKind("recipebyoutput").buildRead();
            static {
                Operators.REGISTRY.registerSerializer(new PositionedOperator.Serializer(
                        PositionedOperatorRecipeHandlerRecipeByOutput.class, new ResourceLocation(Reference.MOD_ID, "positioned_recipe_handler_recipe_by_output")));
            }

            public static final IAspectValuePropagator<Pair<PartTarget, IAspectProperties>, IEnergyStorage>
                    PROP_GET = input -> EnergyHelpers.getEnergyStorage(input.getLeft().getTarget()).orElse(null);

            public static final AspectBuilder<ValueTypeBoolean.ValueBoolean, ValueTypeBoolean, IEnergyStorage>
                    BUILDER_BOOLEAN = AspectReadBuilders.BUILDER_BOOLEAN.handle(PROP_GET, "fe");
            public static final AspectBuilder<ValueTypeInteger.ValueInteger, ValueTypeInteger, IEnergyStorage>
                    BUILDER_INTEGER = AspectReadBuilders.BUILDER_INTEGER.handle(PROP_GET, "fe");
            public static final AspectBuilder<ValueTypeDouble.ValueDouble, ValueTypeDouble, IEnergyStorage>
                    BUILDER_DOUBLE = AspectReadBuilders.BUILDER_DOUBLE.handle(PROP_GET, "fe");

            public static final IAspectRead<ValueTypeBoolean.ValueBoolean, ValueTypeBoolean> BOOLEAN_ISENERGY =
                    BUILDER_BOOLEAN.handle(
                        Objects::nonNull
                    ).handle(AspectReadBuilders.PROP_GET_BOOLEAN, "applicable").buildRead();
            public static final IAspectRead<ValueTypeBoolean.ValueBoolean, ValueTypeBoolean> BOOLEAN_ISENERGYRECEIVER =
                    BUILDER_BOOLEAN.handle(
                        data -> data != null && data.canReceive()
                    ).handle(AspectReadBuilders.PROP_GET_BOOLEAN, "isreceiver").buildRead();
            public static final IAspectRead<ValueTypeBoolean.ValueBoolean, ValueTypeBoolean> BOOLEAN_ISENERGYPROVIDER =
                    BUILDER_BOOLEAN.handle(
                        data -> data != null && data.canExtract()
                    ).handle(AspectReadBuilders.PROP_GET_BOOLEAN, "isprovider").buildRead();
            public static final IAspectRead<ValueTypeBoolean.ValueBoolean, ValueTypeBoolean> BOOLEAN_CANEXTRACTENERGY =
                    BUILDER_BOOLEAN.handle(
                        data -> data != null && data.extractEnergy(1, true) == 1
                    ).handle(AspectReadBuilders.PROP_GET_BOOLEAN, "canextract").buildRead();
            public static final IAspectRead<ValueTypeBoolean.ValueBoolean, ValueTypeBoolean> BOOLEAN_CANINSERTENERGY =
                    BUILDER_BOOLEAN.handle(
                        data -> data != null && data.receiveEnergy(1, true) == 1
                    ).handle(AspectReadBuilders.PROP_GET_BOOLEAN, "caninsert").buildRead();
            public static final IAspectRead<ValueTypeBoolean.ValueBoolean, ValueTypeBoolean> BOOLEAN_ISENERGYFULL =
                    BUILDER_BOOLEAN.handle(
                        data -> data != null && data.getEnergyStored() == data.getMaxEnergyStored()
                    ).handle(AspectReadBuilders.PROP_GET_BOOLEAN, "isfull").buildRead();
            public static final IAspectRead<ValueTypeBoolean.ValueBoolean, ValueTypeBoolean> BOOLEAN_ISENERGYEMPTY =
                    BUILDER_BOOLEAN.handle(
                        data -> data != null && data.getEnergyStored() == 0
                    ).handle(AspectReadBuilders.PROP_GET_BOOLEAN, "isempty").buildRead();
            public static final IAspectRead<ValueTypeBoolean.ValueBoolean, ValueTypeBoolean> BOOLEAN_ISENERGYNONEMPTY =
                    BUILDER_BOOLEAN.handle(
                        data -> data != null && data.getEnergyStored() != 0
                    ).handle(AspectReadBuilders.PROP_GET_BOOLEAN, "isnonempty").buildRead();

            public static final IAspectRead<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_ENERGYSTORED =
                    BUILDER_INTEGER.handle(
                        data -> data != null ? data.getEnergyStored() : 0
                    ).handle(AspectReadBuilders.PROP_GET_INTEGER, "amount").buildRead();
            public static final IAspectRead<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_ENERGYCAPACITY =
                    BUILDER_INTEGER.handle(
                        data -> data != null ? data.getMaxEnergyStored() : 0
                    ).handle(AspectReadBuilders.PROP_GET_INTEGER, "capacity").buildRead();

            public static final IAspectRead<ValueTypeDouble.ValueDouble, ValueTypeDouble> DOUBLE_ENERGYFILLRATIO =
                    BUILDER_DOUBLE.handle(data -> {
                        if(data != null) {
                            double capacity = (double) data.getMaxEnergyStored();
                            if(capacity == 0.0D) {
                                return 0.0D;
                            }
                            return ((double) data.getEnergyStored()) / capacity;
                        }
                        return 0.0D;
                    }).handle(AspectReadBuilders.PROP_GET_DOUBLE, "fillratio").buildRead();

        }

        public static final class Network {

            public static final IAspectRead<ValueTypeBoolean.ValueBoolean, ValueTypeBoolean> BOOLEAN_APPLICABLE =
                    AspectReadBuilders.Network.BUILDER_BOOLEAN.handle(
                        Objects::nonNull
                    ).handle(AspectReadBuilders.PROP_GET_BOOLEAN, "applicable").buildRead();

            public static final IAspectRead<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_ELEMENT_COUNT =
                    AspectReadBuilders.Network.BUILDER_INTEGER.handle(
                        network -> network != null ? network.getElements().size() : 0
                    ).handle(AspectReadBuilders.PROP_GET_INTEGER, "elementcount").buildRead();
            public static final IAspectRead<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_ENERGY_BATTERY_COUNT =
                    AspectReadBuilders.Network.BUILDER_INTEGER.handle(
                        network -> network != null ? network.getCapability(EnergyNetworkConfig.CAPABILITY)
                                .map(energyNetwork -> energyNetwork.getPositions().size())
                                .orElse(0): 0
                    ).handle(AspectReadBuilders.PROP_GET_INTEGER, "energy").appendKind("batterycount").buildRead();
            public static final IAspectRead<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_ENERGY_STORED =
                    AspectReadBuilders.Network.ENERGY_BUILDER.handle(
                        storage -> storage != null ? storage.getEnergyStored() : 0
                    ).handle(AspectReadBuilders.PROP_GET_INTEGER, "energy").appendKind("stored").buildRead();
            public static final IAspectRead<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_ENERGY_MAX =
                    AspectReadBuilders.Network.ENERGY_BUILDER.handle(
                        storage -> storage != null ? storage.getMaxEnergyStored() : 0
                    ).handle(AspectReadBuilders.PROP_GET_INTEGER, "energy").appendKind("max").buildRead();
            public static final IAspectRead<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_ENERGY_CONSUMPTION_RATE =
                    AspectReadBuilders.Network.BUILDER_INTEGER.handle(
                            network -> network != null && GeneralConfig.energyConsumptionMultiplier > 0
                                    ? network.getElements().stream()
                                    .mapToInt((e) -> e instanceof IEnergyConsumingNetworkElement
                                            ? ((IEnergyConsumingNetworkElement) e).getConsumptionRate() : 0).sum()
                                    * GeneralConfig.energyConsumptionMultiplier : 0
                    ).handle(AspectReadBuilders.PROP_GET_INTEGER, "energy").appendKind("consumptionrate").buildRead();
            public static final IAspectRead<IValue, ValueTypeCategoryAny> ANY_VALUE =
                    AspectReadBuilders.BUILDER_ANY.appendKind("network").handle(
                            data -> {
                                PartPos target = data.getLeft().getTarget();
                                IValueInterface valueInterface = TileHelpers.getCapability(
                                        target.getPos(), target.getSide(), ValueInterfaceConfig.CAPABILITY).orElse(null);
                                if (valueInterface != null) {
                                    return valueInterface.getValue().orElseThrow(() ->
                                            new EvaluationException("No valid value interface value was found."));
                                }
                                throw new EvaluationException("No valid value interface was found.");
                            }
                    ).appendKind("value").buildRead();

        }

        public static final class Redstone {

            public static final IAspectRead<ValueTypeBoolean.ValueBoolean, ValueTypeBoolean> BOOLEAN_LOW =
                    AspectReadBuilders.Redstone.BUILDER_BOOLEAN.handle(
                        input -> input == 0
                    ).withUpdateType(AspectUpdateType.BLOCK_UPDATE)
                            .handle(AspectReadBuilders.PROP_GET_BOOLEAN, "low").buildRead();
            public static final IAspectRead<ValueTypeBoolean.ValueBoolean, ValueTypeBoolean> BOOLEAN_NONLOW =
                    AspectReadBuilders.Redstone.BUILDER_BOOLEAN.handle(
                        input -> input > 0
                    ).withUpdateType(AspectUpdateType.BLOCK_UPDATE)
                            .handle(AspectReadBuilders.PROP_GET_BOOLEAN, "nonlow").buildRead();
            public static final IAspectRead<ValueTypeBoolean.ValueBoolean, ValueTypeBoolean> BOOLEAN_HIGH =
                    AspectReadBuilders.Redstone.BUILDER_BOOLEAN.handle(
                        input -> input == 15
                    ).withUpdateType(AspectUpdateType.BLOCK_UPDATE)
                            .handle(AspectReadBuilders.PROP_GET_BOOLEAN, "high").buildRead();
            public static final IAspectRead<ValueTypeBoolean.ValueBoolean, ValueTypeBoolean> BOOLEAN_CLOCK =
                    AspectReadBuilders.Redstone.BUILDER_BOOLEAN_CLOCK.handle(AspectReadBuilders.PROP_GET_BOOLEAN, "clock").buildRead();

            public static final IAspectRead<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_VALUE =
                    AspectReadBuilders.Redstone.BUILDER_INTEGER.withUpdateType(AspectUpdateType.BLOCK_UPDATE)
                            .handle(AspectReadBuilders.PROP_GET_INTEGER, "value").buildRead();
            public static final IAspectRead<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_COMPARATOR =
                    AspectReadBuilders.Redstone.BUILDER_INTEGER_COMPARATOR.withUpdateType(AspectUpdateType.BLOCK_UPDATE)
                            .handle(AspectReadBuilders.PROP_GET_INTEGER, "comparator").buildRead();

        }

        public static final class World {

            public static final IAspectRead<ValueTypeBoolean.ValueBoolean, ValueTypeBoolean> BOOLEAN_WEATHER_CLEAR =
                    AspectReadBuilders.World.BUILDER_BOOLEAN.handle(AspectReadBuilders.World.PROP_GET_WORLD).handle(
                        world -> !world.isRaining()
                    ).handle(AspectReadBuilders.PROP_GET_BOOLEAN, "weather").appendKind("clear").buildRead();
            public static final IAspectRead<ValueTypeBoolean.ValueBoolean, ValueTypeBoolean> BOOLEAN_WEATHER_RAINING =
                    AspectReadBuilders.World.BUILDER_BOOLEAN.handle(AspectReadBuilders.World.PROP_GET_WORLD).handle(
                        net.minecraft.world.World::isRaining
                    ).handle(AspectReadBuilders.PROP_GET_BOOLEAN, "weather").appendKind("raining").buildRead();
            public static final IAspectRead<ValueTypeBoolean.ValueBoolean, ValueTypeBoolean> BOOLEAN_WEATHER_THUNDER =
                    AspectReadBuilders.World.BUILDER_BOOLEAN.handle(AspectReadBuilders.World.PROP_GET_WORLD).handle(
                        net.minecraft.world.World::isThundering
                    ).handle(AspectReadBuilders.PROP_GET_BOOLEAN, "weather").appendKind("thunder").buildRead();
            public static final IAspectRead<ValueTypeBoolean.ValueBoolean, ValueTypeBoolean> BOOLEAN_ISDAY =
                    AspectReadBuilders.World.BUILDER_BOOLEAN.handle(AspectReadBuilders.World.PROP_GET_WORLD).handle(
                            net.minecraft.world.World::isDaytime
                    ).handle(AspectReadBuilders.PROP_GET_BOOLEAN, "isday").buildRead();
            public static final IAspectRead<ValueTypeBoolean.ValueBoolean, ValueTypeBoolean> BOOLEAN_ISNIGHT =
                    AspectReadBuilders.World.BUILDER_BOOLEAN.handle(AspectReadBuilders.World.PROP_GET_WORLD).handle(
                        world -> !world.isDaytime()
                    ).handle(AspectReadBuilders.PROP_GET_BOOLEAN, "isnight").buildRead();

            public static final IAspectRead<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_RAINCOUNTDOWN =
                    AspectReadBuilders.World.BUILDER_INTEGER.handle(AspectReadBuilders.World.PROP_GET_WORLD).handle(
                        world -> world.getWorldInfo().getRainTime()
                    ).handle(AspectReadBuilders.PROP_GET_INTEGER, "raincountdown").buildRead();
            public static final IAspectRead<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_TICKTIME =
                    AspectReadBuilders.World.BUILDER_INTEGER.handle(AspectReadBuilders.World.PROP_GET_WORLD).handle(
                        world -> (int) DoubleMath.mean(ServerLifecycleHooks.getCurrentServer().getTickTime(world.getDimension().getType()))
                    ).handle(AspectReadBuilders.PROP_GET_INTEGER, "ticktime").buildRead();
            public static final IAspectRead<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_DAYTIME =
                    AspectReadBuilders.World.BUILDER_INTEGER.handle(AspectReadBuilders.World.PROP_GET_WORLD).handle(
                        world -> (int) (world.getDayTime() % MinecraftHelpers.MINECRAFT_DAY)
                    ).handle(AspectReadBuilders.PROP_GET_INTEGER, "daytime").buildRead();
            public static final IAspectRead<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_LIGHTLEVEL =
                    AspectReadBuilders.World.BUILDER_INTEGER.handle(
                        dimPos -> dimPos.getWorld(true).getLight(dimPos.getBlockPos())
                    ).handle(AspectReadBuilders.PROP_GET_INTEGER, "lightlevel").buildRead();

            public static final IAspectRead<ValueTypeDouble.ValueDouble, ValueTypeDouble> DOUBLE_TPS =
                    AspectReadBuilders.World.BUILDER_DOUBLE.handle(AspectReadBuilders.World.PROP_GET_WORLD).handle(
                            world -> Math.min(20, Stats.meanOf(ServerLifecycleHooks.getCurrentServer().getTickTime(world.getDimension().getType())) / 1000)
                    ).handle(AspectReadBuilders.PROP_GET_DOUBLE, "tps").buildRead();

            public static final IAspectRead<ValueTypeLong.ValueLong, ValueTypeLong> LONG_TIME =
                    AspectReadBuilders.World.BUILDER_LONG.handle(AspectReadBuilders.World.PROP_GET_WORLD).handle(
                        net.minecraft.world.World::getDayTime
                    ).handle(AspectReadBuilders.PROP_GET_LONG, "time").buildRead();
            public static final IAspectRead<ValueTypeLong.ValueLong, ValueTypeLong> LONG_TOTALTIME =
                    AspectReadBuilders.World.BUILDER_LONG.handle(AspectReadBuilders.World.PROP_GET_WORLD).handle(
                        net.minecraft.world.World::getGameTime
                    ).handle(AspectReadBuilders.PROP_GET_LONG, "totaltime").buildRead();

            public static final IAspectRead<ValueTypeString.ValueString, ValueTypeString> STRING_NAME =
                    AspectReadBuilders.World.BUILDER_STRING.handle(AspectReadBuilders.World.PROP_GET_WORLD).handle(
                        world -> world.getWorldInfo().getWorldName()
                    ).handle(AspectReadBuilders.PROP_GET_STRING, "worldname").buildRead();

            public static final IAspectRead<ValueTypeList.ValueList, ValueTypeList> LIST_PLAYERS =
                    AspectReadBuilders.World.BUILDER_LIST.handle(dimPos ->
                            ValueTypeList.ValueList.ofList(ValueTypes.OBJECT_ENTITY, Lists.transform(dimPos.getWorld(true).getPlayers(),
                                ValueObjectTypeEntity.ValueEntity::of))
                    ).appendKind("players").buildRead();

        }

    }

    public static final class Write {

        public static final class Audio {

            public static final IAspectWrite<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_HARP_NOTE =
                    AspectWriteBuilders.Audio.BUILDER_INTEGER_INSTRUMENT
                            .handle(AspectWriteBuilders.Audio.propWithInstrument(NoteBlockInstrument.HARP), "harp")
                            .handle(AspectWriteBuilders.Audio.PROP_SET).buildWrite();
            public static final IAspectWrite<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_BASEDRUM_NOTE =
                    AspectWriteBuilders.Audio.BUILDER_INTEGER_INSTRUMENT
                            .handle(AspectWriteBuilders.Audio.propWithInstrument(NoteBlockInstrument.BASEDRUM), "basedrum")
                            .handle(AspectWriteBuilders.Audio.PROP_SET).buildWrite();
            public static final IAspectWrite<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_SNARE_NOTE =
                    AspectWriteBuilders.Audio.BUILDER_INTEGER_INSTRUMENT
                            .handle(AspectWriteBuilders.Audio.propWithInstrument(NoteBlockInstrument.SNARE), "snare")
                            .handle(AspectWriteBuilders.Audio.PROP_SET).buildWrite();
            public static final IAspectWrite<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_HAT_NOTE =
                    AspectWriteBuilders.Audio.BUILDER_INTEGER_INSTRUMENT
                            .handle(AspectWriteBuilders.Audio.propWithInstrument(NoteBlockInstrument.HAT), "hat")
                            .handle(AspectWriteBuilders.Audio.PROP_SET).buildWrite();
            public static final IAspectWrite<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_BASS_NOTE =
                    AspectWriteBuilders.Audio.BUILDER_INTEGER_INSTRUMENT
                            .handle(AspectWriteBuilders.Audio.propWithInstrument(NoteBlockInstrument.BASS), "bass")
                            .handle(AspectWriteBuilders.Audio.PROP_SET).buildWrite();
            public static final IAspectWrite<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_FLUTE_NOTE =
                    AspectWriteBuilders.Audio.BUILDER_INTEGER_INSTRUMENT
                            .handle(AspectWriteBuilders.Audio.propWithInstrument(NoteBlockInstrument.FLUTE), "flute")
                            .handle(AspectWriteBuilders.Audio.PROP_SET).buildWrite();
            public static final IAspectWrite<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_BELL_NOTE =
                    AspectWriteBuilders.Audio.BUILDER_INTEGER_INSTRUMENT
                            .handle(AspectWriteBuilders.Audio.propWithInstrument(NoteBlockInstrument.BELL), "bell")
                            .handle(AspectWriteBuilders.Audio.PROP_SET).buildWrite();
            public static final IAspectWrite<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_GUITAR_NOTE =
                    AspectWriteBuilders.Audio.BUILDER_INTEGER_INSTRUMENT
                            .handle(AspectWriteBuilders.Audio.propWithInstrument(NoteBlockInstrument.BASS), "guitar")
                            .handle(AspectWriteBuilders.Audio.PROP_SET).buildWrite();
            public static final IAspectWrite<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_CHIME_NOTE =
                    AspectWriteBuilders.Audio.BUILDER_INTEGER_INSTRUMENT
                            .handle(AspectWriteBuilders.Audio.propWithInstrument(NoteBlockInstrument.CHIME), "chime")
                            .handle(AspectWriteBuilders.Audio.PROP_SET).buildWrite();
            public static final IAspectWrite<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_XYLOPHONE_NOTE =
                    AspectWriteBuilders.Audio.BUILDER_INTEGER_INSTRUMENT
                            .handle(AspectWriteBuilders.Audio.propWithInstrument(NoteBlockInstrument.XYLOPHONE), "xylophone")
                            .handle(AspectWriteBuilders.Audio.PROP_SET).buildWrite();
            public static final IAspectWrite<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_IRON_XYLOPHONE_NOTE =
                    AspectWriteBuilders.Audio.BUILDER_INTEGER_INSTRUMENT
                            .handle(AspectWriteBuilders.Audio.propWithInstrument(NoteBlockInstrument.IRON_XYLOPHONE), "iron_xylophone")
                            .handle(AspectWriteBuilders.Audio.PROP_SET).buildWrite();
            public static final IAspectWrite<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_COW_BELL_NOTE =
                    AspectWriteBuilders.Audio.BUILDER_INTEGER_INSTRUMENT
                            .handle(AspectWriteBuilders.Audio.propWithInstrument(NoteBlockInstrument.COW_BELL), "cow_bell")
                            .handle(AspectWriteBuilders.Audio.PROP_SET).buildWrite();
            public static final IAspectWrite<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_DIDGERIDOO_NOTE =
                    AspectWriteBuilders.Audio.BUILDER_INTEGER_INSTRUMENT
                            .handle(AspectWriteBuilders.Audio.propWithInstrument(NoteBlockInstrument.DIDGERIDOO), "didgeridoo")
                            .handle(AspectWriteBuilders.Audio.PROP_SET).buildWrite();
            public static final IAspectWrite<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_BIT_NOTE =
                    AspectWriteBuilders.Audio.BUILDER_INTEGER_INSTRUMENT
                            .handle(AspectWriteBuilders.Audio.propWithInstrument(NoteBlockInstrument.BIT), "bit")
                            .handle(AspectWriteBuilders.Audio.PROP_SET).buildWrite();
            public static final IAspectWrite<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_BANJO_NOTE =
                    AspectWriteBuilders.Audio.BUILDER_INTEGER_INSTRUMENT
                            .handle(AspectWriteBuilders.Audio.propWithInstrument(NoteBlockInstrument.BANJO), "banjo")
                            .handle(AspectWriteBuilders.Audio.PROP_SET).buildWrite();
            public static final IAspectWrite<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_PLING_NOTE =
                    AspectWriteBuilders.Audio.BUILDER_INTEGER_INSTRUMENT
                            .handle(AspectWriteBuilders.Audio.propWithInstrument(NoteBlockInstrument.PLING), "pling")
                            .handle(AspectWriteBuilders.Audio.PROP_SET).buildWrite();

            public static final IAspectWrite<ValueTypeString.ValueString, ValueTypeString> STRING_SOUND =
                    AspectWriteBuilders.Audio.BUILDER_STRING.withProperties(AspectWriteBuilders.Audio.PROPERTIES_SOUND)
                            .handle(input -> {
                                IAspectProperties properties = input.getMiddle();
                                BlockPos pos = input.getLeft().getTarget().getPos().getBlockPos();
                                if(!StringUtils.isNullOrEmpty(input.getRight())) {
                                    float f = (float) properties.getValue(AspectWriteBuilders.Audio.PROP_FREQUENCY).getRawValue();
                                    float volume = (float) properties.getValue(AspectWriteBuilders.Audio.PROP_VOLUME).getRawValue();
                                    SoundEvent soundEvent = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(input.getRight()));

                                    if (soundEvent != null) {
                                        World world = input.getLeft().getTarget().getPos().getWorld(false);
                                        if (world != null) {
                                            world.playSound(null,
                                                    (double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D,
                                                    soundEvent, SoundCategory.RECORDS, volume, f);
                                        }
                                    }
                                }
                                return null;
                            }, "sound").buildWrite();

            public static final IAspectWrite<ValueTypeString.ValueString, ValueTypeString> STRING_TEXT =
                    AspectWriteBuilders.Audio.BUILDER_STRING.withProperties(AspectWriteBuilders.Audio.PROPERTIES_TEXT)
                            .handle(input -> {
                                IAspectProperties properties = input.getMiddle();
                                World world = input.getLeft().getTarget().getPos().getWorld(true);
                                BlockPos pos = input.getLeft().getTarget().getPos().getBlockPos();
                                if(!StringUtils.isNullOrEmpty(input.getRight())) {
                                    int range = properties.getValue(AspectWriteBuilders.Audio.PROP_RANGE).getRawValue();
                                    IntegratedDynamics._instance.getPacketHandler().sendToAllAround(
                                            new SpeakTextPacket(input.getRight()),
                                            LocationHelpers.createTargetPointFromLocation(world, pos, range));
                                }
                                return null;
                            }, "text").buildWrite();

        }

        public static final class Effect {

            public static IAspectWrite<ValueTypeDouble.ValueDouble, ValueTypeDouble> createForParticle(final IParticleData particle) {
                return AspectWriteBuilders.Effect.BUILDER_DOUBLE_PARTICLE.appendKind("particle").appendKind(particle
                        .getType().getRegistryName().toString().toLowerCase(Locale.ROOT).replaceAll(":", "_"))
                        .handle(input -> {
                            double velocity = input.getRight();
                            if (velocity < 0) {
                                return null;
                            }

                            IAspectProperties properties = input.getMiddle();
                            PartPos pos = input.getLeft().getTarget();

                            double x = pos.getPos().getBlockPos().getX() + properties.getValue(AspectWriteBuilders.Effect.PROP_OFFSET_X).getRawValue();
                            double y = pos.getPos().getBlockPos().getY() + properties.getValue(AspectWriteBuilders.Effect.PROP_OFFSET_Y).getRawValue();
                            double z = pos.getPos().getBlockPos().getZ() + properties.getValue(AspectWriteBuilders.Effect.PROP_OFFSET_Z).getRawValue();
                            int numberOfParticles = properties.getValue(AspectWriteBuilders.Effect.PROP_PARTICLES).getRawValue();

                            double xDir = properties.getValue(AspectWriteBuilders.Effect.PROP_SPREAD_X).getRawValue();
                            double yDir = properties.getValue(AspectWriteBuilders.Effect.PROP_SPREAD_Y).getRawValue();
                            double zDir = properties.getValue(AspectWriteBuilders.Effect.PROP_SPREAD_Z).getRawValue();

                            ServerWorld world = ((ServerWorld) pos.getPos().getWorld(false));
                            if (world != null) {
                                world.spawnParticle(particle, x, y, z, numberOfParticles, xDir, yDir, zDir, velocity);
                            }
                            return null;
                        }).buildWrite();
            }

        }

        public static final class Redstone {

            public static final IAspectWrite<ValueTypeBoolean.ValueBoolean, ValueTypeBoolean> BOOLEAN =
                    AspectWriteBuilders.Redstone.BUILDER_BOOLEAN.handle(
                        input -> Triple.of(input.getLeft(), input.getMiddle(), input.getRight() ? 15 : 0)
                    ).handle(AspectWriteBuilders.Redstone.PROP_SET).buildWrite();
            public static final IAspectWrite<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER =
                    AspectWriteBuilders.Redstone.BUILDER_INTEGER.handle(AspectWriteBuilders.Redstone.PROP_SET).buildWrite();
            public static final IAspectWrite<ValueTypeBoolean.ValueBoolean, ValueTypeBoolean> BOOLEAN_PULSE =
                    AspectWriteBuilders.Redstone.BUILDER_BOOLEAN
                            .withProperties(AspectWriteBuilders.Redstone.PROPERTIES_REDSTONE_PULSE)
                            .appendKind("pulse")
                            .handle(input -> Triple.of(input.getLeft(), input.getMiddle(), input.getRight() ? 15 : 0)
                    ).handle(AspectWriteBuilders.Redstone.PROP_SET_PULSE).buildWrite();
            public static final IAspectWrite<ValueTypeInteger.ValueInteger, ValueTypeInteger> INTEGER_PULSE =
                    AspectWriteBuilders.Redstone.BUILDER_INTEGER
                            .withProperties(AspectWriteBuilders.Redstone.PROPERTIES_REDSTONE_PULSE)
                            .appendKind("pulse")
                            .handle(AspectWriteBuilders.Redstone.PROP_SET_PULSE).buildWrite();

        }

    }

}
