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

import java.net.MalformedURLException;
import java.net.URL;
import org.netbeans.validation.api.Problems;
import org.netbeans.validation.localization.LocalizationSupport;

/**
 *
 * @author Tim Boudreau
 */
class UrlValidator extends StringValidator {

    @Override
    public void validate(Problems problems, String compName, String model) {
        try {
            URL url = new URL (model);
            //java.net.url does not require US-ASCII host names,
            //but the spec does
            String host = url.getHost();
            if (!"".equals(host)) { //NOI18N
                new ValidHostNameOrIPValidator(true).validate(problems,
                        compName, host);
                return;
            }
            String protocol = url.getProtocol();
            if ("mailto".equals(protocol)) { //NOI18N
                String emailAddress = url.toString().substring("mailto:".length()); //NOI18N
                emailAddress = emailAddress == null ? "" : emailAddress;
                new EmailAddressValidator().validate(problems, compName,
                        emailAddress);
            }
        } catch (MalformedURLException e) {
            String problem = LocalizationSupport.getMessage(UrlValidator.class,
                    "URL_NOT_VALID", model); //NOI18N
            problems.add(problem);
        }
    }

}
