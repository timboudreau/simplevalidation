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
 *
 * @author Tim Boudreau
 */
final class EmptyStringIllegalValidator extends StringValidator {
    @Override
    public void validate(Problems problems, String compName, String model) {
        if (model.isEmpty()) {
            String message = LocalizationSupport.getMessage(EmptyStringIllegalValidator.class,
                "MSG_MAY_NOT_BE_EMPTY", compName); //NOI18N
            problems.add (message);
        }
    }
}
