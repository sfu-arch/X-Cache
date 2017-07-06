int foo(){
    int a[3];
    a[0] = 1;
    a[1] = 2;
    a[3] = a[0] + a[1];
    return a[3];
}
