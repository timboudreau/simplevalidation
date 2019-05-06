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
package org.netbeans.validation.api;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Allows limiting the severity produced by other validators.
 *
 * @author Tim Boudreau
 */
final class CustomLevelValidator<T> extends AbstractValidator<T> {
    private final Validator<T> other;
    private final Severity customSeverity;
    CustomLevelValidator(Severity customSeverity, Validator<T> other) {
        super (other.modelType());
        this.customSeverity = customSeverity;
        this.other = other;
        if (customSeverity == Severity.FATAL) {
            Logger.getLogger(CustomLevelValidator.class.getName()).log(Level.INFO,
                    "Pointless to filter to Severity.FATAL",
                    new IllegalArgumentException());
        }
    }

    @Override
    public void validate(Problems problems, String compName, T model) {
        Problems nue = new Problems();
        other.validate(nue, compName, model);
        List<? extends Problem> l = nue.allProblems();
        for (Problem p : l) {
            if (p.severity().compareTo(customSeverity) > 0) {
                p = new Problem (p.getMessage(), customSeverity);
            }
            problems.append(p);
        }
    }

}
