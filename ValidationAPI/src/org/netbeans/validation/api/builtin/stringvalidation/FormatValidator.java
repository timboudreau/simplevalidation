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

import java.text.Format;
import java.text.ParseException;
import org.netbeans.validation.api.Problems;
import org.netbeans.validation.localization.LocalizationSupport;

/**
 *
 * @author Tim Boudreau
 */
final class FormatValidator extends StringValidator {
    private final Format fmt;
    FormatValidator(Format fmt) {
        this.fmt = fmt;
    }

    @Override
    public void validate(Problems problems, String compName, String model) {
        try {
            fmt.parseObject(model);
        } catch (ParseException ex) {
            problems.add(LocalizationSupport.getMessage(FormatValidator.class,
                    "MSG_DOES_NOT_MATCH_NUMBER_FORMAT", compName, model)); //NOI18N
        }
    }

}
