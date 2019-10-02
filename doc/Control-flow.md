# Control flow

For supporting arbitrary control flow, we’ve made a few assumptions up front, which I’ll explain them first.
Every value in the graph must be generated once and consumed once, it’s the same as SSA form. In our dataflow execution, all the nodes **always** have to accept new data and produce an output. Trigger signal only tells the node if it has to produce a valid output or it can just produce garbage output and proceed. For instance, if the node is Load and trigger signal is zero, then the node shouldn’t make any memory request. However, it has to produce some output value for its predecessors. Hence, we put zero on the data line and make the value valid. Another example is divider. For example, divider takes 5 cycles. If trigger value is zero we just skip the operation and produce a garbage value for the predecessors in a single cycle.
In the loop case, loop’s interface to the outside is live-in and live-out registers. Therefore, what loop does is accepting live-in values, and then fires exit signal with false value and make all the live-out registers valid with again garbage value.
Trigger units are merge point of all control signals. Base on the input values trigger unit decides if it fires true value or false value.

![Control Example](https://www.dropbox.com/s/fjeq0z0k3a27ta3/Controlflow.png?raw=1)

![Serial Loop](https://www.dropbox.com/s/ohobbax6e2b5lf3/Serial-Loop.png?raw=1)
