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

import java.util.regex.Pattern;
import org.netbeans.validation.api.Problems;
import org.netbeans.validation.api.Validator;
import org.netbeans.validation.localization.LocalizationSupport;

/**
 * 
 * @author Tim Boudreau
 */
final class ValidHostNameOrIPValidator extends StringValidator {
    private final HostNameValidator hostVal;
    private final Validator<String> ipVal = StringValidators.IP_ADDRESS;
    ValidHostNameOrIPValidator(boolean allowPort) {
        hostVal = new HostNameValidator(allowPort);
    }

    ValidHostNameOrIPValidator() {
        this(true);
    }

    private static final Pattern NUMBERS = Pattern.compile("^\\d+$");
    @Override
    public void validate(Problems problems, String compName, String model) {
        if (HostNameValidator.isIpV6(model)) {
            new HostNameValidator(true).validate(problems, compName, model);
            return;
        }
        String[] parts = model.split ("\\.");
        boolean hasIntParts = false;
        boolean hasNonIntParts = false;
        if (model.indexOf(" ") > 0 || model.indexOf ("\t") > 0) {
            problems.add (LocalizationSupport.getMessage(ValidHostNameOrIPValidator.class,
                    "HOST_MAY_NOT_CONTAIN_WHITESPACE", compName, model)); //NOI18N
            return;
        }
        if (parts.length == 0) { //the string "."
            problems.add (LocalizationSupport.getMessage(ValidHostNameOrIPValidator.class,
                    "INVALID_HOST_OR_IP", compName, model)); //NOI18N
            return;
        }
        for (int i = 0; i < parts.length; i++) {
            String s = parts[i];
            if (i == parts.length - 1 && s.contains(":")) { //NOI18N
                String[] partAndPort = s.split(":"); //NOI18N
                if (partAndPort.length > 2) {
                    problems.add (LocalizationSupport.getMessage(ValidHostNameOrIPValidator.class,
                            "TOO_MANY_COLONS", compName, model)); //NOI18N
                    return;
                }
                if (partAndPort.length == 0) { //the string ":"
                    problems.add (LocalizationSupport.getMessage(ValidHostNameOrIPValidator.class,
                            "INVALID_HOST_OR_IP", compName, model)); //NOI18N
                    return;
                }
                s = partAndPort[0];
                if (partAndPort.length == 2) {
                    try {
                        Integer.parseInt (partAndPort[1]);
                    } catch (NumberFormatException nfe) {
                        problems.add (LocalizationSupport.getMessage(ValidHostNameOrIPValidator.class,
                            "INVALID_PORT", compName, partAndPort[1])); //NOI18N
                        return;
                    }
                }
            }
            boolean num = NUMBERS.matcher(s).find();
            hasIntParts |= num;
            hasNonIntParts |= !num;
        }
        if(hasNonIntParts){
            hostVal.validate(problems, compName, model);
        } else {
            assert hasIntParts;
            ipVal.validate(problems, compName, model);
        }
    }
}
