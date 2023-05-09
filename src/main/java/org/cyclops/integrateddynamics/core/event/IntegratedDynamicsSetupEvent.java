package org.cyclops.integrateddynamics.core.event;

import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.event.lifecycle.ModLifecycleEvent;

/**
 * This event is invoked during the FMLCommonSetupEvent of Integrated Dynamics,
 * and allows registrations towards Integrated Dynamics to be made.
 * @author rubensworks
 */
public class IntegratedDynamicsSetupEvent extends ModLifecycleEvent {

    public IntegratedDynamicsSetupEvent(final ModContainer container) {
        super(container);
    }

}
