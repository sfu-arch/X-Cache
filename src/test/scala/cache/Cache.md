# Cache test

Test case example:


``` c
unsigned cacheDF(unsigned *a, unsigned i, unsigned flag){
    unsigned val = 0;
    if(flag){
        val = a[i];
    }
    else{
        a[i] = i;
    }
    return val;
}
```

Our initial memory state:
```
0X0 -> 10
0X4 -> 20
0X8 -> 30
0Xc -> 40
0X10 -> 50
```


How to try different inputs:

```scala
  poke(c.io.in.valid, true.B)
  poke(c.io.in.bits.enable.control, true.B)
  poke(c.io.in.bits.data("field0").data, 0.U)   // Address of %a
  poke(c.io.in.bits.data("field0").predicate, true.B)
  poke(c.io.in.bits.data("field1").data, 2.U)  // index of a, %i
  poke(c.io.in.bits.data("field1").predicate, true.B)
  poke(c.io.in.bits.data("field2").data, 1.U) // Flag: Read(1), Write(0)
  poke(c.io.in.bits.data("field2").predicate, true.B)
  poke(c.io.out.ready, true.B)
  step(1)
```


Test files in `src/test/scala/cache`:

* **cacheDFTest.scala:** Basic cache implementation
```scala
val cache = Module(new Cache)
``` 

* **SuperCacheDFTest.scala**
```scala
  val cache = Module(new NCache(NumTiles = 2, NumBanks = 2)) // Simple Nasti Cache
```

* **SuperParallelCacheDFTest.scala**
```scala
  val cache = Module(new NParallelCache(NumTiles = 2, NumBanks = 2)) // Simple Nasti Cache
```