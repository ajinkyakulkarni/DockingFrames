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
package bibliothek.gui.dock.util;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.JComponent;

import bibliothek.gui.DockTheme;
import bibliothek.util.Todo;
import bibliothek.util.Todo.Compatibility;
import bibliothek.util.Todo.Version;

/**
 * A {@link BackgroundPaint} is used to paint the background of various {@link Component}s of 
 * this framework.<br>
 * Please note that some items provided by a {@link DockTheme} do not use a {@link BackgroundPaint} 
 * because they already paint their background in a specific way. 
 * @author Benjamin Sigg
 */
@Todo( target=Version.VERSION_1_1_1, compatibility=Compatibility.BREAK_MINOR, priority=Todo.Priority.MINOR,
		description="allow BackgroundPaint to enabled/disable transparency" )
public interface BackgroundPaint {
	/**
	 * Informs this paint that is will be used by <code>component</code>.
	 * @param component the component that is going to use this paint, not <code>null</code>
	 */
	public void install( BackgroundComponent component );
	
	/**
	 * Informs this paint that it is no longer used by <code>component</code>.
	 * @param component the component that no longer uses this paint, not <code>null</code>
	 */
	public void uninstall( BackgroundComponent component );
	
	/**
	 * Paints the background <code>component</code> using the graphics context <code>g</code>. The
	 * exact behavior of this method may depend on the type of <code>component</code>.<br>
	 * This method should be aware of the methods {@link PaintableComponent#paintBackground(Graphics)} and
	 * {@link PaintableComponent#paintForeground(Graphics)}:
	 * <ul>
	 * 	<li>If these methods are not called, then they will be executed automatically.</li>
	 * 	<li>If these methods are called with an argument of <code>null</code> then they will neither paint nor be executed automatically.</li>
	 * 	<li>If these methods are called with an argument not <code>null</code> then they paint but will not be executed automatically.</li>
	 * </ul>
	 * Further more implementations should follow these guide lines to prevent artifacts while painting:
	 * <ul>
	 * 	<li>If <code>paintable</code> is solid, then the entire background must be painted (every pixel must be filled).</li>
	 *  <li>Painting over children ({@link Component}s) will almost certainly not work. The framework uses special transparent panels to do that.</li>
	 *  <li>Painting semi-transparent children will almost certainly not work because the children may not be marked as transparent (see {@link JComponent#isOpaque()}.</li>
	 *  <li>In general painting should not require much time (a few milliseconds at most) because painting can happen often.</li>
	 * </ul>
	 * @param background the component to paint, is installed on this paint, not <code>null</code>
	 * @param paintable the part of the component that is to be painted, may be a child-{@link Component}
	 * @param g the graphics context to use
	 * @throws IllegalArgumentException if <code>component</code> is not equal to <code>background</code> or not
	 * a child of <code>background</code>
	 */
	public void paint( BackgroundComponent background, PaintableComponent paintable, Graphics g );
}