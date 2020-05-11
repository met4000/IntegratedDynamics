package org.cyclops.integrateddynamics.api.evaluate;

import net.minecraft.util.text.ITextComponent;

/**
 * Exception to indicate a failed evaluation.
 * @author rubensworks
 */
public class EvaluationException extends Exception {

    private final ITextComponent errorMessage;

    public EvaluationException(ITextComponent errorMessage) {
        super(errorMessage.getString());
        this.errorMessage = errorMessage;
    }

    public ITextComponent getErrorMessage() {
        return errorMessage;
    }
}
