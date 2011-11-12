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

import org.netbeans.modules.xml.xam.Nameable;

/**
 *
 * @author Rohan Ranade
 */
public interface Bean extends StrutsComponent, Nameable<StrutsComponent> {
    // Attributes
    public static final String TYPE_PROPERTY = "type";
    public static final String CLASS_PROPERTY = "class";
    public static final String SCOPE_PROPERTY = "scope";
    public static final String STATIC_PROPERTY = "static";
    public static final String OPTIONAL_PROPERTY = "optional";
    
    String getType();
    void setType(String type);
    
    String getClassValue();
    void setClassValue(String classValue);
    
    String getScope();
    void setScope(String scope);
    
    String getStatic();
    void setStatic(String st);
    
    String getOptional();
    void setOptional(String opt);
}
