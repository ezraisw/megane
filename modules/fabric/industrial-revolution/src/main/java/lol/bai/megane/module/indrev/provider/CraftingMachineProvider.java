package lol.bai.megane.module.indrev.provider;

import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IDataWriter;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerAccessor;
import mcp.mobius.waila.api.data.ProgressData;
import me.steven.indrev.blockentities.crafters.CraftingMachineBlockEntity;
import me.steven.indrev.components.CraftingComponent;

public class CraftingMachineProvider implements IDataProvider<CraftingMachineBlockEntity<?>> {

    @Override
    public void appendData(IDataWriter data, IServerAccessor<CraftingMachineBlockEntity<?>> accessor, IPluginConfig config) {
        data.add(ProgressData.class, res -> {
            var ratio = 0f;

            for (CraftingComponent<?> component : accessor.getTarget().getCraftingComponents()) {
                float current = component.getProcessTime();
                float max = component.getTotalProcessTime();
                ratio = Math.max(ratio, current / max);
            }

            res.add(ProgressData.ratio(ratio));
        });
    }

}
