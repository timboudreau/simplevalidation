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
import org.junit.Test;
import org.netbeans.validation.api.Problem;
import org.netbeans.validation.api.Problems;
import static org.junit.Assert.*;

/**
 *
 * @author Tim Boudreau
 */
public class EmailAddressValidatorTest {

    @Test
    public void testPattern() throws Exception {
        Pattern p = EmailAddressValidator.ADDRESS_PATTERN;
//        assertTrue (p.matcher("foo@bar.com").matches());
        assertTrue (p.matcher("Foo Bar <foo@bar.com>").matches());
    }

    private Problem doValidate(String emailAddress){
        Problems p = new Problems();
        String compName = "";
        new EmailAddressValidator().validate(p, compName, emailAddress);
        return p.getLeadProblem();
    }

    @Test
    public void testValidate() {
        assertTrue (true);
        // boolean val = doValidate("foo@bar.com");
        assertTrue (doValidate("foo@bar.com") == null);
        assertTrue (doValidate("Foo Bar <foo@bar.com>") == null);
        assertTrue (doValidate(" Foo Bar <foo@bar.com>") == null);
        assertTrue (doValidate(" Foo Bar <foo@192.168.2.1>") == null);

        assertFalse (doValidate("Foo Bar <foo@bar.com> ") == null);
        assertFalse (doValidate(" Foo Bar <foo@bar.com> ") == null);
        assertFalse (doValidate(" foo@bar.com") == null);
        assertFalse (doValidate("foo@") == null);
        assertFalse (doValidate(" foo@.") == null);

        assertFalse (doValidate("Foo Bar < foo@bar.com>") == null);
        assertFalse (doValidate("Foo Bar <foo@>") == null);
        assertFalse (doValidate("Foo Bar < foo@.>") == null);
        assertFalse (doValidate("Foo Bar < foo@128.>") == null);
        assertFalse (doValidate("Foo Bar < foo@128.foo>") == null);

        assertTrue (doValidate("фу бар <foo@bar.com>") == null);

        assertFalse (doValidate("Foo Bar <фу@бар.com>") == null);
        assertFalse (doValidate("Foo Bar <фу@bar.com>") == null);
        assertFalse (doValidate("Foo Bar <foo@bar@baz.com>") == null);

        assertFalse (doValidate("foo@bar.") == null);
        assertFalse (doValidate("фу бар <фу@бар.com>") == null);
        assertFalse (doValidate("фу@бар.com") == null);
        assertFalse (doValidate("Foo Bar <foo@bar.>") == null);
        assertFalse (doValidate("Foo Bar <foo@bar.com >") == null);
        assertTrue (doValidate(" Foo Bar <foo@192.168.2.boo>") == null);
        assertFalse (doValidate("foo@bar.com ") == null);
        assertFalse (doValidate("Foo Bar <foo@bar.com:>") == null);
    }
}