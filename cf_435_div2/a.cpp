#include <iostream>
#include <fstream>
#include <algorithm>
#include <vector>
#include <map>
#include <set>
#include <string>
#include <cstring>

using namespace std;

map<int, int> a;

int main() {
    //ifstream cin("in.txt");
    int n,x;
    cin >> n >> x;

    for (int i = 0; i < n; i++) {
        int b;
        cin >> b;
        a[b]++;
    }
    int res = 0;
    for (int i = 0; i < x; i++) {

        if (a[i] == 0) {
            res++;
        }
    }
    if (a[x] == 0) {
        cout << res << "\n";
    } else {
        cout << res + a[x] << "\n";
    }




    return 0;
}
