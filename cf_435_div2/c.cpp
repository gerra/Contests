#include <iostream>
#include <fstream>
#include <algorithm>
#include <vector>
#include <map>
#include <set>
#include <string>
#include <cstring>
#include <ctime>        // std::time
#include <cstdlib>      // std::rand, std::srand

using namespace std;

int a[1000005];
int tmp[100005];
set<int> st;

int main() {
    //ifstream cin("in.txt");
    int n, x;
    cin >> n >> x;

    int f0 = 1;
    int f1 = 0;
    std::srand ( unsigned ( std::time(0) ) );
    int bits = 19;

    int curBits = 0;
    for (int i = 0; i < n; i++) {
        a[i] |= (1 << curBits);
    }

    for (int i = 0; i < bits; i++) {
        int b = x&(1 << i);

        for (int j = 0; j < n; j++) {

        }
        if (b) {

        }

        random_shuffle ( tmp, tmp+n );
        for (int j = 0; j < n; j++) {
            a[j] |= tmp[j];
        }
    }
    int xo = 0;
    for (int i = 0; i < n; i++) {
        //cout << a[i] << "\n";
        if (a[i] > 1000000 || st.find(a[i]) != st.end()) {
            cout << "NO!";
            return 0;
        }
        xo ^= a[i];
        st.insert(a[i]);
    }
    if (xo != x) {
        cout << "NO";
        return 0;
    }
    cout << "YES\n";
    for (int i = 0; i < n; i++) {
        cout << a[i] << " ";
    }
    return 0;
}
