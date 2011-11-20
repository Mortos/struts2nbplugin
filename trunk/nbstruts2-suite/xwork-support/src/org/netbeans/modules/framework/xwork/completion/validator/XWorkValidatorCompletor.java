package org.netbeans.modules.framework.xwork.completion.validator;

import java.util.Collection;
import org.netbeans.spi.editor.completion.CompletionItem;

/**
 *
 * @author Aleh
 */
public interface XWorkValidatorCompletor {

    Collection<? extends CompletionItem> items();
}
