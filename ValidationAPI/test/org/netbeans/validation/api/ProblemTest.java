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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Tim Boudreau
 */
public class ProblemTest {
    /**
     * Test of isWorseThan method, of class Problem.
     */
    @Test
    public void testIsWorseThan() {
        Problem p0 = null;
        Problem a = new Problem ("a", Severity.INFO);
        Problem b = new Problem ("b", Severity.WARNING);
        Problem c = new Problem ("c", Severity.FATAL);

        assertSame( a, Problem.worst(a, p0) );
        assertSame( b, Problem.worst(p0, b) );
        assertSame( c, Problem.worst(c, p0) );
        assertSame( b, Problem.worst(b, a) );
        assertSame( c, Problem.worst(a, c) );
        assertSame( c, Problem.worst(c, b) );

        List<Problem> l = new ArrayList<>(Arrays.asList(a, b, c, p0));
        assertSame (a, l.get(0));
        assertSame (c, l.get(2));
        assertSame (p0, l.get(3));

        assertTrue (c.isFatal());
        assertFalse (b.isFatal());
        assertFalse (a.isFatal());

        assertEquals ("a", a.getMessage());
        assertEquals ("b", b.getMessage());
        assertEquals ("c", c.getMessage());

        Problems problems = new Problems();

        problems.append (a);
        assertFalse(problems.hasFatal());
        problems.append (b);
        assertFalse(problems.hasFatal());
        problems.append (c);
        assertTrue(problems.hasFatal());
        problems.append(p0);
        assertTrue(problems.hasFatal());
        assertSame (c, problems.getLeadProblem());
        assertTrue(problems.hasFatal());

        Problems clean = new Problems();
        assertFalse(clean.hasFatal());
        clean.addAll(problems);
        assertTrue(clean.hasFatal());
        Problems alsoClean = new Problems();
        assertFalse(alsoClean.hasFatal());
        alsoClean.append("info", Severity.INFO);
        assertFalse(alsoClean.hasFatal());
        alsoClean.append("warning", Severity.WARNING);
        assertFalse(alsoClean.hasFatal());
        alsoClean.append("fatal");
        assertTrue(alsoClean.hasFatal());
    }
}