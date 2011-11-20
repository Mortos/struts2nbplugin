package org.netbeans.modules.framework.xwork.completion.validator;

import org.netbeans.modules.framework.xwork.completion.resources.XWorkValidatorCompletionItemColors;
import org.netbeans.modules.framework.xwork.completion.resources.XWorkValidatorCompletionItemIcons;

/**
 *
 * @author Aleh
 */
public class XWorkValidatorAttributeCompletionItem extends XWorkValidatorCompletionItem {

    public XWorkValidatorAttributeCompletionItem(String text) {
        super(text, XWorkValidatorCompletionItemIcons.VALUE_ICONS, XWorkValidatorCompletionItemColors.VALUE_COLORS);
    }
}
