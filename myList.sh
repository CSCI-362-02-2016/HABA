printf "<html>" >> output2.html
for file in *; do
    printf "%s <br>" $file 
done | >> output2.html
printf "</html>" >> output2.html
xdg-open output2.html
