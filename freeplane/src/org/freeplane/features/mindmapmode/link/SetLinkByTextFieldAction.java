/*
 *  Freeplane - mind map editor
 *  Copyright (C) 2008 Joerg Mueller, Daniel Polansky, Christian Foltin, Dimitry Polivaev
 *
 *  This file is modified by Dimitry Polivaev in 2008.
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
package org.freeplane.features.mindmapmode.link;

import java.awt.event.ActionEvent;

import org.freeplane.core.controller.Controller;
import org.freeplane.core.modecontroller.ModeController;
import org.freeplane.core.model.NodeModel;
import org.freeplane.core.resources.ResourceController;
import org.freeplane.core.ui.AFreeplaneAction;
import org.freeplane.core.ui.components.UITools;
import org.freeplane.core.undo.IUndoableActor;
import org.freeplane.features.common.link.NodeLinks;

class SetLinkByTextFieldAction extends AFreeplaneAction {
	public SetLinkByTextFieldAction(final Controller controller) {
		super(controller, "set_link_by_textfield", (String) null);
	}

	public void actionPerformed(final ActionEvent e) {
		final ModeController modeController = getModeController();
		String inputValue = UITools.showInputDialog(getController(), getController().getSelection().getSelected(), ResourceController
		    .getText("edit_link_manually"), NodeLinks.getLink(modeController.getMapController().getSelectedNode()));
		if (inputValue != null) {
			if (inputValue.equals("")) {
				inputValue = null;
			}
			setLink(modeController.getMapController().getSelectedNode(), inputValue);
		}
	}

	public void setLink(final NodeModel node, final String link) {
		final IUndoableActor actor = new IUndoableActor() {
			private String oldlink;
			private String oldTargetID;

			public void act() {
				NodeLinks links = NodeLinks.getLinkExtension(node);
				if (links != null) {
					oldlink = links.getLink();
					oldTargetID = links.removeLocalHyperLink();
				}
				else {
					links = NodeLinks.createLinkExtension(node);
				}
				if (link != null && link.startsWith("#")) {
					links.setLocalHyperlink(link.substring(1));
				}
				links.setLink(link);
				getModeController().getMapController().nodeChanged(node);
			}

			public String getDescription() {
				return "setLink";
			}

			public void undo() {
				final NodeLinks links = NodeLinks.getLinkExtension(node);
				links.setLocalHyperlink(oldTargetID);
				links.setLink(oldlink);
				getModeController().getMapController().nodeChanged(node);
			}
		};
		getModeController().execute(actor);
	}
}
