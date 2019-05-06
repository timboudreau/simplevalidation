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
package org.netbeans.validation.api;

import org.netbeans.validation.api.builtin.stringvalidation.StringValidators;

/**
 * Validator that can validate some aspect of a component's model, and
 * indicate problems to the user.
 *
 * <p> Note that the enum {@link StringValidators} provides many
 * built-in validators to perform common tasks.
 *
 * @author Tim Boudreau
 */
public interface Validator<T> {
    /**
     * Validate the passed model.  If the component is invalid, this
     * method shall add problems to the passed list.
     *
     * @param problems A list of problems.
     * @param compName The name of the component in question (may be null in some cases)
     * @param model The model in question
     */
    void validate (Problems problems, String compName, T model);
    /**
     * The type of the model object which can be validated.  Necessary due
     * to limitations of the Java implementation of generics, so that
     * model conversions can be done at runtime, and declaratively registered
     * ValidationListeners can be matched with Validator types.
     * <p/>
     * The return value of this method is expected to remain constant
     * throughout the life of this validator.
     * 
     * @return The type of the model object expected.  Note that a validator
     * may be passed a subclass of this type.
     */
    Class<T> modelType();
}
