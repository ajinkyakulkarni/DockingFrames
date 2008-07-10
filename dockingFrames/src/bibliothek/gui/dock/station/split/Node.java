/*
 * Bibliothek - DockingFrames
 * Library built on Java/Swing, allows the user to "drag and drop"
 * panels containing any Swing-Component the developer likes to add.
 * 
 * Copyright (C) 2007 Benjamin Sigg
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

package bibliothek.gui.dock.station.split;

import java.awt.Dimension;
import java.awt.Rectangle;

import bibliothek.gui.Dockable;
import bibliothek.gui.dock.SplitDockStation;
import bibliothek.gui.dock.SplitDockStation.Orientation;
import bibliothek.gui.dock.station.split.SplitDockTree.Key;

/**
 * A Node represents an element in the tree of a {@link SplitDockStation}.
 * Every node has two children. The areas of the children are separated by
 * a "divider", whose position can be changed.
 * @author Benjamin Sigg
 */
public class Node extends SplitNode{
    /** The area of the left child is either at the left or at the top of the area of this node. */
    private SplitNode left;
    /** The area of the right child is either at the right or at the bottom of the area of this node. */
    private SplitNode right;
    /** The location of the divider. It's the fraction of the area of the node which is given to the left child. */
    private double divider = 0.5;
    /** The order in which the children are aligned. */
    private Orientation orientation = Orientation.VERTICAL;
    /** The area of the divider between the two children */
    private Rectangle dividerBounds = new Rectangle();
    
    /**
     * Constructs a new node.
     * @param access the access to the owner-station of this node.
     * @param left the left child
     * @param right the right child
     * @param orientation how the children are aligned
     */
    public Node( SplitDockAccess access, SplitNode left, SplitNode right, Orientation orientation ){
        this( access, left, right );
        this.orientation = orientation;
    }
    
    /**
     * Constructs a new node.
     * @param access the access to the owner-station of this node
     * @param left the left child
     * @param right the right child
     */
    public Node( SplitDockAccess access, SplitNode left, SplitNode right ){
        super( access );
        setRight( right );
        setLeft( left );
    }
    
    /**
     * Constructs a new node.
     * @param access the access to the owner-station of this node
     */
    public Node( SplitDockAccess access ){
        super( access );
    }
    
    /**
     * Sets the left child of  this node. The area of this child
     * will be in the left or the upper half of the area of this node.<br>
     * Note that setting the child to <code>null</code> does not delete
     * the child from the system, only a call to {@link SplitNode#delete(boolean)}
     * does that.
     * @param left the left child or <code>null</code>
     */
    public void setLeft( SplitNode left ){
        if( this.left != null )
            this.left.setParent( null );
        this.left = left;
        if( left != null ){
            left.delete( false );
            left.setParent( this );
        }
        
        getAccess().getOwner().revalidate();
        getAccess().getOwner().repaint();
    }
    
    /**
     * Gets the left child of this node.
     * @return the child
     * @see #setLeft(SplitNode)
     */
    public SplitNode getLeft() {
        return left;
    }
    
    /**
     * Sets the right child of this node. The area of this child
     * will be in the right or the bottom half of the area of this
     * node.<br>
     * Note that setting the child to <code>null</code> does not delete
     * the child from the system, only a call to {@link SplitNode#delete(boolean)}
     * does that.
     * @param right the right child
     */
    public void setRight( SplitNode right ){
        if( this.right != null )
            this.right.setParent( null );
        this.right = right;
        if( right != null ){
            right.delete( false );
            right.setParent( this );
        }
        
        getAccess().getOwner().revalidate();
        getAccess().getOwner().repaint();
    }

    /**
     * Gets the right child of this node.
     * @return the child
     * @see #setRight(SplitNode)
     */
    public SplitNode getRight() {
        return right;
    }
    
    @Override
    public int getChildLocation( SplitNode child ) {
        if( left == child )
            return 0;
        if( right == child )
            return 1;
        
        return -1;
    }
    
