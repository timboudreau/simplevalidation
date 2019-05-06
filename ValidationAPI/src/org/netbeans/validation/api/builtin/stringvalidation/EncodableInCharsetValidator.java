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

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import org.netbeans.validation.api.Problems;
import org.netbeans.validation.localization.LocalizationSupport;

/**
 *
 * @author Tim Boudreau
 */
final class EncodableInCharsetValidator extends StringValidator {

    private final String charsetName;

    EncodableInCharsetValidator(String charsetName) {
        this.charsetName = charsetName;
        //Be fail-fast with respect to exceptions
        Charset.forName(charsetName);
    }

    EncodableInCharsetValidator() {
        this(Charset.defaultCharset().name());
    }

    @Override
    public void validate(Problems problems, String compName, String model) {
        char[] c = model.toCharArray();
        boolean result = true;
        String curr;
        for (int i = 0; i < c.length; i++) {
            curr = new String(new char[]{c[i]});
            try {
                String nue = new String(curr.getBytes(charsetName));
                result = c[i] == nue.charAt(0);
                if (!result) {
                    problems.add(LocalizationSupport.getMessage(
                            EncodableInCharsetValidator.class,
                            "INVALID_CHARACTER", compName, curr, charsetName)); //NOI18N
                    break;
                }
            } catch (UnsupportedEncodingException ex) {
                //Already tested in constructor
                throw new AssertionError(ex);
            }
        }
    }
}

