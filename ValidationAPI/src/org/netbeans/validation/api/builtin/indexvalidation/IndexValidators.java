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
package org.netbeans.validation.api.builtin.indexvalidation;
import org.netbeans.validation.api.Problems;
import org.netbeans.validation.api.Validator;

/**
 * An enumeration of validator factories for commonly needed forms of
 * selection validaton. This is useful for validating the user selection in components
 * such as a {@code JList} or a number of {@code AbstractButtons}.
 *
 * @author Tim Boudreau, Hugo Heden
 */
public enum IndexValidators implements Validator<Integer[]> {

    REQUIRE_SELECTION
            ;

    private Validator<Integer[]> instantiate() {
        Validator<Integer[]> result;
        switch (this) {
            case REQUIRE_SELECTION :
                result = new SelectionMustBeNonEmptyValidator();
                break;
            default :
                throw new AssertionError();
        }
        return result;
    }

    @Override public void validate (Problems problems, String compName, Integer[] model) {
        instantiate().validate(problems, compName, model);
    }

    public Class<Integer[]> modelType() {
        return Integer[].class;
    }


}
