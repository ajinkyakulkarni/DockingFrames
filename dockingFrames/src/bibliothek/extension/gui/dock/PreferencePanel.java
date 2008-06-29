package bibliothek.extension.gui.dock;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import bibliothek.extension.gui.dock.preference.PreferenceModel;
import bibliothek.extension.gui.dock.preference.PreferenceTreeModel;

/**
 * A panel that shows a {@link JTree} and a {@link PreferenceTable}. The tree
 * is filled by a {@link PreferenceTreeModel}, and the selected node of the
 * tree is shown in the table.
 * @author Benjamin Sigg
 */
public class PreferencePanel extends JPanel{
    private PreferenceTreeModel model;
    
    private JTree tree;
    private PreferenceTable table;
    
    /**
     * Creates a new panel.
     */
    public PreferencePanel(){
        this( null );
    }
    
    /**
     * Creates a new panel.
     * @param model the contents of this panel, might be <code>null</code>
     */
    public PreferencePanel( PreferenceTreeModel model ){
        if( model == null )
            model = new PreferenceTreeModel();
        
        this.model = model;
        setLayout( new GridLayout( 1, 1 ) );
        JSplitPane pane = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT );
        
        tree = new JTree( model );
        tree.setEditable( false );
        tree.setRootVisible( false );
        tree.setShowsRootHandles( true );
        tree.getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION );
        pane.setLeftComponent( new JScrollPane( tree ) );
        
        table = new PreferenceTable();
        pane.setRightComponent( new JScrollPane( table ) );
        
        tree.addTreeSelectionListener( new TreeSelectionListener(){
            public void valueChanged( TreeSelectionEvent e ) {
                checkSelection();
            }
        });
        
        add( pane );
        checkSelection();
    }
    
    private void checkSelection(){
        PreferenceModel preference = null;
        TreePath path = tree.getSelectionPath();
        if( path != null ){
            PreferenceTreeModel.Node node = (PreferenceTreeModel.Node)path.getLastPathComponent();
            preference = node.getModel();
        }
        table.setModel( preference );
    }
    
    /**
     * Sets the model of this panel.
     * @param model the new model
     */
    public void setModel( PreferenceTreeModel model ) {
        if( model == null )
            throw new IllegalArgumentException( "model must not be null" );
        this.model = model;
    }
    
    /**
     * Gets the model which is shown on this panel.
     * @return the model that is shown
     */
    public PreferenceTreeModel getModel() {
        return model;
    }
}
