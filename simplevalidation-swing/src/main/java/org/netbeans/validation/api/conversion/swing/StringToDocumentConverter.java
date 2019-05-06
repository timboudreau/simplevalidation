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
package org.netbeans.validation.api.conversion.swing;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.netbeans.validation.api.AbstractValidator;
import org.netbeans.validation.api.Problems;
import org.netbeans.validation.api.Validator;
import org.netbeans.validation.api.conversion.Converter;
import org.netbeans.validation.api.conversion.Converter;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Tim Boudreau
 */
@ServiceProvider(service=Converter.class)
public final class StringToDocumentConverter extends Converter <String, Document> {

    public StringToDocumentConverter() {
        super (String.class, Document.class);
    }

    @Override
    public Validator<Document> convert(Validator<String> from) {
        return new DocValidator (from);
    }

    private static class DocValidator extends AbstractValidator<Document> {
        private Validator<String> wrapped;

        private DocValidator(Validator<String> from) {
            super (Document.class);
            this.wrapped = from;
        }

        @Override
        public void validate(Problems problems, String compName, Document model) {
            try {
                String text = model.getText(0, model.getLength());
                wrapped.validate(problems, compName, text);
            } catch (BadLocationException ex) {
                throw new IllegalStateException (ex);
            }
        }

        @Override
        public String toString() {
            return "DocValidator for [" + wrapped + "]";
        }
    }
}
