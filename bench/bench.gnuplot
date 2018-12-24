#!/bin/bash

# initial graph file from the following article
# http://www.bradlanders.com/2013/04/15/apache-bench-and-gnuplot-youre-probably-doing-it-wrong/


# format of the ab's gnu plot file is the following:

# starttime	seconds	ctime	dtime	ttime	wait
# Sun Dec 23 20:26:21 2018	1545614781	0	212	212	211
# Sun Dec 23 20:26:21 2018	1545614781	0	222	222	220
# ...

# The following gnuplot takes the seconds in epoch time (column #2) as the x-axis, 
# and the ttime (total wall time taken for the request)

# Let's output to a jpeg file
set terminal jpeg size 500,500
# This sets the aspect ratio of the graph
set size 1, 1
# The file we'll write to
set output "graphs/timeseries.jpg"
# The graph title
set title "Benchmark testing"
# Where to place the legend/key
set key left top
# Draw gridlines oriented on the y axis
set grid y
# Specify that the x-series data is time data
set xdata time
# Specify the *input* format of the time data
set timefmt "%s"
# Specify the *output* format for the x-axis tick labels
set format x "%S"
# Label the x-axis
set xlabel 'seconds'
# Label the y-axis
set ylabel "response time (ms)"
# Tell gnuplot to use tabs as the delimiter instead of spaces (default)
set datafile separator '\t'
# Plot the data
plot "bench.tsv" every ::2 using 2:5 title 'response time' with points
exit
