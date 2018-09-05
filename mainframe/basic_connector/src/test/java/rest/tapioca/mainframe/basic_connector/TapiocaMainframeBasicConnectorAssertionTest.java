package rest.tapioca.mainframe.basic_connector;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class TapiocaMainframeBasicConnectorAssertionTest {
	@Test
	public void variablesUsed() throws Exception {
		final String s = "connect(\"${hostname}\")\n" //
				+ "string(\"logon ${username}\")\n" //
				+ "enter\n" //
				+ "wait(InputField)\n" //
				+ "string(\"${password}\")\n" //
				+ "enter\n" //
				+ "pf(3)\n" //
				+ "ascii\n" //
				+ "disconnect\n" //
				;
		tmbca.setCode("code-${program}");
		tmbca.setScript(s);
		assertThat(asList(tmbca.getVariablesUsed()), equalTo(asList("program", "hostname", "username", "password")));
		assertThat(tmbca.getDestinationTarget().getTargetName(), equalTo("Response"));
	}

	TapiocaMainframeBasicConnectorAssertion tmbca = new TapiocaMainframeBasicConnectorAssertion();
}
