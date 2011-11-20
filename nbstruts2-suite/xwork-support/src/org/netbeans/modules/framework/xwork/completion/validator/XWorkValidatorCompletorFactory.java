package org.netbeans.modules.framework.xwork.completion.validator;

import org.netbeans.api.xml.lexer.XMLTokenId;

/**
 *
 * @author Aleh
 */
public class XWorkValidatorCompletorFactory {

    private static final XWorkValidatorCompletor EMPTY_COMPLETOR = new XWorkValidatorEmptyCompletor();

    public static XWorkValidatorCompletor completor(XWorkValidatorCompletionContext context) {
        if (context.hasTokenId(XMLTokenId.VALUE)) {
            return new XWorkValidatorStaticAttributeValueCompletor(context);
        }
        
        return EMPTY_COMPLETOR;
    }
}
