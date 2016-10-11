# Team HABA: Hugo Felkel, Arthur Hilgendorg, Brett Perrine, Angel Rodriguez
# Created for CSCI 362 at the College of Charleston, Fall 2016 

# List all the files/folders in current directory and append the results to a html file
# Open that html file in the browser

echo "<html>" > lsout2.html
for file in *; do
    printf "%s <br>" $file >> lsout2.html
done 
echo "</html>" >> lsout2.html
xdg-open lsout2.html
