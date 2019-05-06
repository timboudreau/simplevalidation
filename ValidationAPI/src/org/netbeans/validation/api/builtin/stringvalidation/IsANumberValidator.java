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

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;
import org.netbeans.validation.api.Problems;
import org.netbeans.validation.localization.LocalizationSupport;
/**
 *
 * @author Tim Boudreau
 */
final class IsANumberValidator extends StringValidator {
    private Locale locale;

    IsANumberValidator() {
        this (null);
    }

    IsANumberValidator(Locale l) {
        this.locale = l;
    }

    @Override
    public void validate(Problems problems, String compName, String model) {
        ParsePosition p = new ParsePosition(0);
        NumberFormat.getNumberInstance(locale == null ? Locale.getDefault() :
            locale).parse(model, p);
        if (model.length() != p.getIndex() || p.getErrorIndex() != -1) {
            try {
                Double.valueOf(model);
                if (model.length() != model.trim().length()) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                problems.add(LocalizationSupport.getMessage(IsANumberValidator.class, "NOT_A_NUMBER", model, compName)); //NOI18N
            }
        }
    }

}
