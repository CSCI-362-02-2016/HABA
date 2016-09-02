printf "<html>" tee output2.html
for file in *; do
    printf "%s <br>" $file 
done | tee output2.html
printf "</html>" tee output2.html
xdg-open output2.html
