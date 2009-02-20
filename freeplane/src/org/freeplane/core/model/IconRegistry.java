/*
 *  Freeplane - mind map editor
 *  Copyright (C) 2008 Dimitry Polivaev
 *
 *  This file author is Dimitry Polivaev
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.freeplane.core.model;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.freeplane.core.extension.IExtension;
import org.freeplane.core.filter.util.SortedMapListModel;
import org.freeplane.core.modecontroller.MapController;

/**
 * @author Dimitry Polivaev
 * 03.01.2009
 */
public class IconRegistry implements IExtension {
	final private SortedMapListModel mapIcons;

	IconRegistry(MapController mapController, final MapModel map) {
		super();
		mapIcons = new SortedMapListModel();
		registryNodeIcons(mapController, map.getRootNode());
	}

	void addIcon(final MindIcon icon) {
		mapIcons.add(icon);
	}

	public SortedMapListModel getIcons() {
		return mapIcons;
	}

	private void registryNodeIcons(MapController mapController, final NodeModel node) {
		final List icons = node.getIcons();
		final Iterator i = icons.iterator();
		while (i.hasNext()) {
			final MindIcon icon = (MindIcon) i.next();
			addIcon(icon);
		}
		final ListIterator<NodeModel> iterator = mapController.childrenUnfolded(node);
		while (iterator.hasNext()) {
			final NodeModel next = iterator.next();
			registryNodeIcons(mapController, next);
		}
	}
}
