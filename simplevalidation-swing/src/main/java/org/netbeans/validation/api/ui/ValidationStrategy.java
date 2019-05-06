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

/**
 * Determines what event on the component will trigger running validation.
 * For example, if validation of a text component's content involves expensive
 * calculation which could slow down the UI, you may want to use ON_FOCUS_LOSS;
 * however ON_CHANGE_OR_ACTION (reacting every time a key is typed) provides
 * more satisfactory user experience.
 *
 * @author Tim Boudreau
 */
public enum ValidationStrategy {
    /** Use whatever is generally most appropriate for this component */
    DEFAULT,
    /** Validate when focus is lost */
    ON_FOCUS_LOSS,
    /** Validate on a document change or action performed or change event */
    ON_CHANGE_OR_ACTION,
    /** Validate using JComponent.setInputVerifier */
    INPUT_VERIFIER,
}
