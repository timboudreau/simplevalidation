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

import org.netbeans.validation.api.Problems;
import org.netbeans.validation.localization.LocalizationSupport;

/**
 *
 * @author Tim Boudreau
 */
final class IpAddressValidator extends StringValidator {

    @Override
    public void validate(Problems problems, String compName, String s) {
        if (s.startsWith(".") || s.endsWith(".")) { //NOI18N
            problems.add(LocalizationSupport.getMessage(IpAddressValidator.class,
                    "HOST_STARTS_OR_ENDS_WITH_PERIOD", s)); //NOI18N
            return;
        }
        if (s.indexOf(' ') >= 0 || s.indexOf ('\t') >= 0) {
            problems.add(LocalizationSupport.getMessage(IpAddressValidator.class,
                    "IP_ADDRESS_CONTAINS_WHITESPACE", compName, s)); //NOI18N
            return;
        }
        String[] parts = s.split("\\.");
        if (parts.length > 4) {
            problems.add(LocalizationSupport.getMessage(IpAddressValidator.class,
                    "TOO_MANY_LABELS", s)); //NOI18N
            return;
        }
        if( parts.length < 4) {
            problems.add(LocalizationSupport.getMessage(IpAddressValidator.class,
                            "ADDR_PART_BAD", s)); //NOI18N
                    return;
        }
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            if (i == parts.length - 1 && part.indexOf(':') > 0) { //NOI18N
                if (part.endsWith(":")) {
                    problems.add(LocalizationSupport.getMessage(IpAddressValidator.class,
                            "TOO_MANY_COLONS", compName, s)); //NOI18N
                    return;
                }
                String[] pts = part.split(":"); //NOI18N
                try {
                    int addr = Integer.parseInt(pts[0]);
                    if (addr < 0) {
                        problems.add(LocalizationSupport.getMessage(IpAddressValidator.class,
                                "ADDR_PART_NEGATIVE", pts[1])); //NOI18N
                        return;
                    }
                    if (addr > 255) {
                        problems.add(LocalizationSupport.getMessage(IpAddressValidator.class,
                                "ADDR_PART_HIGH", pts[1])); //NOI18N
                        return;
                    }
                } catch (NumberFormatException e) {
                    problems.add(LocalizationSupport.getMessage(IpAddressValidator.class,
                            "ADDR_PART_BAD", pts.length >= 2 ? pts[1] : "''")); //NOI18N
                    return;
                }
                if (pts.length == 2 && pts[1].length() == 0) {
                    problems.add(LocalizationSupport.getMessage(IpAddressValidator.class,
                            "INVALID_PORT", compName, "")); //NOI18N
                    return;
                }
                if (pts.length == 1 ) {
                    problems.add(LocalizationSupport.getMessage(IpAddressValidator.class,
                            "INVALID_PORT", compName, "")); //NOI18N
                    return;
                }
                if (pts.length > 1) {
                    try {
                        int port = Integer.parseInt(pts[1]);
                        if (port < 0) {
                            problems.add(LocalizationSupport.getMessage(IpAddressValidator.class,
                                    "NEGATIVE_PORT", pts[1])); //NOI18N
                            return;
                        } else if (port >= 65536) {
                            problems.add(LocalizationSupport.getMessage(IpAddressValidator.class,
                                    "PORT_TOO_HIGH", pts[1])); //NOI18N
                            return;
                        }
                    } catch (NumberFormatException e) {
                        problems.add(LocalizationSupport.getMessage(IpAddressValidator.class,
                                "INVALID_PORT", compName, pts[1])); //NOI18N
                        return;
                    }
                }
            } else {
                try {
                    int addr = Integer.parseInt(part);
                    if (addr < 0) {
                        problems.add(LocalizationSupport.getMessage(IpAddressValidator.class,
                                "ADDR_PART_NEGATIVE", part)); //NOI18N
                        return;
                    }
                    if (addr > 255) {
                        problems.add(LocalizationSupport.getMessage(IpAddressValidator.class,
                                "ADDR_PART_HIGH", part)); //NOI18N
                        return;
                    }
                } catch (NumberFormatException e) {
                    problems.add(LocalizationSupport.getMessage(IpAddressValidator.class,
                            "ADDR_PART_BAD", part)); //NOI18N
                    return;
                }
            }
        } //for
    }
}
