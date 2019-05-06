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
import org.netbeans.validation.api.Problem;

/**
 * User interface controller which can show the user one problem. This
 * could involve showing an icon and some textual info in a status
 * bar, and/or disabling some OK-button (if the problem is of {@code
 * Severity.FATAL}) etc.
 *
 * <p> For one {@link ValidationGroup} (a group of UI-components
 * validated together), typically one or a few {@code ValidationUI}
 * instances might be used, but one instance of {@code ValidationUI}
 * should only be used with at most one {@code ValidationGroup} -
 * otherwise a new {@code Problem} in one {@code ValidationGroup} will
 * hide any {@code Problem}s in others.
 *
 * <p> Also, typically a {@code ValidationUI} instance is also used
 * for decorating each separate GUI-component that has a Problem. The
 * {@link org.netbeans.validation.api.ui.swing.SwingComponentDecorationFactory} is
 * a factory class creating such {@code ValidationUI} instances for decorating
 * Swing components when there is a validation problem in them.
 *
 * @author Tim Boudreau
 */
public interface ValidationUI {
    /**
     * Sets the {@link Problem} to be displayed to the user. Depending on the
     * severity of the problem, the user interface may want to block the
     * user from continuing until it is fixed (for example, disabling the
     * Next button in a wizard or the OK button in a dialog).
     * @param problem A problem that the user should be shown, which may
     * affect the state of the UI as a whole.  Should never be null.
     */
    public void showProblem(final Problem problem);
    /**
     * Clear the problem shown in this UI.
     */
    public void clearProblem();

    /**
     * Access a ValidationUI instance that does nothing.
     */
    public static final ValidationUI NO_OP = new ValidationUI(){
        @Override
        public void showProblem(Problem problem) {}
        public void clearProblem() {}
    };

}
