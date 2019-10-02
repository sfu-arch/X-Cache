# Nested Parallelism

Say you have an imperfect nested loop -- can you pipeline dependences from the outer-loop to the inner loop (so that they can run concurrently?).

I tried to find an example and explain how imperfect nested loops work in our case. I found this paper discussing pipelining imperfect loops on CGRAs. Their example looks handy to me to explain how would our execution model work:

https://ieeexplore.ieee.org/document/7412757/


Suppose we have the same four loops, as they’ve mentioned second loop and fourth loop are loops with the trip counts equal to one:

![Nested loop](https://www.dropbox.com/s/hej8wxjz5e0gh7j/nested-loop.png?raw=1)

So first I explain how IL_0 works and then go for how the interaction between loops work:

The way that my base implementation works right now is that I wait until c fires and then fire a new loop iteration because I make a dependency from instruction c to my loop header. But I would run alias analysis and say I actually can run a new iteration after running b because c would never run until b fires, statically I know the ordering between b and c, so instead of making loop header dependent on c I make it dependent on b. The advantage is that know I can overlap a with c  **if my loop initiation interval is equal or less than 2 cycles, I previously explained to you for example how fusion affect our initiation interval.** Base on these changes from the left schedule you would get the right schedule:

![Loop Schedule](https://www.dropbox.com/s/d415rhnshwcws1w/loop-schedule.png?raw=1)

We had that discussion do we pipeline serial loops? I told you no, but actually, the correct answer is that it depends on the dependencies. I have four types of dependencies for a serial loop:
Trigger control (Or we can say loop initiation interval)

  1. Carry loop dependency
  2. Memory dependency
  3. Live-out values

It’s possible to overlap two iterations of a loop, as soon as these four dependencies are satisfied. It really depends on how we connect these dependencies and infer them implicitly from other dependencies.
Now let’s look at the overall structure of nested for loop:

![Loop Structure](https://www.dropbox.com/s/k5effhh8srw1m8o/loop-structure.png?raw=1)


Loop1 is dependent on loop 0 base on Var. Loop2 is dependent on Loop1 base on D. And Loop3 is dependent on loop2 base on E. In this example, out of four dependencies that I mentioned, first one always exists. But the other three ones in this particular example don’t exist. Hence, it’s possible to overlap the execution of the different iterations of the outer loop. Again because there are no other dependencies in this example. Therefore, we can get the ideal schedule with the assumption of our loop initiation interval is equal to 2 cycles.

![Loop Overall](https://www.dropbox.com/s/wzp11dk18q7aoak/loop-overall.png?raw=1)

The point that I want to make is that it’s possible to reach the ideal schedule, but it depends on how conservative are we in making dependencies.

