namespace BST
{
    class Program
    {
        static void Main(string[] args)
        {
            BinarySearchTree tree = new BinarySearchTree(5);

            tree.append(tree.headNode, 6);
            tree.append(tree.headNode, 4);
            tree.append(tree.headNode, 4);
            tree.append(tree.headNode, 3);

            tree.traverse(tree.headNode);
        }
    }
}
