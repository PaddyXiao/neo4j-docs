cd docs
make html
cd ..
git add -f *
git commit -m "$1"
git push -u origin master
