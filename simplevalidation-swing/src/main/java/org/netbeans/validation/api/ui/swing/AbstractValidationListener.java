/*
 * Copyright 2010-2019 Tim Boudreau
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.netbeans.validation.api.ui.swing;

import javax.swing.JComponent;
import org.netbeans.validation.api.Problem;
import org.netbeans.validation.api.Problems;
import org.netbeans.validation.api.Validator;
import org.netbeans.validation.api.ui.ValidationListener;
import org.netbeans.validation.api.ui.ValidationUI;


/**
 * Base class for ValidationListeners.
 *
 * @author Tim Boudreau
 */
public abstract class AbstractValidationListener<CompType extends JComponent, T>
        extends ValidationListener<CompType> {
    private final Validator<T> validator;
    /**
     * Create a new AbstractValidationListener for the single component
     * passed here as an argument.  If the component is not expected to
     * live after the validator is detached, you can add this object as a
     * listener to the component in the constructor (but remember that
     * this means the component will reference this validator forever).
     * @param comp
     */
    public AbstractValidationListener(Class<CompType> type, CompType comp, ValidationUI ui, Validator<T> validator) {
        super(type, ui, comp);
        this.validator = validator;
    }

    /**
     * Get the name of the component which should be passed to
     * validate.  The default implementation delegates to
     * <code>nameForComponent</code> which will either return the
     * client-property based name or the result of getName() on
     * the component.
     *
     * @param comp The component
     * @return A localized name
     */
    protected String findComponentName (CompType comp) {
        return SwingValidationGroup.nameForComponent(comp);
    }

    /**
     * Get the model object that will be passed to validate
     * @param comp The component
     * @return The model object
     */
    protected abstract T getModelObject(CompType comp);

    /**
     * Called when validation runs.  The default implementation does nothing;
     * some validators may want to change the visual appearance of the component
     * to indicate an error.
     *
     * @param component The component
     * @param validationResult The result of validation
     */
    protected void onValidate(CompType component, Problem validationResult){}

    @Override
    protected final void performValidation(Problems ps) {
        CompType comp = getTarget();
        if (!comp.isEnabled()) {
            return;
        }
        validator.validate(ps, SwingValidationGroup.nameForComponent(comp),
                 getModelObject(comp)); //XXX generics quirk
        onValidate(getTarget(), ps.getLeadProblem());
    }
}
