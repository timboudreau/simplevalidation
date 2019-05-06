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
class NonNegativeNumberValidator extends StringValidator {

    @Override
    public void validate(Problems problems, String compName, String text) {
        try {
            double d = Double.parseDouble(text);
            if (d < 0D) {
                String problem = LocalizationSupport.getMessage(
                        NonNegativeNumberValidator.class,
                        "ERR_NEGATIVE_NUMBER", compName); //NOI18N
                problems.add (problem);
            }
        } catch (NumberFormatException e) {
            //do nothing - if someone wants not-a-number validation, they should
            //chain an IntegerDocumentValidator or similar
        }
    }
}
