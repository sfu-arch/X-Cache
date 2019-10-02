µIR creates a hierarchical network of microarchitecture boxes to form the accelerator. Each box describes a specific function in the overall design. This function can be either from the µIR's microarchitecture library or can be custom defined by the domain expert. The logic box implements the dataflow graph of the acceleration region. The dataflow graph itself is strongly typed; each node in the dataflow graph specifies an explicit type. The operations in the dataflow graph support the full LLVM instruction set.

In such a system each box needs to communicate with other boxes independent from its functional unit.
Hence, µIR needs to have a sophisticated interface to connect all these boxes together and have a fully pipelined system while the interface doesn't affect the box functionality.
Having support for all these pieces forms the µIR's node system.

## Handshaking

A dataflow graph mapped may contain nodes with multiple cycle latency (e.g. floating point operations), and unpredictable latency (e.g., memory operations). Hence, we support pipelined dataflow execution.
Each node communicates via a decoupled handshaking interface so that µIR can multiplex multiple iterations of a data parallel loop on the same logic box.
Figure shows an add2D tensor unit

![Pipeline](https://www.dropbox.com/s/tn3p4cfkeucjin0/Pipeline.png?raw=1)

The add2D function unit communicates with Load left2D, Load right2D and Store result2D via decoupled handshaking signals which contain ready and valid signals in addition to data. Each input and output to the dataflow node has a pair of credit 1 bit ready/valid signals addition to the data. Valid and Ready are pipeline handshaking signals which allow the source and the sink to communicate with regards to when it is time to pass data. When valid is high (source ready to send data) and sink is ready (sink ready to receive data. µIR does not explicitly schedule any of the operations in the dataflow graph permitting flexibility in both what the operations are and what types they operate on. The figure also shows the state machine governing each operation in the dataflow graph. The interface monitors the input signals, it is initially in state \textit{IDLE}, as soon as it detects one of the input is fired it latches the data. If both of the inputs are latched, the node moves to \textit{EXE} state. The transition from \textit{EXE} state to \textit{IDLE} state happens as soon as the node completes execution and passes on the data to the output (i.e., when the sink's ready and valid fire).


### µIR lowered to Chisel source

![Src](https://www.dropbox.com/s/236a39nuc6xex24/SrcTensor2D.png?raw=1)

### Handshaking types

To be able to support new operations easily, µIR's micro architecture library separates handshaking logic with the computation logic. A user can create a new function unit by simply borrowing one of the handshaking templates and adding code for just the operation itself (typically a few lines;1 for integer operations)
Handshaking interfaces are \textit{typed}, this implies the \textit{source} and \textit{destination} type of the function unit must be same.

The handshaking interface delivers the inputs from the source node (e.g., \code{Load2D}) to the operand registers at the sink node \code{Add2D}, deliver output to the sink nodes, and include other lines that control the state machine of the node. Each input and output have its own specific data type (i.e., 2DTensor, UInt32), and the type can differ from one node to another node. Typed interface force the µIR to keep the \textit{source} and \textit{destination} type of a signal the same.

The majority of the LLVM IR operations need 2 input --- 1 output operation, mentioned in Figure~\ref{fig:example}, which was designed to translate IR to processor binary easily. Accelerator designs do not have such limitations~\cite{clark2004application}.
The remaining three handshaking interfaces are

There are three other types of pipeline handshaking include i) Multi-Output: In accelerators, there does exist situation when an operations fans-out the result to many sink operations i.e., single producer - multi-consumer pattern. Especially since the operations in µIR's dataflow graph could be manipulating complex data types. In such cases, we define a pipeline handshaking with a list of outputs each of which can be connnected to a different destination node. The number of outputs is a parameter and is set by \WORK\ during instantiation. ii) Compound-operations: Accelerators typically include compound domain-specific function units not found in processors. Such compound function units typically fuse multiple operations and hence require multiple inputs. Handshaking for compound operations defines a list of inputs and outputs, the number of inputs and outputs is a parameter set by \WORK\ when generating the sketch, iii) Ordering: Finally, in certain cases the handshaking may also include control information (e.g., memory ordering\S~\ref{sec:memory}). As shown in the figure a single token bit controls enforces the following sequence Pred$\rightarrow$Op$\rightarrow$Succ of execution.

![Handshakes](https://www.dropbox.com/s/axdjzha06outlqg/Handshakes.png?raw=1)

## Balancing dataflow pipelines

Balancing pipeline stages in the presence of memory operations with variable latency is a hard problem and a typical HLS tool uses scheduling algorithms such as SDC, to consider worst-case cycles for scheduled design. For example, legup stalls an accelerator when a DRAM memory operation occurs. On the contrary, \WORK\ can rely on domain-insights and compiler optimization to discover MLP and keep the pipeline busy. For example, in Figure~\ref{fig:overview}, the \code{load2D} can use the type information of Tensor2D to issue up to four memory operations in parallel. \WORK\ can also be used to parameterize the pipeline registers between operations and optimize intermediate buffer depths based on operation latencies.

## Higher order operations

The implementation of the function units on the user-defined type is abstracted out to µIR's microarchitecture library in Chisel. We illustrate how a matrix operator is defined.

![Higher Order Ops](https://www.dropbox.com/s/tctpf5qo76uts11/HigherOrder.png?raw=1)

To aid hardware developers providing type implementation, we further abstract the specific implementations of each type from the functional unit module as well. The hardware developer is only expected to declare the operations within the type class (see class tensors) and then for each type define the operations within OperatorModule. The function unit module is generic and do not need to be modified for each type. During the Chisel to Verilog translation phase, when user creates object (new TypCompute(mat2x2)) the templates elaborate and create specific mat2x2 type IO interfaces (Type IO) and create a function module (Type-function).Xketchup matches up the user types against the programmer annotation to integrate the function units into the dataflow. All the user- defined types(tensor) will reuse the same templated function unit module.
