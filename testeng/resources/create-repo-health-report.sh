#!/bin/bash
set -e
export LC_ALL=C.UTF-8
export LANG=C.UTF-8

virtualenv --python=/usr/bin/python3.5 venv -q
source venv/bin/activate

cd pytest-repo-health
make requirements
pip install -e .
pytest --repo-health --repo-health-path ../edx-repo-health --repo-path ../target_repo
