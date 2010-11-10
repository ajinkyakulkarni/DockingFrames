/*
 * Bibliothek - DockingFrames
 * Library built on Java/Swing, allows the user to "drag and drop"
 * panels containing any Swing-Component the developer likes to add.
 * 
 * Copyright (C) 2010 Benjamin Sigg
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 * Benjamin Sigg
 * benjamin_sigg@gmx.ch
 * CH - Switzerland
 */
package bibliothek.gui.dock.common.perspective;

import bibliothek.gui.dock.common.CStation;
import bibliothek.gui.dock.common.intern.CDockable;
import bibliothek.gui.dock.perspective.PerspectiveStation;

/**
 * Represents a {@link CDockable} in a {@link CPerspective}.
 * @author Benjamin Sigg
 */
public interface CDockablePerspective extends CElementPerspective{
	/**
	 * Gets the next parent of this perspective that represents a {@link CStation}. This method
	 * may jump over some parents to get to the next station.
	 * @return the next station, may be <code>null</code>
	 */
	public CStationPerspective getParent();
	
	/**
	 * Sets the working-area of this element. This user will not be able drag
	 * this element away from that working-area.
	 * @param workingArea the working-area, can be <code>null</code>
	 */
	public void setWorkingArea( CStationPerspective workingArea );
	
	/**
	 * Gets the working-area of this element
	 * @return the area, can be <code>null</code>
	 * @see #setWorkingArea(PerspectiveStation)
	 */
	public CStationPerspective getWorkingArea();
}