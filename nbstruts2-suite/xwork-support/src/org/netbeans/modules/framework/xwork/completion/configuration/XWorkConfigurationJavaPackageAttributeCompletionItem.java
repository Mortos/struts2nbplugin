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
 * The Original Software is XWork Support. The Initial Developer of the Original
 * Software is 
 * Aleh Maksimovich <aleh.maksimovich@gmail.com>, 
 *                  <aleh.maksimovich@hiqo-solutions.com>.
 * Portions Copyright 2011 Aleh Maksimovich. All Rights Reserved.
 */
package org.netbeans.modules.framework.xwork.completion.configuration;

import org.netbeans.modules.framework.xwork.editor.EditorSupport;
import org.netbeans.modules.framework.xwork.completion.XWorkCompletionItem;
import org.netbeans.modules.framework.xwork.completion.configuration.resource.XWorkConfigurationCompletionItemColors;
import org.netbeans.modules.framework.xwork.completion.configuration.resource.XWorkConfigurationCompletionItemIcon;

/**
 *
 * @author Aleh
 */
public class XWorkConfigurationJavaPackageAttributeCompletionItem extends XWorkCompletionItem {

    public XWorkConfigurationJavaPackageAttributeCompletionItem(EditorSupport context, String caption, String completion) {
        super(context, caption, completion,
                XWorkConfigurationCompletionItemIcon.PACKAGE_ICON.getImageIcon(),
                XWorkConfigurationCompletionItemColors.PACKAGE_COLORS);
    }
}
