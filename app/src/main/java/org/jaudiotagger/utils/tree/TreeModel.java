
package org.jaudiotagger.utils.tree;




public interface TreeModel
{


    public Object getRoot();



    public Object getChild(Object parent, int index);



    public int getChildCount(Object parent);



    public boolean isLeaf(Object node);


    public void valueForPathChanged(TreePath path, Object newValue);


    public int getIndexOfChild(Object parent, Object child);

//
//  Change Events
//


    void addTreeModelListener(TreeModelListener l);


    void removeTreeModelListener(TreeModelListener l);

}
