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
package org.netbeans.validation.api;

import java.util.HashSet;
import java.util.Set;
import org.junit.Test;
import static org.junit.Assert.*;
import org.netbeans.validation.api.builtin.stringvalidation.StringValidators;

public class CustomLevelValidatorTest {


    @Test
    public void testValidate() {
        Validator<String> base = ValidatorUtils.merge(StringValidators.REQUIRE_NON_EMPTY_STRING,
                StringValidators.REQUIRE_VALID_INTEGER,
                StringValidators.REQUIRE_NON_NEGATIVE_NUMBER, new AlwaysWarnValidator(),
                new AlwaysInfoValidator());
        
        Validator<String> toTest = ValidatorUtils.limitSeverity(Severity.INFO, base);

        Problems pr = new Problems();
        toTest.validate(pr, "foo", "");
        assertTrue (pr.allProblems().size() > 0);
        Set<Severity> s = new HashSet<Severity>();
        for (Problem p : pr.allProblems()) {
            s.add(p.severity());
            assertEquals (Severity.INFO, p.severity());
        }
        assertFalse(pr.hasFatal());
        assertEquals(1, s.size());
        assertTrue (s.contains(Severity.INFO));

        toTest = ValidatorUtils.limitSeverity(Severity.WARNING, base);
        toTest.validate(pr, "foo", "-42");
        assertTrue (pr.allProblems().size() > 0);
        s.clear();
        for (Problem p : pr.allProblems()) {
            assertNotSame (Severity.FATAL, p.severity());
            s.add(p.severity());
        }
        assertEquals (2, s.size());
        assertFalse(pr.hasFatal());
        assertTrue (s.contains(Severity.INFO) && s.contains(Severity.WARNING));

    }


    private static final class AlwaysWarnValidator implements Validator<String> {
        @Override
        public void validate(Problems problems, String compName, String model) {
            problems.append(new Problem("I am a warning", Severity.WARNING));
        }

        public Class<String> modelType() {
            return String.class;
        }
    }

    private static final class AlwaysInfoValidator implements Validator<String> {
        @Override
        public void validate(Problems problems, String compName, String model) {
            problems.append(new Problem("I am info", Severity.INFO));
        }

        public Class<String> modelType() {
            return String.class;
        }
    }


}