package org.skyve.metadata.sail.language.step.context;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.skyve.impl.sail.execution.AutomationContext;
import org.skyve.impl.util.UtilImpl;
import org.skyve.impl.util.XMLMetaData;
import org.skyve.impl.web.UserAgentType;
import org.skyve.metadata.sail.execution.Executor;
import org.skyve.metadata.sail.language.step.interaction.navigation.NavigateList;

/**
 * Push a new list automation context onto the stack.
 * @author mike
 */
@XmlType(namespace = XMLMetaData.SAIL_NAMESPACE)
@XmlRootElement(namespace = XMLMetaData.SAIL_NAMESPACE)
public class PushListContext extends NavigateList {
	private String uxui;
	private UserAgentType userAgentType;

	public String getUxui() {
		return uxui;
	}
	
	@XmlAttribute(name = "uxui")
	public void setUxui(String uxui) {
		this.uxui = UtilImpl.processStringValue(uxui);
	}

	public UserAgentType getUserAgentType() {
		return userAgentType;
	}
	
	@XmlAttribute(name = "userAgentType")
	public void setUserAgentType(UserAgentType userAgentType) {
		this.userAgentType = userAgentType;
	}
	
	@Override
	public void execute(Executor executor) {
		executor.executePushListContext(this);
	}

	@Override
	public String getIdentifier(AutomationContext context) {
		return NavigateList.listGridIdentifier(context, getModuleName(), getQueryName(), getDocumentName(), getModelName());
	}
}
