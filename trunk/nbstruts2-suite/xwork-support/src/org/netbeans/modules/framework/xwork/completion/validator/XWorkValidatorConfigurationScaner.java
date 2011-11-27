package org.netbeans.modules.framework.xwork.completion.validator;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.openide.filesystems.FileAttributeEvent;
import org.openide.filesystems.FileChangeListener;
import org.openide.filesystems.FileEvent;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileRenameEvent;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 *
 * @author Aleh
 */
public class XWorkValidatorConfigurationScaner implements FileChangeListener {

    private static final String XWORK_VALIDATOR_NAME_XPATH = "/validators/validator/@name";
    private static final XPathExpression selector;
    private FileObject file;
    private boolean actual;
    private Set<String> choises = new HashSet<String>();

    static {
        XPathExpression expression = null;
        try {
            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xPath = xPathFactory.newXPath();
            expression = xPath.compile(XWORK_VALIDATOR_NAME_XPATH);
        } catch (XPathExpressionException ex) {
        }
        selector = expression;
    }

    public XWorkValidatorConfigurationScaner(FileObject file) {
        this.file = file;
        this.actual = false;
    }

    public Set<String> getChoises() {
        if (!actual) {
            refreshChoises();
        }
        return choises;
    }

    @Override
    public void fileFolderCreated(FileEvent fe) {
    }

    @Override
    public void fileDataCreated(FileEvent fe) {
    }

    @Override
    public void fileChanged(FileEvent fe) {
        actual = false;
    }

    @Override
    public void fileDeleted(FileEvent fe) {
    }

    @Override
    public void fileRenamed(FileRenameEvent fre) {
    }

    @Override
    public void fileAttributeChanged(FileAttributeEvent fae) {
    }

    private void refreshChoises() {
        try {
            InputStream fileStream = file.getInputStream();
            InputSource fileSource = new InputSource(fileStream);

            NodeList validatorNames = (NodeList) selector.evaluate(fileSource, XPathConstants.NODESET);

            choises.clear();
            int validatorNamesCount = validatorNames.getLength();
            for (int i = 0; i < validatorNamesCount; i++) {
                Node nameAttribute = validatorNames.item(i);
                choises.add(nameAttribute.getNodeValue());
            }
            actual = true;
        } catch (IOException ex) {
        } catch (XPathExpressionException ex) {
        }
    }
}
