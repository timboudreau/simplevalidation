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
package org.netbeans.validation.api.conversion;

import javax.swing.text.Document;
import org.junit.Test;
import static org.junit.Assert.*;
import org.netbeans.validation.api.AbstractValidator;
import org.netbeans.validation.api.Problems;
import org.netbeans.validation.api.Validator;
public class ConverterTest {
    @Test(expected=IllegalArgumentException.class)
    public void testFind() {
        assertNotNull (Converter.find(String.class, Document.class));
        try {
            Converter.find (String.class, Foo.class);
            fail ("Exception not thrown for unavailable converter");
        } catch (IllegalArgumentException e) {}
        Converter.register(new ConverterImpl());
        assertNotNull (Converter.find (String.class, Foo.class));
        assertNotNull (Converter.find (String.class, Bar.class));
    }

    public static class Foo {
        private final String s;
        public Foo(String s) {
            this.s = s;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Foo other = (Foo) obj;
            if ((this.s == null) ? (other.s != null) : !this.s.equals(other.s)) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 79 * hash + (this.s != null ? this.s.hashCode() : 0);
            return hash;
        }

        @Override
        public String toString() {
            return s;
        }
    }

    public static class Bar {
        private final String s;
        public Bar(String s) {
            this.s = s;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Foo other = (Foo) obj;
            if ((this.s == null) ? (other.s != null) : !this.s.equals(other.s)) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 79 * hash + (this.s != null ? this.s.hashCode() : 0);
            return hash;
        }

        @Override
        public String toString() {
            return s;
        }
    }

    public static class ConverterImpl extends Converter<String, Foo> {

        public ConverterImpl() {
            super(String.class, Foo.class);
        }

        public Validator<Foo> convert(Validator<String> from) {
            return new V();
        }

        private static final class V extends AbstractValidator<Foo> {
            V() {
                super (Foo.class);
            }

            public void validate(Problems problems, String compName, Foo model) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        }
    }

    public static class BarConverter extends Converter<String, Bar> {

        public BarConverter() {
            super(String.class, Bar.class);
        }

        public Validator<Bar> convert(Validator<String> from) {
            return new V();
        }

        private static final class V extends AbstractValidator<Bar> {
            V() {
                super (Bar.class);
            }

            public void validate(Problems problems, String compName, Bar model) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        }
    }

}