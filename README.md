# TurboTape
Java Library for very fast, portable and reasonably compact Object serialization
and deserialization. The main goal is resource optimization, trying to minimize
the CPU time and Memory usage during serialization, while still resulting in a
decently compact serialization. 

The intended usage is situations where you need very fast serialization of large
data amounts, while minimizing resource consumption (network, cpu and memory).
It is not the most convenient serializer out there, but for large but simple
datastructures it should outperform the alternatives.



