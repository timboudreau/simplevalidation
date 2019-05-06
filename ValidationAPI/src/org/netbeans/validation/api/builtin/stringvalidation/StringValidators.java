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

import org.netbeans.validation.api.ValidatorUtils;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.text.Format;
import java.util.Locale;
import org.netbeans.validation.api.Problems;
import org.netbeans.validation.api.Validator;

/**
 * An enumeration of validator factories for commonly needed forms of
 * validation such as non-empty strings, valid file names and URLs and
 * so forth.
 * <p>
 * Also contains static factory methods for validators which do things
 * like match regexp's and split strings and run another validator over
 * the components.
 *
 * @author Tim Boudreau
 */
public enum StringValidators implements Validator<String> {
    /**
     * Factory for validators which require non-zero length text.
     */
    REQUIRE_NON_EMPTY_STRING,
    /**
     * Factory for validators which require a legal filename
     */
    REQUIRE_VALID_FILENAME,
    /**
     * Factory for validators which require a valid integer
     */
    REQUIRE_VALID_INTEGER,
    /**
     * Factory for validators which require a non-negative number (may
     * be floating point or int)
     */
    REQUIRE_NON_NEGATIVE_NUMBER,
    /**
     * Factory for validators which require a valid number of some sort
     */
    REQUIRE_VALID_NUMBER,
    /**
     * Factory for validators which require that strings not be a
     * Java keyword
     */
    REQUIRE_JAVA_IDENTIFIER,
    /**
     * Factory for validators that require that strings be a valid
     * hexadecimal number
     */
    VALID_HEXADECIMAL_NUMBER,
    /**
     * Factory for validators that require that strings not contain whitespace
     */
    NO_WHITESPACE,
    /**
     * Factory for validators that require that a string represent a file
     * which exists on disk
     */
    FILE_MUST_EXIST,
    /**
     * Factory for validators that require that a string represent a file
     * which is a file, not a directory
     */
    FILE_MUST_BE_FILE,
    /**
     * Factory for validators that require that a string represent a file
     * which is a directory, not a data file
     */
    FILE_MUST_BE_DIRECTORY,
    /**
     * Factory for validators that require that a string represent a valid URL
     */
    URL_MUST_BE_VALID,

    /**
     * Factory for validators that check the validity of an IP address (may
     * contain port info)
     */
    IP_ADDRESS,

    /**
     * Factory for validators that check the validity of an host name (may
     * contain port info)
     */
    HOST_NAME,
    /**
     * Factory for validators that check the validity of an IP address or
     * host name (may contain port info)
     */
    HOST_NAME_OR_IP_ADDRESS,
    /**
     * Factory for validators that do not allow strings which start with
     * a digit
     */
    MAY_NOT_START_WITH_DIGIT,

    /**
     * Factory for validators that validate standard internet email addresses
     * (name + @ + valid hostname or ip).
     * <p>
     * <b>Note:</b>  This validator is not useful for all legal email addresses -
     * for example, "root" with is a legal email address on a
     * Unix machine.  Do not use this validator where users
     * might legitimately expect to be able to enter such unqualified email
     * addresses.
     */
    EMAIL_ADDRESS,

    /**
     * Factory for validators that require the passed string to be a valid
     * character set name according to the specification of
     * <code>java.nio.Charset.forName(String)</code>.
     */
    CHARACTER_SET_NAME,

    /**
     * Factory for validators that validate a java package name.  Note that
     * this does not mean the package name actually exists anywhere, just
     * that it does not contain java keywords.
     */
    JAVA_PACKAGE_NAME,

    /**
     * Validator which only passes non-existent file names
     */
    FILE_MUST_NOT_EXIST,
    /**
     * Validator which fails any string that ends with a .
     */
    MAY_NOT_END_WITH_PERIOD,
    ;



