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

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.netbeans.validation.api.Problems;
import org.netbeans.validation.api.Severity;
import org.netbeans.validation.api.Validator;
import org.netbeans.validation.localization.LocalizationSupport;

/**
 *
 * @author Tim Boudreau
 */
class EmailAddressValidator extends StringValidator {
    private final Validator<String> hv = new ValidHostNameOrIPValidator(false);
    private final Validator<String> spv = new MayNotContainSpacesValidator();
    private final Validator<String> encv = new EncodableInCharsetValidator("US-ASCII");

    static final Pattern ADDRESS_PATTERN = Pattern.compile("(.*?)<(.*)>$"); //NOI18N

    @Override
    public void validate(Problems problems, String compName, String model) {
        Matcher m = ADDRESS_PATTERN.matcher(model);
        String address;
        if (m.lookingAt()) {
            if (m.groupCount() == 2) {
                address = m.group(2);
            } else {
                address = m.group(1);
            }
        } else {
            address = model;
        }
        String[] nameAndHost = address.split("@");
        if (nameAndHost.length == 0) {
            problems.add (LocalizationSupport.getMessage(EmailAddressValidator.class,
                    "NO_AT_SYMBOL", compName, address));
            return;
        }        
        if (nameAndHost.length == 1 && nameAndHost[0].contains("@")) {
            problems.add(LocalizationSupport.getMessage(EmailAddressValidator.class,
                    "EMAIL_MISSING_HOST", compName, nameAndHost[0]));
            return;
        }
        if (nameAndHost.length > 2) {
            problems.add(LocalizationSupport.getMessage(EmailAddressValidator.class,
                    "EMAIL_HAS_>1_@", compName, address));
            return;
        }
        String name = nameAndHost[0];
        if (name.length() == 0) {
            problems.add(LocalizationSupport.getMessage(EmailAddressValidator.class,
                    "EMAIL_MISSING_NAME", compName, name));
            return;
        }
        if (name.length() > 64) {
            problems.add(LocalizationSupport.getMessage(EmailAddressValidator.class,
                    "ADDRESS_MAY_BE_TOO_LONG", compName, name), Severity.WARNING);
        }
        String host = nameAndHost.length >= 2 ? nameAndHost[1] : null;
        if(host == null) {
            problems.add(LocalizationSupport.getMessage(EmailAddressValidator.class,
                    "EMAIL_MISSING_HOST", compName, nameAndHost[0]));
            return;
        }
        hv.validate(problems, compName, host);
        spv.validate(problems, compName, name);
        encv.validate(problems, compName, address);
    }
}
