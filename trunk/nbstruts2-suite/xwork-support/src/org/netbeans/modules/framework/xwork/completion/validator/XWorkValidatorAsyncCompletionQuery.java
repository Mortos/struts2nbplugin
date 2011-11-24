package org.netbeans.modules.framework.xwork.completion.validator;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.netbeans.api.xml.lexer.XMLTokenId;
import org.netbeans.spi.editor.completion.CompletionResultSet;
import org.netbeans.spi.editor.completion.support.AsyncCompletionQuery;
import org.openide.util.Exceptions;

/**
 *
 * @author Aleh
 */
public class XWorkValidatorAsyncCompletionQuery extends AsyncCompletionQuery {

    @Override
    protected void query(CompletionResultSet completionResultSet, Document document, int caretOffset) {
        try {
            XWorkValidatorValueCompletionContext context = new XWorkValidatorValueCompletionContext(document, caretOffset);
            XWorkValidatorCompletor completor = XWorkValidatorCompletorFactory.completor(context);
            completionResultSet.addAllItems(completor.items());
        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            completionResultSet.finish();
        }
    }
}
