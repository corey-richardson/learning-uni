﻿namespace List
{
    class Program
    {
        static void Main(string[] args)
        {
            LinkedList ll = new LinkedList(5);

            ll.append(4);
            ll.append(3);
            ll.append(2);
            ll.append(1);
            ll.append(0);

            ll.prepend(4);
            ll.prepend(3);
            ll.prepend(2);
            ll.prepend(1);
            ll.prepend(0);

            ll.display();
            Console.WriteLine(ll.stringify());
            Console.WriteLine(ll.getLength());

            Console.WriteLine(ll.getIndex(10));
            Console.WriteLine(ll.getIndex(11));

            ll.removeByValue(5);
            ll.removeByValue(0);
            ll.removeByValue(0);
            ll.display();
        }
    }
}
