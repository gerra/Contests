#include <iostream>
#include <fstream>
#include <algorithm>
#include <vector>
#include <map>
#include <set>
#include <string>
#include <cstring>

using namespace std;

const int N = 100005;

int n;
int a[N], d[N];
vector<int> g[N];

void dfs(int v) {


    for (int i = 0; i < g[v].size(); i++) {
        int to = g[v][i];
        if (a[to] == 0) {
        a[to] = 3-a[v];
        dfs(to);
        } else if (a[to] == a[v]) {
            cout << 0;
            exit(0);
        }
    }
}

int main() {
   // ifstream cin("./in.txt");
    cin >> n;
    int c1 = 0, c2 = 0;
    for (int i = 0; i < n-1; i++) {
        int f, t;
        cin >> f >> t;
        g[f].push_back(t);
        g[t].push_back(f);

        d[t]++;
        d[f]++;
    }
a[1] = 1;
dfs(1);
    for (int i = 1; i <= n; i++) {
            cout << a[i] << "\n";
        if (a[i] == 1) c1++;
        if (a[i] == 2) c2++;
    }
    long long res1 = 0;
    long long res2 = 0;
    for (int i = 1; i <= n; i++) {
        if (a[i] == 1) {
            res1 += (c2 - d[i]);
        } else {
            res2 += (c1 - d[i]);
        }
    }
    cout << max(res1, res2) << "\n";

    return 0;
}
