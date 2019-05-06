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
 * @author Tim Boudreau
 */
public class NumberRangeTest {

    public NumberRangeTest() {
    }

    @Test
    public void testValidate() {
        NumberRange n = new NumberRange (0, 65535);
        Problems p = new Problems();
        n.validate(p, "x", 1 + "");
        assertNull(p.getLeadProblem());
        p = new Problems();
        n.validate(p, "x", 0 + "");
        assertNull(p.getLeadProblem());
        p = new Problems();
        n.validate(p, "x", 65535 + "");
        assertNull(p.getLeadProblem());
        p = new Problems();
        n.validate(p, "x", 65536 + "");
        assertTrue(p.hasFatal());
        p = new Problems();
        n.validate(p, "x", -1 + "");
        assertTrue(p.hasFatal());
        p = new Problems();
        n.validate(p, "x", Integer.MAX_VALUE + "");
        assertTrue(p.hasFatal());
        p = new Problems();
        n.validate(p, "x", Long.MAX_VALUE + "");
        assertTrue(p.hasFatal());
        p = new Problems();
        n.validate(p, "x", 65534.9999D + "");
        assertNull(p.getLeadProblem());
        p = new Problems();
        n.validate(p, "x", 65535.9999D + "");
        assertTrue(p.hasFatal());
    }

}