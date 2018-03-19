if [ -z "${CONCURRENT}" ]; then
    CONCURRENT=2
fi

#BEGIN_TIME=`date`
APPS=( \
#    'cilk_for_test04TopA' \
#    'cilk_for_test04TopB' \
#    'cilk_for_test04TopC' \
    'cilk_for_test05TopA' \
    'cilk_for_test05TopB' \
    'cilk_for_test05TopC' \
    'cilk_for_test06TopA' \
    'cilk_for_test06TopB' \
    'cilk_for_test06TopC' \
    'cilk_for_test08TopA' \
    'cilk_for_test08TopB' \
    'cilk_for_test08TopC' \
)

for app in ${APPS[@]}; do
    echo "Generating Quartus project."
    pushd ${app} &&  make_quartus_prj.sh --dir . --instance ${app} --source ${app}.v && popd
done

# Use xargs to spawn a maximum of $CONCURRENT builds at a time
printf "%s\n" ${APPS[@]} | xargs -n1 --max-procs=${CONCURRENT} -I APP bash -c 'echo "Compiling FPGA for APP"; cd APP/APP*; source build.sh >& make_compile.log'


#END_TIME=`date`

#sendEmail -f smargerm@sfu.ca -t smargerm@sfu.ca -s mailgate.sfu.ca \
#          -u "compile_hybrids.sh complete" \
#          -m "Begin: ${BEGIN_TIME}\nEnd  : ${END_TIME}"
