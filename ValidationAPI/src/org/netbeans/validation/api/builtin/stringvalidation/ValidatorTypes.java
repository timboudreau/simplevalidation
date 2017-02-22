package org.netbeans.validation.api.builtin.stringvalidation;

import org.netbeans.validation.api.Validator;

/**
 *
 * @author Tim Boudreau
 */
public class ValidatorTypes {
    private ValidatorTypes(){}
    public static Class<? extends Validator> HOST_NAME = HostNameValidator.class;
    public static Class<? extends Validator> MAY_NOT_START_WITH_DIGIT = MayNotStartWithDigit.class;
    public static Class<? extends Validator> IP_ADDRESS = IpAddressValidator.class;
    public static Class<? extends Validator> HOST_NAME_OR_IP_ADDRESS = IpAddressValidator.class;
    public static Class<? extends Validator> NO_WHITESPACE = MayNotContainSpacesValidator.class;
    
}
