date
scp `ls -lt target/*.jar | head -n 1 | awk '{print $9}'` $DEST