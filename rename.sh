#!/bin/sh

 git filter-branch --commit-filter '
        if [ "$GIT_COMMITTER_NAME" = "collierw" ];
        then
                GIT_COMMITTER_NAME="WCollier";
                GIT_AUTHOR_NAME="WCollier";
                GIT_COMMITTER_EMAIL="w.collier@lancaster.ac.uk";
                GIT_AUTHOR_EMAIL="w.collier@lancaster.ac.uk";
                git commit-tree "$@";
        else
                git commit-tree "$@";
        fi' HEAD
