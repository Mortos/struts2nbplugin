package org.netbeans.modules.framework.xwork.completion.validator;

/**
 *
 * @author Aleh
 */
public class XWorkValidatorCompletorFactory {

    private static final XWorkValidatorCompletor EMPTY_COMPLETOR = new XWorkValidatorEmptyCompletor();

    public static XWorkValidatorCompletor completor(XWorkValidatorValueCompletionContext context) {
        if (context.isValid()) {
            return new XWorkValidatorStaticAttributeValueCompletor(context);
        }

        return EMPTY_COMPLETOR;
    }
}
