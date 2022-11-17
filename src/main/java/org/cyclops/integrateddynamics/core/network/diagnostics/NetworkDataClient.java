package org.cyclops.integrateddynamics.core.network.diagnostics;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Data;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import org.cyclops.cyclopscore.datastructure.DimPos;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.integrateddynamics.api.part.PartPos;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author rubensworks
 */
public class NetworkDataClient {

    private static final Multimap<Integer, ObservablePartData> networkDataParts = ArrayListMultimap.create();
    private static final Multimap<Integer, ObservableObserverData> networkDataObservers = ArrayListMultimap.create();

    public static void setNetworkData(int id, RawNetworkData rawNetworkData) {
        synchronized (networkDataParts) {
            Collection<ObservablePartData> previous = networkDataParts.removeAll(id);

            // The positions that were being rendered previously
            Set<PartPos> previousPositionsWithRender = Sets.newHashSet();
            for (ObservablePartData partData : previous) {
                PartPos pos = partData.toPartPos();
                if (pos != null && NetworkDiagnosticsPartOverlayRenderer.getInstance().hasPartPos(pos)) {
                    previousPositionsWithRender.add(pos);
                }
            }

            if (rawNetworkData != null) {
                List<ObservablePartData> parts = Lists.newArrayList();
                for (RawPartData rawPartData : rawNetworkData.getParts()) {
                    ObservablePartData partData = new ObservablePartData(
                            rawNetworkData.getId(), rawNetworkData.getCables(),
                            rawPartData.getDimension(), rawPartData.getPos(),
                            rawPartData.getSide(), rawPartData.getName(),
                            rawPartData.getLast20TicksDurationNs());
                    parts.add(partData);

                    // Remove this position from the previously rendered list
                    PartPos pos = partData.toPartPos();
                    if (pos != null) {
                        previousPositionsWithRender.remove(pos);
                    }
                }

                // Remove all remaining positions from the renderlist,
                // because those do not exist anymore.
                for (PartPos partPos : previousPositionsWithRender) {
                    NetworkDiagnosticsPartOverlayRenderer.getInstance().removePos(partPos);
                }

                networkDataParts.putAll(id, parts);
            }

            Collection<ObservableObserverData> previousObservers = networkDataObservers.removeAll(id);

            // The positions that were being rendered previously
            Set<PartPos> previousPositionsWithRenderObservers = Sets.newHashSet();
            for (ObservableObserverData partData : previousObservers) {
                PartPos pos = partData.toPartPos();
                if (pos != null && NetworkDiagnosticsPartOverlayRenderer.getInstance().hasPartPos(pos)) {
                    previousPositionsWithRenderObservers.add(pos);
                }
            }

            if (rawNetworkData != null) {
                List<ObservableObserverData> observers = Lists.newArrayList();
                for (RawObserverData rawPartData : rawNetworkData.getObservers()) {
                    ObservableObserverData partData = new ObservableObserverData(
                            rawNetworkData.getId(),
                            rawPartData.getDimension(), rawPartData.getPos(),
                            rawPartData.getSide(), rawPartData.getName(),
                            rawPartData.getLast20TicksDurationNs());
                    observers.add(partData);

                    // Remove this position from the previously rendered list
                    PartPos pos = partData.toPartPos();
                    if (pos != null) {
                        previousPositionsWithRenderObservers.remove(pos);
                    }
                }

                // Remove all remaining positions from the renderlist,
                // because those do not exist anymore.
                for (PartPos partPos : previousPositionsWithRenderObservers) {
                    NetworkDiagnosticsPartOverlayRenderer.getInstance().removePos(partPos);
                }

                networkDataObservers.putAll(id, observers);
            }
        }
    }

    public static String getAsJsonString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject responseObject = new JsonObject();

        responseObject.add("parts", getPartsJson());
        responseObject.add("observers", getObserversJson());

        return gson.toJson(responseObject) + "\n";
    }

    private static JsonElement getPartsJson() {
        JsonArray jsonArray = new JsonArray();
        for (Map.Entry<Integer, ObservablePartData> entry : networkDataParts.entries()) {
            JsonObject jsonPart = new JsonObject();
            ObservablePartData part = entry.getValue();
            jsonPart.addProperty("network", part.getNetworkId());
            jsonPart.addProperty("cables", part.getNetworkCables());
            jsonPart.addProperty("part", part.getName());
            jsonPart.addProperty("ticktime", String.format("%.6f", ((double) part.getLast20TicksDurationNs()) / MinecraftHelpers.SECOND_IN_TICKS / 1000000));
            jsonPart.addProperty("dimension", part.getDimension().location().toString());
            jsonPart.addProperty("position", part.getPos().toShortString());
            jsonPart.addProperty("side", part.getSide().name());
            jsonArray.add(jsonPart);
        }
        return jsonArray;
    }

    private static JsonElement getObserversJson() {
        JsonArray jsonArray = new JsonArray();
        for (Map.Entry<Integer, ObservableObserverData> entry : networkDataObservers.entries()) {
            JsonObject jsonPart = new JsonObject();
            ObservableObserverData observer = entry.getValue();
            jsonPart.addProperty("network", observer.getNetworkId());
            jsonPart.addProperty("part", observer.getName());
            jsonPart.addProperty("ticktime", String.format("%.6f", ((double) observer.getLast20TicksDurationNs()) / MinecraftHelpers.SECOND_IN_TICKS / 1000000));
            jsonPart.addProperty("dimension", observer.getDimension().location().toString());
            jsonPart.addProperty("position", observer.getPos().toShortString());
            jsonPart.addProperty("side", observer.getSide().name());
            jsonArray.add(jsonPart);
        }
        return jsonArray;
    }

    @Data
    private static class ObservablePartData {
        private final int networkId;
        private final int networkCables;
        private final ResourceKey<Level> dimension;
        private final BlockPos pos;
        private final Direction side;
        private final String name;
        private final long last20TicksDurationNs;

        public PartPos toPartPos() {
            Level world = Minecraft.getInstance().level;
            if (getDimension().location().equals(world.dimension().location())) {
                return PartPos.of(DimPos.of(world, getPos()), getSide());
            }
            return null;
        }
    }

    @Data
    private static class ObservableObserverData {
        private final int networkId;
        private final ResourceKey<Level> dimension;
        private final BlockPos pos;
        private final Direction side;
        private final String name;
        private final long last20TicksDurationNs;

        public PartPos toPartPos() {
            Level world = Minecraft.getInstance().level;
            if (getDimension().location().equals(world.dimension().location())) {
                return PartPos.of(DimPos.of(world, getPos()), getSide());
            }
            return null;
        }
    }

}
