/**
 * 
 */
package org.chris.portmapper.gui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractListModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chris.portmapper.Settings;
import org.chris.portmapper.model.PortMappingPreset;

/**
 * @author chris
 * @version $Id: PresetListModel.java 126 2013-08-04 15:18:04Z christoph $
 */
public class PresetListModel extends AbstractListModel<PortMappingPreset>
		implements PropertyChangeListener {

	private static final long serialVersionUID = 1L;
	private final Log logger = LogFactory.getLog(this.getClass());
	private final Settings settings;

	public PresetListModel(final Settings settings) {
		super();
		this.settings = settings;
		settings.addPropertyChangeListener(
				Settings.PROPERTY_PORT_MAPPING_PRESETS, this);
	}

	@Override
	public void propertyChange(final PropertyChangeEvent arg0) {
		logger.debug("Presets have changed: update list");
		this.fireContentsChanged(this, 0, settings.getPresets().size() - 1);
	}

	@Override
	public PortMappingPreset getElementAt(final int index) {
		return settings.getPresets().get(index);
	}

	@Override
	public int getSize() {
		if (settings == null || settings.getPresets() == null) {
			return 0;
		}
		return settings.getPresets().size();
	}
}
