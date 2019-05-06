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

import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.netbeans.validation.api.Problems;

/**
 *
 * @author Philip Stoehrer
 */
@RunWith(Parameterized.class)
public class MayNotEndWithValidatorTest {

    @Parameters
    public static List<Object[]> parameters() {
        return Arrays.asList(new Object[][]{{
            "foo", // test string that should be validated
            'f', // character that is not allowed at the end
            true // expected result 
        }, {
            "", ' ', true
        }, {
            "foo", 'o', false
        }}
        );
    }
    private final boolean expectValid;
    private final String toValidate;
    private final MayNotEndWithValidator validator;

    public MayNotEndWithValidatorTest(String toValidate, char disallowedChar, boolean expectValid) {
        this.toValidate = toValidate;
        this.validator = new MayNotEndWithValidator(disallowedChar);
        this.expectValid = expectValid;
    }

    @Test
    public void testValidate() {
        Problems problems = new Problems();
        validator.validate(problems, "test", toValidate);
        boolean isValid = problems.getLeadProblem() == null;

        assertEquals("Validation for \"" + toValidate + "\" failed", expectValid, isValid);
    }

}
