namespace BST
{
    internal class BinarySearchTree
    {
        public Node headNode;

        public BinarySearchTree(int value)
        {
            headNode = new Node(value);
        }

        public Node append(Node cursorNode, int val)
        {
            if (cursorNode == null)
            {
                cursorNode = new Node(val);
            }
            else if (val < cursorNode.getNodeValue())
            {
                cursorNode.setLeftNode(this.append(cursorNode.getLeftNode(), val));
            }
            else
            {
                cursorNode.setRightNode(this.append(cursorNode.getRightNode(), val));
            }

            return cursorNode;
        }

        public void traverse(Node cursorNode)
        {

            if (cursorNode == null) 
            {
                Console.WriteLine("-");
                return;
            }

            Console.WriteLine(cursorNode.getNodeValue());

            traverse(cursorNode.getLeftNode());
            traverse(cursorNode.getRightNode());    
        }
    }
}
