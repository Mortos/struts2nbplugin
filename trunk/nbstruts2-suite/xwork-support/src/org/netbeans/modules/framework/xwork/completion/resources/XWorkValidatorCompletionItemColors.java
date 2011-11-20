package org.netbeans.modules.framework.xwork.completion.resources;

import java.awt.Color;

/**
 *
 * @author Aleh
 */
public enum XWorkValidatorCompletionItemColors {

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

    public Color backColor(Color defaultColor, boolean selected) {
        return (selected) ? selectedBackColor(defaultColor) : normalBackColor(defaultColor);
    }

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
