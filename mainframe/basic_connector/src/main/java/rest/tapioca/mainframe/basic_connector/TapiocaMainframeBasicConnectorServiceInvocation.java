package rest.tapioca.mainframe.basic_connector;

import static com.j3270.external.CallParser.parse;
import static java.util.logging.Level.FINEST;
import static java.util.logging.Level.WARNING;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.j3270.base.J3270;
import com.j3270.base.J3270Exception;
import com.j3270.external.Call;
import com.l7tech.policy.assertion.ext.CustomAssertion;
import com.l7tech.policy.assertion.ext.CustomAssertionStatus;
import com.l7tech.policy.assertion.ext.ServiceInvocation;
import com.l7tech.policy.assertion.ext.message.CustomJsonData;
import com.l7tech.policy.assertion.ext.message.CustomMessage;
import com.l7tech.policy.assertion.ext.message.CustomMessageAccessException;
import com.l7tech.policy.assertion.ext.message.CustomPolicyContext;
import com.l7tech.policy.assertion.ext.message.format.CustomMessageFormat;
import com.l7tech.policy.assertion.ext.message.format.NoSuchMessageFormatException;
import com.l7tech.policy.variable.NoSuchVariableException;
import com.l7tech.policy.variable.VariableNotSettableException;

public final class TapiocaMainframeBasicConnectorServiceInvocation extends ServiceInvocation {
	private static final Logger logger = Logger.getLogger(TapiocaMainframeBasicConnectorServiceInvocation.class.getName());

	@Override
	public CustomAssertionStatus checkRequest(CustomPolicyContext cpc) {
		try {
			final String script = script(cpc);
			final List<Call> cs = parse(script);
			log(FINEST, cs);
			writeResponse(cpc, invoke(cs));
			return CustomAssertionStatus.NONE;
		} catch (Exception exc) {
			logger.log(WARNING, "Failed to process: " + code(cpc), exc);
			return CustomAssertionStatus.FAILED;
		}
	}

	private void writeResponse(CustomPolicyContext cpc, CustomJsonData cjd)
			throws NoSuchMessageFormatException, CustomMessageAccessException, NoSuchVariableException {
		final CustomMessageFormat<CustomJsonData> cmf = cpc.getFormats().getFormat(CustomJsonData.class);
		cmf.overwrite(targetMessage(cpc), cjd);
	}

	private CustomJsonData invoke(List<Call> cs) throws J3270Exception, IOException {
		final StringBuilder sb = new StringBuilder();
		final J3270 j = new J3270();
		try {
			char separator = '[';
			for (final Call c : cs) {
				for (final String s : c.invoke(j)) {
					sb.append(separator).append('"');
					escape(sb, s);
					sb.append('"').append('\r').append('\n');
					separator = ',';
				}
			}
			sb.append(']');
		} finally {
			j.close();
		}
		final String s = sb.toString();
		log(FINEST, s);
		return new TapiocaJsonData(s);
	}

	private void log(Level level, String s) {
		System.out.println(s);
		if (logger.isLoggable(level)) {
			logger.log(level, s);
		}
	}

	private <T> void log(Level level, Iterable<T> i) {
		if (logger.isLoggable(level)) {
			final StringBuilder sb = new StringBuilder();
			for (final T t : i) {
				sb.append(t).append('\r').append('\n');
			}
			logger.log(level, sb.toString());
		}
		for (final T t : i) {
			System.out.println(t);
		}
	}

	private String code(CustomPolicyContext cpc) {
		return cpc.expandVariable(tapiocaBasicAssertion().getCode());
	}

	private String script(CustomPolicyContext cpc) {
		return cpc.expandVariable(tapiocaBasicAssertion().getScript()).replaceAll("\\\\n", "\n");
	}

	private CustomMessage targetMessage(CustomPolicyContext cpc) throws VariableNotSettableException, NoSuchVariableException {
		final String tmv = tapiocaBasicAssertion().getDestinationTarget().getTargetMessageVariable();
		System.out.println(tmv);
		return cpc.getMessage(tmv);
	}

	private TapiocaMainframeBasicConnectorAssertion tapiocaBasicAssertion() {
		final CustomAssertion ca = this.customAssertion;
		if (ca instanceof TapiocaMainframeBasicConnectorAssertion) { return (TapiocaMainframeBasicConnectorAssertion) ca; }
		throw new UnsupportedOperationException("Not a " + TapiocaMainframeBasicConnectorAssertion.class.getSimpleName() + ": " + ca);
	}

	private static final char[] HEX = "0123456789abcdef".toCharArray();

	private void escape(StringBuilder sb, String s) {
		for (final char c : s.toCharArray()) {
			if (isEscapingRequired(c)) {
				final char hex1 = HEX[(c >> 8) & 0xF];
				final char hex2 = HEX[c & 0xF];
				sb.append('\\').append('u').append('0').append('0').append(hex1).append(hex2);
			} else {
				sb.append(c);
			}
		}
	}

	private boolean isEscapingRequired(char c) {
		return (c < ' ') || (c > '~') || (c == '"');
	}
}
