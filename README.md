# BOOKSTER wiki pages

## Table of content
---
1. Introduction
2. Project description
3. Technology description
4. Instalation
5. Instalation troubleshoot
6. Contributors
7. License
---

## 1. Introduction
Hello stranger, I am really glad that I can invite you to the team. But first thing first lets start to work and then we can talk. This is bookster (convene.sk) web application project. It should contain all important information of project structure and source codes. Bookster is web application that provide a option to create events and manage attendees. It should help all conveners to better understand their audience and to allow them provide feedbacks. Source codes are strictly diveded to backend and frontend part and the crosspoints is achvieved by used frameworks. Let me to explain: backend application is created using spring boot with hipernate but mostly written in kotlin. Code should be clear for anybody right at the first glance. Thankfully to kotlin of course. However frontend is not created using thymleaf or laravel as spring boot developers recommend. But mainly using vuejs that provides perfect introduction to single page applications. And together we have perfect combination.

## 2. Project description
As it is written in introduction: "Bookster is web application that provides an option to create events and manage attendees." This is all what it is. But it provides more possibilities when we dive deeper to it. You can see that it contains more than event domain objects. Users are able to create pages and even conferences that can contain multiple or event hundrets of events if required. Pages are simple events holders or packagers for customers  which want to have trademark. Conferences are most valuable product of convene, It provides same posibilities as pages but provides even detailed registrations, customer/attendees management and surveys wuth any limitations - mostly for highly commercial usage.

## 3. Technology description

## 4. Instalation
First of all you have to prepare your enviroment. 

**Pre-requisities:**

_git, mysql database, java 8 SDK, Kotlin, Maven, npm, node, python 2.7, MS C++ build tools_  

Steps to configure enviroment:
- Install git,
- install xampp (required for mysql database and ease to use phpmyadmin web interface),
- instal node and npm,
- install python27,
- set python enviroment variable, steps: 
1. Hold Win and press Pause.
2. Click Advanced System Settings.
3. Click Environment Variables.
4. Append `;C:\python27` to the Path variable.
5. Restart Command Prompt.

- Install MS C++ build tools, steps:
1. `npm install --global --production windows-build-tools`
2. `npm update` might be required within project folder afterwards.

**Installation:**

Recommended integrated development enviroment (IDE): **GitKraken, IntelliJ Idea, Visual Studio Code** (vetur plugin)

When you have IDE installed and prepared please clone bookster webapp repository and import to IntelliJ idea. Then download all maven sources and prepare for build & `mvn clean install` right after pre-requisities installation.

- Install GitKraken,
- generate ssh key pair, steps:
[This might help](https://help.github.com/articles/generating-a-new-ssh-key-and-adding-it-to-the-ssh-agent/)

1. `ssh-keygen -t rsa -b 4096 -C "your_email@example.com"`,
2. start ssh agent `eval $(ssh-agent -s)`,
3. add private part of key to ssh agent `ssh-add ~/.ssh/id_rsa`.

- now clone a repository `git clone ssh://and so on...`.
- Download artifactory configuration file `settings.xml,` place it to the `~/.m2` folder,
- Ensure that all pre-requisities all installed and configured correctly. All of them are required to correctly run the web application.
- Run xampp control panel and complete initial configuration then start apache web server and mysql database. If everything is okay then proceed to database creation. You should create database with name `bookster_db`.
- Run `mvn clean install`. Installation process is going to download all maven and npm dependencies so it might take a long time to run for the first time. If any error occures please read instalation troubleshoot section below.

**Start:**
- Create run configuration at idea editor. Add local profile and specify build before run process. Run created configuration afterwards. If any problem occurs please read runtine troubleshoot section of this document.
- Start webpack dev server by using `npm run dev` command. Webpack should pack all dependencies to bundle and  serve the content with hot reload instantly and automatically. You should see the content at http://localhost:3000 where is dev server running.
- You are ready to develop ! Please read project code standards before you start to mess everything up.

## 5. Instalation troubleshoot

**Missing MS C++ build tools error**

`MSBUILD : error MSB3428: Could not load the Visual C++ component "VCBuild.exe". To fix this, 1) install the .NET Framework 2.0 SDK, 2) install Microsoft Visual Studio 2005 or 3) add the location of the component to the system path if it is installed elsewhere.  [C:\Users\patrikp\projects\bookster\node_modules\contextify\build\binding.sln]`

This error below might occur when you haven't got MS build tools installed on your system. Please repeat last step of installation process again. If this does not solve the problem then try restart your system. In some cases Visual studio installation is required as well...

**XAMPP port 80 is used by process with PID 4**

`Port 80 in use by "Unable to open process" with PID 4! Apache WILL NOT start without the configured ports free! You need to uninstall/disable/reconfigure the blocking application or reconfigure Apache and the Control Panel to listen on a different port.`

PID 4 means system process. You might resolve this by killing World Wide Web Publisher process.