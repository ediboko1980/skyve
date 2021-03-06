package org.skyve.impl.metadata.view;

import org.skyve.metadata.DecoratedMetaData;

/**
 * This interface is used to indicate whether a widget will by default
 * use up a form column for displaying a field label.
 * @author mike
 */
public interface FormItemWidget extends DecoratedMetaData {
	/**
	 * Indicates whether a widget will by default use up a form column displaying a field label.
	 * @return	whether a field label will be displayed by default.
	 */
	public boolean showsLabelByDefault();
}
