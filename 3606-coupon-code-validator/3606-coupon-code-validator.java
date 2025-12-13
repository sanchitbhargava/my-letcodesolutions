import java.util.*;

class Solution {
    public List<String> validateCoupons(String[] code, String[] businessLine, boolean[] isActive) {
        // Step 1: Prepare data structures
        List<String> result = new ArrayList<>();
        Map<String, List<String>> validCouponsByCategory = new HashMap<>();
        List<String> businessOrder = Arrays.asList("electronics", "grocery", "pharmacy", "restaurant");
        Set<String> validCategories = new HashSet<>(businessOrder);

        // Step 2: Iterate through all coupons
        for (int i = 0; i < code.length; i++) {
            String currentCode = code[i];
            String currentCategory = businessLine[i];
            boolean active = isActive[i];

            // Check if code is non-empty and valid format
            if (currentCode != null && !currentCode.isEmpty() && currentCode.matches("[a-zA-Z0-9_]+")) {
                // Check businessLine is valid and isActive is true
                if (validCategories.contains(currentCategory) && active) {
                    validCouponsByCategory
                        .computeIfAbsent(currentCategory, k -> new ArrayList<>())
                        .add(currentCode);
                }
            }
        }

        // Step 3: Sort and merge results by businessLine order and code
        for (String category : businessOrder) {
            List<String> codes = validCouponsByCategory.getOrDefault(category, new ArrayList<>());
            Collections.sort(codes); // Sort codes lexicographically
            result.addAll(codes);
        }

        return result;
    }
}
