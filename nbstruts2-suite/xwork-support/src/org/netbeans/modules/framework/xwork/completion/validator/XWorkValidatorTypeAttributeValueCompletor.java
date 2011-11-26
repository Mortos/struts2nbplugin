package org.netbeans.modules.framework.xwork.completion.validator;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.netbeans.api.java.classpath.ClassPath;
import org.openide.filesystems.FileObject;
import org.openide.xml.XMLUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author Aleh
 */
public class XWorkValidatorTypeAttributeValueCompletor implements XWorkValidatorCompletor {

    private static final String DEFAULT_XWORK_VALIDATORS_CONFIGURATION = "com/opensymphony/xwork2/validator/validators/default.xml";
    private static final String CUSTOM_XWORK_VALIDATORS_CONFIGURATION = "validators.xml";
    private static final String XWORK_VALIDATOR_NAME_XPATH = "/validators/validator/@name";
    private static final XPathExpression selector;
    private Set<String> choises = new HashSet<String>();
    private String typedText;

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

    public XWorkValidatorTypeAttributeValueCompletor(XWorkValidatorValueCompletionContext context) {
        typedText = context.typedText().toString();

        ClassPath compileClassPath = ClassPath.getClassPath(context.file(), ClassPath.COMPILE);
        FileObject xworkLibraryConfig = compileClassPath.findResource(DEFAULT_XWORK_VALIDATORS_CONFIGURATION);
        extractValidatorNames(xworkLibraryConfig);

        ClassPath sourceClassPath = ClassPath.getClassPath(context.file(), ClassPath.SOURCE);
        FileObject xworkSourceConfig = sourceClassPath.findResource(CUSTOM_XWORK_VALIDATORS_CONFIGURATION);
        extractValidatorNames(xworkSourceConfig);
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

    private void extractValidatorNames(FileObject file) {
        if (!isValidConfig(file)) {
            return;
        }

        try {
            InputStream fileStream = file.getInputStream();
            InputSource fileSource = new InputSource(fileStream);
            Document document = XMLUtil.parse(fileSource, true, true, null, null);

            NodeList validatorNames = (NodeList) selector.evaluate(document, XPathConstants.NODESET);
            int validatorNamesCount = validatorNames.getLength();
            for (int i = 0; i < validatorNamesCount; i++) {
                Node nameAttribute = validatorNames.item(i);
                choises.add(nameAttribute.getNodeValue());
            }
        } catch (IOException ex) {
        } catch (SAXException ex) {
        } catch (XPathExpressionException ex) {
        }
    }

    private boolean isValidConfig(FileObject file) {
        return (file != null)
                && file.isValid()
                && XWorkValidatorXML.CONFIGURATION.isMimeType(file.getMIMEType());
    }
}
