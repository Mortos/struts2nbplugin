package org.netbeans.modules.framework.xwork.completion.validator;

import javax.swing.text.JTextComponent;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.spi.editor.completion.CompletionProvider;
import org.netbeans.spi.editor.completion.CompletionTask;
import org.netbeans.spi.editor.completion.support.AsyncCompletionTask;

/**
 * Completion provided for struts2 validation XML files.
 *
 * @author Aleh Maksimovich (aleh.maksimovich@gmail.com)
 */
@MimeRegistration(mimeType = "text/x-xwork-validator+xml", service = CompletionProvider.class)
public class XWorkValidatorCompletionProvider implements CompletionProvider {

    @Override
    public CompletionTask createTask(int queryType, JTextComponent textComponent) {

        if ((queryType & CompletionProvider.COMPLETION_QUERY_TYPE) != 0) {
            return new AsyncCompletionTask(new XWorkValidatorAsyncCompletionQuery(), textComponent);
        }
        return null;

    }

    @Override
    public int getAutoQueryTypes(JTextComponent textComponent, String typedText) {
        // TODO: at the moment automatic completion is disabled.
        return 0;
    }
}
