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

import java.util.ArrayList;
import java.util.List;
import javax.swing.ButtonModel;
import org.netbeans.validation.api.AbstractValidator;
import org.netbeans.validation.api.Problems;
import org.netbeans.validation.api.Validator;
import org.netbeans.validation.api.conversion.Converter;
import org.netbeans.validation.api.conversion.Converter;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Hugo Heden
 */
@ServiceProvider(service=Converter.class)
public class SelectedIndicesToButtonModelArrayConverter extends Converter <Integer[], ButtonModel[]> {

    public SelectedIndicesToButtonModelArrayConverter() {
        super (Integer[].class, ButtonModel[].class);
    }

    @Override
    public Validator<ButtonModel[]> convert(Validator<Integer[]> from) {
        return new V(from);
    }

    private static final class V extends AbstractValidator<ButtonModel[]> {
        private final Validator<Integer[]> wrapped;

        public V(Validator<Integer[]> wrapped) {
            super (ButtonModel[].class);
            this.wrapped = wrapped;
        }

        @Override
        public void validate(Problems problems, String compName, ButtonModel[] buttonModels) {
            List<Integer> selectedElements = new ArrayList<Integer>(buttonModels.length);
            int index = 0;
            for( ButtonModel m : buttonModels ){
                if(m.isSelected()){
                    selectedElements.add(index);
                }
                ++index;
            }
            wrapped.validate(problems, compName, selectedElements.toArray(new Integer[selectedElements.size()]));
        }
    }
}
