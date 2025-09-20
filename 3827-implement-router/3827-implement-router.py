from collections import deque, defaultdict
from bisect import bisect_left, bisect_right
from typing import List

class Router:
    """
    A class to simulate a network router with a fixed memory limit.
    """

    def __init__(self, memoryLimit: int):
        """
        Initializes the Router with a memory limit and data structures.
        """
        self.memory_limit = memoryLimit
        # Main queue for FIFO logic (forwarding, eviction)
        self.packet_queue = deque()
        # Set for O(1) duplicate detection
        self.packet_set = set()
        # Map for efficient counting: destination -> deque of timestamps
        self.destination_map = defaultdict(deque)

    def addPacket(self, source: int, destination: int, timestamp: int) -> bool:
        """
        Adds a packet to the router. Handles duplicates and memory eviction.
        Returns:
            bool: True if the packet was added, False if it was a duplicate.
        """
        packet = (source, destination, timestamp)
        
        # 1. Check for duplicates using the set for O(1) lookup.
        if packet in self.packet_set:
            return False

        # 2. If the router is full, remove the oldest packet to make space.
        if len(self.packet_queue) == self.memory_limit:
            oldest_packet = self.packet_queue.popleft()
            self.packet_set.remove(oldest_packet)
            
            # Also remove the timestamp from the corresponding destination's deque.
            old_destination = oldest_packet[1]
            self.destination_map[old_destination].popleft()

        # 3. Add the new packet to all data structures.
        self.packet_queue.append(packet)
        self.packet_set.add(packet)
        self.destination_map[destination].append(timestamp)
        
        return True

    def forwardPacket(self) -> List[int]:
        """
        Forwards the next packet in FIFO order.
        Returns:
            List[int]: The forwarded packet [source, dest, time], or [] if empty.
        """
        # 1. Check if the router has any packets.
        if not self.packet_queue:
            return []

        # 2. Get the oldest packet from the front of the queue.
        packet_to_forward = self.packet_queue.popleft()
        
        # 3. Remove it from the helper data structures.
        self.packet_set.remove(packet_to_forward)
        destination = packet_to_forward[1]
        self.destination_map[destination].popleft()
        
        return list(packet_to_forward)

    def getCount(self, destination: int, startTime: int, endTime: int) -> int:
        """
        Counts packets for a destination within an inclusive time range.
        Returns:
            int: The number of matching packets.
        """
        # 1. Check if we have any packets for this destination.
        if destination not in self.destination_map:
            return 0
        
        timestamps = self.destination_map[destination]
        
        # 2. Use binary search to find the number of timestamps in the range.
        # bisect_left finds the first element >= startTime.
        start_index = bisect_left(timestamps, startTime)
        
        # bisect_right finds the first element > endTime.
        end_index = bisect_right(timestamps, endTime)
        
        # The difference is the count of items in [startTime, endTime].
        return end_index - start_index