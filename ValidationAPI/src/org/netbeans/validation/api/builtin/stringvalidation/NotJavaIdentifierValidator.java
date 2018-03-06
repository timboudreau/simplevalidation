/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2009 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * Contributor(s):
 *
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2006 Sun
 * Microsystems, Inc. All Rights Reserved.
 *
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 */
package org.netbeans.validation.api.builtin.stringvalidation;

import java.util.Arrays;
import org.netbeans.validation.api.Problems;
import org.netbeans.validation.localization.LocalizationSupport;

/**
 *
 * @author Tim Boudreau
 */
final class NotJavaIdentifierValidator extends StringValidator {

    @Override
    public void validate(Problems problems, String compName, String text) {
        if (text.trim().length() == 0) {
            return;
        }
        if (!isJavaIdentifier(text)) {
            problems.add(LocalizationSupport.getMessage(NotJavaIdentifierValidator.class,
                "ERR_JAVA_IDENTIFIER", text));
        }
    }

    public static final boolean isJavaIdentifier(String id) {
        if (id == null) {
            return false;
        }

        if (id.equals("")) {
            return false;
        }

        if (!(java.lang.Character.isJavaIdentifierStart(id.charAt(0)))) {
            return false;
        }

        for (int i = 1; i < id.length(); i++) {
            if (!(java.lang.Character.isJavaIdentifierPart(id.charAt(i)))) {
                return false;
            }
        }

        return Arrays.binarySearch(keywords, id) < 0;
    }

    private static final String[] keywords = new String[] {

            //If adding to this, insert in alphabetical order!
            "abstract", "assert", "boolean", "break", "byte", "case", //NOI18N
            "catch", "char", "class", "const", "continue", "default", //NOI18N
            "do", "double", "else", "enum", "extends", "false", "final", //NOI18N
            "finally", "float", "for", "goto", "if", "implements", //NOI18N
            "import", "instanceof", "int", "interface", "long", //NOI18N
            "native", "new", "null", "package", "private", //NOI18N
            "protected", "public", "return", "short", "static", //NOI18N
            "strictfp", "super", "switch", "synchronized", "this", //NOI18N
            "throw", "throws", "transient", "true", "try", "void", //NOI18N
            "volatile", "while" //NOI18N
    };

}
