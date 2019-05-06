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

import javax.swing.ComboBoxModel;
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
public class StringToComboBoxModelConverter extends Converter <String, ComboBoxModel> {

    public StringToComboBoxModelConverter() {
        super (String.class, ComboBoxModel.class);
    }

    @Override
    public Validator<ComboBoxModel> convert(Validator<String> from) {
        return new V(from);
    }

    private static final class V extends AbstractValidator<ComboBoxModel> {
        private final Validator<String> wrapped;

        public V(Validator<String> wrapped) {
            super (ComboBoxModel.class);
            this.wrapped = wrapped;
        }

        @Override
        public void validate(Problems problems, String compName, ComboBoxModel model) {
            Object o = model.getSelectedItem();
            String s = o == null ? "" : o.toString();
            wrapped.validate(problems, compName, s);
        }
    }
}
