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

package org.netbeans.modules.web.frameworks.struts2.api.configmodel;

import java.util.List;
import org.netbeans.modules.xml.xam.Nameable;

/**
 *
 * @author Rohan Ranade
 */
public interface Action extends ResultsProvider, Nameable<StrutsComponent> {
    // Attributes
    public static final String CLASS_PROPERTY = "class";
    public static final String METHOD_PROPERTY = "method";
    public static final String CONVERTER_PROPERTY = "converter";
    
    String getClassValue();
    void setClassValue(String classValue);
    
    String getMethod();
    void setMethod(String method);
    
    String getConverter();
    void setConverter(String converter);
    
    // Children
    public static final String PARAM_PROPERTY = "param";
    public static final String INTERCEPTOR_REF_PROPERTY = "interceptor-ref";
    public static final String EXCEPTION_MAPPING_PROPERTY = "exception-mapping";
    
    List<Param> getParams();
    void addParam(int index, Param param);
    void removeParam(Param param);
    
    List<InterceptorRef> getInterceptorRefs();
    void addInterceptorRef(int index, InterceptorRef interceptorRef);
    void removeInterceptorRef(InterceptorRef interceptorRef);
    
    List<ExceptionMapping> getExceptionMappings();
    void addExceptionMapping(int index, ExceptionMapping exceptionMapping);
    void removeExceptionMapping(ExceptionMapping exceptionMapping);
}
