#!/usr/bin/env bash

# deploy the script for the web front-end

# This file is responsible for preprocessing all TypeScript files, making sure
# all dependencies are up-to-date, and copying all necessary files into
# the web deploy directory

# This is the resource folder where maven expects to find our files
TARGETFOLDER=../backend/src/main/resources

# This is the folder the we used with the Spark.staticFileLocation command
WEBFOLDERNAME=web

# Step1: make sure we have someplace to put everything. We will delete the old
#        folder tree, and then make it from scratch
echo "deleteing $TARGETFOLER and creating an empty $TARGETFOLER/$WEBFOLDERNAME"
rm -rf $TARGETFOLDER
mkdir $TARGETFOLDER
mkdir $TARGETFOLDER/$WEBFOLDERNAME

# There are many more steps. For now, we will just copy an HTML file
echo "copying index_simple.html to $TARGETFOLDER/$WEBFOLDERNAME/index.html"
cp index_simple.html $TARGETFOLDER/$WEBFOLDERNAME/index.html #note we also rename to index.html

# Step 2: update our npm dependencies
echo "updating npm dependencies"
npm update

# Step 3: copy javascript and other files from src folder
echo "Copying source files to $TARGETFOLDER/$WEBFOLDERNAME"
cp todo.js $TARGETFOLDER/$WEBFOLDERNAME
cp -r src $TARGETFOLDER/$WEBFOLDERNAME/src

# Step 4: Copy css files
echo "Copying todo.css and app.css to $TARGETFOLDER/$WEBFOLDERNAME"
cp todo.css app.css $TARGETFOLDER/$WEBFOLDERNAME

# Step 5: compile TypeScript files
echo "Compiling typescript files"
node_modules/typescript/bin/tsc app.ts --lib "es2015","dom" --target es5 --strict --outFile $TARGETFOLDER/$WEBFOLDERNAME/app.js
#note: also included libraries for promises for asynchronous code for fetch()

# Step 7: set up Jasmine
node_modules/typescript/bin/tsc apptest.ts --strict --outFile $TARGETFOLDER/$WEBFOLDERNAME/apptest.js
cp spec_runner.html $TARGETFOLDER/$WEBFOLDERNAME
cp node_modules/jasmine-core/lib/jasmine-core/*.css $TARGETFOLDER/$WEBFOLDERNAME
cp node_modules/jasmine-core/lib/jasmine-core/*.js $TARGETFOLDER/$WEBFOLDERNAME