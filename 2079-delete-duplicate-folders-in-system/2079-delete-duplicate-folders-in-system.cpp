class Solution {
public:
    struct TrieNode {
        unordered_map<string, TrieNode*> children;
        string name;
        bool deleted = false;
    };

    TrieNode* root = new TrieNode();
    unordered_map<string, vector<TrieNode*>> seen;

    void insert(vector<string>& path) {
        TrieNode* curr = root;
        for (const string& folder : path) {
            if (!curr->children.count(folder)) {
                curr->children[folder] = new TrieNode();
                curr->children[folder]->name = folder;
            }
            curr = curr->children[folder];
        }
    }

    string serialize(TrieNode* node) {
        if (node->children.empty()) return "";

        vector<pair<string, string>> serials;
        for (auto& [name, child] : node->children) {
            serials.push_back({name, serialize(child)});
        }

        sort(serials.begin(), serials.end()); // ensure uniqueness across orders

        string serial;
        for (auto& [name, sub] : serials) {
            serial += "(" + name + sub + ")";
        }

        seen[serial].push_back(node); // track serialization
        return serial;
    }

    void markDuplicates() {
        for (auto& [key, nodes] : seen) {
            if (nodes.size() > 1) {
                for (auto* node : nodes) {
                    node->deleted = true;
                }
            }
        }
    }

    void collect(TrieNode* node, vector<string>& path, vector<vector<string>>& res) {
        for (auto& [name, child] : node->children) {
            if (!child->deleted) {
                path.push_back(name);
                res.push_back(path);
                collect(child, path, res);
                path.pop_back();
            }
        }
    }

    vector<vector<string>> deleteDuplicateFolder(vector<vector<string>>& paths) {
        for (auto& path : paths) {
            insert(path);
        }

        serialize(root);
        markDuplicates();

        vector<vector<string>> res;
        vector<string> path;
        collect(root, path, res);
        return res;
    }
};
