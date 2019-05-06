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

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.netbeans.validation.api.Problems;

/**
 *
 * @author Tim Boudreau
 */
public class IpAddressValidatorTest {
    private IpAddressValidator v = new IpAddressValidator();

    @Test
    public void testValidate() {
        assertValid ("192.168.2.1");
        assertValid ("192.168.2.1:2080");
        assertValid ("0.0.0.0");
        assertValid ("255.255.255.255");

        assertInvalid ("192.168.2.1:2080303");
        assertInvalid ("1.1.1.256");
        assertInvalid ("1.1.1.256:10");
        assertInvalid ("1.2");
        assertInvalid ("192.168.2.1:");
        assertInvalid ("192.168.2.1.");
        assertInvalid ("192.168.2 .1");
        assertInvalid ("192.168. .1");
        assertInvalid ("192.168. 2.1");
        assertInvalid (".192.168.2.1");
        assertInvalid ("192.168.2.bad");
        assertInvalid ("192.168.1");
        assertInvalid ("foo.bar.com");
        assertInvalid ("foo.bar.com:2080");
        assertInvalid ("foo");
        assertInvalid ("192.168.2.1::");
        assertInvalid (":192.168.2.1");
        assertInvalid ("2020:192.168.2.1");
        assertInvalid ("192.168.2.1:192.168.2.1");
        assertInvalid ("192.168.2.1:202p");
        assertInvalid ("192.168.2.1:2080303:3030");
        assertInvalid ("192.168.2..1:2080303:3030");
        assertInvalid ("192.168.2..1:2080");
        assertInvalid ("192.168.2..1::2080");
        assertInvalid ("192.168:23.2.1:2080303:3030");
        assertInvalid ("192.168:23.2.1:2080 ");
        assertInvalid (" 192.168:23.2.1:2080 ");
        assertInvalid (" 192.168:23.2.1:2080");
        assertInvalid (" 192.168:23.2.1");
        assertInvalid (" 192.168:23.2.1 ");
        assertInvalid ("192.168:23.2.1 ");
        assertInvalid ("192.168.2.1.abc");
        assertInvalid ("192.168.2.abc");
        assertInvalid ("192.168.2:2020:");
        assertInvalid ("");
        assertInvalid (".");
    }

   private void assertValid(String string) {
        Problems p = new Problems();
        v.validate(p, "X", string);
        assertNull(p.getLeadProblem());
    }

    private void assertInvalid(String string) {
        Problems p = new Problems();
        v.validate(p, "X", string);
        assertTrue(p.hasFatal());
    }
}