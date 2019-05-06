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
package org.netbeans.validation.api.ui;

import org.netbeans.validation.api.Problem;
import org.netbeans.validation.api.Problems;

/**
 * Abstract base class for UI component listeners that can trigger validation when an interesting
 * event occurs, and when requested perform validation of the UI component using a {@link org.netbeans.validation.api.Validator}.
 * <p>
 * Implement whatever UI component listener interface
 * is necessary, add it as a listener to the UI component(s) it should listen to, and
 * then pass the listener to {@code ValidationGroup.add()}.
 * <p>
 * When an event that should
 * trigger validation occurs, let the listener (the subclass of {@code ValidationListener}) call the
 * {@link #performValidation() } method to notfify the simplevalidation infrastructure
 * what's going on. The infrastrure will then usually call the {@link #performValidation(org.netbeans.validation.api.Problems) }
 * method, in which the the listener (the subclass of {@code ValidationItem})
 * should validate the UI-component using a suitable {@link org.netbeans.validation.api.Validator}.
 * <p>
 * Note that one validation listener may not belong to more than one ValidationGroup.
 * <p>
 *
 * @author Tim Boudreau
 * @author Hugo Heden
 */
public abstract class ValidationListener<TargetType> extends ValidationItem implements java.util.EventListener {
    private TargetType target;
    private final Class<TargetType> targetType;
    protected ValidationListener(Class<TargetType> targetType, ValidationUI ui, TargetType component){
        super(ui);
        this.targetType = targetType;
        this.target = component;
    }

    /**
     * Called by subclasses to indicate to the simple-validation infrastructure
     * that user has interacted with the UI-component in a way that makes revalidation
     * needed.
     * <p>
     * This will initiate the validation logic (unless the validation is suspended, see
     * {@link ValidationItem#runWithValidationSuspended(java.lang.Runnable)}:
     * A call to {@link ValidationListener#performValidation(org.netbeans.validation.api.Problems)}
     * will occur.
     * <p>
     * If this results in a {@link Problem},  the {@link ValidationUI}
     * managed by this {@code ValidationListener} (such as an error icon
     * decorating the UI-component) will be activated, indicating the {@code Problem}
     * to the user.
     * <p>
     * If this ValidationListener is added to a {@link ValidationGroup},
     * the latter will update its {@code ValidationUI}:s as well (unless there happens
     * to be a more severe {@code Problem} somewhere else within that {@code ValidationGroup})
     */
//    protected final void performValidation() { // Intended to be called by subclasses
//        super.performValidation();
//    }


    @Override
    final void subtreeRevalidation(){ // Intended to be called by parent ValidationGroup
        if (isSuspended()) {
            return;
        }
        Problems ps = new Problems();
        this.performValidation(ps);
        Problem lead = ps.getLeadProblem();
        super.setCurrentLeadProblem(lead);
    }
    
    /**
     * Called by the simplevalidation infrastructure when it is time to
     * perform the validation. The instance of the class subclassing ValidationListener
     * should have access to the UI-component to validate, and to a validator, and simply call
     * the validator's validate() method with the appropriate arguments.
     * <p>
     * If the component to be validated is disabled (it is greyed out and can't
     * be interacted with) it is usually considered to have no problems.
     */
    protected abstract void performValidation(Problems problems);

    protected final TargetType getTarget() {
        return target;
    }

    Class<TargetType> targetType() {
        return targetType;
    }
}
