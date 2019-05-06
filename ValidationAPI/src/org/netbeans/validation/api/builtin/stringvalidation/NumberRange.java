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
final class NumberRange extends StringValidator {
    private Number max;
    private Number min;
    NumberRange (Number min, Number max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public void validate(Problems problems, String compName, String model) {
        try {
            double val = Double.parseDouble(model);
            double minn = min.doubleValue();
            double maxx = max.doubleValue();
            boolean result = val >= minn && val <= maxx;
            if (!result) {
                problems.add (LocalizationSupport.getMessage(NumberRange.class,
                        "VALUE_OUT_OF_RANGE", new Object[] { //NOI18N
                        compName, model, min, max})); 
            }
        } catch (NumberFormatException e) {
            //should be handled by another validator
        }
    }

}
