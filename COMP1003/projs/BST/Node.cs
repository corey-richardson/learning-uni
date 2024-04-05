namespace BST
{
    internal class Node
    {
        private int? value;
        private Node? leftNode;
        private Node? rightNode;

        public Node(int? val = null, Node? left = null, Node? right = null)
        {
            value = val;
            leftNode = left;
            rightNode = right;
        }

        public int? getNodeValue()
        {
            return value;
        }

        public Node? getLeftNode()
        {
            return leftNode;
        }

        public Node? getRightNode()
        {
            return rightNode;
        }

        public void setLeftNode(Node newNode)
        {
            leftNode = newNode;
        }

        public void setRightNode(Node newNode)
        {
            rightNode = newNode;
        }
    }
}
