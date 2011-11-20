package org.netbeans.modules.framework.xwork.completion.validator;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 *
 * @author Aleh
 */
public class XWorkValidatorStaticAttributeValueCompletor implements XWorkValidatorCompletor {

    private static final String[] VALIDATOR_TYPES = {
        "required", "requiredstring", "int", "long", "short", "double", "date",
        "expression", "fieldexpression", "email", "url", "visitor", "conversion",
        "stringlength", "regex", "conditionalvisitor"
    };
    private static final Map<CharSequence, Map<CharSequence, String[]>> VALUE_COMPLETION_MAP =
            new HashMap<CharSequence, Map<CharSequence, String[]>>();
    private String[] choises;
    private String typedText;

    static {
        Map<CharSequence, String[]> validatorAttributes = new HashMap<CharSequence, String[]>();
        validatorAttributes.put("type", VALIDATOR_TYPES);
        VALUE_COMPLETION_MAP.put("validator", validatorAttributes);

        Map<CharSequence, String[]> fieldValidatorAttributes = new HashMap<CharSequence, String[]>();
        fieldValidatorAttributes.put("type", VALIDATOR_TYPES);
        VALUE_COMPLETION_MAP.put("field-validator", fieldValidatorAttributes);
    }

    public XWorkValidatorStaticAttributeValueCompletor(XWorkValidatorCompletionContext context) {
        typedText = context.typedText().toString();
        Map<CharSequence, String[]> attributeChoises = VALUE_COMPLETION_MAP.get(context.tagName());
        if (attributeChoises != null) {
            choises = attributeChoises.get(context.getAttributeName());
            if (choises != null) {
                return;
            }
        }
        choises = new String[0];
    }

    @Override
    public Collection<XWorkValidatorCompletionItem> items() {
        Collection<XWorkValidatorCompletionItem> result = new LinkedList<XWorkValidatorCompletionItem>();
        for (String choise : choises) {
            if (choise.startsWith(typedText)) {
                result.add(new XWorkValidatorAttributeCompletionItem(choise));
            }
        }
        return result;
    }
}
