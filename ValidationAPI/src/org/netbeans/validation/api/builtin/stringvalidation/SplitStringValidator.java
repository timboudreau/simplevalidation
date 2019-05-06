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
 * A validator of strings that first splits the string in question using the
 * passed regular expression, and then runs another validator over each
 * component string.
 *
 * @author Tim Boudreau
 */
final class SplitStringValidator extends StringValidator {
    private final String regexp;
    private final Validator<String> other;
    public SplitStringValidator(String regexp, Validator<String> other) {
        this.regexp = regexp;
        this.other = other;
    }

    @Override
    public void validate(Problems problems, String compName, String model) {
        String[] components = model.split (regexp);
        for (String component : components) {
            other.validate (problems, compName, component);
        }
    }

    @Override
    public String toString() {
        return "SplitStringValidator for " + other; //NOI18N
    }
}
