# Desc

Distributed file encryption system, having the following elements

* A WebSocket in the browser for telling the user when the process is done
* A Spring Boot application which takes a file and some parameters
* A Kafka Broker for distribution of the messages
* Another Spring Boot application which handles the send messages
* A Open MP C application for parallel file encryption

The typical flow is \
User Sends Data to User Layer -> Gets Received by the Kafka Broker -> Sends it to the Service Layer -> \
The Service Layer invokes the C application -> The application executes the Enc/Dec operation -> \
The Service Layer gets the status of the process -> Sends another message to a different topic to Kafka -> \
The message is Received via a WebSocket which tells the browser the operation is done -> User can download the result

The achieves decoupling as elements can be changed and scalability as we can add more Kafka Brokers.
