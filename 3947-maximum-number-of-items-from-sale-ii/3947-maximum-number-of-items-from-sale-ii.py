from typing import List


class Solution:
    def maximumSaleItems(self, items: List[List[int]], budget: int) -> int:
        n = len(items)
        frequency = [0] * (n + 1)
        cheapest = min(price for _, price in items)

        for factor, _ in items:
            frequency[factor] += 1

        free_limit = [0] * (n + 1)
        for factor in range(1, n + 1):
            if frequency[factor]:
                free_limit[factor] = (
                    sum(frequency[multiple] for multiple in range(factor, n + 1, factor))
                    - 1
                )

        total = 0
        for factor, price in sorted(items, key=lambda item: item[1]):
            if price >= 2 * cheapest or price > budget:
                break

            pairs = min(free_limit[factor], budget // price)
            total += 2 * pairs
            budget -= pairs * price

        return total + budget // cheapest