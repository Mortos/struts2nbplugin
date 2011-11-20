package org.netbeans.modules.framework.xwork.completion.validator;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.netbeans.api.lexer.Token;
import org.netbeans.api.lexer.TokenHierarchy;
import org.netbeans.api.lexer.TokenSequence;
import org.netbeans.api.xml.lexer.XMLTokenId;
import org.netbeans.editor.BaseDocument;
import org.netbeans.modules.xml.text.dom.SyntaxElement;
import org.netbeans.modules.xml.text.dom.Tag;
import org.netbeans.modules.xml.text.dom.XMLSyntaxSupport;

/**
 *
 * @author Aleh Maksimovich
 */
public class XWorkValidatorCompletionContext {

    private boolean valid;
    private Token<XMLTokenId> token;
    private int offset;
    private int typedLength;
    private CharSequence typedText;
    private CharSequence attributeName;
    private SyntaxElement element;

    public XWorkValidatorCompletionContext(Document document, int caretOffset) throws BadLocationException {
        final TokenHierarchy<Document> tokenHierarchy = TokenHierarchy.get(document);
        final TokenSequence<XMLTokenId> tokenSequence = tokenHierarchy.tokenSequence(XMLTokenId.language());
        tokenSequence.move(caretOffset);
        if (!tokenSequence.moveNext()) {
            valid = false;
            return;
        }
        token = tokenSequence.token();

        final int internalOffset = internalOffset(token);
        offset = token.offset(tokenHierarchy) + internalOffset;
        typedLength = caretOffset - offset;
        typedText = document.getText(offset, typedLength);

        attributeName = attributeName(tokenSequence);

        final XMLSyntaxSupport syntaxSupport = XMLSyntaxSupport.getSyntaxSupport((BaseDocument) document);
        element = syntaxSupport.getElementChain(caretOffset);

        valid = true;
    }

    public boolean isValid() {
        return valid;
    }

    public boolean hasTokenId(XMLTokenId id) {
        return hasTokenId(token, id);
    }

    public int offset() {
        return offset;
    }

    public int typedLength() {
        return typedLength;
    }

    public CharSequence typedText() {
        return typedText;
    }

    public CharSequence getAttributeName() {
        return attributeName;
    }

    public String tagName() {
        if (element instanceof Tag) {
            return ((Tag) element).getNodeName().substring(1);
        }
        return null;
    }

    private boolean hasTokenId(Token<XMLTokenId> token, XMLTokenId id) {
        return id.equals(token.id());
    }

    private int internalOffset(Token<XMLTokenId> token) {
        if (hasTokenId(token, XMLTokenId.VALUE)) {
            return 1;
        }
        return 0;
    }

    private CharSequence attributeName(TokenSequence<XMLTokenId> tokenSequence) {
        while (tokenSequence.movePrevious()) {
            Token<XMLTokenId> previousToken = tokenSequence.token();
            if (XMLTokenId.ARGUMENT.equals(previousToken.id())) {
                return previousToken.text();
            }
        }
        return null;
    }
}
