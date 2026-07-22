from typing import List

class Solution:
    def maxActiveSectionsAfterTrade(self, s: str, queries: List[List[int]]) -> List[int]:
        total_ones = s.count('1')
        n = len(s)
        
        power2 = 1
        while power2 < n:
            power2 *= 2
            
        tree = [None] * (2 * power2)
        
        for i in range(power2):
            if i < n:
                if s[i] == '0':
                    tree[power2 + i] = (True, True, True, 0, 1, (1,))
                else:
                    tree[power2 + i] = (False, False, False, 0, 0, ())
            else:
                tree[power2 + i] = (False, False, False, 0, 0, ())
                
        def merge(left: tuple, right: tuple) -> tuple:
            sz_l, ez_l, iz_l, ms_l, k_l, bl_l = left
            sz_r, ez_r, iz_r, ms_r, k_r, bl_r = right
            
            iz_c = iz_l and iz_r
            ms_c = ms_l if ms_l > ms_r else ms_r
            
            do_merge = ez_l and sz_r
            
            if do_merge:
                blocks = bl_l[:-1] + (bl_l[-1] + bl_r[0],) + bl_r[1:]
                k_c = k_l + k_r - 1
            else:
                blocks = bl_l + bl_r
                k_c = k_l + k_r
                
            for i in range(len(blocks) - 1):
                b1 = blocks[i]
                b2 = blocks[i+1]
                if b1 != -1 and b2 != -1:
                    s_val = b1 + b2
                    if s_val > ms_c:
                        ms_c = s_val
                        
            if k_c <= 4:
                n_blocks = tuple(x for x in blocks if x != -1)
            else:
                ints = [x for x in blocks if x != -1]
                n_blocks = (ints[0], ints[1], -1, ints[-2], ints[-1])
                
            return (sz_l, ez_r, iz_c, ms_c, k_c, n_blocks)
            
        for i in range(power2 - 1, 0, -1):
            tree[i] = merge(tree[i << 1], tree[i << 1 | 1])
            
        ans = []
        for l, r in queries:
            ql = l + power2
            qr = r + power2
            
            left_nodes = []
            right_nodes = []
            
            while ql <= qr:
                if ql & 1:
                    left_nodes.append(tree[ql])
                    ql += 1
                if not (qr & 1):
                    right_nodes.append(tree[qr])
                    qr -= 1
                ql >>= 1
                qr >>= 1
                
            res = None
            for node in left_nodes:
                if res is None:
                    res = node
                else:
                    res = merge(res, node)
                    
            for i in range(len(right_nodes) - 1, -1, -1):
                if res is None:
                    res = right_nodes[i]
                else:
                    res = merge(res, right_nodes[i])
                    
            ans.append(total_ones + res[3])
            
        return ans