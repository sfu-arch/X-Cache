if [ -z "${CONCURRENT}" ]; then
    CONCURRENT=2
fi

# Set family to either "Cyclone V" or "Arria 10"
#FAMILY="Cyclone V"
#PART=5CSEMA5F31C6

#
FAMILY="Arria 10"
PART=10AS016E4F29E3LG

USE_VCD=ON

APPS=( \
    #'addTop'
    #'andTop'
    #'shiftTop'
    #'xorTop'
    #'stencilTop'\
    #'bbgemmTop'\
    'kernelCovarianceTop'
    #'covarianceTop'\
    #'test18Top'\
    #'testf02Top'\
    #'kernel_2mmTop'\
    #'kernel_3mmTop'\
    #'saxpy'\
    #'saxpyTop'\
    #'mergesortTop'\
    #'fibTop'
)

for app in ${APPS[@]}; do
    echo "Generating Quartus project."
    pushd ${app} &&  ../../scripts/make_quartus_prj.sh --dir . --instance ${app} --source ${app}.v \
					 --vcd=${app}.vcd --use_vcd={USE_VCD} \
					 --part ${PART} --family "${FAMILY}" && popd
done

# Use xargs to spawn a maximum of $CONCURRENT builds at a time
printf "%s\n" ${APPS[@]} | xargs -n1 --max-procs=${CONCURRENT} -I APP bash -c 'echo "Compiling FPGA for APP"; cd APP/APP*; source build.sh >& make_compile.log'


#END_TIME=`date`

#sendEmail -f smargerm@sfu.ca -t smargerm@sfu.ca -s mailgate.sfu.ca \
#          -u "compile_hybrids.sh complete" \
#          -m "Begin: ${BEGIN_TIME}\nEnd  : ${END_TIME}"
