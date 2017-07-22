#!/bin/bash

function goto_chisel_temp {
      cd $TMPDIR
      dir=`\ls -ltr | awk "/$1/"' { print $NF }' | tail -1`
      if test -n "4dir"; then
        echo "Changing to latest tempdir for $1 $dir"
        cd $dir
      else
        echo "Could not find latest tempdir for $1"
      fi
    }
