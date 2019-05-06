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

import org.junit.Test;
import org.netbeans.validation.api.Problems;
import static org.junit.Assert.*;

/**
 *
 * @author Joe Jensen
 */
public class MinimumLengthTest {

    public MinimumLengthTest() {
    }

    @Test
    public void testValidate() {
        MinimumLength n = new MinimumLength(5);

        Problems p = new Problems();
        n.validate(p, "x", "abcde");
        assertNull(p.getLeadProblem());
        n.validate(p, "x", "abcdef");
        assertNull(p.getLeadProblem());
        n.validate(p, "x", "abcd");
        assertTrue(p.hasFatal());
    }

}