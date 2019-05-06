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
import org.netbeans.validation.api.Severity;
import org.netbeans.validation.localization.LocalizationSupport;

/**
 *
 * @author Tim Boudreau
 */
final class FileValidator extends StringValidator {
    private final Type type;
    FileValidator(Type type) {
        this.type = type;
    }

    @Override
    public void validate(Problems problems, String compName, String model) {
        File file = new File (model);
        String key;
        boolean ok;
        switch (type) {
            case MUST_EXIST :
                key = "FILE_DOES_NOT_EXIST"; //NOI18N
                ok = file.exists();
                break;
            case MUST_BE_DIRECTORY :
                key = "FILE_IS_NOT_A_DIRECTORY"; //NOI18N
                ok = file.isDirectory();
                break;
            case MUST_BE_FILE :
                key = "FILE_IS_NOT_A_FILE"; //NOI18N
                ok = file.isFile();
                break;
            case MUST_NOT_EXIST :
                key = "FILE_EXISTS"; //NOI18N
                ok = !file.exists();
                break;
            default :
                throw new AssertionError();
        }
        if (!ok) {
            String problem = LocalizationSupport.getMessage(FileValidator.class, key,
                    file.getName());
            problems.append(problem, Severity.FATAL);
        }
    }
    
    enum Type {
        MUST_EXIST,
        MUST_NOT_EXIST,
        MUST_BE_DIRECTORY,
        MUST_BE_FILE,
    }

}
