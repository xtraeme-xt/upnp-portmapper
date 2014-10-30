/**
 * 
 */
package org.chris.portmapper.gui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.chris.portmapper.PortMapperApp;
import org.chris.portmapper.model.Protocol;
import org.chris.portmapper.model.SinglePortMapping;

/**
 * @author chris
 * @version $Id: PortsTableModel.java 126 2013-08-04 15:18:04Z christoph $
 */
public class PortsTableModel extends AbstractTableModel implements
		PropertyChangeListener {

	private static final long serialVersionUID = 1L;
	private final List<SinglePortMapping> ports;

	public PortsTableModel(final List<SinglePortMapping> ports) {
		super();
		this.ports = ports;
	}

	@Override
	public int getColumnCount() {
		return 3;
	}

	@Override
	public int getRowCount() {
		return ports.size();
	}

	@Override
	public Object getValueAt(final int rowIndex, final int columnIndex) {
		final SinglePortMapping port = ports.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return port.getProtocol();
		case 1:
			return port.getExternalPort();
		case 2:
			return port.getInternalPort();
		default:
			throw new IllegalArgumentException("Column " + columnIndex //$NON-NLS-1$
					+ " does not exist"); //$NON-NLS-1$
		}
	}

	@Override
	public Class<?> getColumnClass(final int columnIndex) {
		switch (columnIndex) {
		case 0:
			return Protocol.class;
		case 1:
			return Integer.class;
		case 2:
			return Integer.class;
		default:
			throw new IllegalArgumentException("Column " + columnIndex //$NON-NLS-1$
					+ " does not exist"); //$NON-NLS-1$
		}
	}

	@Override
	public String getColumnName(final int column) {
		switch (column) {
		case 0:
			return PortMapperApp.getResourceMap().getString(
					"preset_dialog.ports.protocol");
		case 1:
			return PortMapperApp.getResourceMap().getString(
					"preset_dialog.ports.external");
		case 2:
			return PortMapperApp.getResourceMap().getString(
					"preset_dialog.ports.internal");
		default:
			throw new IllegalArgumentException("Column " + column //$NON-NLS-1$
					+ " does not exist"); //$NON-NLS-1$
		}
	}

	@Override
	public boolean isCellEditable(final int rowIndex, final int columnIndex) {
		return true;
	}

	@Override
	public void setValueAt(final Object value, final int rowIndex,
			final int columnIndex) {
		final SinglePortMapping port = ports.get(rowIndex);
		switch (columnIndex) {
		case 0:
			port.setProtocol((Protocol) value);
			break;
		case 1:
			port.setExternalPort((Integer) value);
			break;
		case 2:
			port.setInternalPort((Integer) value);
			break;
		default:
			throw new IllegalArgumentException("Column " + columnIndex //$NON-NLS-1$
					+ " does not exist"); //$NON-NLS-1$
		}
	}

	@Override
	public void propertyChange(final PropertyChangeEvent evt) {
		this.fireTableDataChanged();
	}
}
