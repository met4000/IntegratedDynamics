package org.cyclops.integrateddynamics.core.part.aspect;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.integrateddynamics.IntegratedDynamics;
import org.cyclops.integrateddynamics.Reference;
import org.cyclops.integrateddynamics.api.advancement.criterion.ValuePredicate;
import org.cyclops.integrateddynamics.api.advancement.criterion.VariableFacadePredicate;
import org.cyclops.integrateddynamics.api.advancement.criterion.VariablePredicate;
import org.cyclops.integrateddynamics.api.evaluate.variable.IValueType;
import org.cyclops.integrateddynamics.api.item.IAspectVariableFacade;
import org.cyclops.integrateddynamics.api.item.IVariableFacadeHandlerRegistry;
import org.cyclops.integrateddynamics.api.part.IPartType;
import org.cyclops.integrateddynamics.api.part.aspect.IAspect;
import org.cyclops.integrateddynamics.api.part.aspect.IAspectRead;
import org.cyclops.integrateddynamics.api.part.aspect.IAspectRegistry;
import org.cyclops.integrateddynamics.api.part.aspect.IAspectVariable;
import org.cyclops.integrateddynamics.api.part.aspect.IAspectWrite;
import org.cyclops.integrateddynamics.core.item.AspectVariableFacade;
import org.cyclops.integrateddynamics.part.aspect.Aspects;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Registry for {@link IAspect}.
 * @author rubensworks
 */
public final class AspectRegistry implements IAspectRegistry {

    private static AspectRegistry INSTANCE = new AspectRegistry();
    private static final IAspectVariableFacade INVALID_FACADE = new AspectVariableFacade(false, -1, null);

    private final Map<IPartType, Set<IAspect>> partAspects = new IdentityHashMap<>();
    private final Map<IPartType, Set<IAspectRead>> partReadAspects = new IdentityHashMap<>();
    private final Map<IPartType, Set<IAspectWrite>> partWriteAspects = new IdentityHashMap<>();
    private final Map<IPartType, List<IAspectRead>> partReadAspectsListTransform = new IdentityHashMap<>();
    private final Map<IPartType, List<IAspectWrite>> partWriteAspectsListTransform = new IdentityHashMap<>();
    private final Map<String, IAspect> unlocalizedAspects = Maps.newHashMap();
    private final Map<String, IAspectRead> unlocalizedReadAspects = Maps.newHashMap();
    private final Map<String, IAspectWrite> unlocalizedWriteAspects = Maps.newHashMap();
    @OnlyIn(Dist.CLIENT)
    private Map<IAspect, ResourceLocation> aspectModels;

    private AspectRegistry() {
        IntegratedDynamics._instance.getRegistryManager().getRegistry(IVariableFacadeHandlerRegistry.class).registerHandler(this);
        if(MinecraftHelpers.isClientSide()) {
            aspectModels = new IdentityHashMap<>();
        }
    }

    /**
     * @return The unique instance.
     */
    public static AspectRegistry getInstance() {
        return INSTANCE;
    }

    @Override
    public IAspect register(IPartType partType, IAspect aspect) {
        registerSubAspectType(partType, aspect, partAspects, unlocalizedAspects);
        if(aspect instanceof IAspectRead) {
            registerSubAspectType(partType, (IAspectRead) aspect, partReadAspects, unlocalizedReadAspects);
            partReadAspectsListTransform.put(partType, Lists.newArrayList(partReadAspects.get(partType)));
        }
        if(aspect instanceof IAspectWrite) {
            registerSubAspectType(partType, (IAspectWrite) aspect, partWriteAspects, unlocalizedWriteAspects);
            partWriteAspectsListTransform.put(partType, Lists.newArrayList(partWriteAspects.get(partType)));
        }
        return aspect;
    }

    protected <T extends IAspect> void registerSubAspectType(IPartType partType, T aspect, Map<IPartType,
                                                             Set<T>> partAspects, Map<String, T> unlocalizedAspects) {
        Set<T> aspects = partAspects.get(partType);
        if(aspects == null) {
            aspects = Sets.newLinkedHashSet();
            partAspects.put(partType, aspects);
        }
        aspects.add(aspect);
        unlocalizedAspects.put(aspect.getUniqueName().toString(), aspect);
    }

    @Override
    public void register(IPartType partType, Collection<IAspect> aspects) {
        for(IAspect aspect : aspects) {
            register(partType, aspect);
        }
    }

