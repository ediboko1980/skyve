package org.skyve.wildcat.metadata.repository.document;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.skyve.metadata.model.document.Condition;
import org.skyve.wildcat.metadata.repository.NamedMetaData;
import org.skyve.wildcat.util.UtilImpl;
import org.skyve.wildcat.util.XMLUtil;

@XmlType(namespace = XMLUtil.DOCUMENT_NAMESPACE, 
			name = "condition",
			propOrder = {"documentation", "description", "expression"})
public class ConditionMetaData extends NamedMetaData implements Condition {
	private String description;
	private String documentation;
	private String expression;
	
	@Override
	public String getDocumentation() {
		return documentation;
	}

	@XmlElement(namespace = XMLUtil.DOCUMENT_NAMESPACE)
	public void setDocumentation(String documentation) {
		this.documentation = UtilImpl.processStringValue(documentation);
	}
	
	@Override
	public String getDescription() {
		return description;
	}

	@XmlElement(namespace = XMLUtil.DOCUMENT_NAMESPACE)
	public void setDescription(String description) {
		this.description = UtilImpl.processStringValue(description);
	}
	
	@Override
	public String getExpression() {
		return expression;
	}

	@XmlElement(namespace = XMLUtil.DOCUMENT_NAMESPACE, required = true)
	public void setExpression(String expression) {
		this.expression = UtilImpl.processStringValue(expression);
	}
}
