## Assignment Overview:
This assignment focuses on building a key-value store that operates over both TCP and UDP protocols in Java. It requires implementing a client-server communication model where clients can perform basic operations like PUT, GET, and DELETE on the server's key-value store. 
The assignment emphasizes understanding the differences between TCP and UDP, handling client requests efficiently, and implementing robust error-handling and logging mechanisms. 
Additionally, this project involves carring out my Java's socket programming skills to write the server-side, client-side and util classes to manage server-client communication. 
Last but not least, I also challenged myself to learn Docker, which required additional learning about containers and how to set up a networked environment for server-client communication.

## Technical Impression:
I found this assignment challenging but really rewarding. It provided an excellent opportunity to strengthen my understanding of Java socket programming and the fundamental differences between TCP and UDP protocols. 
Implementing both protocols required an in-depth knowledge of Java’s I/O libraries, such as Socket, BufferedReader, PrintWriter etc. 
The importance for a modular and scalable design became clear as I approach the assignment. 
And I get to refactor a couple times to structure the code with a high-level KeyValueStore class and then separate client and server utilities to reduce redundancy and improve maintainability. 
Another challenge I encountered was pre-populating the key-value store with data and automating client inputs for testing purposes, which required very careful handling of request formats. 
Finally, The use of Docker also added a layer of complexity. But with the docker tutorial and starter scripts provided, I was finally able to adopt the Docker containers into my final deliverables.
Despite the challenge, containerizing the system was rewarding, as it provided a way more controlled environment for running the application. 
Overall, I liked this assignment because it reinforced concepts of object-oriented design, abstraction, and modular programming, while also enhancing my knowledge of networking, socket, and distributed systems in practice.


## Use case:
A real-world use case of this key-value store can be seen in the architecture of many distributed data services, such as Amazon DynamoDB, Redis, and Memcached. 
These systems use key-value stores to manage data efficiently, enabling operations such as fast retrieval and storage of data across multiple clients. 
I am very excited because the foundation built in this assignment can be applied to these systems, with additional consideration of reliability, scalability, speed, cost etc. 
In real world, improvements to this design will include adding multithreading to handle multiple client requests simultaneously and implementing caching mechanisms to improve performance for frequent data lookups. 
Additionally, introducing more advanced error handling and a mechanism for remote server shutdown would make the system more robust and suitable for real-world deployment.