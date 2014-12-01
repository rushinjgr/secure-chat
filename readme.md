Deliverable 4


I will be testing my CS 1501 SecureChat Project.


SecureChat is a client/server application. Portions of both the client and server will be tested. The test suites will cover performance and security testing.


Modifications:

-Add a count of messages received (client).

-Add a count of messages sent (client).

-Add getters for above counts.

-Add a send message method.



Test Plan   
|   
|-Performance Test Suite   
|	|   
|	|-128 Bit Encryption    
|	|-Substitute Encryption   
|	|-SecureChatServer Memory Use   
|	|-SecureChatClient Memory Use   
|	|-SecureChatClient Response Time   
|	   
|-Security Testing  
|	|-Attempt to bruteforce keys of varying bit lengths.   
|	|	|- Perform tests multiple times, get average length to bruteforce.   
|	|-Attempt to pull unecrypted messages from the wire (application has ability to send unencrypted messages)   
