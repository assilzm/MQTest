package component.impl.tree

import base.model.BasicPage


/**
 * 树节点
 */
class TreeNode extends BasicPage{

    //TODO:其余属性待设计

    //节点中依然可以包含节点
    List<TreeNode> subNodes=new ArrayList<>()
}
