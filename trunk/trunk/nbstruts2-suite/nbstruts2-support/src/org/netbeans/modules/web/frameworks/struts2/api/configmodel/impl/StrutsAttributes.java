/*
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at http://www.netbeans.org/cddl.html
 * or http://www.netbeans.org/cddl.txt.
 *
 * When distributing Covered Code, include this CDDL Header Notice in each file
 * and include the License file at http://www.netbeans.org/cddl.txt.
 * If applicable, add the following below the CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2007 Sun
 * Microsystems, Inc. All Rights Reserved.
 */

package org.netbeans.modules.web.frameworks.struts2.api.configmodel.impl;

import org.netbeans.modules.web.frameworks.struts2.api.configmodel.Action;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.Bean;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.Constant;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.ExceptionMapping;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.Include;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.Result;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.ResultType;
import org.netbeans.modules.web.frameworks.struts2.api.configmodel.StrutsPackage;
import org.netbeans.modules.xml.xam.Named;
import org.netbeans.modules.xml.xam.dom.Attribute;

/**
 *
 * @author Rohan Ranade
 */
public enum StrutsAttributes implements Attribute {

    NAME(Named.NAME_PROPERTY),
    EXTENDS(StrutsPackage.EXTENDS_PROPERTY),
    NAMESPACE(StrutsPackage.NAMESPACE_PROPERTY),
    ABSTRACT(StrutsPackage.ABSTRACT_PROPERTY),
    EXTERNAL_REFERENCE_RESOLVER(StrutsPackage.EXTERNAL_REF_RESOLVER_PROPERTY),
    CLASS(ResultType.CLASS_PROPERTY),
    DEFAULT(ResultType.DEFAULT_PROPERTY, Boolean.class),
    TYPE(Result.TYPE_PROPERTY),
    EXCEPTION(ExceptionMapping.EXCEPTION_PROPERTY),
    RESULT(ExceptionMapping.RESULT_PROPERTY),
    METHOD(Action.METHOD_PROPERTY),
    CONVERTER(Action.CONVERTER_PROPERTY),
    FILE(Include.FILE_PROPERTY),
    SCOPE(Bean.SCOPE_PROPERTY),
    STATIC(Bean.STATIC_PROPERTY),
    OPTIONAL(Bean.OPTIONAL_PROPERTY),
    VALUE(Constant.VALUE_PROPERTY);
    
    private String name;
    private Class type;

    StrutsAttributes(String name) {
        this(name, String.class);
    }

    StrutsAttributes(String name, Class type) {
        this.name = name;
        this.type = type;
    }
    
    public Class getType() {
        return type;
    }
    
    public String getName() {
        return name;
    }

    public Class getMemberType() {
        return null;
    }

}
