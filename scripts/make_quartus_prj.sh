#!/bin/bash

# Usage
# ./make_quartus_prj.sh --dir ../TypeStackSpace --source TypeStackFile_16_8_16_low.v \
#                  --instance TypeStackFile --vcd TypeStackFile_16_8_16_low.vcd \
#                  --use_vcd ON --rate 12.5 --family "Cyclone V" --part 5CSEMA5F31C6

#Defaults
VCD=foo.vcd
USE_VCD=OFF
RATE=12.5
#PART=5CSEMA5F31C6
PART=10AS016E4F29E3LG
#PART=10AS066N3F40E2SG
#FAMILY="Cyclone V"
FAMILY="Arria 10"

# Parse the command line options
while [[ $# -gt 1 ]]
do
key="$1"

case $key in
    -d|--dir)
    DIR="$2"
    shift # past argument
    ;;
    -s|--source)
    SRC="$2"
    shift # past argument
    ;;
    -i|--instance)
    INSTANCE="$2"
    shift # past argument
    ;;
    -v|--vcd)
    VCD="$2"
    shift # past argument
    ;;
    -u|--use_vcd)
    USE_VCD="$2"
    shift # past argument
    ;;
    -r|--rate) 
    RATE="$2"
    shift # past argument
    ;;
    -p|--part) 
    PART="$2"
    shift # past argument
    ;;
    -f|--family) 
    FAMILY="$2"
    shift # past argument
    ;;
    *)
            # unknown option
    ;;
esac
shift # past argument or value
done

# Create quartus build directory for design by copying templates.
# *** Warning, removes any existing directory
BUILD_DIR=${SRC%.v}-$PART
rm -fr  $DIR/$BUILD_DIR
cp -r ~/git/dandelion-lib/scripts/quartus_template $DIR/$BUILD_DIR

# Modify template design files for current build
pushd $DIR/$BUILD_DIR &> /dev/null
if [ "$FAMILY" == "Cyclone V" ] ; then
    cp top.qsf.cv top.qsf
else
    cp top.qsf.a10 top.qsf
fi

echo SRC=$SRC
echo VCD=$VCD
echo INSTANCE=$INSTANCE
echo USE_VCD=$USE_VCD
echo RATE=$RATE
echo PART=$PART
echo FAMILY=$FAMILY

sed -i -e "s/<SRC>/..\/$SRC/g" top.qsf
sed -i -e "s/<VCD>/..\/$VCD/g" top.qsf
sed -i -e "s/<INSTANCE>/$INSTANCE/g" top.qsf
sed -i -e "s/<USE_VCD>/$USE_VCD/g" top.qsf
sed -i -e "s/<RATE>/$RATE/g" top.qsf
sed -i -e "s/<FAMILY>/\"$FAMILY\"/g" top.qsf
sed -i -e "s/<PART>/$PART/g" top.qsf

# Return
popd &> /dev/null
