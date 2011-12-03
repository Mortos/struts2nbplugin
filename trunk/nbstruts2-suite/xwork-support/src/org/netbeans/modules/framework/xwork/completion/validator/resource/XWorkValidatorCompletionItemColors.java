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
 * Software is 
 * Aleh Maksimovich <aleh.maksimovich@gmail.com>, 
 *                  <aleh.maksimovich@hiqo-solutions.com>.
 * Portions Copyright 2011 Aleh Maksimovich. All Rights Reserved.
 */
package org.netbeans.modules.framework.xwork.completion.validator.resource;

import java.awt.Color;
import org.netbeans.modules.framework.xwork.completion.resource.XWorkTextColors;

/**
 *
 * @author Aleh
 */
public enum XWorkValidatorCompletionItemColors implements XWorkTextColors {

    ATTRIBUTE_COLORS(null, null, null, null),
    ELEMENT_COLORS(null, null, null, null),
    VALUE_COLORS(Color.BLUE, null, null, null);
    private Color foreColor;
    private Color backColor;
    private Color selectedForeColor;
    private Color selectedBackColor;

    private XWorkValidatorCompletionItemColors(Color foreColor, Color backColor, Color selectedForeColor, Color selectedBackColor) {
        this.foreColor = foreColor;
        this.backColor = backColor;
        this.selectedForeColor = selectedForeColor;
        this.selectedBackColor = selectedBackColor;
    }

    @Override
    public Color backColor(Color defaultColor, boolean selected) {
        return (selected) ? selectedBackColor(defaultColor) : normalBackColor(defaultColor);
    }

    @Override
    public Color foreColor(Color defaultColor, boolean selected) {
        return (selected) ? selectedForeColor(defaultColor) : normalForeColor(defaultColor);
    }

    private Color normalBackColor(Color defaultColor) {
        return (backColor != null) ? backColor : defaultColor;
    }

    private Color normalForeColor(Color defaultColor) {
        return (foreColor != null) ? foreColor : defaultColor;
    }

    private Color selectedBackColor(Color defaultColor) {
        return (selectedBackColor != null) ? selectedBackColor : defaultColor;
    }

    private Color selectedForeColor(Color defaultColor) {
        return (selectedForeColor != null) ? selectedForeColor : defaultColor;
    }
}