    /**
     * Get a validator of strings.
     * @param trim If true, String.trim() is called before passing the value
     * to the actual validator
     * @return A validator for strings
     */
    private Validator<String> instantiate(boolean trim) {
        Validator<String> result;
        switch (this) {
            case REQUIRE_JAVA_IDENTIFIER :
                result = new NotJavaIdentifierValidator();
                break;
            case REQUIRE_NON_EMPTY_STRING :
                result = new EmptyStringIllegalValidator();
                break;
            case REQUIRE_NON_NEGATIVE_NUMBER :
                result = new NonNegativeNumberValidator();
                break;
            case REQUIRE_VALID_FILENAME :
                result = new IllegalCharactersInFileNameValidator();
                break;
            case REQUIRE_VALID_INTEGER :
                result = new IsAnIntegerValidator();
                break;
            case REQUIRE_VALID_NUMBER :
                result = new IsANumberValidator();
                break;
            case VALID_HEXADECIMAL_NUMBER :
                result = new ValidHexadecimalNumberValidator();
                break;
            case NO_WHITESPACE :
                result = new MayNotContainSpacesValidator();
                break;
            case FILE_MUST_BE_DIRECTORY :
                result = new FileValidator (FileValidator.Type.MUST_BE_DIRECTORY);
                break;
            case FILE_MUST_BE_FILE :
                result = new FileValidator (FileValidator.Type.MUST_BE_FILE);
                break;
            case FILE_MUST_EXIST :
                result = new FileValidator (FileValidator.Type.MUST_EXIST);
                break;
            case FILE_MUST_NOT_EXIST :
                result = new FileValidator (FileValidator.Type.MUST_NOT_EXIST);
                break;
            case URL_MUST_BE_VALID :
                result = new UrlValidator();
                break;
            case IP_ADDRESS :
                result = new IpAddressValidator();
                break;
            case HOST_NAME :
                result = new HostNameValidator(true);
                break;
            case HOST_NAME_OR_IP_ADDRESS :
                result = new ValidHostNameOrIPValidator();
                break;
            case MAY_NOT_START_WITH_DIGIT :
                result = new MayNotStartWithDigit();
                break;
            case EMAIL_ADDRESS :
                result = new EmailAddressValidator();
                return result;
            case JAVA_PACKAGE_NAME :
                result = StringValidators.splitString("\\.", StringValidators.REQUIRE_JAVA_IDENTIFIER); // NOI18N
                return result;
            case MAY_NOT_END_WITH_PERIOD :
                result = new MayNotEndWithValidator('.');
                return result;
            default :
                throw new AssertionError();
        }
        if (trim) {
            return new TrimStringValidator(result);
        } else {
            return result;
        }
    }

    @Override 
    public void validate (Problems problems, String compName, String model) {
        instantiate(false).validate(problems, compName, model);
    }

    /**
     * Returns a Validator<String> that will first call {@code trim()} on the {@code String} to be validated,
     * and then passes the resulting (trimmed) {@code String} to this instance of {@code StringValidators}.
     *
     * <p>See also the static {@link #trimString(org.netbeans.validation.api.Validator[]) }
     * that does a similar thing for any {@code Validator<String>} (or a chain thereof)
     * that need not be an instance of the {@code StringValidators} enum
     */
    public Validator<String> trim() {
        return instantiate(true);
    }

    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////

    /**
     * Creates a {@code Validator<String>} that will first call {@code trim()} on the {@code String} to be validated,
     * and then passes the resulting (trimmed) {@code String} to the passed {@code Validator<String>} (or chain thereof).
     *
     * <p>See also {@link ValidatorUtils#merge(org.netbeans.validation.api.Validator[]) }
     * which merges validators (any {@code Validator}, not just {@code
     * Validator<String>}) <b>without</b> wrapping the result in one
     * that does String trimming.
     *
     * @param validators a chain of String validators
     */
    @SafeVarargs
    public static Validator<String> trimString(Validator<String>... validators) {
        assert sameType(validators);
        return new TrimStringValidator( ValidatorUtils.merge(validators) );
    }