    @Override
    public void setChild( SplitNode child, int location ) {
        if( location == 0 )
            setLeft( child );
        else if( location == 1 )
            setRight( child );
        else
            throw new IllegalArgumentException( "Location not valid " + location );
    }
    
    /**
     * Gets the orientation of this node. The orientation tells how to layout
     * the children. If the orientation is {@link Orientation#VERTICAL}, one child
     * will be at the top and the other at the bottom.
     * @return the orientation
     */
    public Orientation getOrientation() {
        return orientation;
    }
    
    /**
     * Changes the orientation of this node.
     * @param orientation the new orientation
     */
    public void setOrientation( Orientation orientation ) {
        if( orientation == null )
            throw new NullPointerException( "orientation must not be null" );
        this.orientation = orientation;
        getAccess().getOwner().revalidate();
    }
    
    @Override
    public Dimension getMinimumSize() {
    	Dimension minLeft = left == null ? new Dimension() : left.getMinimumSize();
    	Dimension minRight = right == null ? new Dimension() : right.getMinimumSize();
    	int divider = getAccess().getOwner().getDividerSize();
    	
    	if( orientation == Orientation.HORIZONTAL ){
    		return new Dimension( minLeft.width + divider + minRight.width,
    				Math.max( minLeft.height, minRight.height ));
    	}
    	else{
    		return new Dimension( Math.max( minLeft.width, minRight.width),
    				minLeft.height + divider + minRight.height );
    	}
    }
    
    /**
     * Sets the location of the divider. The area of the left child is the area
     * of the whole node multiplied with <code>divider</code>. 
     * @param divider the divider
     */
    public void setDivider( double divider ){
        this.divider = divider;
        getAccess().getOwner().revalidate();
        getAccess().getOwner().repaint();
    }
    
    /**
     * Gets the location of the divider.
     * @return the divider
     * @see #setDivider(double)
     */
    public double getDivider() {
        return divider;
    }
    
    @Override
    public void updateBounds( double x, double y, double width, double height, double factorW, double factorH, boolean components ) {
        super.updateBounds( x, y, width, height, factorW, factorH, components );
        
        divider = getAccess().validateDivider( divider, this );
        int dividerSize = getAccess().getOwner().getDividerSize();
        
        if( orientation == Orientation.HORIZONTAL ){
            // Components are left and right
            double dividerWidth = factorW > 0 ? Math.max( 0, dividerSize / factorW) : 0.0;
            double dividerLocation = width * divider;
            
            if( left != null )
                left.updateBounds( x, y, dividerLocation - dividerWidth/2, height, factorW, factorH, components );
            
            if( right != null )
                right.updateBounds( x + dividerLocation + dividerWidth/2, y, 
                    width - dividerLocation - dividerWidth/2, height, factorW, factorH, components );
            
            dividerBounds.setBounds(
                    (int)(( x+dividerLocation-dividerWidth/2 )*factorW + 0.5 ),
                    (int)( y*factorH + 0.5 ),
                    dividerSize,
                    (int)( height*factorH + 0.5 ));
        }
        else{
            double dividerHeight = factorH > 0 ? Math.max( 0, dividerSize / factorH ) : 0.0;
            double dividerLocation = height * divider;
            
            if( left != null)
                left.updateBounds( x, y, width, dividerLocation - dividerHeight / 2, factorW, factorH, components );
            
            if( right != null)
                right.updateBounds( x, y + dividerLocation + dividerHeight / 2,
                    width, height - dividerLocation - dividerHeight/2, factorW, factorH, components );
            
            dividerBounds.setBounds(
                    (int)(x*factorW + 0.5),
                    (int)((y+dividerLocation-dividerHeight/2)*factorH + 0.5),
                    (int)(width*factorW + 0.5),
                    dividerSize );
        }
    }
    
