# Parallel loop implementation:

Parallel loops can be implemented in different ways. I start with enabling pipeline execution and then explain how we can increase the number of execution tiles.

The first step for implementing a pipeline loop for us is extracting body of a loop to a separate unit and wrap it with call interface. We break down a single call node into two separate nodes, Call and Sync. Having call and Sync nodes is the first step to support pipelining. When we decoupled Call from Sync, or return, node we make call node non-blocking. Therefore, as soon as the body is ready for accepting a new data we can fire a new iteration. Otherwise, for each invocation, we need to wait for the body to return and then call a new iteration.
In this case, Sync node acts as a counter, whenever we fire a new iteration, there is a control signal that increases a counter within Sync node. And on each return from loop’s body, we decrease the counter by one. We continue to decrease the counter until all the iterations of the loops are fired and all of them are returned, then Sync fire the rest of the graph.

![Parallel Example](https://www.dropbox.com/s/0kqhi8zremdzi3p/Parallel-exmple.png?raw=1)

There are a couple of points about the implementation. In this example, we can fire each iteration of the loop every 5 cycles. But why? Because initially, we’ve decided that each operation in our dataflow takes one cycle. Hence, in this case for calculating i we need to execute the following instructions:

*Loop -> Select -> Add -> CMP -> CBranch  -> …*


However, we can see for this example, Add-> Cmp -> Cbranch all can be done in a single cycle. So that’s where fusion kicks in and fusing these instructions together will improve our pipelining. The following example shows how fusion will transform the graph. In the new graph, loop induction variable is calculated every 3 cycles instead of 5 cycles.
Select node itself also can be a single cycle node, because what it does is actually only selecting between inputs. And make the selected input available for the predecessor nodes. Therefore, ideally, we can fire a new iteration every 2 cycles instead of 5 cycles.

![Parallel opt1](https://www.dropbox.com/s/9uyu0oif4cjlik7/Parallel-opt1.png?raw=1)

The second change that we apply to the graph is adding a queue in front of our call interface. First, adding queue helps us to have a better tolerance for variable latency operations. Second, it enables us to increase execution units. Instead of sending functions arguments directly to the loop body we send them to the queue (Task controller) and then task controller manages our execution units. In this case, Sync node follows the number of on fired iterations until all of them are done and then triggers the rest of the graph.

![Parallel opt2](https://www.dropbox.com/s/0gs8bcccxlh7g77/Parallel-op2.png?raw=1)

Another possible transformation that we can apply after forming tasks is increasing memory parallelism. For example, implementing this type of memory banking [1] is possible in our framework. In the ISCA submission, we didn’t combine our memory banking with task parallelism, this is one of the experiments that we can add or try for a couple of workloads.

![Parallel opt3](https://www.dropbox.com/s/gmkean5l2qbaq5y/Parallel-opt3.png?raw=1)


Another feature that we can add to the parallel execution is tagging iterations using TaskID. This part has been done for TAPAS for supporting recursion but just mentioning how it works. In this case, whenever we fire a task, we tag all the arguments with the parent’s task ID + 1. We also changed our Sync node from a single counter to a collection of counters. Therefore, we can keep track of all the tasks. As a result, we fire tasks in order, but they can complete out of order.




[1] [Automated generation of banked memory architectures in the high-level synthesis of multi-threaded software](https://ieeexplore.ieee.org/document/8056841)

