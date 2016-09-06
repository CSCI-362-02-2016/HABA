echo "<html>" > lsout2.html
for file in *; do
    printf "%s <br>" $file >> lsout2.html
done 
echo "</html>" >> lsout2.html
xdg-open lsout2.html
