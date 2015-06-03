package component.impl.tree

import component.Component
import component.interfaces.Tree

/**
 * TODO:
 * 树组件的抽象类
 */
abstract class AbstractTree extends Component implements Tree{

    List<TreeNode> rootNodes=new ArrayList<>()
}
