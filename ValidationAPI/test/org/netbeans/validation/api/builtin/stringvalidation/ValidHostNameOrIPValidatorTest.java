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
import static org.junit.Assert.*;
import org.netbeans.validation.api.Problems;
import org.netbeans.validation.api.Validator;

/**
 *
 * @author Tim Boudreau
 */
public class ValidHostNameOrIPValidatorTest {

    @Test
    public void testValidate() {
        Validator<String> v = new IpAddressValidator();
        assertValid (v, "192.168.2.1");
        assertValid (v, "127.0.0.1");
        assertValid (v, "192.168.2.1:8100");
        assertNotValid (v, "192.168.2.1.5.2.3.4:8100");
        assertNotValid (v, ":8100");
        assertNotValid (v, "192.168.2.1:");
        assertNotValid (v, "192.168.2.1:81000");
        assertNotValid (v, "192.168.2.1:boo");
        assertNotValid (v, "192.168.2.1:");
        assertNotValid (v, "192.168.boo.1");
        assertNotValid (v, "com.foo.bar.baz:1");
        assertNotValid (v, "com.foo.bar.baz");
        assertNotValid (v, "");

        v = new HostNameValidator(true);
        assertValid (v, "java.sun.com");
        assertValid (v, "central");
        assertValid (v, "sun.com");
        assertValid (v, "netbeans.org");
        assertValid (v, "netbeans.org:2203");
        assertNotValid (v, "netbeans.192.1");
        assertNotValid (v, "netbeans.org.192:239");
        assertNotValid (v, "");

        v = new ValidHostNameOrIPValidator();
        assertValid (v, "192.168.2.1");
        assertValid (v, "192.168.2.1");
        assertValid (v, "192.168.2.1:8100");
        assertValid (v, "java.sun.com");
        assertValid (v, "java.sun.com:8100");
        assertNotValid (v, "");
        assertNotValid (v, "java.100.com:boo");
        assertValid (v, "java.100.com:8100");
        assertNotValid (v, ":8100");
        assertNotValid (v, ".com");
        assertNotValid (v, ".com:8100");
        assertNotValid (v, " .com:8100");
        assertNotValid (v, " foo.com:8100");
        assertNotValid (v, " foo.com");
        assertNotValid (v, "foo.com ");
        assertNotValid (v, ".128");
        assertNotValid (v, ".128.");
        assertNotValid (v, "128.");
        assertNotValid (v, "128 ");
        assertNotValid (v, "128 .");
        assertNotValid (v, "myhost:");
        assertNotValid (v, "foo:bar.myhost:2020");
        assertNotValid (v, "foo:2020.myhost:2020");
        assertNotValid(v, "www.foo.com:2040:2802");
        assertNotValid (v, "1.2");
        assertNotValid (v, "127.0.0.1:");
        assertNotValid (v, "com.foo.bar:203:");
   }

    private void assertValid(Validator<String> v, String string) {
        Problems p = new Problems();
        v.validate(p, "X", string);
        assertNull(p.getLeadProblem());
    }

    private void assertNotValid(Validator<String> v, String string) {
        Problems p = new Problems();
        v.validate(p, "X", string);
        assertTrue( p.hasFatal());
    }


}