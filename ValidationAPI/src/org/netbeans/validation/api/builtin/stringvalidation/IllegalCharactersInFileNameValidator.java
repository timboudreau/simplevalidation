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

import java.io.File;
import org.netbeans.validation.api.Problems;
import org.netbeans.validation.localization.LocalizationSupport;

/**
 *
 * @author Tim Boudreau
 */
final class IllegalCharactersInFileNameValidator extends StringValidator {

    @Override
    public void validate(Problems problems, String compName, String text) {
        boolean invalid = text.contains(File.separator) || text.contains(File.pathSeparator);
        if (!invalid && isWindows()) {
            invalid = text.contains(":");
        }
        if( invalid ) {
            problems.append( LocalizationSupport.getMessage (IllegalCharactersInFileNameValidator.class,
                    "ERR_INVALID_FILE_NAME", compName, text)) ; //NOI18N
        }
    }

    public static boolean isWindows() {
        return System.getProperty("os.name").contains("Windows");
    }
}
