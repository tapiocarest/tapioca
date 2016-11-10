package rest.tapioca.mainframe.basic_connector;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.swing.JDialog;
import javax.swing.JPanel;

import com.l7tech.policy.assertion.ext.AssertionEditor;
import com.l7tech.policy.assertion.ext.AssertionEditorSupport;
import com.l7tech.policy.assertion.ext.EditListener;

public final class TapiocaMainframeBasicConnectorAssertionEditor extends JDialog implements AssertionEditor {
	private static final long serialVersionUID = 9148243717894678079L;
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(TapiocaMainframeBasicConnectorUI.class.getName());
	private final TapiocaMainframeBasicConnectorAssertion assertion;
	@SuppressWarnings("unused")
	private final Map<String,Object> context;
	private final AssertionEditorSupport support;

	public TapiocaMainframeBasicConnectorAssertionEditor(TapiocaMainframeBasicConnectorAssertion tapiocaMainframeBasicConnectorAssertion,
			Map<String,Object> consoleContext) {
		super(Frame.getFrames().length > 0 ? Frame.getFrames()[0] : null, true);
    setTitle("Tapioca Basic Mainframe Connector");
		assertion = tapiocaMainframeBasicConnectorAssertion;
		context = consoleContext;
		support = new AssertionEditorSupport(this);
		final JPanel main = new TapiocaMainframeBasicConnectorAssertionView(support, assertion);
		setLayout(new BorderLayout());
		add(main, BorderLayout.CENTER);
		pack();
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				main.setVisible(false);
				dispose();
			}
		});
	}

	@Override
	public void addEditListener(EditListener listener) {
		support.addListener(listener);
	}

	@Override
	public void removeEditListener(EditListener listener) {
		support.addListener(listener);
	}

	@Override
	public void edit() {
		setVisible(true);
	}

	public static void main(String[] args) {
		final TapiocaMainframeBasicConnectorAssertion tmbca = new TapiocaMainframeBasicConnectorAssertion();
		tmbca.setScript("Connect(\"mainframe\")\n" //
				+ "Ascii\n" //
				+ "Disconnect");
		final TapiocaMainframeBasicConnectorAssertionEditor tmbcae = new TapiocaMainframeBasicConnectorAssertionEditor(tmbca,
				new HashMap<String,Object>());
		tmbcae.addEditListener(new EditListener() {
			@Override
			public void onEditCancelled(Object source, Object bean) {
				System.out.println("cancelled " + bean);
			}

			@Override
			public void onEditAccepted(Object source, Object bean) {
				System.out.println("accepted " + bean);
			}
		});
		System.out.println(tmbca.getScript());
		tmbcae.edit();
		System.out.println(tmbca.getScript());
	}
}
