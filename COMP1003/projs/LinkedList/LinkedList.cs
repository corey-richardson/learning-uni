namespace List
{
    internal class LinkedList
    {
        private Node headNode;
        private Node tailNode;

        public LinkedList(int value)
        {
            headNode = new Node(value, null);
            tailNode = headNode;
        }

        public void append(int newValue)
        {
            tailNode.setNextNode(new Node(newValue, null));
            tailNode = tailNode.getNextNode();
        }

        public void prepend(int newValue)
        {
            Node holder = headNode;
            headNode = new Node(newValue, holder);
        }

        public void display()
        {
            Node currentNode = headNode;
            while (currentNode.getNextNode() != null)
            {
                Console.WriteLine(currentNode.getNodeValue());
                currentNode = currentNode.getNextNode();
            }

            Console.WriteLine(currentNode.getNodeValue());
        }

    }
}
