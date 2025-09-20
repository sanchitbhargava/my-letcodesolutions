import java.util.Objects;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

class Router {

    /**
     * Private inner class to represent a packet.
     * Overriding hashCode() and equals() is crucial for the HashSet to detect duplicates correctly.
     */
    private static class Packet {
        int source;
        int destination;
        int timestamp;

        Packet(int source, int destination, int timestamp) {
            this.source = source;
            this.destination = destination;
            this.timestamp = timestamp;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Packet packet = (Packet) o;
            return source == packet.source &&
                   destination == packet.destination &&
                   timestamp == packet.timestamp;
        }

        @Override
        public int hashCode() {
            // Objects.hash provides a convenient way to generate a hash code
            // from multiple fields.
            return Objects.hash(source, destination, timestamp);
        }
    }

    private final int memoryLimit;
    private final Queue<Packet> packetQueue; // For FIFO order
    private final Set<Packet> packetSet;     // For O(1) duplicate detection
    private final Map<Integer, List<Packet>> destinationMap; // For efficient counting

    /**
     * Initializes the Router with a specific memory capacity.
     */
    public Router(int memoryLimit) {
        this.memoryLimit = memoryLimit;
        this.packetQueue = new LinkedList<>();
        this.packetSet = new HashSet<>();
        this.destinationMap = new HashMap<>();
    }
    
    /**
     * Adds a packet to the router.
     * Returns false if the packet is a duplicate.
     * If the router is full, the oldest packet is removed.
     */
    public boolean addPacket(int source, int destination, int timestamp) {
        Packet newPacket = new Packet(source, destination, timestamp);
        
        // Use the HashSet for an efficient duplicate check.
        if (packetSet.contains(newPacket)) {
            return false;
        }

        // If memory limit is reached, evict the oldest packet.
        if (packetQueue.size() == memoryLimit) {
            Packet oldestPacket = packetQueue.poll(); // Dequeue
            
            // Remove the evicted packet from all helper data structures.
            packetSet.remove(oldestPacket);
            destinationMap.get(oldestPacket.destination).remove(0);
        }

        // Add the new packet to all data structures.
        packetQueue.offer(newPacket); // Enqueue
        packetSet.add(newPacket);
        destinationMap.computeIfAbsent(destination, k -> new ArrayList<>()).add(newPacket);
        
        return true;
    }
    
    /**
     * Removes and returns the next packet in FIFO order.
     * Returns an empty array if the router is empty.
     */
    public int[] forwardPacket() {
        if (packetQueue.isEmpty()) {
            return new int[0];
        }

        // Get the oldest packet from the front of the queue.
        Packet packetToForward = packetQueue.poll();
        
        // Remove it from the helper data structures as well.
        packetSet.remove(packetToForward);
        destinationMap.get(packetToForward.destination).remove(0);

        return new int[]{packetToForward.source, packetToForward.destination, packetToForward.timestamp};
    }
    
    /**
     * Counts packets for a specific destination within an inclusive timestamp range.
     */
    public int getCount(int destination, int startTime, int endTime) {
        if (!destinationMap.containsKey(destination)) {
            return 0;
        }

        List<Packet> packets = destinationMap.get(destination);
        
        // Use binary search to efficiently find the packets in the time range.
        int startIndex = findFirstGreaterOrEqual(packets, startTime);
        int endIndex = findFirstGreater(packets, endTime);
        
        return endIndex - startIndex;
    }

    /**
     * Helper method to find the index of the first packet whose timestamp is >= time.
     * This is equivalent to a "lower_bound" search.
     */
    private int findFirstGreaterOrEqual(List<Packet> packets, int time) {
        int low = 0, high = packets.size() - 1;
        int ans = packets.size();
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (packets.get(mid).timestamp >= time) {
                ans = mid;
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return ans;
    }
    
    /**
     * Helper method to find the index of the first packet whose timestamp is > time.
     * This is equivalent to an "upper_bound" search.
     */
    private int findFirstGreater(List<Packet> packets, int time) {
        int low = 0, high = packets.size() - 1;
        int ans = packets.size();
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (packets.get(mid).timestamp > time) {
                ans = mid;
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return ans;
    }
}