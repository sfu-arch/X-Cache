#cd $1*
quartus_sh --flow compile top
quartus_sh -t resources.tcl -project top.qpf -revision top -panel "Fitter||Resource Section||Fitter Resource Utilization by Entity" -file resources.csv

# Look for the offload block summary
grep 'Compilation Hierarchy Node.*' resources.csv >  offload_block_size.csv
egrep "^\|${1}" resources.csv >> offload_block_size.csv

FMAX=`grep -A6 "Slow.*0C Model Fmax" output_files/top.sta.rpt | cut -d " " -f2-3 | grep MHz`
echo "$1 $FMAX" >> performance.txt
