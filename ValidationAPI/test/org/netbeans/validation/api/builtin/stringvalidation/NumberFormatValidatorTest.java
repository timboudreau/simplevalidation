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

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;
import org.junit.Test;
import org.netbeans.validation.api.Problems;
import org.netbeans.validation.api.Validator;
import static org.junit.Assert.*;

/**
 *
 * @author Tim Boudreau
 */
public class NumberFormatValidatorTest {

    public NumberFormatValidatorTest() {
    }

    @Test
    public void testValidate() {
        Format nf = new Format() {
            @Override
            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
                return null;
            }

            @Override
            public Object parseObject(String source) throws ParseException {
                if ("good".equals(source)) {
                    return 0D;
                } else {
                    throw new ParseException(source, 0);
                }
            }

            @Override
            public Object parseObject(String source, ParsePosition pos) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

        };
        assertTrue (true);
        Validator<String> v = new FormatValidator (nf);
        Problems p = new Problems();
        v.validate(p, "x", "good");
        assertNull(p.getLeadProblem());
        v.validate(p, "x", "bad");
        assertTrue(p.hasFatal());

    }

}