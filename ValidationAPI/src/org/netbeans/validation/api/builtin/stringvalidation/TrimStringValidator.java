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
package org.netbeans.validation.api.builtin.stringvalidation;

import org.netbeans.validation.api.Problems;
import org.netbeans.validation.api.Validator;

/**
 * Validator which wraps another String validator and simply calls trim()
 * on the value before passing it to the other validator.
 *
 * @author Tim Boudreau
 */
final class TrimStringValidator extends StringValidator {
    private final Validator<String> other;

    TrimStringValidator (Validator<String> other) {
        this.other = other;
    }

    @Override
    public void validate(Problems problems, String compName, String model) {
        other.validate(problems, compName, model == null ? null : model.trim());
    }

    @Override
    public String toString() {
        return "TrimStringValidator for " + other; //NOI18N
    }

}
