package component.impl.tree

import component.Component
import component.interfaces.Tree

/**
 * TODO:
 * ������ĳ�����
 */
abstract class AbstractTree extends Component implements Tree{

    List<TreeNode> rootNodes=new ArrayList<>()
}
