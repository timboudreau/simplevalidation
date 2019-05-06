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
import org.netbeans.validation.api.Problem;
import org.netbeans.validation.api.Problems;
import org.netbeans.validation.api.Validator;
import static org.junit.Assert.*;

/**
 *
 * @author Tim Boudreau
 */
public class HostNameValidatorTest {
    
    @Test
    public void testIpV6() {
        Validator<String> v = new HostNameValidator(false);
        assertNull(check(v, "2001:db8:1f70::999:de8:7648:6e8"));
        assertNull(check(v, "2001:db8:85a3::8a2e:370:7334"));
        v = new HostNameValidator(true);
        assertNull(check(v, "[2001:db8:85a3::8a2e:370:7334]:8080"));
        
        assertNull(check(StringValidators.HOST_NAME_OR_IP_ADDRESS, "2001:db8:1f70::999:de8:7648:6e8"));
        assertNull(check(StringValidators.HOST_NAME_OR_IP_ADDRESS, "2001:db8:85a3::8a2e:370:7334"));
    }

    private Problem check(Validator<String> v, String str){
        Problems p = new Problems();
        v.validate(p, "", str);
        return p.getLeadProblem();
    }

    @Test
    public void testValidate() {
        assertTrue(true);
        Validator<String> v = new HostNameValidator(true);
        // Problems p = new Problems();

        assertNull(check(v, "www.foo.com"));
        assertNull(check(v, "www.foo.com:8080"));
        //test AIOOBE
        assertTrue(check(v,  "bar.com ").isFatal());
        assertTrue(check(v,  " bar.com").isFatal());
        assertTrue(check(v,  ":").isFatal());

        v = new HostNameValidator(false);
        assertTrue(check(v,  "myhost.com:204").isFatal());
        assertTrue(check(v,  "myhost.com:204-").isFatal());

        v = new HostNameValidator(true);
        assertTrue(check(v,  "128.foo.129").isFatal());
        assertTrue(check(v,  "128.foo.129:1024").isFatal());
        assertTrue(check(v,  "www.foo.com:abcd").isFatal());
        assertTrue(check(v,  "foo.").isFatal());
        assertTrue(check(v,  "128.").isFatal());
        assertNull(check(v,  "www.foo-bar.com"));
        assertTrue(check(v,  "www.foo-bar.com-").isFatal());
        assertTrue(check(v,  "-www.foo-bar.com").isFatal());
        assertTrue(check(v,  "www.foo-bar.com ").isFatal());
        assertTrue(check(v,  " www.foo-bar.com").isFatal());
        assertTrue(check(v,  "204.128").isFatal());

        assertTrue(check(v,  "foo@bar.com").isFatal());
        assertTrue(check(v,  "foo.бar.com").isFatal());
        assertTrue(check(v,  "фу.бар.ком").isFatal());
        assertTrue(check(v,  "myhost:").isFatal());
        assertTrue(check(v,  "myhost::").isFatal());
        assertTrue(check(v,  "www.foo.com::2040").isFatal());
        assertTrue(check(v,  "www.foo.com:2040802").isFatal());
        assertTrue(check(v,  "www.foo.com:2040:2802").isFatal());
        assertTrue(check(v,  "www.foo.com:2040:").isFatal());
        assertTrue(check(v,  "www..foo.com").isFatal());
    }

    @Test
    public void testValidateHostOrIP() {
        assertTrue(true);
        Validator<String> v = new ValidHostNameOrIPValidator(true);

        assertNull(check(v, "www.foo.com"));
        assertNull(check(v, "www.foo.com:8080"));
        assertTrue(check(v,  "bar.com ").isFatal());
        assertTrue(check(v,  " bar.com").isFatal());
        assertTrue(check(v,  ":").isFatal());
        assertTrue(check(v,  "myhost:").isFatal());

        v = new ValidHostNameOrIPValidator(false);
        assertTrue(check(v,  "myhost.com:204").isFatal());
        assertTrue(check(v,  "myhost.com:204-").isFatal());
        assertNull(check(v,  "205.foo.com"));

        v = new ValidHostNameOrIPValidator(true);
        assertTrue(check(v,  "128.foo.129").isFatal());
        assertTrue(check(v,  "128.foo.129:1024").isFatal());
        assertTrue(check(v,  "www.foo.com:abcd").isFatal());
        assertTrue(check(v,  "foo.").isFatal());
        assertTrue(check(v,  "128.").isFatal());
        assertNull(check(v, "www.foo-bar.com"));
        assertTrue(check(v,  "www.foo-bar.com-").isFatal());
        assertTrue(check(v,  "-www.foo-bar.com").isFatal());
        assertTrue(check(v,  "www.foo-bar.com ").isFatal());
        assertTrue(check(v,  " www.foo-bar.com").isFatal());
        assertTrue(check(v,  "204.128").isFatal());

        assertTrue(check(v,  "foo@bar.com").isFatal());
        assertTrue(check(v,  "foo.бar.com").isFatal());
        assertTrue(check(v,  "фу.бар.ком").isFatal());
        assertTrue(check(v,  "фу.бар.ком:1023").isFatal());
        assertTrue(check(v,  "фу.бар.ком:102a").isFatal());
        assertTrue(check(v,  "2050").isFatal());
        assertTrue(check(v,  ":2050").isFatal());
        assertTrue(check(v,  "фу.бар.ком:10232034").isFatal());
        assertTrue(check(v,  "www..foo.com").isFatal());
        assertTrue(check(v,  "192.168.2.1::1024").isFatal());
        assertTrue(check(v,  "192.168.2.1::1024:").isFatal());
        assertTrue(check(v,  ".").isFatal());
        assertTrue(check(v,  "").isFatal());
    }
}