    @Override
    public Set<IAspect> getAspects(IPartType partType) {
        Set<IAspect> aspects = partAspects.get(partType);
        if(aspects == null) {
            return Collections.unmodifiableSet(Collections.<IAspect>emptySet());
        }
        return Collections.unmodifiableSet(aspects);
    }

    @Override
    public List<IAspectRead> getReadAspects(IPartType partType) {
        List<IAspectRead> aspects = partReadAspectsListTransform.get(partType);
        if (aspects == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(aspects);
    }

    @Override
    public List<IAspectWrite> getWriteAspects(IPartType partType) {
        List<IAspectWrite> aspects = partWriteAspectsListTransform.get(partType);
        if (aspects == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(aspects);
    }

    @Override
    public Set<IAspect> getAspects() {
        return ImmutableSet.copyOf(unlocalizedAspects.values());
    }

    @Override
    public Set<IAspectRead> getReadAspects() {
        return ImmutableSet.copyOf(unlocalizedReadAspects.values());
    }

    @Override
    public Set<IAspectWrite> getWriteAspects() {
        return ImmutableSet.copyOf(unlocalizedWriteAspects.values());
    }

    @Override
    public IAspect getAspect(ResourceLocation name) {
        return unlocalizedAspects.get(name.toString());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void registerAspectModel(IAspect aspect, ResourceLocation modelLocation) {
        aspectModels.put(aspect, modelLocation);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ResourceLocation getAspectModel(IAspect aspect) {
        return aspectModels.get(aspect);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Collection<ResourceLocation> getAspectModels() {
        return Collections.unmodifiableCollection(aspectModels.values());
    }

    @Override
    public ResourceLocation getUniqueName() {
        return new ResourceLocation(Reference.MOD_ID, "aspect");
    }

    @Override
    public IAspectVariableFacade getVariableFacade(int id, CompoundTag tag) {
        if(!tag.contains("partId", Tag.TAG_INT)
                || !tag.contains("aspectName", Tag.TAG_STRING)) {
            return INVALID_FACADE;
        }
        int partId = tag.getInt("partId");
        IAspect aspect = getAspect(new ResourceLocation(tag.getString("aspectName")));
        if(aspect == null) {
            return INVALID_FACADE;
        }
        return new AspectVariableFacade(id, partId, aspect);
    }

    @Override
    public void setVariableFacade(CompoundTag tag, IAspectVariableFacade variableFacade) {
        tag.putInt("partId", variableFacade.getPartId());
        tag.putString("aspectName", variableFacade.getAspect().getUniqueName().toString());
    }

    @Override
    public VariablePredicate deserializeVariablePredicate(JsonObject element, @Nullable IValueType valueType, ValuePredicate valuePredicate) {
        JsonElement aspectElement = element.get("aspect");
        IAspect aspect = null;
        if (aspectElement != null && !aspectElement.isJsonNull()) {
            aspect = Aspects.REGISTRY.getAspect(new ResourceLocation(GsonHelper.getAsString(element, "aspect")));
            if (aspect == null) {
                throw new JsonSyntaxException("Unknown aspect type '" + GsonHelper.getAsString(element, "aspect") + "', valid types are: "
                        + Aspects.REGISTRY.getAspects().stream().map(IAspect::getUniqueName).collect(Collectors.toList()));
            }
        }
        return new AspectVariablePredicate(valueType, valuePredicate, aspect);
    }

    @Override
    public VariableFacadePredicate deserializeVariableFacadePredicate(JsonObject element) {
        return null;
    }

    public static class AspectVariablePredicate extends VariablePredicate<IAspectVariable> {

        private final IAspect aspect;

        public AspectVariablePredicate(@Nullable IValueType valueType, ValuePredicate valuePredicate, @Nullable IAspect aspect) {
            super(IAspectVariable.class, valueType, valuePredicate);
            this.aspect = aspect;
        }

        @Override
        protected boolean testTyped(IAspectVariable variable) {
            return super.testTyped(variable) && (aspect == null || variable.getAspect() == aspect);
        }
    }

    public static class AspectVariableFacadePredicate extends VariableFacadePredicate<IAspectVariableFacade> {

        private final IAspect aspect;

        public AspectVariableFacadePredicate(@Nullable IAspect aspect) {
            super(IAspectVariableFacade.class);
            this.aspect = aspect;
        }

        @Override
        protected boolean testTyped(IAspectVariableFacade variableFacade) {
            return super.testTyped(variableFacade) && (aspect == null || variableFacade.getAspect() == aspect);
        }
    }
}