    /**
     * Calculates the location and the size of the area which represents the divider.
     * The user can grab this area with the mouse and drag it around.
     * @param divider The location of the divider, should be between 0 and 1.
     * @param bounds A rectangle in which the result will be stored. It can be <code>null</code>
     * @return Either <code>bounds</code> or a new {@link Rectangle} if <code>bounds</code>
     * was <code>null</code>
     */
    public Rectangle getDividerBounds( double divider, Rectangle bounds ){
        if( bounds == null )
            bounds = new Rectangle();
        
        Root root = getRoot();
        double factorW = root.getWidthFactor();
        double factorH = root.getHeightFactor();
        int dividerSize = getAccess().getOwner().getDividerSize();
        
        if( orientation == Orientation.HORIZONTAL ){
            // Components are left and right
            double dividerWidth = dividerSize / factorW;
            double dividerLocation = width * divider;
            
            bounds.setBounds(
                    (int)(( x+dividerLocation-dividerWidth/2 )*factorW + 0.5 ),
                    (int)( y*factorH + 0.5 ),
                    dividerSize,
                    (int)( height*factorH + 0.5 ));
        }
        else{
            double dividerHeight = dividerSize / factorH;
            double dividerLocation = height * divider;
            
            bounds.setBounds(
                    (int)(x*factorW + 0.5),
                    (int)((y+dividerLocation-dividerHeight/2)*factorH + 0.5),
                    (int)(width*factorW + 0.5),
                    dividerSize );
        }
        
        return bounds;
    }
    
    /**
     * Calculates the value which the divider must have on condition that
     * the point <code>x/y</code> lies inside the {@link #getDividerBounds(double, Rectangle) divider bounds}.
     * @param x x-coordinate of the point in pixel
     * @param y y-coordinate of the point in pixel
     * @return The value that the divider should have. This value might not
     * be valid if the coordinates of the point are too extreme.
     */
    public double getDividerAt( int x, int y ){
        Root root = getRoot();
        if( orientation == Orientation.HORIZONTAL ){
            double mx = x / root.getWidthFactor();
            return (mx - this.x) / width;
        }
        else{
            double my = y / root.getHeightFactor();
            return (my - this.y) / height;
        }
    }
    
    @Override
    public PutInfo getPut( int x, int y, double factorW, double factorH, Dockable drop ){
        if( orientation == Orientation.HORIZONTAL ){
            if( x < (this.x + divider*width)*factorW ){
                // left
                return left == null ? null : left.getPut( x, y, factorW, factorH, drop );
            }
            else{
                // right
                return right == null ? null : right.getPut( x, y, factorW, factorH, drop );
            }
        }
        else{
            if( y < (this.y + divider*height)*factorH ){
                // top
                return left == null ? null : left.getPut( x, y, factorW, factorH, drop );
            }
            else{
                // bottom
                return right == null ? null : right.getPut( x, y, factorW, factorH, drop );
            }
        }
    }
    
    @Override
    public boolean isInOverrideZone( int x, int y, double factorW, double factorH ) {
        if( orientation == Orientation.HORIZONTAL ){
            if( x < (this.x + divider*width)*factorW ){
                // left
                return left.isInOverrideZone( x, y, factorW, factorH );
            }
            else{
                // right
                return right.isInOverrideZone( x, y, factorW, factorH );
            }
        }
        else{
            if( y < (this.y + divider*height)*factorH ){
                // top
                return left.isInOverrideZone( x, y, factorW, factorH );
            }
            else{
                // bottom
                return right.isInOverrideZone( x, y, factorW, factorH );
            }
        }
    }
    
    @Override
    public void evolve( Key key, boolean checkValidity ){
    	SplitDockTree tree = key.getTree();
    	
    	if( tree.isHorizontal( key )){
    		orientation = SplitDockStation.Orientation.HORIZONTAL;
    		setLeft( create( tree.getLeft( key ), checkValidity ));
    		setRight( create( tree.getRight( key ), checkValidity ));
    		setDivider( tree.getDivider( key ));
    	}
    	else{
    		orientation = SplitDockStation.Orientation.VERTICAL;
    		setLeft( create( tree.getTop( key ), checkValidity ));
    		setRight( create( tree.getBottom( key ), checkValidity ));
    		setDivider( tree.getDivider( key ));
    	}
    }
    
