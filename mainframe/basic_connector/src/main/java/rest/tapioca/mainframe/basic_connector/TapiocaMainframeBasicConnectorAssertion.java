package rest.tapioca.mainframe.basic_connector;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.l7tech.policy.assertion.UsesVariables;
import com.l7tech.policy.assertion.ext.CustomAssertion;
import com.l7tech.policy.assertion.ext.targetable.CustomMessageTargetable;
import com.l7tech.policy.assertion.ext.targetable.CustomMessageTargetableSupport;

public final class TapiocaMainframeBasicConnectorAssertion implements CustomAssertion, UsesVariables {
	private static final long serialVersionUID = -883509105512812641L;
	private String code;
	private String script;
	private CustomMessageTargetable response;

	public TapiocaMainframeBasicConnectorAssertion() {
		init();
	}

	private Object readResolve() {
		init();
		return this;
	}

	private void init() {
		code = coalesce(code, "");
		script = coalesce(script, "");
		response = (response == null) ? new CustomMessageTargetableSupport(CustomMessageTargetableSupport.TARGET_RESPONSE, false) : response;

	}

	@Override
	public String getName() {
		return "Route via Tapioca Basic Mainframe Connector";
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = coalesce(code, "");
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = coalesce(script, "");
	}

	public CustomMessageTargetable getDestinationTarget() {
		return response;
	}

	@Override
	public String[] getVariablesUsed() {
		final List<String> l = new ArrayList<>();
		l.addAll(asList(response.getVariablesUsed()));
		addVariablesUsed(l, code);
		addVariablesUsed(l, script);
		return l.toArray(new String[l.size()]);
	}

	@Override
	public String toString() {
		return "TapiocaMainframeBasicConnectorAssertion: " + script;
	}

	private static <T> T coalesce(T t1, T t2) {
		return (t1 == null) ? t2 : t1;
	}

	private static final Pattern PATTERN = Pattern.compile("\\$\\{([^}]+)\\}");

	private static void addVariablesUsed(List<String> l, String s) {
		for (final Matcher m = PATTERN.matcher(s); m.find();) {
			final int gc = m.groupCount();
			for (int i = 1; i <= gc; i++) {
				l.add(m.group(i));
			}
		}
	}
}
