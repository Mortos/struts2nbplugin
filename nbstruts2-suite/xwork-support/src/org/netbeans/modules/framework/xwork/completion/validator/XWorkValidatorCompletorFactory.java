package org.netbeans.modules.framework.xwork.completion.validator;

/**
 *
 * @author Aleh
 */
public class XWorkValidatorCompletorFactory {

    private static final XWorkValidatorCompletor EMPTY_COMPLETOR = new XWorkValidatorEmptyCompletor();
    private static final String VALIDATOR_TAG = "validator";
    private static final String FIELD_VALIDATOR_TAG = "field-validator";
    private static final String TYPE_ATTRIBITE = "type";

    public static XWorkValidatorCompletor completor(XWorkValidatorValueCompletionContext context) {
        if (context.isValid()) {
            if (TYPE_ATTRIBITE.equals(context.attributeName())
                    && VALIDATOR_TAG.equals(context.tagName()) || FIELD_VALIDATOR_TAG.equals(context.tagName())) {
                return new XWorkValidatorTypeAttributeValueCompletor(context);
            } else {
                return new XWorkValidatorStaticAttributeValueCompletor(context);
            }
        }

        return EMPTY_COMPLETOR;
    }
}
