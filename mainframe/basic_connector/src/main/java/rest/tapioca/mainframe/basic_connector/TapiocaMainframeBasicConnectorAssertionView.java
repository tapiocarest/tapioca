package rest.tapioca.mainframe.basic_connector;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.l7tech.policy.assertion.ext.AssertionEditorSupport;

public final class TapiocaMainframeBasicConnectorAssertionView extends JPanel {
	private static final long serialVersionUID = -1207603512926151188L;
	private AssertionEditorSupport support;
	private TapiocaMainframeBasicConnectorAssertion assertion;

	public TapiocaMainframeBasicConnectorAssertionView(AssertionEditorSupport support, TapiocaMainframeBasicConnectorAssertion assertion) {
		super(new BorderLayout());
		this.support = support;
		this.assertion = assertion;
		final JTextArea area = newTextArea();
		add(area, BorderLayout.CENTER);
		add(newButtons(area), BorderLayout.SOUTH);
	}

	private void cancel() {
		support.fireCancelled(assertion);
	}

	private void save(String script) {
		assertion.setScript(script);
		support.fireEditAccepted(assertion);
	}

	private JTextArea newTextArea() {
		final JTextArea area = new JTextArea();
		area.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		area.setFont(new Font("Monospaced", Font.BOLD, 16));
		area.setBackground(Color.BLACK);
		area.setForeground(Color.GREEN);
		area.setCaretColor(Color.GREEN);
		area.setText(assertion.getScript());
		area.setRows(40);
		area.setColumns(80);
		area.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent ke) {
				if (ke.isAltDown() && ke.getKeyChar() == KeyEvent.VK_ENTER) {
					ke.consume();
					save(area.getText());
					close(area);
				}
			}
		});
		return area;
	}

	private JPanel newButtons(JTextArea area) {
		final JPanel buttons = new JPanel(new BorderLayout());
		buttons.add(newCancel(), BorderLayout.WEST);
		buttons.add(newSave(area), BorderLayout.EAST);
		return buttons;
	}

	private JButton newSave(final JTextArea area) {
		final JButton save = new JButton("Save");
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				save(area.getText());
				close(save);
			}
		});
		return save;
	}

	private JButton newCancel() {
		final JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				cancel();
				close(cancel);
			}
		});
		return cancel;
	}

	private void close(JComponent component) {
		final Window w = (Window) component.getTopLevelAncestor();
		w.dispatchEvent(new WindowEvent(w, WindowEvent.WINDOW_CLOSING));
	}
}