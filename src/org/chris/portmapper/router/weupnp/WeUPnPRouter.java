/**
 * 
 */
package org.chris.portmapper.router.weupnp;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chris.portmapper.model.PortMapping;
import org.chris.portmapper.model.Protocol;
import org.chris.portmapper.router.AbstractRouter;
import org.chris.portmapper.router.IRouter;
import org.chris.portmapper.router.RouterException;
import org.wetorrent.upnp.GatewayDevice;
import org.wetorrent.upnp.PortMappingEntry;
import org.wetorrent.upnp.WeUPnPException;

/**
 * This class is an implements an {@link IRouter} using the weupnp library's
 * {@link GatewayDevice}.
 * 
 * @author chris
 * @version $Id: WeUPnPRouter.java 126 2013-08-04 15:18:04Z christoph $
 */
public class WeUPnPRouter extends AbstractRouter {

	private final Log logger = LogFactory.getLog(this.getClass());
	private final GatewayDevice device;

	/**
	 * @param device
	 */
	WeUPnPRouter(final GatewayDevice device) {
		super(device.getFriendlyName());
		this.device = device;
	}

	@Override
	public void addPortMapping(final PortMapping mapping)
			throws RouterException {
		try {
			device.addPortMapping(mapping.getExternalPort(),
					mapping.getInternalPort(), mapping.getInternalClient(),
					mapping.getProtocol().getName(), mapping.getDescription());
		} catch (final WeUPnPException e) {
			throw new RouterException("Could not add portmapping", e);
		}
	}

	@Override
	public void addPortMappings(final Collection<PortMapping> mappings)
			throws RouterException {
		for (final PortMapping mapping : mappings) {
			this.addPortMapping(mapping);
		}
	}

	@Override
	public void disconnect() {
		// noting to do right now
	}

	@Override
	public String getExternalIPAddress() throws RouterException {
		try {
			return device.getExternalIPAddress();
		} catch (final WeUPnPException e) {
			throw new RouterException("Could not get external IP address", e);
		}
	}

	@Override
	public String getInternalHostName() {
		final String url = device.getPresentationURL();
		if (url == null || url.trim().length() == 0) {
			return null;
		}
		try {
			return new URL(url).getHost();
		} catch (final MalformedURLException e) {
			logger.warn("Could not get URL for internal host name '" + url
					+ "'", e);
			return url;
		}
	}

	@Override
	public int getInternalPort() throws RouterException {
		String url = device.getPresentationURL();
		if (url == null) {
			logger.info("Presentation url is null: use url base");
			url = device.getURLBase();
		}
		if (url == null) {
			throw new RouterException("Presentation URL and URL base are null");
		}

		try {
			return new URL(url).getPort();
		} catch (final MalformedURLException e) {
			throw new RouterException("Could not get internal port from URL '"
					+ url + "'", e);
		}
	}

	@Override
	public Collection<PortMapping> getPortMappings() throws RouterException {
		final Collection<PortMapping> mappings = new LinkedList<>();
		boolean morePortMappings = true;
		int index = 0;
		while (morePortMappings) {
			PortMappingEntry entry = null;
			try {
				logger.debug("Getting port mapping " + index + "...");
				entry = device.getGenericPortMappingEntry(index);
				logger.debug("Got port mapping " + index + ": " + entry);
			} catch (final WeUPnPException e) {
				morePortMappings = false;
				logger.debug("Got an exception with message '" + e.getMessage()
						+ "' for index " + index
						+ ", stop getting more mappings");
			}

			if (entry != null) {
				final Protocol protocol = entry.getProtocol().equalsIgnoreCase(
						"TCP") ? Protocol.TCP : Protocol.UDP;
				final PortMapping m = new PortMapping(protocol,
						entry.getRemoteHost(), entry.getExternalPort(),
						entry.getInternalClient(), entry.getInternalPort(),
						entry.getPortMappingDescription());
				mappings.add(m);
			} else {
				logger.debug("Got null port mapping for index " + index);
			}
			index++;
		}
		return mappings;
	}

	@Override
	public void logRouterInfo() throws RouterException {
		final Map<String, String> info = new HashMap<>();
		info.put("friendlyName", device.getFriendlyName());
		info.put("manufacturer", device.getManufacturer());
		info.put("modelDescription", device.getModelDescription());

		final SortedSet<String> sortedKeys = new TreeSet<>(info.keySet());

		for (final String key : sortedKeys) {
			final String value = info.get(key);
			logger.info("Router Info: " + key + " \t= " + value);
		}

		logger.info("def loc " + device.getLocation());
		logger.info("device type " + device.getDeviceType());
	}

	@Override
	public void removeMapping(final PortMapping mapping) throws RouterException {
		this.removePortMapping(mapping.getProtocol(), mapping.getRemoteHost(),
				mapping.getExternalPort());
	}

	@Override
	public void removePortMapping(final Protocol protocol,
			final String remoteHost, final int externalPort)
			throws RouterException {
		try {
			device.deletePortMapping(externalPort, protocol.getName());
		} catch (final WeUPnPException e) {
			throw new RouterException("Could not delete port mapping", e);
		}
	}
}
