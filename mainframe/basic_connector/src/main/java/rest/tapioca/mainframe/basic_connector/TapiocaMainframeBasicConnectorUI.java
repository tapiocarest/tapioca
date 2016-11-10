package rest.tapioca.mainframe.basic_connector;

import java.io.Serializable;
import java.util.Map;

import javax.swing.ImageIcon;

import com.l7tech.policy.assertion.ext.AssertionEditor;
import com.l7tech.policy.assertion.ext.CustomAssertion;
import com.l7tech.policy.assertion.ext.CustomAssertionUI;
import com.l7tech.policy.assertion.ext.cei.UsesConsoleContext;

public final class TapiocaMainframeBasicConnectorUI implements CustomAssertionUI, UsesConsoleContext<String,Object>, Serializable {
	private static final long serialVersionUID = -7819561578771820404L;
	private Map<String,Object> consoleContext;

	public AssertionEditor getEditor(CustomAssertion customAssertion) {
		final TapiocaMainframeBasicConnectorAssertion tmbca = checkCast(TapiocaMainframeBasicConnectorAssertion.class, customAssertion);
		return new TapiocaMainframeBasicConnectorAssertionEditor(tmbca, consoleContext);
	}

	@Override
	public ImageIcon getSmallIcon() {
		return resourceAsImageIcon("icon.tapioca.16.png");
	}

	@Override
	public ImageIcon getLargeIcon() {
		return resourceAsImageIcon("icon.tapioca.32.png");
	}

	@Override
	public void setConsoleContextUsed(Map<String,Object> map) {
		consoleContext = map;
	}

	private static <T> T checkCast(Class<T> type, Object object) {
		if (!type.isInstance(object)) { throw new IllegalArgumentException(type.getName() + " type is required"); }
		return type.cast(object);
	}

	private static ImageIcon resourceAsImageIcon(final String name) {
		try {
			return new ImageIcon(TapiocaMainframeBasicConnectorUI.class.getClassLoader().getResource(name));
		} catch (Exception ignored) {
			return null;
		}
	}
}
