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

import java.awt.Color;
import org.netbeans.validation.api.Validator;
import org.netbeans.validation.api.ui.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.JColorChooser;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.netbeans.validation.api.Problems;

/**
 * THIS CLASS AND EVERYTHING ELSE IN THIS PACKAGE IS NOT API.  DO NOT CALL
 * OR INSTANTIATE DIRECTLY.
 *
 * @author Hugo Heden
 */
class ButtonsValidationListenerImpl extends ValidationListener<AbstractButton[]> implements ItemListener, ChangeListener{

    private final Validator<ButtonModel[]> validator;
    private final AbstractButton[] buttons;
    public ButtonsValidationListenerImpl(AbstractButton[] buttons, ValidationUI validationUI, Validator<ButtonModel[]> validator) {
        super(AbstractButton[].class, validationUI, buttons);
        this.validator = validator;
        this.buttons = buttons;
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].getModel().addChangeListener(this);
            buttons[i].getModel().addItemListener(this);
        }
        performValidation(); // Make sure any initial errors are discovered immediately.
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        performValidation();
    }
    @Override
    public void stateChanged(ChangeEvent pce) {
        performValidation();
    }

    @Override
    protected void performValidation(Problems ps) {
        boolean theyreAllDisabled = true;
        List<ButtonModel> selectedButtons = new ArrayList<ButtonModel>();
        for (AbstractButton button : buttons) {
            if (button.getModel().isEnabled()) {
                theyreAllDisabled = false;
                if (button.getModel().isSelected()) {
                    selectedButtons.add(button.getModel());
                }
            }
        }
        if (!theyreAllDisabled) {
            validator.validate(ps, null, selectedButtons.toArray(new ButtonModel[selectedButtons.size()]));
        }
    }

}



