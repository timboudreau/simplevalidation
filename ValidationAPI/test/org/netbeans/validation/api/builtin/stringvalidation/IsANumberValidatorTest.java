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

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import org.junit.Test;
import org.netbeans.validation.api.Problems;
import org.netbeans.validation.api.Validator;
import static org.junit.Assert.*;

/**
 *
 * @author Tim Boudreau
 */
public class IsANumberValidatorTest {

    public IsANumberValidatorTest() {
    }

    @Test
    public void testValidate() throws ParseException {
        //XXX need to find a locale that does *not* support 100's position separators for
        //this test to be meaningful - both 1,234 and 1.234 are valid numbers
        //in both Locale.US & Locale.GERMAN
        Validator <String> v = new IsANumberValidator ();
        Locale.setDefault(Locale.US);
        Problems p = new Problems();
        v.validate(p, "foo", "1.234");
        assertNull(p.getLeadProblem());

        NumberFormat nf = NumberFormat.getNumberInstance(Locale.getDefault());
        Number n = nf.parse("1.234");
        assertTrue(1.234D == n.doubleValue());

        v.validate(p, "foo", "2.0e7");
        assertNull(p.getLeadProblem());
        
        v.validate(p, "foo", "-0e-0");
        assertNull(p.getLeadProblem());

        p = new Problems();
        v.validate(p, "foo", " 2.0e7 "); // whitespace, should fail
        assertTrue(p.hasFatal());
        p = new Problems();
        v.validate(p, "foo", "10.0blahbla");
        assertTrue(p.hasFatal());
        p = new Problems();
        v.validate(p, "foo", "blahbla10.0");
        assertTrue(p.hasFatal());

        p = new Problems();
        Locale.setDefault(Locale.GERMAN);
        v.validate(p, "foo", "1,234");
        assertNull(p.getLeadProblem());
    }

}