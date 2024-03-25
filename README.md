# CSE 216 Team Repo
This is a team repository.  It is intended for use during phase 1 and beyond.

## Details
- Semester: Spring 2024
- Team Number: 09
- Team Name: goku
- Bitbucket Repository: https://bitbucket.org/sml3/cse216_sp24_team_09
- Jira Board: https://cse216-24sp-exh226.atlassian.net/jira/software/projects/C2T9/boards/2 
- backend url: [exh226@dokku.lehigh.edu](https://cse216_sp24_team_09-exh226.dokku.cse.lehigh.edu/)
- Database : ElephantSQL [Goku]
- Release 0.0

## Team Members
- Sam Maloof (Backend)
*sjm225@lehigh.edu
- Evan Hu (Project Manager)
*exh226@lehigh.edu
- Jialin Lin (Web Front End)
*jil426@lehigh.edu
- Zander Fahs (Mobile)
*zah326@lehigh.edu
- Ivory Le (Admin)
*hhl226@lehigh.edu


## Doku Instructions
- ssh -i ~/.ssh/id_ed25519 -t dokku@dokku.cse.lehigh.edu 'ps:start cse216_sp24_team_09'

## Admin Instructions
- POSTGRES_IP=tiny.db.elephantsql.com POSTGRES_PORT=5432 POSTGRES_USER=syldyaly POSTGRES_PASS=dV4x4m4VuM9WWQ2qHkkwU46bi8CmwMwp


## Git Instructions  
1. After finishing writing code, DO NOT merge to master and then push the master branch.
2. Instead, do ```git push origin 'branch name' ```
3. Then, go to BitBucket and go to 'Pull Requests' in the side menu
4. Click, 'Create pull request'
5. Select your branch as the source and master as destination
6. Fill in form and click 'Create pull request'

To Load Others Changes into your local repository:

1. ```git fetch origin 'branch name'``` or ```git pull origin 'branch name'```
2. This loads the changes other people made to the branch into your local branch
3. Note: you will probably have resolve conflicts. Be careful when selecting what changes to (add/not add)