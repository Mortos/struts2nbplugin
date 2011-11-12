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

/**
 *
 * @author Rohan Ranade
 */
public interface Struts extends ReferenceableStrutsComponent {
    public static final String PACKAGE_PROPERTY = "package"; // NOI18N
    public static final String INCLUDE_PROPERTY = "include"; // NOI18N
    public static final String BEAN_PROPERTY = "bean"; // NOI18N
    public static final String CONSTANT_PROPERTY = "constant"; // NOI18N
    public static final String UNKNOWN_HANDLER_STACK_PROPERTY = "unknown-handler-stack"; // NOI18N
    
    List<StrutsPackage> getPackages();
    void addPackage(int index, StrutsPackage pack);
    void removePackage(StrutsPackage pack);
    
    List<Include> getIncludes();
    void addInclude(int index, Include incl);
    void removeInclude(Include incl);
    
    List<Bean> getBeans();
    void addBean(int index, Bean bean);
    void removeBean(Bean bean);
    
    List<Constant> getConstants();
    void addConstant(int index, Constant constant);
    void removeConstant(Constant constant);

    UnknownHandlerStack getUnknownHandlerStack();
    void setUnknownHandlerStack(UnknownHandlerStack unknownHandlerStack);
    
    // XXX - method to get all children?
}
