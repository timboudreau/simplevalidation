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
package org.netbeans.validation.api.ui;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.netbeans.validation.api.Problem;

/**
 *
 * @author Tim Boudreau
 */
final class MulticastValidationUI implements ValidationUI {

    private final Set<ValidationUI> real;

    MulticastValidationUI(ValidationUI... real) {
        this.real = new HashSet<ValidationUI>(Arrays.asList(real));
        assert validUIs(real);
    }

    public void add(ValidationUI ui) {
        if (ui == null) {
            throw new NullPointerException();
        }
        assert !contains(ui) : "Already a member: " + ui;
        real.add (ui);
    }

    public void remove(ValidationUI ui) {
        if (ui == null) {
            throw new NullPointerException();
        }
        assert contains(ui) : "Not a member: " + ui;
        real.remove(ui);
    }

    public boolean contains(ValidationUI check) {
        boolean result = real.contains(check);
        if (!result) {
            for (ValidationUI ui : real) {
                if (ui instanceof MulticastValidationUI) {
                    if (result = ((MulticastValidationUI) ui).contains(check)) {
                        break;
                    }
                }
            }
        }
        return result;
    }

    @Override
    public void showProblem(Problem problem) {
        assert real != null;
        for (ValidationUI ui : real) {
            ui.showProblem(problem);
        }
    }

    public boolean validUIs(ValidationUI[] uis) {
        for (int i = 0; i < uis.length; i++) {
            ValidationUI ui = uis[i];
            if (ui == null) {
                throw new NullPointerException("Element " + i + " of ui " +
                        "array is null");
            }
        }
        return true;
    }

    public void clearProblem() {
        assert real != null;
        for (ValidationUI ui : real) {
            ui.clearProblem();
        }
    }

}

