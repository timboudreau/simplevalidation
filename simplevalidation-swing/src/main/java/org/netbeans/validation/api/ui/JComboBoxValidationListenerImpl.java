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
package org.netbeans.validation.api.ui;

import javax.swing.ComboBoxModel;
import org.netbeans.validation.api.Validator;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import java.util.EventListener;
import javax.swing.JComboBox;
import org.netbeans.validation.api.Problems;
import org.netbeans.validation.api.ui.swing.SwingValidationGroup;

/**
 * 
 * @author Tim Boudreau
 */
class JComboBoxValidationListenerImpl extends ValidationListener<JComboBox>
        implements EventListener, ItemListener, FocusListener {
    private Validator<ComboBoxModel> validator;
    private boolean hasFatalProblem = false;


    public JComboBoxValidationListenerImpl(JComboBox component,
            ValidationStrategy strategy,
            ValidationUI validationUI,
            Validator<ComboBoxModel> validator
            ) {
        super(JComboBox.class, validationUI, component);
        this.validator = validator;
        if (strategy == null) {
            throw new NullPointerException("strategy null");
        }
        component.addPropertyChangeListener("enabled", new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                performValidation();
            }
        });
        switch (strategy) {
            case DEFAULT:
            case ON_CHANGE_OR_ACTION:
                component.addItemListener(this);
                break;
            case ON_FOCUS_LOSS:
                component.addFocusListener(this);
                break;
            case INPUT_VERIFIER:
                component.setInputVerifier( new InputVerifier() {
                    @Override
                    public boolean verify(JComponent input) {
                        performValidation();
                        return !hasFatalProblem;
                    }
                });
                break;
            default:
                throw new AssertionError();
        }
        performValidation(); // Make sure any initial errors are discovered immediately.
    }

    @Override
    protected void performValidation(Problems ps){
        JComboBox component = getTarget();
        if (!component.isEnabled()) {
            return;
        }
        validator.validate(ps, SwingValidationGroup.nameForComponent(component), component.getModel());
        hasFatalProblem = ps.hasFatal();
    }

    @Override
    public void focusLost(FocusEvent e) {
        performValidation();
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        performValidation();
    }

    @Override
    public void focusGained(FocusEvent e) {
    }

}
