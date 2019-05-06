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
import javax.swing.ListSelectionModel;
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
public class SelectedIndicesToListSelectionModelConverter extends Converter <Integer[], ListSelectionModel> {

    public SelectedIndicesToListSelectionModelConverter() {
        super (Integer[].class, ListSelectionModel.class);
    }

    @Override
    public Validator<ListSelectionModel> convert(Validator<Integer[]> from) {
        return new V(from);
    }

    private static final class V extends AbstractValidator<ListSelectionModel> {
        private final Validator<Integer[]> wrapped;

        public V(Validator<Integer[]> wrapped) {
            super (ListSelectionModel.class);
            this.wrapped = wrapped;
        }

        @Override
        public void validate(Problems problems, String compName, ListSelectionModel model) {
            if(model.isSelectionEmpty()){
                wrapped.validate(problems, compName, new Integer[0]);
            } else {
                List<Integer> list = new ArrayList<Integer>(model.getMaxSelectionIndex()+1 - model.getMinSelectionIndex());
                for ( int i = model.getMinSelectionIndex(); i <= model.getMaxSelectionIndex(); ++i ){
                    if( model.isSelectedIndex(i)){
                        list.add(i);
                    }
                }
                wrapped.validate(problems, compName, list.toArray(new Integer[list.size()]));
            }
        }
    }
}