    private static boolean sameType(Validator<?>[] v) {
        Class<?> type = null;
        for (int i = 0; i < v.length; i++) {
            if (type == null) {
                type = v[i].modelType();
            } else {
                if (type != v[i].modelType()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Creates a {@code Validator<String>} that will first call {@code trim()} on the {@code String} to be validated,
     * and then passes the resulting (trimmed) {@code String} to the passed {@code Validator<String>}.
     * <p>Unlike {@link #trimString(Validator...)}, calling this method does not trigger warnings under {@code -Xlint:unchecked}.
     * If you wish to trim more than one validator, simply trim the result of {@link ValidatorUtils#merge(Validator,Validator)}.
     * @param validator a String validator
     */
    public static Validator<String> trimString(Validator<String> validator) {
        return new TrimStringValidator(validator);
    }

    /**
     * Returns a validator which first splits the string to be evaluated according
     * to the passed regexp, then passes each component of the split string to
     * the passed validator.
     *
     * @param regexp The regular expression pattern to use to split the string
     * @param validators the validator (or chain of validators) that the returned one
     * should delegate to validate each component of the split string
     * @return A validator which evaluates each of component of the split string
     * using the passed Validator (or chain of validators)
     */
    @SafeVarargs
    public static Validator<String> splitString(String regexp, Validator<String>... validators) {
        assert sameType(validators);
        return new SplitStringValidator(regexp, ValidatorUtils.merge(validators) );
    }

    /**
     * Returns a validator which first splits the string to be evaluated according
     * to the passed regexp, then passes each component of the split string to
     * the passed validator.
     * <p>Unlike {@link #splitString(String,Validator...)}, calling this method does not trigger warnings under {@code -Xlint:unchecked}.
     * If you wish to split more than one validator, simply split the result of {@link ValidatorUtils#merge(Validator,Validator)}.
     * @param regexp The regular expression pattern to use to split the string
     * @param validator the validator that the returned one
     * should delegate to validate each component of the split string
     * @return A validator which evaluates each of component of the split string
     * using the passed Validator
     */
    public static Validator<String> splitString(String regexp, Validator<String> validator) {
        return new SplitStringValidator(regexp, validator);
    }

    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////

    /**
     * Create a validator which does not allow you to terminate a string
     * with a particular character
     * @param ch The character
     * @return A validator
     */
    public static Validator<String> mayNotEndWith(char ch) {
        return new MayNotEndWithValidator(ch);
    }


    /**
     * Get a validator which fails if any of the characters in the
     * passed char array are contained in the evaluated text
     * @param chars The array of illegal characters
     * @return A validator which will show an error if any of the passed
     * characters are in the String it encounters
     */
    public static Validator<String> disallowChars(char[] chars) {
        return new DisallowCharactersValidator(chars);
    }

    /**
     * Get a validator which fails if the text to validate does not match a
     * passed regular expression.
     * @param regexp The regular expression
     * @param message The output message if there is a problem.  The message
     * may refer to the component name as {0} and the text that has not matched
     * as {1} if desired
     * @param acceptPartialMatches if true, will use <code>Matcher.lookingAt()</code> rather
     * than <code>Matcher.matches()</code>
     * @return A validator
     */
    public static Validator<String> regexp (String regexp, String message, boolean acceptPartialMatches) {
        return new RegexpValidator(regexp, message, acceptPartialMatches);
    }

    /**
     * Create a number validator that uses a specific locale.  For the default
     * locale, use StringValidators.REQUIRE_VALID_NUMBER.  Use this if you specifically
     * want validation for another locale.
     * @param l The locale to use
     * @return A string validator for numbers
     */
    public static Validator<String> validNumber (Locale l) {
        return new IsANumberValidator(l);
    }

    /**
     * Get a validator that uses a specific <code>Format</code> (e.g.
     * <code>NumberFormat</code>) instance and fails
     * if <code>fmt.parseObject()</code> throws a <code>ParseException</code>
     * @param fmt A <code>java.text.Format</code>
     * @return A string validator that uses the provided <code>NumberFormat</code>
     */
    public static Validator<String> forFormat(Format fmt) {
        return new FormatValidator(fmt);
    }

    /**
     * Get a validator which determines if the passed string can be encoded
     * in the specified encoding.  Useful, for example, for validating
     * strings that are specified to be in a particular encoding (e.g.
     * email addresses and US-ASCII)
     * 
     * @param charsetName The name of a character set recognized by
     * <code>java.nio.Charset</code>
     * @return A validator of character set names
     * @throws UnsupportedCharsetException if the character set is unsupported
     * @throws IllegalCharsetNameException if the character set is illegal
     */
    public static Validator<String> encodableInCharset(String charsetName) {
        return new EncodableInCharsetValidator(charsetName);
    }

    /**
     * Get a validator that guarantees that a number is within a certain
     * range (inclusive)
     * @param min The minimum value
     * @param max The maximum value
     * @return A validator for number ranges
     */
    public static Validator<String> numberRange (Number min, Number max) {
        return new NumberRange(min, max);
    }

    /**
     * Validator that enforces minimum input length
     * @param length
     * @return A validator for string lengths
     */
    public static Validator<String> minLength (int length) {
        return new MinimumLength(length);
    }

    /**
     * Validator that enforces maximum input length
     * @param length
     * @return A validator for string lengths
     */
    public static Validator<String> maxLength (int length) {
        return new MaximumLength(length);
    }
    
    /**
     * Validate that a string evaluates to a number greater
     * than an amount
     * @param amount The amount
     * @return A validator
     */
    public static Validator<String> greaterThan(int amount) {
        return new BoundValidator(amount, false);
    }

    /**
     * Validate that a string evaluates to a number less
     * than an amount
     * @param amount The amount
     * @return A validator
     */
    public static Validator<String> lessThan(int amount) {
        return new BoundValidator(amount, true);
    }

    public static Validator<String> between(int min, int max) {
        BoundValidator a = new BoundValidator(Math.min(min, max), false);
        BoundValidator b = new BoundValidator(Math.max(max, min), true);
        return ValidatorUtils.merge(a, b);
    }    

    public Class<String> modelType() {
        return String.class;
    }
}
