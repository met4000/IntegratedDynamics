package org.cyclops.integrateddynamics.core.evaluate.variable;

import net.minecraft.nbt.INBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import org.cyclops.integrateddynamics.Reference;
import org.cyclops.integrateddynamics.api.evaluate.EvaluationException;
import org.cyclops.integrateddynamics.api.evaluate.variable.IValueType;
import org.cyclops.integrateddynamics.core.logicprogrammer.ValueTypeLPElementBase;

import java.util.List;

/**
 * Dummy value type
 * @author rubensworks
 */
public class DummyValueType implements IValueType<DummyValueType.DummyValue> {

    public static final DummyValueType TYPE = new DummyValueType();

    @Override
    public boolean isCategory() {
        return false;
    }

    @Override
    public boolean isObject() {
        return false;
    }

    @Override
    public DummyValue getDefault() {
        return null;
    }

    @Override
    public String getTypeName() {
        return "boolean";
    }

    @Override
    public ResourceLocation getUniqueName() {
        return new ResourceLocation(Reference.MOD_ID, "dummy");
    }

    @Override
    public String getTranslationKey() {
        return "boolean";
    }

    @Override
    public void loadTooltip(List<ITextComponent> lines, boolean appendOptionalInfo, DummyValue value) {

    }

    @Override
    public ITextComponent toCompactString(DummyValue value) {
        return new StringTextComponent("dummy");
    }

    @Override
    public int getDisplayColor() {
        return 0;
    }

    @Override
    public TextFormatting getDisplayColorFormat() {
        return TextFormatting.WHITE;
    }

    @Override
    public boolean correspondsTo(IValueType<?> valueType) {
        return false;
    }

    @Override
    public INBT serialize(DummyValue value) {
        return null;
    }

    @Override
    public ITextComponent canDeserialize(INBT value) {
        return null;
    }

    @Override
    public DummyValue deserialize(INBT value) {
        return null;
    }

    @Override
    public DummyValue materialize(DummyValue value) {
        return value;
    }

    @Override
    public String toString(DummyValue value) {
        return "";
    }

    @Override
    public DummyValue parseString(String value) throws EvaluationException {
        return DummyValue.of();
    }

    @Override
    public ValueTypeLPElementBase createLogicProgrammerElement() {
        return null;
    }

    public static class DummyValue extends ValueBase {

        private DummyValue() {
            super(TYPE);
        }

        public static DummyValue of() {
            return new DummyValue();
        }

    }

}
