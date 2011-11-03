package org.openide.util;

import java.util.Arrays;

/**
 *
 * @author Tim Boudreau
 */
public class Utilities {
    public static boolean isWindows() {
        return System.getProperty("os.name").contains("Windows");
    }

    /** Test whether a given string is a valid Java identifier.
    * @param id string which should be checked
    * @return <code>true</code> if a valid identifier
    */
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
