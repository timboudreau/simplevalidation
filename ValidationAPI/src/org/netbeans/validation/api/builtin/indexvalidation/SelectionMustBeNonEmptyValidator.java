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
package org.netbeans.validation.api.builtin.indexvalidation;

import org.netbeans.validation.api.AbstractValidator;
import org.netbeans.validation.api.Severity;
import org.netbeans.validation.api.Problems;

/**
 *
 * @author Hugo Heden
 */
final class SelectionMustBeNonEmptyValidator extends AbstractValidator<Integer[]> {
    SelectionMustBeNonEmptyValidator() {
        super (Integer[].class);
    }

    @Override
    public void validate(Problems problems, String compName, Integer[] selectedItems) {
        if (selectedItems.length == 0) {
            String str = "Something must be selected";
            problems.append(str, Severity.FATAL);
        }
    }

}
