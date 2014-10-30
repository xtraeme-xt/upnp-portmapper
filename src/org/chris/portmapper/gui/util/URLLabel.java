/**
 * 
 */
package org.chris.portmapper.gui.util;

import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JLabel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class implements a label that looks and behaves like a link, i.e. you
 * can click on it and the URL is opened in a browser.
 * 
 * @author chris
 * @version $Id: URLLabel.java 126 2013-08-04 15:18:04Z christoph $
 */
public class URLLabel extends JLabel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final static Log logger = LogFactory.getLog(URLLabel.class);

	private String text;

	private final Desktop desktop;

	private URI uri;

	public URLLabel(final String name) {
		this.text = name;
		this.setLabelText();
		this.setName(name);
		this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		this.desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop()
				: null;

		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent arg0) {
				openUrl();
			}
		});
	}

	private void openUrl() {
		logger.debug("User clicked on URLLabel: open URL '" + uri
				+ "' in browser");
		if (desktop == null || !desktop.isSupported(Desktop.Action.BROWSE)) {
			logger.warn("Opening URLs is not supported on this machine, please open url manually: "
					+ uri);
			return;
		}
		try {
			desktop.browse(uri);
		} catch (final IOException e) {
			throw new RuntimeException("Error opening uri " + uri, e);
		}
	}

	private static URI createUri(final String url) {
		try {
			return new URI(url);
		} catch (final URISyntaxException e) {
			throw new RuntimeException("Error creating URI for url " + url);
		}
	}

	private void setLabelText() {
		final String url = uri != null ? uri.toString() : "";
		super.setText("<html><a href=\\\\\\\\\\\"" + url + "\\\\\\\\\\\">"
				+ text + "</a></html>");
	}

	public String getUrl() {
		return uri.toString();
	}

	public void setUrl(final String url) {
		this.uri = createUri(url);
		setLabelText();
	}

	public String getLabel() {
		return text;
	}

	public void setLabel(final String text) {
		this.text = text;
		setLabelText();
	}
}
