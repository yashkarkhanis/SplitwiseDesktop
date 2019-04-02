Splitwise For Desktop
=====================


Setting up development enviroment

Make sure following are installed:

Java JDK 1.8.0_201
Java JRE 1.8.0_201
Git 2.20.1	https://git-scm.com/downloads
https://desktop.github.com/
Tutorial https://www.atlassian.com/git/tutorials/install-git#windows


Setting up Git
==============

1. Make a separate folder for the project
2. Ideally, Folder Structure should be:
		COEN275
			Project
				Splitwise

3. On Command Prompt/Terminal, Go to the folder location
4. Initialize empty repository, using following command

		 $ cd <absolutePath>/COEN275/Project/Splitwise
		 
		 $ git init
5. Verify by listing all files. It must contain ".git" file
	For linux and mac, or Git Bash on Windows, following command will work:
	
		$ ls -a
		
6. Configure local repository parameters
	
		$ git config --local user.name "Vaibhaw Raj"
		
		$ git config --local user.email "vaibhaw2020@gmail.com"
	Verify using
	
		$ git config --local -l
7. Adding remote repository
	
		$ git remote add origin https://github.com/vaibhawraj/SplitwiseDesktop.git


Workflow : To get Started
==========================
Step 1: Fetch latest code from master

		$ git fetch origin master

Step 2: Create a separate branch : This ensure that original code is preserved

		$ git branch "your_branch_name"

Step 3: Switch to your branch

		$ git checkout "your_branch_name"
		
		Verify by typing
		
		$ git branch

Step 4: Make your changes

Workflow : To push your code

Step 1: Add all files

		$ git add .
		
Step 2: Make local commit

		$ git commit -m "somemeaning full message"
		
		Meaningful message should be on following line:
		
			"Added something"
			
			"Created something"
			
			"Fixed something"
			
			"Updated somefile"
Step 3: Push your changes to your branch

		$ git push origin "your_branch_name"

Step 4: Once you are sure that your code is ready for review and merge, Go on github website and create a pull request
Step 5: Once your code is merges to the master, you may delete your branch


Exercise 1 : Make your first commit
===================================

$ cd <absolutePath>/COEN275/Project/Splitwise
	
$ git init

$ git config --local user.name "Vaibhaw Raj"

$ git config --local user.email "vaibhaw2020@gmail.com"

$ git remote add origin https://github.com/vaibhawraj/SplitwiseDesktop.git

$ git pull origin master

$ git branch "your_branch_name"

$ git checkout "your_branch_name"

$ git branch

Create a file and add your name and email address

$ git add .

$ git commit -m "First commit"

$ git push origin "your_branch_name"

Create a pull request


Github Cheatsheet
=================

1. To fetch latest code from "Master" branch

		$ git pull origin master

2. Create your separate branch

		$ git branch SplitwiseSDK

3. View your current working branch

		$ git branch

4. To switch branch

		$ git checkout <branch_name>

5. To delete branch

		$ git branch -d <branch_name>





