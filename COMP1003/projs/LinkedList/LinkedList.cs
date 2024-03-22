using System.Runtime.CompilerServices;

namespace List
{
    internal class LinkedList
    {
        private Node headNode;
        private Node tailNode;

        public LinkedList(int value)
        {
            headNode = new Node(value);
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

        public string stringify()
        {
            string s = "[";
            Node currentNode = headNode;
            while (currentNode.getNextNode() != null)
            {
                s += currentNode.getNodeValue() + ", ";
                currentNode = currentNode.getNextNode();
            }
            s += currentNode.getNodeValue() + "]";
            return s;
        }

        public int getLength()
        {
            int length = 1;
            Node currentNode = headNode;
            while (currentNode.getNextNode() != null)
            {
                length++;
                currentNode = currentNode.getNextNode();
            }
            return length;
        }

        public int? getIndex(int index) 
        {
            if (index < 0 || index >= this.getLength())
            {
                return null;
            }

            Node currentNode = headNode;
            for (int i = 0; i < index; i++) 
            {
                currentNode = currentNode.getNextNode();
            }

            return currentNode.getNodeValue();
        }

        public int removeByValue(int valueToRemove)
        {
            if (headNode.getNodeValue() == valueToRemove)
            {
                headNode = headNode.getNextNode();
                return 0; // success
            }

            Node currentNode = headNode;

            while (currentNode.getNextNode() != null)
            {
                Node nextNode = currentNode.getNextNode();

                if (nextNode.getNodeValue() == valueToRemove)
                {
                    currentNode.setNextNode(nextNode.getNextNode());
                    return 0;
                }

                currentNode = currentNode.getNextNode();
            }

            return -1;
        }

    }
}
