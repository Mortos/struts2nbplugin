package org.netbeans.modules.web.frameworks.struts2.completion;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import javax.swing.text.StyledDocument;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.netbeans.api.lexer.Token;
import org.netbeans.api.lexer.TokenHierarchy;
import org.netbeans.api.lexer.TokenSequence;
import org.netbeans.api.xml.lexer.XMLTokenId;
import org.netbeans.editor.BaseDocument;
import org.netbeans.modules.web.frameworks.struts2.TestUtil;
import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileSystem;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;

/**
 *
 * @author Sujit Nair, Rohan Ranade
 */
public class LexerAttributeValueCompletorTest {

    public LexerAttributeValueCompletorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void doCompletionTest() throws Exception {
        String docText = TestUtil.createXMLConfigText(
                "<package name='mailreader-default' namespace='/' extends='ff'/>");
        int offset = docText.indexOf("ff'/>") + 2;

        //((BaseDocument) doc).readLock();
        TokenHierarchy<String> hierarchy = TokenHierarchy.create(docText, XMLTokenId.language());
        TokenSequence<XMLTokenId> xmlTokens = hierarchy.tokenSequence(XMLTokenId.language());
        xmlTokens.move(offset);
        xmlTokens.moveNext();
        Token<XMLTokenId> token = xmlTokens.token();
        token.id();
        //((BaseDocument) doc).readUnlock();
    }
}