    @Override
    public boolean insert( SplitDockPathProperty property, int depth, Dockable dockable ) {
        if( depth >= property.size() ){
            // there is no description where to put the element
            // try using the theoretical boundaries of the element
            return getAccess().drop( dockable, property.toLocation(), this );
        }
        else{
            SplitDockPathProperty.Node node = property.getNode( depth );

            boolean expand = 
            // if this node is horizontal, but the path is vertical
                ( orientation == SplitDockStation.Orientation.HORIZONTAL &&
                    (node.getLocation() == SplitDockPathProperty.Location.TOP ||
                     node.getLocation() == SplitDockPathProperty.Location.BOTTOM )) ||
            // ... or if this node is vertical, but the path is horizontal
                ( orientation == SplitDockStation.Orientation.VERTICAL &&
                    (node.getLocation() == SplitDockPathProperty.Location.LEFT ||
                     node.getLocation() == SplitDockPathProperty.Location.RIGHT ));
            
            if( expand ){
                // split up this node
                Leaf leaf = create( dockable, true );
                if( leaf == null )
                    return false;
            
                SplitDockStation.Orientation orientation;
                if( node.getLocation() == SplitDockPathProperty.Location.TOP ||
                        node.getLocation() == SplitDockPathProperty.Location.BOTTOM )
                    orientation = SplitDockStation.Orientation.VERTICAL;
                else
                    orientation = SplitDockStation.Orientation.HORIZONTAL;
                
                Node split;
                SplitNode parent = getParent();
                int location = parent.getChildLocation( this );
                if( node.getLocation() == SplitDockPathProperty.Location.LEFT ||
                        node.getLocation() == SplitDockPathProperty.Location.TOP ){
                    split = new Node( getAccess(), leaf, this, orientation );
                    split.setDivider( node.getSize() );
                }
                else{
                    split = new Node( getAccess(), this, leaf, orientation );
                    split.setDivider( 1-node.getSize() );
                }
                parent.setChild( split, location );
                return true;
            }
            else{
                // forward the call to a child
                if( node.getLocation() == SplitDockPathProperty.Location.LEFT ||
                        node.getLocation() == SplitDockPathProperty.Location.TOP ){
                    return left.insert( property, depth+1, dockable );
                }
                else{
                    return right.insert( property, depth+1, dockable );
                }
            }
        }
    }
    
    @Override
    public <N> N submit( SplitTreeFactory<N> factory ) {
        if( orientation == SplitDockStation.Orientation.HORIZONTAL )
            return factory.horizontal( left.submit( factory ), right.submit( factory ), divider );
        else
            return factory.vertical( left.submit( factory ), right.submit( factory ), divider );
    }
    
    @Override
    public Leaf getLeaf( Dockable dockable ) {
        if( left != null ){
            Leaf leaf = left.getLeaf( dockable );
            if( leaf != null )
                return leaf;
        }
        
        if( right != null )
            return right.getLeaf( dockable );
        else
            return null;
    }
    
    @Override
    public Node getDividerNode( int x, int y ){
        if( dividerBounds.contains( x, y ))
            return this;
        
        if( left != null ){
            Node node = left.getDividerNode( x, y );
            if( node != null )
                return node;
        }
        
        if( right != null ){
            return right.getDividerNode( x, y );
        }
        else{
            return null;
        }
    }
    
    @Override
    public void visit( SplitNodeVisitor visitor ) {
        visitor.handleNode( this );
        left.visit( visitor );
        right.visit( visitor );
    }
    
    @Override
    public void toString( int tabs, StringBuilder out ) {
        out.append( "Node[ ");
        out.append( orientation );
        out.append( " ]" );
        
        out.append( '\n' );
        for( int i = 0; i < tabs+1; i++ )
            out.append( '\t' );
        if( left == null )
            out.append( "<null>" );
        else
            left.toString( tabs+1, out );
        
        out.append( '\n' );
        for( int i = 0; i < tabs+1; i++ )
            out.append( '\t' );
        if( right == null )
            out.append( "<null>" );
        else
            right.toString( tabs+1, out );
    }
}