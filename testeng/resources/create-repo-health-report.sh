
virtualenv --python=python$PYTHON_VERSION venv -q
source venv/bin/activate

cd pytest-repo-health
make requirements
pip install -e .
pytest --repo-health --repo-health-path ../edx-repo-health
