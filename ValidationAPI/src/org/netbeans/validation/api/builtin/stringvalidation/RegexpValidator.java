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

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.netbeans.validation.api.Problems;
import org.netbeans.validation.api.Validator;

/**
 *
 * @author Tim Boudreau
 */
final class RegexpValidator extends StringValidator {
    private final Pattern pattern;
    private final String message;
    private boolean acceptPartialMatches;
    RegexpValidator(String pattern, String message, boolean acceptPartialMatches) {
        this.pattern = Pattern.compile(pattern);
        this.message = message;
        this.acceptPartialMatches = acceptPartialMatches;
    }

    @Override
    public void validate(Problems problems, String compName, String model) {
        Matcher m = pattern.matcher(model);
        boolean result = acceptPartialMatches ? m.lookingAt() : m.matches();
        if (!result) {
            String prb = message;
            prb = MessageFormat.format(prb, new Object[] { compName, model });
            problems.add(prb);
        }
    }

}
