namespace List
{
    internal class Node
    {
        private int? value;
        private Node? nextNode;

        public Node(int? val = null, Node? next = null)
        {
            value = val;
            nextNode = next;
        }

        public int? getNodeValue()
        {
            return value;
        }

        public Node? getNextNode()
        {
            return nextNode;
        }

        public void setNextNode(Node newNextNode)
        {
            nextNode = newNextNode;
        }
    }
}
