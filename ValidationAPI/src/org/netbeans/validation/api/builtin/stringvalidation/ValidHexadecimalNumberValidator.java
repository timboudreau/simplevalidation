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
final class ValidHexadecimalNumberValidator extends StringValidator {

    @Override
    public void validate(Problems problems, String compName, String model) {
        if (model.length() % 2 != 0) {
            problems.append(LocalizationSupport.getMessage(ValidHexadecimalNumberValidator.class,
                    "ODD_LENGTH_HEX", compName)); //NOI18N
            return;
        }
        for (char c : model.toCharArray()) {
            boolean good = (c >= 'A' && c <= 'F') || (c >= 'a' && c <= 'f') ||
                    (c >= '0' && c <= '9');
            if (!good) {
                problems.append (LocalizationSupport.getMessage(ValidHexadecimalNumberValidator.class,
                        "INVALID_HEX", //NOI18N
                        new String(new char[] { c }), compName));
                return;
            }
        }
    }

}
