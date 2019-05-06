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
    public static Class<? extends Validator> URL = UrlValidator.class;
}
