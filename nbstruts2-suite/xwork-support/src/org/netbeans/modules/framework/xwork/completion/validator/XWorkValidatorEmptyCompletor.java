package org.netbeans.modules.framework.xwork.completion.validator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import org.netbeans.spi.editor.completion.CompletionItem;

/**
 *
 * @author Aleh
 */
public class XWorkValidatorEmptyCompletor implements XWorkValidatorCompletor {

    private static final Collection<? extends CompletionItem> EMPTY_COMPLETION_COLLECTION =
            Collections.unmodifiableCollection(new ArrayList<CompletionItem>(0));

    @Override
    public Collection<? extends CompletionItem> items() {
        return EMPTY_COMPLETION_COLLECTION;
    }
}
