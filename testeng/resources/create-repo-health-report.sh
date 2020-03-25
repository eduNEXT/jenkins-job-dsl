#!/bin/bash
set -e -v

# click requires this to work cause it interfaces weirdly with python 3 ASCII default
export LC_ALL=C.UTF-8
export LANG=C.UTF-8

virtualenv --python=/usr/bin/python3.6 venv -q
source venv/bin/activate

ls

cd pytest-repo-health
make requirements
pip install -e .
deactivate
source ../venv/bin/activate

touch tmp.txt
pytest --repo-health --repo-health-path ../edx-repo-health --repo-path ../target_repo -c tmp.txt --noconftest
