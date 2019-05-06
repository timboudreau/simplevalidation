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
import org.netbeans.validation.localization.LocalizationSupport;

/**
 * Does not allow a string to terminate with a particular character
 *
 * @author Tim Boudreau
 */
final class MayNotEndWithValidator extends StringValidator {
    private final char c;
    public MayNotEndWithValidator(char c) {
        this.c = c;
    }

    @Override
    public void validate(Problems problems, String compName, String model) {
        if (model != null && !model.isEmpty() && model.charAt(model.length() - 1) == c) {
            problems.add(LocalizationSupport.getMessage(MayNotEndWithValidator.class,
                    "MAY_NOT_END_WITH", compName, new String(new char[] { c })));
        }
    }

}
