#!/bin/sh

ORIGDIR=`pwd`
export COUNT=100;
export MAPR_HOME=./java

#generate storybook:
echo "Generating Storybook"

mkdir storybook
cd storybook
cp ../genconfig.txt ./genconfig.txt
java -Xms2000m -Xmx6000m -classpath "$MAPR_HOME/lib/*" com.chrisargenta.domains.Generator Storybook . $COUNT 100 > out_gen_storybook_`date +"%Y%m%d%H%M"`.txt
cd $ORIGDIR

#generate collector
echo "Generating Collector"

mkdir collector
cd collector
cp ../genconfig.txt ./genconfig.txt
java -Xms2000m -Xmx6000m -classpath "$MAPR_HOME/lib/*" com.chrisargenta.domains.Generator Collector . $COUNT 101 > out_gen_collector_`date +"%Y%m%d%H%M"`.txt
cd $ORIGDIR

#generate Blocks
echo "Generating TeamBlocksWorld"

mkdir teamblocks
cd teamblocks
cp ../genconfig.txt ./genconfig.txt
java -Xms2000m -Xmx6000m -classpath "$MAPR_HOME/lib/*" com.chrisargenta.domains.Generator TeamBlocks . $COUNT 102 > out_gen_blocks_`date +"%Y%m%d%H%M"`.txt
cd $ORIGDIR

