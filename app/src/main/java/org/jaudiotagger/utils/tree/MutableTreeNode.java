

package org.jaudiotagger.utils.tree;



public interface MutableTreeNode extends TreeNode
{

    void insert(MutableTreeNode child, int index);


    void remove(int index);


    void remove(MutableTreeNode node);


    void setUserObject(Object object);


    void removeFromParent();


    void setParent(MutableTreeNode newParent);
}
